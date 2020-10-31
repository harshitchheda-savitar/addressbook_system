package addressbook.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import addressbook.models.AddressBook;
import addressbook.models.Contacts;
import addressbook.services.DB.DBStatementType;
import addressbook.utils.Constants;
import addressbook.utils.QueryConstants;

public class AddressBookDBService {

	private static List<AddressBook> addressbookList;

	public List<AddressBook> readData(String query, List<String> inParams, DBStatementType statementType)
			throws SQLException {
		addressbookList = new ArrayList<>();
		DB db = new DB();
		List<Map<String, Object>> resultList = db.getResultSet(query, inParams, statementType);
		for (Map<String, Object> result : resultList) {
			int id = (int) result.get("id");
			String addressbookName = (String) result.get("addressbook_name");
			String type = (String) result.get("type_name");
			List<Contacts> contactList = getContactList(QueryConstants.GET_ALL_CONTACTS,
					Collections.singletonList(Integer.toString(id)), statementType);
			addressbookList.add(new AddressBook(addressbookName, type, contactList));
		}
		db.closeInstance();
		return addressbookList;

	}

	private List<Contacts> getContactList(String query, List<String> inParams, DBStatementType statementType)
			throws SQLException {
		List<Contacts> contactsList = new ArrayList<>();
		DB db = new DB();
		List<Map<String, Object>> resultList = db.getResultSet(query, inParams, statementType);
		for (Map<String, Object> result : resultList) {
			int id = (int) result.get("id");
			String firstName = (String) result.get("first_name");
			String lastName = (String) result.get("last_name");
			String aadhar = (String) result.get("aadhar");
			String address = (String) result.get("address");
			String city = (String) result.get("city");
			String state = (String) result.get("state");
			String zip = (String) result.get("zip");
			List<String> phoneList = getPhoneList(QueryConstants.GET_ALL_PHONE_LIST,
					Collections.singletonList(Integer.toString(id)), statementType);
			List<String> emailList = getEmailList(QueryConstants.GET_ALL_EMAIL_LIST,
					Collections.singletonList(Integer.toString(id)), statementType);
			contactsList
					.add(new Contacts(firstName, lastName, address, city, state, zip, phoneList, emailList, aadhar));
		}
		db.closeInstance();
		return contactsList;
	}

	private List<String> getPhoneList(String query, List<String> inParams, DBStatementType statementType)
			throws SQLException {
		List<String> phoneList = new ArrayList<>();
		DB db = new DB();
		List<Map<String, Object>> resultList = db.getResultSet(query, inParams, statementType);
		for (Map<String, Object> result : resultList) {
			phoneList.add((String) result.get("phone_number"));
		}
		db.closeInstance();
		return phoneList;
	}

	private List<String> getEmailList(String query, List<String> inParams, DBStatementType statementType)
			throws SQLException {
		List<String> emailList = new ArrayList<>();
		DB db = new DB();
		List<Map<String, Object>> resultList = db.getResultSet(query, inParams, statementType);
		for (Map<String, Object> result : resultList) {
			emailList.add((String) result.get("email_id"));
		}
		db.closeInstance();
		return emailList;
	}

	public void addContact(String firstName, String lastName, String aadhar, String address, String city, String state,
			String zipCode, String[] mobiles, String[] emails, String addressbookName, String type,
			DBStatementType statementType) throws SQLException {
		DB db = new DB();
		List<String> inParams = new ArrayList<>();
		inParams.add(addressbookName);
		inParams.add(Integer.toString(Constants.ADDRESSBOOK_TYPE_MAP.get(type)));
		db.getConn()
				.setAutoCommit(false);
		try {
			int addressbookId = db.insertIntoTable(QueryConstants.ADD_ADDRESSBOOK, inParams, statementType);
			if (addressbookId > 0) {
				inParams.clear();
				inParams.add(firstName);
				inParams.add(lastName);
				inParams.add(aadhar);
				inParams.add(address);
				inParams.add(city);
				inParams.add(state);
				inParams.add(zipCode);
				inParams.add(Integer.toString(addressbookId));
				try {
					int newlyInsertedId = db.insertIntoTable(QueryConstants.ADD_CONTACT, inParams, statementType);
					if (newlyInsertedId > 0) {
						List<String> phoneNumberList = new ArrayList<>();
						List<String> emailList = new ArrayList<>();
						try {
							for (String phoneNumber : mobiles) {
								inParams.clear();
								inParams.add(Integer.toString(newlyInsertedId));
								inParams.add(phoneNumber);
								db.insertIntoTable(QueryConstants.ADD_PHONE_NUMBER, inParams, statementType);
								phoneNumberList.add(phoneNumber);
							}
							for (String email : emails) {
								inParams.clear();
								inParams.add(Integer.toString(newlyInsertedId));
								inParams.add(email);
								db.insertIntoTable(QueryConstants.ADD_EMAILS, inParams, statementType);
								emailList.add(email);
							}
							addAddressBookToList(new Contacts(firstName, lastName, address, city, state, zipCode,
									phoneNumberList, emailList, aadhar), addressbookName, type);
						} catch (Exception e) {
							db.getConn()
									.rollback();
							db.closeInstance();
							return;
						}
					}
				} catch (SQLException e) {
					db.getConn()
							.rollback();
					db.closeInstance();
					return;
				}
			}
		} catch (SQLException e) {
			db.getConn()
					.rollback();
			db.closeInstance();
			return;
		}
		db.getConn()
				.commit();
		db.closeInstance();
	}

	private void addAddressBookToList(Contacts contact, String addressBookName, String addressBookType) {
		AddressBook addressbook = getAddressBook(addressbookList, addressBookName, addressBookType);
		if (addressbook != null) {
			addressbook.getContacts()
					.add(contact);
		} else {
			addressbookList.add(new AddressBook(addressBookName, addressBookType, Collections.singletonList(contact)));
		}
	}

	public AddressBook getAddressBook(List<AddressBook> addressbookList, String addressBookName,
			String addressBookType) {
		AddressBook addressbook = addressbookList.stream()
				.filter(addressBook -> addressBook.getName()
						.equals(addressBookName)
						&& addressBook.getType()
								.equals(addressBookType))
				.findFirst()
				.orElse(null);
		return addressbook;
	}
}
