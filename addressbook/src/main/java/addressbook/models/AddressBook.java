package addressbook.models;

import java.util.List;

public class AddressBook {
	private List<Contacts> contacts;

	public List<Contacts> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contacts> contacts) {
		this.contacts = contacts;
	}

	@Override
	public String toString() {
		return this.contacts.toString();
	}

}