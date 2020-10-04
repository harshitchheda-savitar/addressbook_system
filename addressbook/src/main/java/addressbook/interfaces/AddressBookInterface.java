package addressbook.interfaces;

import addressbook.models.AddressBook;
import addressbook.models.Contacts;

public interface AddressBookInterface {

	public void initializeAddressBook(AddressBook addressBook);

	public void addContacts(AddressBook addressBook, Contacts contact);

	public void editContacts(AddressBook addressBook, Contacts contact);

	public void deleteContacts(int index, AddressBook addressBook);

	public void displayAddressBook(AddressBook addressBook);
}
