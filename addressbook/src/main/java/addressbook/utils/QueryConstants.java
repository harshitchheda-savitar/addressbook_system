package addressbook.utils;

public class QueryConstants {
	public static final String ADD_ADDRESSBOOK = "INSERT INTO address_book(addressbook_name,addressbook_type_id) VALUES (?, ?)";
	public static final String ADD_CONTACT = "INSERT INTO contact(first_name,last_name,aadhar,address,city,state,zip,addressbook_name_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String ADD_PHONE_NUMBER = "INSERT INTO mobile_data(contact_id, phone_number) VALUES (?, ?)";
	public static final String ADD_EMAILS = "INSERT INTO email_data(contact_id, email_id) VALUES (?, ?)";
	public static final String GET_ALL_ADDRESSBOOK = "SELECT A.id,A.addressbook_name,T.type_name FROM address_book A INNER JOIN addressbook_type T ON A.addressbook_type_id = T.id WHERE A.is_active = 1";
	public static final String GET_ALL_CONTACTS = "SELECT C.id,C.first_name,C.last_name,C.aadhar,C.address,C.city,C.state,C.zip FROM contact C INNER JOIN address_book A ON A.id = C.addressbook_name_id WHERE A.is_active = 1 AND C.is_active = 1 AND A.id = ?";
	public static final String GET_ALL_PHONE_LIST = "SELECT M.phone_number FROM contact C INNER JOIN mobile_data M ON M.contact_id = C.id WHERE C.is_active = 1";
	public static final String GET_ALL_EMAIL_LIST = "SELECT E.email_id FROM contact C INNER JOIN email_data E ON E.contact_id = C.id WHERE C.is_active = 1";
}