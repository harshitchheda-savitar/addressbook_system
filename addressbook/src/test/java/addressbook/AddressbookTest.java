package addressbook;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import addressbook.models.AddressBook;
import addressbook.models.Contacts;
import addressbook.services.AddressBookDBService;
import addressbook.services.DB;
import addressbook.services.DB.DBStatementType;
import addressbook.utils.QueryConstants;

public class AddressbookTest {

	private AddressBookDBService addressBookDBService;
	private List<AddressBook> addressbookList;

	@Before
	public void init() {
		addressBookDBService = new AddressBookDBService();
	}

	@Test
	public void givenAddressbookInDB_When_RetrievedShouldMatchAddressbookCount() throws SQLException {

		addressbookList = addressBookDBService.readData(QueryConstants.GET_ALL_ADDRESSBOOK, null,
				DBStatementType.CREATE_STATEMENT);
		assertEquals(3, addressbookList.size());
		assertEquals(7, addressbookList.get(0).getContacts().size());
	}

	@Test
	public void givenContact_WhenUpdated_ShouldMatchWithDB() throws SQLException {
		addressbookList = addressBookDBService.readData(QueryConstants.GET_ALL_ADDRESSBOOK, null,
				DBStatementType.CREATE_STATEMENT);
		addressBookDBService.updateContact("Harshit", "Chheda", "1234567891", "xtz", "Mumbai", "Maharashtra", "400092",
				new String[] { "1234567891", "1234567891" }, new String[] { "abx@gmail.com" }, "personal", "General",
				DBStatementType.PREPARED_STATEMENT);
		AddressBook addressBook = addressBookDBService.getAddressBook(addressbookList, "Personal", "General");
		Contacts contact = addressBookDBService.getContact(addressBook, "Harshit", "Chheda", "1234567891");
		assertEquals("400092", contact.getZip());
	}

	@Test
	public void givenContactInDB_When_RetrievedBetweenDateShouldMatchContactCount() throws SQLException {
		List<String> inParams = new ArrayList<>();
		inParams.add("2019-12-31");
		inParams.add("2020-06-01");
		addressbookList = addressBookDBService.readData(QueryConstants.GET_CONTACTS_BY_DATE, inParams,
				DBStatementType.PREPARED_STATEMENT);
		assertEquals(2, addressbookList.size());
	}

	@Test
	public void givenContactInDB_When_RetrievedByCityShouldMatchCount() throws SQLException {
		DB db = new DB();
		List<Map<String, Object>> result = db.getResultSet(QueryConstants.GET_CITY_COUNT, null,
				DBStatementType.PREPARED_STATEMENT);
		assertEquals("city", (String) result.get(0).get("city"));
		assertEquals(2, (long) result.get(0).get("city_count"));
		db.closeInstance();
	}

	@Test
	public void givenContactInDB_When_RetrievedByStateShouldMatchCount() throws SQLException {
		DB db = new DB();
		List<Map<String, Object>> result = db.getResultSet(QueryConstants.GET_STATE_COUNT, null,
				DBStatementType.PREPARED_STATEMENT);
		assertEquals("city", (String) result.get(0).get("state"));
		assertEquals(2, (long) result.get(0).get("state_count"));
		db.closeInstance();
	}

	@Test
	public void givenContact_WhenAdded_ShouldMatchWithDB() throws SQLException {
		addressbookList = addressBookDBService.readData(QueryConstants.GET_ALL_ADDRESSBOOK, null,
				DBStatementType.CREATE_STATEMENT);
		int size = addressbookList.size();
		addressBookDBService.addContact("Harshit", "Chheda", "1234567891", "xtz", "Mumbai", "Maharashtra", "400092",
				new String[] { "1234567891", "1234567891" }, new String[] { "abx@gmail.com" }, "personal", "General",
				DBStatementType.PREPARED_STATEMENT);
		assertEquals(size + 1, addressbookList.size());
	}

	@Test
	public void givenContacts_WhenAdded_UsingThreading_ShouldMatchWithDB() throws SQLException {
		Object[][] arrOfContacts = {
				new Object[] { "Harshit", "Chheda", "1234567891", "xtz", "Mumbai", "Maharashtra", "400092",
						new String[] { "1234567891", "1234567891" }, new String[] { "abx@gmail.com" }, "personal",
						"General" },
				new Object[] { "Harshit", "Chheda", "1234567891", "xtz", "Mumbai", "Maharashtra", "400092",
						new String[] { "1234567891", "1234567891" }, new String[] { "abx@gmail.com" }, "personal",
						"General" },
				new Object[] { "Harshit", "Chheda", "1234567891", "xtz", "Mumbai", "Maharashtra", "400092",
						new String[] { "1234567891", "1234567891" }, new String[] { "abx@gmail.com" }, "personal",
						"General" },
				new Object[] { "Harshit", "Chheda", "1234567891", "xtz", "Mumbai", "Maharashtra", "400092",
						new String[] { "1234567891", "1234567891" }, new String[] { "abx@gmail.com" }, "personal",
						"General" },
				new Object[] { "Harshit", "Chheda", "1234567891", "xtz", "Mumbai", "Maharashtra", "400092",
						new String[] { "1234567891", "1234567891" }, new String[] { "abx@gmail.com" }, "personal",
						"General" },
				new Object[] { "Harshit", "Chheda", "1234567891", "xtz", "Mumbai", "Maharashtra", "400092",
						new String[] { "1234567891", "1234567891" }, new String[] { "abx@gmail.com" }, "personal",
						"General" } };

		addressbookList = addressBookDBService.readData(QueryConstants.GET_ALL_ADDRESSBOOK, null,
				DBStatementType.CREATE_STATEMENT);
		int size = addressbookList.size();
		addressBookDBService.addContactUsingThread(Arrays.asList(arrOfContacts), DBStatementType.PREPARED_STATEMENT);
		assertEquals(size + 1, addressbookList.size());
	}
}
