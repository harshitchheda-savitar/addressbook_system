package addressbook.models;

import java.util.Arrays;
import java.util.List;

public class Contacts {

	private String firstName;
	private String lastName;
	private String address;
	private String city;
	private String state;
	private String zip;
	private List<String> mobNo;
	private List<String> emailId;
	private String adhaarNumber;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public List<String> getMobNo() {
		return mobNo;
	}

	public void setMobNo(List<String> mobNo) {
		this.mobNo = mobNo;
	}

	public List<String> getEmailId() {
		return emailId;
	}

	public void setEmailId(List<String> emailId) {
		this.emailId = emailId;
	}

	public String getAdhaarNumber() {
		return adhaarNumber;
	}

	public void setAdhaarNumber(String adhaarNumber) {
		this.adhaarNumber = adhaarNumber;
	}

	@Override
	public String toString() {

		if (this.emailId == null && this.mobNo == null)
			return "{firstName:" + this.firstName + ",lastName:" + this.lastName + ",adhaar:" + this.adhaarNumber
					+ ",address:" + this.address + ",city:" + this.city + ",state:" + this.state + ",zip:" + this.zip
					+ ",phoneNumbers:" + "null" + ",emails:" + "null" + "}";

		if (this.mobNo == null)
			return "{firstName:" + this.firstName + ",lastName:" + this.lastName + ",adhaar:" + this.adhaarNumber
					+ ",address:" + this.address + ",city:" + this.city + ",state:" + this.state + ",zip:" + this.zip
					+ ",phoneNumbers:" + "null" + ",emails:" + Arrays.toString(this.emailId.toArray()) + "}";

		if (this.emailId == null)
			return "{firstName:" + this.firstName + ",lastName:" + this.lastName + ",adhaar:" + this.adhaarNumber
					+ ",address:" + this.address + ",city:" + this.city + ",state:" + this.state + ",zip:" + this.zip
					+ ",phoneNumbers:" + Arrays.toString(this.mobNo.toArray()) + ",emails:" + "null" + "}";

		return "{firstName:" + this.firstName + ",lastName:" + this.lastName + ",adhaar:" + this.adhaarNumber
				+ ",address:" + this.address + ",city:" + this.city + ",state:" + this.state + ",zip:" + this.zip
				+ ",phoneNumbers:" + Arrays.toString(this.mobNo.toArray()) + ",emails:"
				+ Arrays.toString(this.emailId.toArray()) + "}";
	}
}