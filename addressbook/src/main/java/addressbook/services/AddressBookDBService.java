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
			contactsList.add(
					new Contacts(id, firstName, lastName, address, city, state, zip, phoneList, emailList, aadhar));
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

	public void updateContact(String firstName, String lastName, String aadhar, String address, String city,
			String state, String zipCode, String[] mobiles, String[] emails, String addressbookName, String type,
			DBStatementType statementType) throws SQLException {
		List<String> inParams = new ArrayList<>();
		inParams.add(firstName);
		inParams.add(lastName);
		inParams.add(aadhar);
		inParams.add(address);
		inParams.add(city);
		inParams.add(state);
		inParams.add(zipCode);
		inParams.add(addressbookName);
		inParams.add(Integer.toString(Constants.ADDRESSBOOK_TYPE_MAP.get(type)));
		DB db = new DB();
		db.getConn()
				.setAutoCommit(false);
		try {
			int result = db.updateTable(QueryConstants.UPDATE_CONTACT_DETAILS, inParams, statementType);
			if (result == 0)
				return;
			AddressBook addressbook = getAddressBook(addressbookList, addressbookName, type);
			Contacts contact = getContact(addressbook, firstName, lastName, aadhar);
			if (contact != null) {
				try {
					List<String> phoneNumberList = new ArrayList<>();
					List<String> emailList = new ArrayList<>();
					inParams.clear();
					inParams.add(Integer.toString(contact.getId()));
					db.updateTable(QueryConstants.DELETE_PHONE_LIST, inParams, statementType);
					for (String phoneNumber : mobiles) {
						inParams.clear();
						inParams.add(Integer.toString(contact.getId()));
						inParams.add(phoneNumber);
						db.insertIntoTable(QueryConstants.ADD_PHONE_NUMBER, inParams, statementType);
						phoneNumberList.add(phoneNumber);
					}
					inParams.clear();
					inParams.add(Integer.toString(contact.getId()));
					db.updateTable(QueryConstants.DELETE_EMAIL_LIST, inParams, statementType);
					for (String email : emails) {
						inParams.clear();
						inParams.add(Integer.toString(contact.getId()));
						inParams.add(email);
						db.insertIntoTable(QueryConstants.ADD_EMAILS, inParams, statementType);
						emailList.add(email);
					}
					contact.setFirstName(firstName);
					contact.setLastName(lastName);
					contact.setAdhaarNumber(aadhar);
					contact.setAddress(address);
					contact.setCity(city);
					contact.setState(state);
					contact.setZip(zipCode);
					contact.setMobNo(phoneNumberList);
					contact.setEmailId(emailList);
				} catch (Exception e) {
					db.getConn()
							.rollback();
					db.closeInstance();
					return;
				}
			} else
				throw new Exception();
		} catch (Exception e) {
			db.getConn()
					.rollback();
			db.closeInstance();
			return;
		}
		db.getConn()
				.commit();
		db.closeInstance();
	}

	public void deleteContactFromTable(String firstName, String lastName, String aadhar, String addressbookName,
			String type, DBStatementType statementType) throws SQLException {
		List<String> inParams = new ArrayList<>();
		inParams.add(firstName);
		inParams.add(lastName);
		inParams.add(aadhar);
		inParams.add(addressbookName);
		inParams.add(Integer.toString(Constants.ADDRESSBOOK_TYPE_MAP.get(type)));
		DB db = new DB();
		int result = db.updateTable(QueryConstants.DELETE_CONTACT, inParams, statementType);
		if (result == 0)
			return;
		AddressBook addressbook = getAddressBook(addressbookList, addressbookName, type);
		Contacts contact = getContact(addressbook, firstName, lastName, aadhar);
		if (contact != null)
			addressbook.getContacts()
					.remove(contact);
		db.closeInstance();
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
							addAddressBookToList(new Contacts(newlyInsertedId, firstName, lastName, address, city,
									state, zipCode, phoneNumberList, emailList, aadhar), addressbookName, type);
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

	private Contacts getContact(AddressBook addressbook, String firstName, String lastName, String aadhar) {
		if (addressbook != null) {
			Contacts contacts = addressbook.getContacts()
					.stream()
					.filter(contact -> contact.getFirstName()
							.equals(firstName)
							&& contact.getLastName()
									.equals(lastName)
							&& contact.getAdhaarNumber()
									.equals(aadhar))
					.findFirst()
					.orElse(null);
			return contacts;
		}
		return null;
	}
}
