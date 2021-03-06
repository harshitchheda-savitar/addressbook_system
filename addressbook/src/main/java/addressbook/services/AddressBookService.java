package addressbook.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.Gson;

import addressbook.interfaces.AddressBookInterface;
import addressbook.models.AddressBook;
import addressbook.models.Contacts;

public class AddressBookService implements AddressBookInterface {

	private static final int STATE = 1;
	private static final int CITY = 2;
	private static final String HOME = "D:/training/addressbook_system/addressbook/src/main/java/resources/";

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

	public void addContactsStateCityWise(Map<String, List<Contacts>> cityMap, Map<String, List<Contacts>> stateMap,
			Contacts contact) {

		cityMap.computeIfAbsent(contact.getCity(), key -> new ArrayList<>()).add(contact);
		stateMap.computeIfAbsent(contact.getState(), key -> new ArrayList<>()).add(contact);
	}

	public void readAddressBooks(Map<String, List<Contacts>> cityMap, Map<String, List<Contacts>> stateMap,
			Map<String, AddressBook> addressBookMap) {
		for (String addressBook : addressBookMap.keySet()) {
			for (Contacts contact : addressBookMap.get(addressBook).getContacts()) {
				addContactsStateCityWise(cityMap, stateMap, contact);
			}
		}
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

	public void bulkAddContacts(final Scanner sc, AddressBook addressBook) {
		System.out.println("How many Contacts you want to insert");
		int count = sc.nextInt();
		if (count > 0) {
			for (int i = 0; i < count; i++) {
				System.out.println("Enter the contact details for person " + (i + 1));
				Contacts contact = getDetails(sc);
				addContacts(addressBook, contact);
			}
			System.out.println("Successfully Added " + count + " Contacts");
			System.out.println();
		}
	}

	public boolean addMultipleBooks(final Scanner sc, Map<String, AddressBook> map, AddressBook addressBook)
			throws IOException {
		System.out.println("Give a name to your addressBook");
		String name = sc.next().trim().toLowerCase();
		map.put(name, addressBook);

		Path homePath = Paths.get(HOME + name + ".json");
		addContactsToJsonFile(addressBook, homePath);

		System.out.println("Successfully saved your addressBook.... Wanna add more addressBooks !?");
		System.out.println("1 - yes , 2 - no");
		return sc.nextInt() == 1;
	}

	private void addContactsToJsonFile(AddressBook addressBook, Path filePath) throws IOException {

		Gson gson = new Gson();
		StringBuilder contactString = new StringBuilder();
		addressBook.getContacts().forEach(contact -> contactString.append("\n").append(gson.toJson(contact)));
		Files.write(filePath, contactString.toString().getBytes(), StandardOpenOption.CREATE,
				StandardOpenOption.APPEND);

	}

	public Map<String, AddressBook> readContactsFromJsonFile() throws IOException {

		Map<String, AddressBook> map = new HashMap<>();
		Path homePath = Paths.get(HOME);
		Gson gson = new Gson();
		Files.list(homePath).filter(path -> path.toString().contains(".json")).forEach(file -> {
			List<Contacts> contacts = new ArrayList<>();
			try (Stream<String> addressBookStream = Files.lines(file)) {

				addressBookStream.forEach(contact -> {
					if (!contact.isEmpty()) {
						Contacts cont = gson.fromJson(contact, Contacts.class);
						contacts.add(cont);
					}
				});

				AddressBook addressBook = new AddressBook();
				addressBook.setContacts(contacts);
				map.put(file.getFileName().toString().split(".json")[0], addressBook);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		return map;
	}

	public boolean checkIfContactExists(Contacts contact, AddressBook addressBook) {
		long count = addressBook.getContacts().stream().filter(cont -> cont.equals(contact)).count();
		return count > 0;
	}

	public void searchContactByCityOrState(final Scanner sc, Map<String, AddressBook> addressBoookMap) {
		Contacts contact = getName(sc);
		System.out.println("Enter the city:");
		contact.setCity(sc.next().toLowerCase().trim());
		System.out.println("Enter the state:");
		contact.setState(sc.next().toLowerCase().trim());

		List<Contacts> contacts;

		for (String key : addressBoookMap.keySet()) {
			System.out.println("Searching in addressBook : " + key);

			System.out.println("Searching by state :");
			contacts = searchContactByCondition(addressBoookMap.get(key).getContacts(), contact, STATE);
			if (contacts.size() > 0) {
				System.out.println(contacts.toString());
				System.out.println("Searching by city : ");
				contacts = searchContactByCondition(addressBoookMap.get(key).getContacts(), contact, CITY);
				System.out.println(contacts.toString());
			} else
				System.out.println("No contact found by state and city");

		}
	}

	public List<Contacts> searchContactByCondition(List<Contacts> contactsList, Contacts contact, int flag) {
		List<Contacts> contacts = null;
		switch (flag) {
		case STATE:
			contacts = contactsList.stream()
					.filter(cont -> cont.getFirstName().equals(contact.getFirstName())
							&& cont.getLastName().equals(contact.getLastName())
							&& cont.getState().equals(contact.getState()))
					.collect(Collectors.toList());
			break;
		case CITY:
			contacts = contactsList.stream()
					.filter(cont -> cont.getFirstName().equals(contact.getFirstName())
							&& cont.getLastName().equals(contact.getLastName())
							&& cont.getCity().equals(contact.getCity()))
					.collect(Collectors.toList());
			break;
		}

		return contacts;
	}

	public void searchPersonInCityState(final Scanner sc, Map<String, List<Contacts>> cityMap,
			Map<String, List<Contacts>> stateMap) {
		Contacts contact = getName(sc);
		System.out.println("Enter the city:");
		contact.setCity(sc.next().toLowerCase().trim());
		System.out.println("Enter the state:");
		contact.setState(sc.next().toLowerCase().trim());

		// Search by state
		if (stateMap.containsKey(contact.getState())) {
			System.out.println("Searching by state :");
			List<Contacts> contacts = searchContactByCondition(stateMap.get(contact.getState()), contact, STATE);
			if (contacts.size() > 0) {
				System.out.println("contacts found : " + contacts.size());
				System.out.println(contacts.toString());
			} else
				System.out.println("No contact found by state");
		} else
			System.out.println("No contact found by state");

		// Search by city
		if (cityMap.containsKey(contact.getCity())) {
			System.out.println("Searching by city : ");
			List<Contacts> contacts = searchContactByCondition(cityMap.get(contact.getCity()), contact, CITY);
			if (contacts.size() > 0) {
				// print the count of contacts
				System.out.println("contacts found : " + contacts.size());
				System.out.println(contacts.toString());
			} else
				System.out.println("No contact found by city");
		} else
			System.out.println("No contact found by city");
	}

	public void sortByName(Map<String, AddressBook> map) {
		List<Contacts> sortedContacts;

		for (String key : map.keySet()) {
			sortedContacts = map.get(key).getContacts().stream()
					.sorted(Comparator.comparing(Contacts::getFirstName).thenComparing(Contacts::getLastName))
					.collect(Collectors.toList());

			System.out.println(sortedContacts.toString());
		}
	}

	public void sortByLocation(Map<String, AddressBook> map) {
		sortByState(map);
		sortByCity(map);
		sortByZipcode(map);
	}

	private void sortByCity(Map<String, AddressBook> map) {
		List<Contacts> sortedContacts;

		for (String key : map.keySet()) {
			sortedContacts = map.get(key).getContacts().stream().sorted(Comparator.comparing(Contacts::getCity))
					.collect(Collectors.toList());

			System.out.println(sortedContacts.toString());
		}
	}

	private void sortByState(Map<String, AddressBook> map) {
		List<Contacts> sortedContacts;

		for (String key : map.keySet()) {
			sortedContacts = map.get(key).getContacts().stream().sorted(Comparator.comparing(Contacts::getState))
					.collect(Collectors.toList());

			System.out.println(sortedContacts.toString());
		}
	}

	private void sortByZipcode(Map<String, AddressBook> map) {
		List<Contacts> sortedContacts;

		for (String key : map.keySet()) {
			sortedContacts = map.get(key).getContacts().stream().sorted(Comparator.comparing(Contacts::getZip))
					.collect(Collectors.toList());

			System.out.println(sortedContacts.toString());
		}
	}

}