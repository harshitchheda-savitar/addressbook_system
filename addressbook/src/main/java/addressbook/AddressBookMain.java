package addressbook;

import addressbook.models.AddressBook;
import addressbook.models.Contacts;
import addressbook.services.AddressBookService;

public class AddressBookMain {
	public static void main(String[] args) {
		AddressBookService addressBookService = new AddressBookService();

		// initialize an address-book
		AddressBook addressBook = new AddressBook();
		addressBookService.initializeAddressBook(addressBook);

		// Get details for the contact
		Contacts contact = new Contacts();
		contact.setFirstName("harshit");
		contact.setLastName("chheda");
		contact.setAddress("somewhere");
		contact.setCity("Mumbai");
		contact.setState("Maharashtra");
		contact.setZip("101010");
		contact.setAdhaarNumber("123512341234");

		// Add contact
		addressBookService.addContacts(addressBook, contact);

		// Print the addressBook
		addressBookService.displayAddressBook(addressBook);
	}
}
