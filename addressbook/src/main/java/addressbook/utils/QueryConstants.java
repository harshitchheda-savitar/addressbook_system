package addressbook.utils;

public class QueryConstants {
	public static final String ADD_ADDRESSBOOK = "INSERT INTO address_book(addressbook_name,addressbook_type_id) VALUES (?, ?)";
	public static final String ADD_CONTACT = "INSERT INTO contact(first_name,last_name,aadhar,address,city,state,zip,addressbook_name_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String ADD_PHONE_NUMBER = "INSERT INTO mobile_data(contact_id, phone_number) VALUES (?, ?)";
	public static final String ADD_EMAILS = "INSERT INTO email_data(contact_id, email_id) VALUES (?, ?)";
	public static final String GET_ALL_ADDRESSBOOK = "SELECT A.id,A.addressbook_name,T.type_name FROM address_book A INNER JOIN addressbook_type T ON A.addressbook_type_id = T.id WHERE A.is_active = 1";
	public static final String GET_CONTACTS_BY_DATE = "SELECT DISTINCT A.id,A.addressbook_name,T.type_name FROM address_book A INNER JOIN addressbook_type T ON A.addressbook_type_id = T.id INNER JOIN contact C ON C.addressbook_name_id = A.id WHERE A.is_active = 1 AND C.is_active = 1 AND (C.created_date BETWEEN ? AND ?)";
	public static final String GET_ALL_CONTACTS = "SELECT C.id,C.first_name,C.last_name,C.aadhar,C.address,C.city,C.state,C.zip FROM contact C INNER JOIN address_book A ON A.id = C.addressbook_name_id WHERE A.is_active = 1 AND C.is_active = 1 AND A.id = ?";
	public static final String GET_ALL_PHONE_LIST = "SELECT M.phone_number FROM contact C INNER JOIN mobile_data M ON M.contact_id = C.id WHERE C.is_active = 1 AND C.id = ?";
	public static final String GET_ALL_EMAIL_LIST = "SELECT E.email_id FROM contact C INNER JOIN email_data E ON E.contact_id = C.id WHERE C.is_active = 1 AND C.id = ?";
	public static final String UPDATE_CONTACT_DETAILS = "UPDATE contact C INNER JOIN address_book A ON A.id=C.addressbook_name_id SET C.first_name = ? AND C.last_name = ? AND C.aadhar = ? AND C.address = ? AND C.city = ? AND C.state = ? AND C.zip = ? WHERE A.is_active = 1 AND C.is_active = 1 AND A.addressbook_name = ? AND A.addressbook_type_id = ?";
	public static final String DELETE_PHONE_LIST = "DELETE FROM mobile_data WHERE contact_id = ?";
	public static final String DELETE_EMAIL_LIST = "DELETE FROM email_data WHERE contact_id = ?";
	public static final String DELETE_CONTACT = "UPDATE contact C INNER JOIN address_book A ON A.id = C.addressbook_name_id SET C.is_active = 0 WHERE C.first_name = ? AND C.last_name = ? AND C.aadhar = ? AND A.addressbook_name = ? AND A.addressbook_type_id = ?";
	public static final String GET_CITY_COUNT = "SELECT city,COUNT(city) AS city_count FROM contact GROUP BY city";
	public static final String GET_STATE_COUNT = "SELECT state,COUNT(state) AS state_count FROM contact GROUP BY state";
}
