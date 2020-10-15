package addressbook.models;

public class Contacts {

	private String firstName;
	private String lastName;
	private String address;
	private String city;
	private String state;
	private String zip;
	private String mobNo;
	private String emailId;
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

	public String getMobNo() {
		return mobNo;
	}

	public void setMobNo(String mobNo) {
		this.mobNo = mobNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
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
					+ ",phoneNumbers:" + "null" + ",emails:" + this.emailId + "}";

		if (this.emailId == null)
			return "{firstName:" + this.firstName + ",lastName:" + this.lastName + ",adhaar:" + this.adhaarNumber
					+ ",address:" + this.address + ",city:" + this.city + ",state:" + this.state + ",zip:" + this.zip
					+ ",phoneNumbers:" + this.mobNo + ",emails:" + "null" + "}";

		return "{firstName:" + this.firstName + ",lastName:" + this.lastName + ",adhaar:" + this.adhaarNumber
				+ ",address:" + this.address + ",city:" + this.city + ",state:" + this.state + ",zip:" + this.zip
				+ ",phoneNumbers:" + this.mobNo + ",emails:" + this.emailId + "}";
	}

	@Override
	public boolean equals(Object object) {
		boolean result = false;
		if (object == null || object.getClass() != getClass()) {
			result = false;
		} else {
			Contacts contact = (Contacts) object;
			if (this.firstName.equals(contact.getFirstName()) && this.lastName.equals(contact.getLastName())
					&& this.adhaarNumber.equals(contact.getAdhaarNumber())) {
				result = true;
			}
		}
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result + (this.firstName != null ? this.firstName.hashCode() : 0);
		result = prime * result + (this.lastName != null ? this.lastName.hashCode() : 0);
		result = prime * result + (this.adhaarNumber != null ? this.adhaarNumber.hashCode() : 0);

		return result;
	}
}