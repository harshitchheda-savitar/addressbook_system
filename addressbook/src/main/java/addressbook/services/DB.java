package addressbook.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import addressbook.utils.StringUtils;

public class DB {
	// Database credentials
	private static final String DB_URL = "jdbc:mysql://localhost:3306/addressbook_service?useSSL=false";
	private static final String USER = "root";
	private static final String PASS = "root";

	public enum DBStatementType {
		PREPARED_STATEMENT, CREATE_STATEMENT
	}

	private static final Logger logger = LogManager.getLogger(DB.class);
	private Connection conn;

	public DB() {
		this.conn = getConnection();
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public void closeInstance() throws SQLException {
		this.conn.close();
	}

	public static Connection getConnection() {
		DOMConfigurator.configure("log4j.xml");
		try {
			logger.debug("Conecting to dbs : " + DB_URL);
			Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			logger.info("Connection is Successfull : " + conn);
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void buildPreparedStatment(PreparedStatement stmt, List<String> params) throws SQLException {
		if (params != null) {
			for (int i = 0; i < params.size(); i++) {
				stmt.setString(i + 1, params.get(i));
			}
		}
	}

	public List<Map<String, Object>> getResultSet(String query, List<String> params, DBStatementType statementType)
			throws SQLException {
		switch (statementType) {
		case CREATE_STATEMENT:
			return getResultSetUsingSqlQuery(query, params);
		case PREPARED_STATEMENT:
			return getResultSetUsingPreparedStatement(query, params);
		}
		return null;
	}

	private List<Map<String, Object>> getResultSetAfterExecuting(ResultSet result) throws SQLException {
		List<Map<String, Object>> list = new ArrayList<>();
		ResultSetMetaData md = result.getMetaData();
		int columns = md.getColumnCount();
		while (result.next()) {
			HashMap<String, Object> row = new HashMap<>();
			for (int i = 1; i <= columns; ++i) {
				row.put(md.getColumnName(i), result.getObject(i));
			}
			list.add(row);
		}
		return list;
	}

	private List<Map<String, Object>> getResultSetUsingSqlQuery(String query, List<String> params) throws SQLException {
		DOMConfigurator.configure("log4j.xml");

		Statement statement = this.conn.createStatement();
		String sql = StringUtils.replacePlaceHolders(query, params);
		logger.info("Executing : " + query);
		ResultSet result = statement.executeQuery(sql);
		return getResultSetAfterExecuting(result);
	}

	private List<Map<String, Object>> getResultSetUsingPreparedStatement(String query, List<String> params)
			throws SQLException {
		DOMConfigurator.configure("log4j.xml");

		PreparedStatement statement = this.conn.prepareStatement(query);
		buildPreparedStatment(statement, params);
		logger.info("Executing : " + query);
		ResultSet result = statement.executeQuery();
		return getResultSetAfterExecuting(result);
	}

	public int updateTable(String query, List<String> params, DBStatementType statementType) throws SQLException {
		switch (statementType) {
		case CREATE_STATEMENT:
			return updateUsingSQLQuery(query, params);
		case PREPARED_STATEMENT:
			return updateUsingPreparedStatement(query, params);
		}
		return 0;

	}

	private int updateUsingSQLQuery(String query, List<String> params) throws SQLException {
		DOMConfigurator.configure("log4j.xml");
		String sql = StringUtils.replacePlaceHolders(query, params);
		Statement stmt = this.conn.createStatement();
		logger.info("Executing : " + sql);
		return stmt.executeUpdate(sql);
	}

	private int updateUsingPreparedStatement(String query, List<String> params) throws SQLException {
		DOMConfigurator.configure("log4j.xml");
		PreparedStatement stmt = this.conn.prepareStatement(query);
		buildPreparedStatment(stmt, params);
		logger.info("Executing : " + query);
		return stmt.executeUpdate();
	}

	public int insertIntoTable(String query, List<String> inParams, DBStatementType statementType) throws SQLException {
		switch (statementType) {
		case CREATE_STATEMENT:
			return insertUsingSQLQuery(query, inParams);
		case PREPARED_STATEMENT:
			return insertUsingPreparedStatement(query, inParams);
		}
		return 0;
	}

	private int insertUsingSQLQuery(String query, List<String> params) throws SQLException {
		DOMConfigurator.configure("log4j.xml");
		String sql = StringUtils.replacePlaceHolders(query, params);
		Statement stmt = this.conn.createStatement();
		logger.info("Executing : " + query);
		int rowsAffected = stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
		if (rowsAffected == 0) {
			throw new SQLException("Insert Call failed, no rows affected.");
		}
		return getGeneratedKeys(stmt);
	}

	private int insertUsingPreparedStatement(String query, List<String> params) throws SQLException {
		DOMConfigurator.configure("log4j.xml");
		PreparedStatement stmt = this.conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		buildPreparedStatment(stmt, params);
		logger.info("Executing : " + query);
		int rowsAffected = stmt.executeUpdate();
		if (rowsAffected == 0) {
			throw new SQLException("Insert Call failed, no rows affected.");
		}
		return getGeneratedKeys(stmt);
	}

	private int getGeneratedKeys(Statement stmt) throws SQLException {
		ResultSet resultSet = stmt.getGeneratedKeys();
		if (resultSet.next())
			return resultSet.getInt(1);
		return 0;
	}
}