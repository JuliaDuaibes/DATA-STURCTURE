package application;

public class martyrNode {
	String Name, event, location, District, Gender, Age;
	martyrNode left, right;
	int height;

	// Constructor
	public martyrNode(String Name, String event, String Age, String location, String District, String Gender) {
		setName(Name);
		setAge(Age);
		setEvent(event);
		setLocation(location);
		setDistrict(District);
		setGender(Gender);
		this.left = null;
		this.right = null;
		this.height = 0; // Initially, height is set to 0
	}

	public martyrNode getLeft() {
		return left;
	}

	public void setLeft(martyrNode left) {
		this.left = left;
	}

	public martyrNode getRight() {
		return right;
	}

	public void setRight(martyrNode right) {
		this.right = right;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
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
		this.District = district;
	}

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		if (gender.equalsIgnoreCase("Gender")) {
			this.Gender = gender;
		} else if (gender.equalsIgnoreCase("F") || gender.equalsIgnoreCase("M")) {
			this.Gender = gender;
		} else {
			throw new IllegalArgumentException("Gender should be F or M");
		}
	}

	public String getAge() {
		return Age;
	}

	public void setAge(String age) {
		if (age.isEmpty() || age.equalsIgnoreCase("Age")) {
			this.Age = age;
		} else if (Integer.parseInt(age) < 0) {
			throw new IllegalArgumentException("Age must be a positive integer.");
		} else {
			this.Age = age;
		}
	}

	@Override
	public String toString() {
		return "Name=" + Name + ", event=" + event + ", location=" + location + ", District=" + District + ", Gender="
				+ Gender + ", Age=" + Age;
	}
}
