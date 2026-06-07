package application;

public class MartyrNode {
	String Name, event, location, District, Gender, Age;
	MartyrNode next;

	// cons
	public MartyrNode(String Name, String event, String Age, String location, String District, String Gender) {
		setName(Name);
		setAge(Age);
		setEvent(event);
		setLocation(location);
		setDistrict(District);
		setGender(Gender);

	}

	// Setters & Getters
	public String getName() {
		return Name;
	}

	public void setName(String name) {
		this.Name = name;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDistrict() {
		return District;
	}

	public void setDistrict(String district) {
		District = district;
	}

	public String getGender() {
		return Gender;
	}

	// set (female/male) to gender
	public void setGender(String gender) {
		if (gender.equalsIgnoreCase("Gender")) {
			this.Age = gender;
		} else if (gender.equalsIgnoreCase("F") || gender.equalsIgnoreCase("M")) {
			this.Gender = gender;
		} else {
			throw new IllegalArgumentException("gender should be f or m ");
		}
	}

	public String getAge() {
		return Age;
	}

	// set age >0 or " "
	public void setAge(String age) {
		if (age.isEmpty() || age.equalsIgnoreCase("Age")) {
			this.Age = age;
		} else if (Integer.parseInt(age) < 0) {
			throw new IllegalArgumentException("Age must be a positive integer.");
		} else {
			this.Age = age;
		}
	}

	public MartyrNode getNext() {
		return next;
	}

	public void setNext(MartyrNode next) {
		this.next = next;
	}

	@Override
	public String toString() {
		return "MartyrNode [Name=" + Name + ", event=" + event + ", location=" + location + ", District=" + District
				+ ", Gender=" + Gender + ", Age=" + Age;
	}

}
