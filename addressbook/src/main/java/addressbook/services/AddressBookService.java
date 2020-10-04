package addressbook.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

	@Override
	public void editContacts(AddressBook addressBook, Contacts contact) {

		List<Contacts> contacts = addressBook.getContacts();
		contacts.add(contact);
		addressBook.setContacts(contacts);
	}

	@Override
	public void deleteContacts(int index, AddressBook addressBook) {
		List<Contacts> contacts = addressBook.getContacts();
		contacts.remove(index);
		addressBook.setContacts(contacts);
	}

	public Contacts getName(final Scanner sc) {
		Contacts contact = new Contacts();
		System.out.println("Enter the firstName:");
		contact.setFirstName(sc.next().trim());
		System.out.println("Enter the lastName:");
		contact.setLastName(sc.next().trim());
		return contact;
	}

	public Contacts getDetails(final Scanner sc) {

		// Take user-input
		Contacts contact = getName(sc);
		System.out.println("Enter the Adhaar number:");
		contact.setAdhaarNumber(sc.next().trim());
		System.out.println("Enter the Address:");
		contact.setAddress(sc.next().trim());
		System.out.println("Enter the city:");
		contact.setCity(sc.next().trim());
		System.out.println("Enter the state:");
		contact.setState(sc.next().trim());
		System.out.println("Enter the zipcode:");
		contact.setZip(sc.next().trim());

		System.out.println("How many Numbers you want to insert");
		int count = sc.nextInt();
		if (count > 0) {
			System.out.println("Enter the numbers followed by a new line for each number");
			List<String> mobNo = new ArrayList<>();
			for (int i = 0; i < count; i++) {
				mobNo.add(sc.next().trim());
			}
			contact.setMobNo(mobNo);
		}

		System.out.println("How many emailIds you want to insert");
		count = sc.nextInt();
		if (count > 0) {
			System.out.println("Enter the emailIds followed by a new line for each emailId");
			List<String> email = new ArrayList<>();
			for (int i = 0; i < count; i++) {
				email.add(sc.next().trim());
			}
			contact.setEmailId(email);
		}
		return contact;
	}

	public int searchForContact(final Scanner sc, AddressBook addressBook) {
		List<Contacts> contacts = addressBook.getContacts();

		Contacts contact = getName(sc);
		System.out.println("Enter the Adhaar number:");
		contact.setAdhaarNumber(sc.next().toLowerCase().trim());

		// Check if the key exists if so take the whole input from user all-over
		if (contacts.contains(contact)) {
			System.out.println("found contact matching your search!!");
			return contacts.indexOf(contact);
		} else {
			System.out.println("No contact found!!");
			return -1;
		}

	}
}