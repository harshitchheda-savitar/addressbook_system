package addressbook;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import addressbook.models.AddressBook;
import addressbook.models.Contacts;
import addressbook.services.AddressBookService;

public class AddressBookMain {

	static final int ADD = 1;
	static final int EDIT = 2;
	static final int DELETE = 3;
	static final int BULK_ADD = 4;
	static final int ADD_ADDRESSBOOK = 5;

	public static void main(String[] args) throws IOException {
		AddressBookService addressBookService = new AddressBookService();

		// initialize an address-book
		AddressBook addressBook = new AddressBook();
		addressBookService.initializeAddressBook(addressBook);

		Map<String, AddressBook> addressBookMap;
		Map<String, List<Contacts>> cityMap = new HashMap<>();
		Map<String, List<Contacts>> stateMap = new HashMap<>();

		Scanner sc = new Scanner(System.in);
		int inputOption;

		boolean flag = true;
		while (flag) {
			addressBookMap = new HashMap<>();

			System.out.println("Enter option[1-ADD, 2-EDIT , 3-DELETE, 4-BULK-ADD, 5-SAVE_ADD_ADDRESSBOOK 0-EXIT]:");
			inputOption = sc.nextInt();
			switch (inputOption) {
			case ADD:
				Contacts contact = addressBookService.getDetails(sc);
				if (!addressBookService.checkIfContactExists(contact, addressBook)) {
					addressBookService.addContacts(addressBook, contact);
					System.out.println("SuccessFully Added!!!!!");
				} else
					System.out.println("Contact already exists!!!!!");
				System.out.println();
				break;
			case EDIT:
				// search for the contact in addressBook
				int indexOfSearchContact = addressBookService.searchForContact(sc, addressBook);
				if (indexOfSearchContact != -1) {
					// get details for the new contact to be added
					contact = addressBookService.getDetails(sc);
					// edit the contact in address-book
					addressBookService.editContacts(addressBook, contact);
					addressBookService.deleteContacts(indexOfSearchContact, addressBook);
				}
				System.out.println("SuccessFully Edited!!!");
				System.out.println();
				break;
			case DELETE:
				// search for the contact in addressBook
				indexOfSearchContact = addressBookService.searchForContact(sc, addressBook);
				if (indexOfSearchContact != -1) {
					// delete the contact
					addressBookService.deleteContacts(indexOfSearchContact, addressBook);
				}
				System.out.println("SuccessFully Deleted!!!");
				System.out.println();
				break;
			case BULK_ADD:
				addressBookService.bulkAddContacts(sc, addressBook);
				break;
			case ADD_ADDRESSBOOK:
				addressBookService.addMultipleBooks(sc, addressBookMap, addressBook);
				if (flag) {
					addressBook = new AddressBook();
					addressBookService.initializeAddressBook(addressBook);
				}
				break;
			default:
				flag = false;
				break;
			}
		}

		// Read the addressBook-list for file-System
		addressBookMap = addressBookService.readContactsFromJsonFile();

		// Print the addressBookMap
		System.out.println(addressBookMap.toString());

		// Search person by city or state across multiple addressBook using streams
		addressBookService.searchContactByCityOrState(sc, addressBookMap);

		addressBookService.readAddressBooks(cityMap, stateMap, addressBookMap);
		// Search by person by city-state using city/state dictionary
		addressBookService.searchPersonInCityState(sc, cityMap, stateMap);

		// Sort addressBook by name
		addressBookService.sortByName(addressBookMap);

		// sort addressBook by location
		addressBookService.sortByLocation(addressBookMap);

	}
}
