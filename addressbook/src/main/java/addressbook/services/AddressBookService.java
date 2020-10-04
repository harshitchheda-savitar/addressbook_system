package addressbook.services;

import java.util.ArrayList;
import java.util.List;

import addressbook.interfaces.AddressBookInterface;
import addressbook.models.AddressBook;
import addressbook.models.Contacts;

public class AddressBookService implements AddressBookInterface {

	@Override
	public void initializeAddressBook(AddressBook addressBook) {
		addressBook.setContacts(new ArrayList<>());
	}

	@Override
	public void addContacts(AddressBook addressBook, Contacts contact) {
		List<Contacts> contacts = addressBook.getContacts();
		contacts.add(contact);
		addressBook.setContacts(contacts);
	}

	@Override
	public void displayAddressBook(AddressBook addressBook) {
		System.out.println(addressBook.getContacts().toString());
	}

}