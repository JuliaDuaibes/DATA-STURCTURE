package application;

public class martyrList {

	MartyrNode head, tail;
	int martyrCount;

	// insert martyr
	public void insert(String name, String event, String age, String location, String district, String gender) {
		MartyrNode newNode = new MartyrNode(name, event, age, location, district, gender);
		if (head == null) {
			head = tail = newNode;
			tail.next = head;
		} else if (newNode.Age.compareTo(head.Age) < 0) { // Insert before head
			newNode.next = head;
			head = newNode;
			tail.next = head; // Update tail's next pointer to maintain circularity
		} else {
			// Insert between two nodes or at the end
			MartyrNode current = head;
			while (current.next.Age.compareTo(newNode.Age) <= 0) { // Simplified loop condition
				current = current.next;
				if (current.next == head) { // Insert at the end
					break;
				}
			}
			newNode.next = current.next;
			current.next = newNode;
			// Update tail if newNode is inserted at the end
			if (newNode.next == head) {
				tail = newNode;
			}
		}
		martyrCount++;
	}

	// display martyr list
	public void display() {
		if (head == null) {
			System.out.println("The martyr list is empty.");
			return;
		}

		MartyrNode current = head;

		do {
			System.out.println(current.toString());
			// Move to the next node
			current = current.next;
		} while (current != head);
	}

	// update district name in martyr list
	public void updateDistrict(String districtName, String newDistrict) {
		if (head == null) {
			return; // No martyrs to update
		}

		MartyrNode current = head;
		while (current.next != head) {
			// check if martyr exists martyr in district
			if (current.District.equalsIgnoreCase(districtName)) {
				current.District = newDistrict; // Update district for this martyr
			}
			current = current.next;
		}

	}

	public void deleteDistrict(String districtName) {
		if (head == null) {
			return; // No martyrs to delete
		}

		// check If the head node matches districtName
		while (head != null && head.District.equalsIgnoreCase(districtName)) {
			head = head.next;
			martyrCount--;
		}

		// If the list still contains nodes after deleting head
		if (head != null) {
			MartyrNode current = head;

			// nodes whose District matches districtName
			do {
				if (current.next.District.equalsIgnoreCase(districtName)) {
					current.next = current.next.next;
					martyrCount--;
				} else {
					current = current.next;
				}
			} while (current != head);

			// Check if the last node matches districtName
			if (current.District.equalsIgnoreCase(districtName)) {
				current.next = head; // Make the last node point to head( circular)
				martyrCount--;
			}
		}
	}

	public void updateLocationName(String oldLocationName, String newLocationName) {
		if (head == null) {
			return; // No martyrs to update
		}

		MartyrNode current = head;
		while (current.next != head) {
			// Check if location matches current martyr's location
			if (current.location.equalsIgnoreCase(oldLocationName)) {
				current.location = newLocationName; // Update location for this martyr
			}
			current = current.next;
		}

	}

	public void deleteLocation(String locationName) {
		if (head == null)
			return; // No martyrs to delete

		// If the head node matches districtName, delete it
		while (head != null && head.location.equals(locationName)) {
			head = head.next;
			martyrCount--;
		}

		// If the list still contains nodes after deleting head
		if (head != null) {
			MartyrNode current = head;

			// delete nodes whose District matches districtName
			do {
				if (current.next.location.equalsIgnoreCase(locationName)) {
					current.next = current.next.next;
					martyrCount--;
				} else {
					current = current.next;
				}
			} while (current != head);

			// Check if the last node matches districtName
			if (current.location.equalsIgnoreCase(locationName)) {
				current.next = head; // Make the last node point to head( circular)
				martyrCount--;
			}
		}
	}

	public int numOfFemale(String locationName) {// number of female in location(locationName)
		int totalF = 0;
		MartyrNode current = head;
		if (current == null) {
			return 0;
		} else {
			while (current.next != head) {
				if (current.Gender.equalsIgnoreCase("f") && current.location.equalsIgnoreCase(locationName)) {
					totalF++;
				}
				current = current.next;
			}
			return totalF;
		}

	}

	public int numOfMale(String locationName) {// number of male in location(locationName)
		int totalF = 0;
		MartyrNode current = head;
		if (current == null) {
			return 0;
		} else {
			while (current.next != head) {
				if (current.Gender.equalsIgnoreCase("m") && current.location.equalsIgnoreCase(locationName)) {
					totalF++;
				}
				current = current.next;
			}
			return totalF;
		}

	}

	public int avergeAge(String locationName) {// averge age in location(locationName)
		int age = 0, count = 0;

		MartyrNode current = head;
		if (current == null) {
			return 0; // Return 0 if the list is empty
		} else {
			while (current.next != head) {
				if (current.location.equalsIgnoreCase(locationName)) {
					if (!current.Age.isEmpty()) {// " "
						age += Integer.parseInt(current.Age);
					}
					count++;
				}
				current = current.next;
			}
			// Check if count is 0 to avoid dividing by 0
			if (count != 0) {
				return age / count;
			} else {
				return 0; // Return 0 if there are no valid ages
			}
		}
	}

	public void removeMartyr(String name) {// remove martyr from martyr list
		if (head == null) {
			return;
		}

		// Check if the martyr to remove is at the head of the list
		if (head.Name.equals(name)) {
			head = head.next;
			tail.next = head;
			martyrCount--;
			return;
		}

		// Check for the martyr to remove in the rest of the list
		MartyrNode current = head;
		while (current.next != head) {
			if (current.next.Name.equals(name)) {
				// If the martyr to remove is found, remove it from the list
				current.next = current.next.next;
				martyrCount--;
				return;
			}
			current = current.next;
		}

		// Check if the martyr to remove is at the tail of the list
		if (tail.Name.equals(name)) {
			MartyrNode currentTail = head;
			while (currentTail.next != tail) {
				currentTail = currentTail.next;
			}
			currentTail.next = head;
			tail = currentTail;
			martyrCount--;
		}
	}

	public boolean ifExistMartyr(String martyrName) {// return martyr node by martyr name from martyr list
		if (head == null) {
			return false;
		} else {
			MartyrNode current = head;
			do {
				if (martyrName.equalsIgnoreCase(current.Name)) {// check if match
					return true;
				}
				current = current.next;
			} while (current != head);
			return false;
		}
	}

	public void setName(String oldName, String newName) {// update Name for specific martyr
		MartyrNode current = head;
		do {
			if (oldName.equalsIgnoreCase(current.Name)) {
				current.setName(newName);
			}
			current = current.next;
		} while (current != head);

	}

	public void setAge(String oldName, String newAge) {// update Age for specific martyr
		MartyrNode current = head;
		do {
			if (oldName.equalsIgnoreCase(current.Name)) {
				current.setAge(newAge);
			}
			current = current.next;
		} while (current != head);

	}

	public void setEvent(String Name, String newEvent) {// update Event for specific martyr
		MartyrNode current = head;
		do {
			if (Name.equalsIgnoreCase(current.Name)) {
				current.setEvent(newEvent);
			}
			current = current.next;
		} while (current != head);
	}

	public void setGender(String Name, String newGender) {// update Gender for specific martyr
		MartyrNode current = head;
		do {
			if (Name.equalsIgnoreCase(current.Name)) {
				current.setGender(newGender);
			}
			current = current.next;
		} while (current != head);

	}

	public void setLocation(String Name, String newLocation) {// update location name
		MartyrNode current = head;
		do {
			if (Name.equalsIgnoreCase(current.Name)) {
				current.setLocation(newLocation);
			}
			current = current.next;
		} while (current != head);

	}

	public void setDistrict(String Name, String newDistrict) {// update district name
		MartyrNode current = head;
		do {
			if (Name.equalsIgnoreCase(current.Name)) {
				current.setDistrict(newDistrict);
			}
			current = current.next;
		} while (current != head);

	}

	public int TotalMartyrInLocation(String LocationName) {// return total martyr in specific location
		if (head == null) {
			return 0;
		}
		MartyrNode m = head;
		int total = 0;
		do {
			if (m.location.equalsIgnoreCase(LocationName)) {
				total += 1;
			}
			m = m.next;
		} while (m != head);
		return total;
	}

	public MartyrNode oldestMartyr(String locationName) {// return oldest martyr in specific location
		MartyrNode oldest = null;
		MartyrNode current = head;

		do {
			if (current.location.equalsIgnoreCase(locationName)) {
				if (oldest == null || current.Age.compareTo(oldest.Age) > 0) {
					oldest = current;
				}
			}
			current = current.next;
		} while (current != head);

		return oldest;
	}

	public MartyrNode youngestMartyr(String locationName) {// return youngest martyr in specific location
		MartyrNode youngest = null;
		MartyrNode current = head;

		do {
			if (current.location.equalsIgnoreCase(locationName)) {
				if (youngest == null || current.Age.compareTo(youngest.Age) < 0) {
					youngest = current;
				}
			}
			current = current.next;
		} while (current != head);

		return youngest;
	}

	public int TotalMartyrInDistrict(String districtName) {// total martyr in specific district
		if (head == null) {
			return 0;
		}
		MartyrNode m = head;
		int total = 0;
		do {
			if (m.District.equalsIgnoreCase(districtName)) {
				total += 1;
			}
			m = m.next;
		} while (m != head);
		return total;
	}

	public int numOfFemaleIndistrict(String districtName) {// total female martyr in specific district
		int totalF = 0;
		MartyrNode current = head;
		if (current == null) {
			return 0;
		} else {
			while (current.next != head) {
				if (current.Gender.equalsIgnoreCase("f") && current.District.equalsIgnoreCase(districtName)) {
					totalF++;
				}
				current = current.next;
			}
			return totalF;
		}

	}

	public int numOfMaleIndistrict(String districtName) {// total male martyr in specific district
		int totalF = 0;
		MartyrNode current = head;
		if (current == null) {
			return 0;
		} else {
			while (current.next != head) {
				if (current.Gender.equalsIgnoreCase("m") && current.District.equalsIgnoreCase(districtName)) {
					totalF++;
				}
				current = current.next;
			}
			return totalF;
		}

	}

	public int avergeAgeIndistrict(String districtName) {// averge age martyr in specific district
		int age = 0, count = 0;

		MartyrNode current = head;
		if (current == null) {
			return 0; // Return 0 if the list is empty
		} else {
			while (current.next != head) {
				if (current.District.equalsIgnoreCase(districtName)) {
					if (!current.Age.isEmpty()) {
						age += Integer.parseInt(current.Age);
						count++;
					}
				}
				current = current.next;
			}
			// Check if count is 0 to avoid dividing by 0
			if (count != 0) {
				return age / count;
			} else {
				return 0; // Return 0 if there are no valid ages
			}
		}
	}

	public int getTotalMartyrsForDate(String date, String districtName) {// total martyr in specific district in
																			// specific date
		int totalMartyrs = 0;

		MartyrNode current = head;
		if (current == null) {
			return 0; // Return 0 if the list is empty
		} else {
			do {
				if (current.District.equalsIgnoreCase(districtName)) {
					if (current.event.equalsIgnoreCase(date)) {
						totalMartyrs++;
					}
				}
				current = current.next;
			} while (current != head);
		}

		return totalMartyrs;
	}

	public MartyrNode searchMartyrByName(String partName, String locationName) {// search martyr my part of name in
																				// specific location
		if (head == null) {
			System.out.println("No martyr records available.");
			return head;
		}

		MartyrNode current = head;
		do {
			if (current.getName().toLowerCase().contains(partName.toLowerCase())) {
				System.out.println("Martyr found: " + current.getName());
				return current;

			}
			current = current.next;
		} while (current != head);
		System.out.println("martyr not found");
		return null;

	}

	public void sortedByDate() {// sorted the list by date
		if (head == null || head.next == null) {
			return;// there's no martyr to sort
		}
		MartyrNode current = head;
		do {
			MartyrNode minEvent = current;// to find the minEvent
			MartyrNode next = current.next;// to compare
			while (next != head) {
				if (next.event.compareTo(minEvent.event) < 0) {
					minEvent = next;
				}
				next = next.next;// update next
			}
			if (minEvent != current) {
				String temp = current.event;// to store value (current)
				current.event = minEvent.event;// store the minEvent at the beginning
				minEvent.event = temp;// Switch
			}
			current = current.next;// update current
		} while (current != head);// end of the list
	}

	public String dateMaxMartyr(String districtName) {// find date have max martyr in district(districtName)
		sortedByDate();
		if (head == null || head.next == null) {
			return head.event;
		}
		// It initializes several variables to keep track of the maximum number of
		// martyrs on a single date and the date with the maximum number of martyrs
		String maxDate = null;
		int maxMartyrs = 0;
		MartyrNode currentNode = head;
		int currentCount = 0;
		String currentDate = currentNode.event;
		do {
			if (currentNode.District.equalsIgnoreCase(districtName)) {// checks if the district name matches the input
																		// parameter
				if (currentNode.event.equals(currentDate)) {
					currentCount++;
				} else {
					if (currentCount > maxMartyrs) {// max martyr
						maxMartyrs = currentCount;
						maxDate = currentDate;
					}
					// move to next date
					currentCount = 1;
					currentDate = currentNode.event;
				}
			}
			currentNode = currentNode.next;// update currentNode
		} while (currentNode != head);// end of list

		if (currentCount > maxMartyrs) {// last group
			maxMartyrs = currentCount;
			maxDate = currentDate;
		}
		return maxDate;
	}
}
