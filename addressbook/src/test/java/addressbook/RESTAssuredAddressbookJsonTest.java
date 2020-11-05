package addressbook;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import addressbook.models.AddressBook;
import addressbook.models.Contacts;
import addressbook.services.AddressBookDBService;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RESTAssuredAddressbookJsonTest {

	private AddressBookDBService addressBookDBService;
	private List<AddressBook> addressBookList;

	private AddressBook[] getAddressbookList() {
		Response response = RestAssured.get("/addressbook/list");
		System.out.println("At First:" + response.asString());
		AddressBook[] addressBooks = new Gson().fromJson(response.asString(), AddressBook[].class);
		return addressBooks;
	}

	private Response addAddressbookToJSONServer(AddressBook addressbook) {
		String addressBookJson = new Gson().toJson(addressbook);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(addressBookJson);
		return request.post("addressbook");
	}

	private Response updateAddressbookToJsonServer(AddressBook addressbook) {
		String addressBookJson = new Gson().toJson(addressbook);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(addressBookJson);
		return request.put("/addressbook/" + addressbook.getId());
	}

	private Response deleteAddressbookToJsonServer(AddressBook addressbook) {
		String addressBookJson = new Gson().toJson(addressbook);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(addressBookJson);
		return request.delete("/addressbook/" + addressbook.getId());
	}

	private boolean addAddressbookListToJSONServer(AddressBook[] addressbooks) {
		boolean[] flag = { true };
		Arrays.asList(addressbooks).forEach(addressbook -> {
			Response response = addAddressbookToJSONServer(addressbook);
			flag[0] = flag[0] && (response.getStatusCode() == 201);
		});
		return flag[0];
	}

	private Response addOrUpdateAddressbookToJSonServer(Contacts contact, String addressbookName,
			String addressbookType) {
		AddressBook addressBook = addressBookDBService.getAddressBook(addressBookList, addressbookName,
				addressbookType);
		if (addressBook == null) {
			addressBook = new AddressBook(0, addressbookName, addressbookType, new ArrayList<>(Arrays.asList(contact)));
			return addAddressbookToJSONServer(addressBook);
		} else {
			addressBook.getContacts().add(contact);
			return updateAddressbookToJsonServer(addressBook);
		}
	}

	@Before
	public void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 4000;
		addressBookDBService = new AddressBookDBService();
	}

	@Test
	public void onCallingList_ReturnAddressbookList() {
		AddressBook[] addressBooks = getAddressbookList();
		addressBookDBService.addToCacheList(Arrays.asList(addressBooks));
		assertEquals(3, addressBookDBService.countEntries());
	}

	@Test
	public void givenAddressbook_WhenAdded_ShouldReturn_200OR201Response() {
		AddressBook[] addressBooks = getAddressbookList();
		addressBookList = addressBookDBService.addToCacheList(Arrays.asList(addressBooks));

		Contacts contact = new Contacts(0, "Ashsih", "Chheda", "Borivali", "Mumbai", "Maharashtra", "400092",
				new ArrayList<>(Arrays.asList("1234567891", "1987654321")),
				new ArrayList<>(Arrays.asList("abc@gmail.com")), "1234567891");
		Response response = addOrUpdateAddressbookToJSonServer(contact, "Club", "Contacts");
		MatcherAssert.assertThat(response.getStatusCode(),
				Matchers.anyOf(Matchers.equalTo(200), Matchers.equalTo(201)));
	}

	@Test
	public void givenAddressbookList_WhenAdded_ShouldReturn_True() {
		AddressBook[] addressBooks = getAddressbookList();
		addressBookList = addressBookDBService.addToCacheList(Arrays.asList(addressBooks));

		Contacts contact = new Contacts(0, "Ashsih", "Chheda", "Borivali", "Mumbai", "Maharashtra", "400092",
				new ArrayList<>(Arrays.asList("1234567891", "1987654321")),
				new ArrayList<>(Arrays.asList("abc@gmail.com")), "1234567891");
		AddressBook[] addressbooks = new AddressBook[] {
				new AddressBook(0, "Club", "Contacts", new ArrayList<>(Arrays.asList(contact))),
				new AddressBook(0, "Club", "Friends", new ArrayList<>(Arrays.asList(contact))),
				new AddressBook(0, "Club", "Professionals", new ArrayList<>(Arrays.asList(contact))) };
		assertTrue(addAddressbookListToJSONServer(addressbooks));
	}

	@Test
	public void givenAddressbook_WhenUpdated_ShouldReturn200Response() {
		AddressBook[] addressBooks = getAddressbookList();
		addressBookList = addressBookDBService.addToCacheList(Arrays.asList(addressBooks));

		AddressBook addressBook = addressBookDBService.getAddressBook(addressBookList, "Club", "Contacts");
		Contacts contact = addressBookDBService.getContact(addressBook, "Ashsih", "Chheda", "1234567891");
		contact.setAddress("xyzsdsfs");

		Response response = updateAddressbookToJsonServer(addressBook);
		assertEquals(200, response.getStatusCode());
	}

	@Test
	public void givenAddressbook_WhenDeleted_ShouldReturn200Response() {
		AddressBook[] addressBooks = getAddressbookList();
		addressBookList = addressBookDBService.addToCacheList(Arrays.asList(addressBooks));

		AddressBook addressBook = addressBookDBService.getAddressBook(addressBookList, "Club", "Contacts");

		Response response = deleteAddressbookToJsonServer(addressBook);
		assertEquals(200, response.getStatusCode());

		addressBookDBService.removeFromList(addressBook);
	}
}
