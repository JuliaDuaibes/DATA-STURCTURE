package application;

import java.util.ArrayList;

//import java.util.ArrayList;

public class martyrList {
	martyrNode head;
	martyrNode tail;
	int martyrCount;

	public void insert(String name, String event, String age, String location, String district, String gender) {
		martyrNode newNode = new martyrNode(name, event, age, location, district, gender);
		if (head == null) { // if list is empty
			head = tail = newNode;
			tail.next = head; // circular
		} else if (newNode.Age.compareTo(head.Age) <= 0
				|| (newNode.Age.equals(head.Age) && newNode.Gender.compareToIgnoreCase(head.Gender) <= 0)) { // Insert
																												// before
																												// head
			newNode.next = head;
			head = newNode;
			tail.next = head; // Update tail
		} else {
			// Insert between two nodes or at the end
			martyrNode current = head;
			while (current.next != head
					&& (newNode.Age.compareTo(current.next.Age) > 0 || (newNode.Age.equals(current.next.Age)
							&& newNode.Gender.compareToIgnoreCase(current.next.Gender) >= 0))) {
				// Move to the next node until the newNode should be inserted after current
				current = current.next;
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

		martyrNode current = head;

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

		martyrNode current = head;
		while (current.next != head) {
			// check if martyr exists martyr in district
			if (current.District.equalsIgnoreCase(districtName)) {
				current.District = newDistrict; // Update district for this martyr
			}
			current = current.next;
		}

	}

	public void updateLocationName(String oldLocationName, String newLocationName) {
		if (head == null) {
			return; // No martyrs to update
		}

		martyrNode current = head;
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

		// If the head node matches locationName, delete it
		while (head != null && head.location.equals(locationName)) {
			head = head.next;
			martyrCount--;
		}

		// If the list still contains nodes after deleting head
		if (head != null) {
			martyrNode current = head;

			// delete nodes whose location matches locationName
			do {
				if (current.next.location.equalsIgnoreCase(locationName)) {
					current.next = current.next.next;
					martyrCount--;
				} else {
					current = current.next;
				}
			} while (current != head);

			// Check if the last node matches locationName
			if (current.location.equalsIgnoreCase(locationName)) {
				current.next = head; // Make the last node point to head( circular)
				martyrCount--;
			}
		}
	}

	public void deleteDistrict(String districtName) {
		if (head == null) {
			return; // No martyrs to delete
		}

		// check If the head node matches districtName(if there is more than 1 martyr in
		// the same district in order)
		while (head != null && head.District.equalsIgnoreCase(districtName)) {
			head = head.next;
			martyrCount--;
		}

		// If the list still contains nodes after deleting head
		if (head != null) {
			martyrNode current = head;
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

	public void removeMartyr(String name) {
		if (head == null) {
			return;
		}

		// Check if the martyr to remove is at the head of the list
		if (head.Name.equals(name)) {
			// If the martyr to remove is the only node in the list
			if (head.next == head) {
				head = null;
				tail = null;
			} else {
				head = head.next;
				tail.next = head;
			}
			martyrCount--;
			return;
		}

		// Check for the martyr to remove in the rest of the list
		martyrNode current = head;
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
			martyrNode currentTail = head;
			while (currentTail.next != tail) {
				currentTail = currentTail.next;
			}
			currentTail.next = head;
			tail = currentTail;
			martyrCount--;
		}
	}

	public int averageAge() {
		int age = 0, count = 0;
		martyrNode current = head;
		if (current == null) {
			return 0;
		} else {
			do {

				if (!current.getAge().isEmpty()) {
					age += Integer.parseInt(current.getAge());
					count++;
				}

				current = current.next;
			} while (current != head);

			if (count != 0) {
				return age / count;
			} else {
				return 0;
			}
		}
	}

	public martyrNode oldestMartyr() {
		martyrNode oldest = null;
		martyrNode current = head;

		if (current != null) {
			do {
				if (current.event != null) {
					if (oldest == null || current.Age.compareTo(oldest.Age) > 0) {
						oldest = current;
					}
				}
				current = current.next;
			} while (current != head);
		}

		return oldest;
	}

	public martyrNode youngestMartyr() {
		martyrNode youngest = null;
		martyrNode current = head;

		if (current != null) {
			do {
				if (current.event != null) {
					if (youngest == null || current.Age.compareTo(youngest.Age) < 0) {
						youngest = current;
					}
				}
				current = current.next;
			} while (current != head);
		}

		return youngest;
	}

	public boolean ifExist(String name) {
		if (head == null) {
			return false;
		} else {
			martyrNode current = head;
			do {
				if (name.equalsIgnoreCase(current.Name)) {
					return true;
				}
				current = current.next;
			} while (current != head);
			return false;
		}
	}

	public martyrNode exist(String name) {// return node from list if exist
		if (head == null) {
			return null;
		}
		martyrNode current = head;
		while (current.next != head) {
			if (name.equalsIgnoreCase(current.Name)) {
				return current;
			}
			current = current.next;
		}
		return current;
	}

	public ArrayList<martyrNode> searchMartyrByName(String partName) {//search martyr by part name 
		if (head == null) {
			System.out.println("No martyr records available.");
			return null;
		}

		ArrayList<martyrNode> matches = new ArrayList<>();
		martyrNode current = head;
		do {
			if (current.getName().toLowerCase().contains(partName.toLowerCase())) {
//				System.out.println("Martyr found: " + current.getName());
				matches.add(current);
			}
			current = current.next;
		} while (current != head);

		if (matches.isEmpty()) {
//			System.out.println("No martyrs found with the given name part.");
		}
		return matches;
	}

	public martyrNode searchMartyrByFullName(String Name) {// search martyr my part of name in
		if (head == null) {
			System.out.println("No martyr records available.");
			return head;
		}

		martyrNode current = head;
		do {
			if (current.getName().equalsIgnoreCase(Name)) {
//				System.out.println("Martyr found: " + current.getName());
				return current;

			}
			current = current.next;
		} while (current != head);

//		System.out.println("martyr not found");
		return null;

	}

	public void setName(String oldName, String newName) {// update Name for specific martyr
		martyrNode current = head;
		do {
			if (oldName.equalsIgnoreCase(current.Name)) {
				current.setName(newName);
			}
			current = current.next;
		} while (current != head);

	}

	public void setAge(String oldName, String newAge) {// update Age for specific martyr
		martyrNode current = head;
		do {
			if (oldName.equalsIgnoreCase(current.Name)) {
				current.setAge(newAge);
			}
			current = current.next;
		} while (current != head);

	}

	public void setEvent(String Name, String newEvent) {// update Event for specific martyr
		martyrNode current = head;
		do {
			if (Name.equalsIgnoreCase(current.Name)) {
				current.setEvent(newEvent);
			}
			current = current.next;
		} while (current != head);
	}

	public void setGender(String Name, String newGender) {// update Gender for specific martyr
		martyrNode current = head;
		do {
			if (Name.equalsIgnoreCase(current.Name)) {
				current.setGender(newGender);
			}
			current = current.next;
		} while (current != head);

	}

	// convert martyr list to array list
	public ArrayList<martyrNode> listMartyr() {
		ArrayList<martyrNode> list = new ArrayList<>();
		martyrNode current = head;
		do {
			if (current != null) {
				list.add(current);
				current = current.next;
			}
		} while (current != head);
		return list;
	}

}
