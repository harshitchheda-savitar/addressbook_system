package addressbook.models;

import java.util.List;

public class AddressBook {
	private int id;
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

	public AddressBook(int id, String name, String type, List<Contacts> contacts) {
		this(name, type, contacts);
		this.setId(id);
	}

	public List<Contacts> getContacts() {
		return contacts;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		AddressBook other = (AddressBook) obj;
		return contacts.equals(other.contacts) && name.equals(other.name) && type.equals(other.type);
	}

}