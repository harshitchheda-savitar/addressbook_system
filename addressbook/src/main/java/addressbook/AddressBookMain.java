package addressbook;

import java.util.Scanner;

import addressbook.models.AddressBook;
import addressbook.models.Contacts;
import addressbook.services.AddressBookService;

public class AddressBookMain {

	static final int ADD = 1;

	public static void main(String[] args) {
		AddressBookService addressBookService = new AddressBookService();

		// initialize an address-book
		AddressBook addressBook = new AddressBook();
		addressBookService.initializeAddressBook(addressBook);

		Scanner sc = new Scanner(System.in);
		int inputOption;

		boolean flag = true;
		while (flag) {
			System.out.println("Enter the option[1-ADD, 0-EXIT]:");
			inputOption = sc.nextInt();
			switch (inputOption) {
			case ADD:
				// get details of the contact
				Contacts contact = addressBookService.getDetails(sc);
				// add contact to address-book
				addressBookService.addContacts(addressBook, contact);
				System.out.println("SuccessFully Added!!!!!");
				System.out.println();
				break;
			default:
				flag = false;
				break;
			}

		}

		// Print the addressBook
		addressBookService.displayAddressBook(addressBook);
	}
}
