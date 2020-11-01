package addressbook.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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

	public void deletePhoneDetails(String contactId, DBStatementType statementType) throws SQLException {
		DB db = new DB();
		deletePhoneDetailsFromDB(db, contactId, statementType);
		db.closeInstance();
	}

	private void deletePhoneDetailsFromDB(DB db, String contactId, DBStatementType statementType) throws SQLException {
		List<String> inParams = new ArrayList<>();
		inParams.add(contactId);
		db.updateTable(QueryConstants.DELETE_PHONE_LIST, inParams, statementType);
	}

	public void deleteEmailDetails(String contactId, DBStatementType statementType) throws SQLException {
		DB db = new DB();
		deleteEmailDetailsFromDB(db, contactId, statementType);
		db.closeInstance();
	}

	private void deleteEmailDetailsFromDB(DB db, String contactId, DBStatementType statementType) throws SQLException {
		List<String> inParams = new ArrayList<>();
		inParams.add(contactId);
		db.updateTable(QueryConstants.DELETE_EMAIL_LIST, inParams, statementType);
	}

	public void insertPhoneDetails(String contactId, String phonenumber, DBStatementType statementType)
			throws SQLException {
		DB db = new DB();
		insertPhoneDetailsFromDB(db, contactId, phonenumber, statementType);
		db.closeInstance();
	}

	private void insertPhoneDetailsFromDB(DB db, String contactId, String phoneNumber, DBStatementType statementType)
			throws SQLException {
		List<String> inParams = new ArrayList<>();
		inParams.add(contactId);
		inParams.add(phoneNumber);
		db.updateTable(QueryConstants.ADD_PHONE_NUMBER, inParams, statementType);
	}

	public void insertEmailDetails(String contactId, String email, DBStatementType statementType) throws SQLException {
		DB db = new DB();
		insertEmailDetailsFromDB(db, contactId, email, statementType);
		db.closeInstance();
	}

	private void insertEmailDetailsFromDB(DB db, String contactId, String email, DBStatementType statementType)
			throws SQLException {
		List<String> inParams = new ArrayList<>();
		inParams.add(contactId);
		inParams.add(email);
		db.updateTable(QueryConstants.ADD_EMAILS, inParams, statementType);
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
				List<String> phoneNumberList = new ArrayList<>();
				List<String> emailList = new ArrayList<>();
				deletePhoneDetails(Integer.toString(contact.getId()), statementType);
				for (String phoneNumber : mobiles) {
					insertPhoneDetailsFromDB(db, Integer.toString(contact.getId()), phoneNumber, statementType);
					phoneNumberList.add(phoneNumber);
				}
				deleteEmailDetails(Integer.toString(contact.getId()), statementType);
				for (String email : emails) {
					insertPhoneDetailsFromDB(db, Integer.toString(contact.getId()), email, statementType);
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

	public void addContactUsingThread(List<Object[]> contactList, DBStatementType statementType) {
		Map<Integer, Boolean> contactAdditionStatus = new HashMap<>();
		contactList.forEach(contact -> {
			Runnable task = () -> {
				try {
					contactAdditionStatus.put(contact.hashCode(), false);
					addContact((String) contact[0], (String) contact[1], (String) contact[2], (String) contact[3],
							(String) contact[4], (String) contact[5], (String) contact[6], (String[]) contact[7],
							(String[]) contact[8], (String) contact[9], (String) contact[10], statementType);
					contactAdditionStatus.put(contact.hashCode(), true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
			Thread thread = new Thread(task, (String) contact[0] + (String) contact[1]);
			thread.start();
		});
		while (contactAdditionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
	}

	public int addContactToAddressBook(String firstName, String lastName, String aadhar, String address, String city,
			String state, String zipCode, String addressbookId, DBStatementType statementType) throws SQLException {
		DB db = new DB();
		int contactId = addContactToAddressBookInDB(db, firstName, lastName, aadhar, address, city, state, zipCode,
				addressbookId, statementType);
		db.closeInstance();
		return contactId;
	}

	private int addContactToAddressBookInDB(DB db, String firstName, String lastName, String aadhar, String address,
			String city, String state, String zipCode, String addressbookId, DBStatementType statementType)
			throws SQLException {
		List<String> inParams = new ArrayList<>();
		inParams.add(firstName);
		inParams.add(lastName);
		inParams.add(aadhar);
		inParams.add(address);
		inParams.add(city);
		inParams.add(state);
		inParams.add(zipCode);
		inParams.add(addressbookId);

		return db.insertIntoTable(QueryConstants.ADD_CONTACT, inParams, statementType);
	}

	private int getAddressBookId(DB db, String addressbookName, String type, DBStatementType statementType)
			throws SQLException {
		List<String> inParams = new ArrayList<>();
		inParams.add(addressbookName);
		inParams.add(Integer.toString(Constants.ADDRESSBOOK_TYPE_MAP.get(type)));
		List<Map<String, Object>> resultset = db.getResultSet(QueryConstants.GET_ADDRESSBOOK_ID, inParams,
				statementType);
		if (resultset.size() > 0)
			return (int) resultset.get(0)
					.get("id");
		return 0;
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
			int addressbookId = getAddressBookId(db, addressbookName, type, statementType);
			if (addressbookId == 0)
				addressbookId = db.insertIntoTable(QueryConstants.ADD_ADDRESSBOOK, inParams, statementType);
			if (addressbookId > 0) {
				int newlyInsertedId = addContactToAddressBookInDB(db, firstName, lastName, aadhar, address, city, state,
						zipCode, Integer.toString(addressbookId), statementType);
				if (newlyInsertedId > 0) {
					List<String> phoneNumberList = new ArrayList<>();
					List<String> emailList = new ArrayList<>();
					for (String phoneNumber : mobiles) {
						insertPhoneDetailsFromDB(db, Integer.toString(newlyInsertedId), phoneNumber, statementType);
						phoneNumberList.add(phoneNumber);
					}
					for (String email : emails) {
						insertEmailDetailsFromDB(db, Integer.toString(newlyInsertedId), email, statementType);
						emailList.add(email);
					}
					addAddressBookToList(new Contacts(newlyInsertedId, firstName, lastName, address, city, state,
							zipCode, phoneNumberList, emailList, aadhar), addressbookName, type);
				}
			}
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

	private void addAddressBookToList(Contacts contact, String addressBookName, String addressBookType) {
		AddressBook addressbook = getAddressBook(addressbookList, addressBookName, addressBookType);
		if (addressbook != null) {
			addressbook.getContacts()
					.add(contact);
		} else {
			addressbookList
					.add(new AddressBook(addressBookName, addressBookType, new ArrayList<>(Arrays.asList(contact))));
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
