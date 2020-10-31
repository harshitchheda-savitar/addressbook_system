package addressbook.models;

import java.util.List;

public class AddressBook {
	private String name;
	private String type;
	private List<Contacts> contacts;

	public AddressBook() {
	}

	public AddressBook(String name, String type, List<Contacts> contacts) {
		this.name = name;
		this.type = type;
		this.contacts = contacts;
	}

	public List<Contacts> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contacts> contacts) {
		this.contacts = contacts;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "AddressBook [name=" + name + ", type=" + type + ", contacts=" + contacts + "]";
	}

}