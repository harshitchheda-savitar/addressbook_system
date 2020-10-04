package addressbook.interfaces;

import addressbook.models.AddressBook;
import addressbook.models.Contacts;

public interface AddressBookInterface {

	public void initializeAddressBook(AddressBook addressBook);

	public void addContacts(AddressBook addressBook, Contacts contact);

	public void displayAddressBook(AddressBook addressBook);
}
