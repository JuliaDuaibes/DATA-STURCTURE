package application;

public class locationList {
	locationNode head, tail;
	int count;

	public locationList() {
		this.head = null;
	}

	public void insert(String locationName) {// insert new location to list
		boolean exist = ifExistLocation(locationName);// check if exist
		locationNode newLocation = new locationNode(locationName);
		if (exist) {// if exist return
			return;
		}
		if (head == null) {// if list is empty
			head = tail = newLocation;
			tail.next = head; // circular
		} else {// add before head
			if (locationName.compareToIgnoreCase(head.locationName) < 0) {
				newLocation.next = head;
				tail.next = newLocation;
				head = newLocation;// update head

			} else {
				// Inserting Between Nodes
				locationNode current = head;
				while (current.next != head && locationName.compareToIgnoreCase(current.next.locationName) >= 0) {
					current = current.next;
				}
				// Insert the new node after the current node
				newLocation.next = current.next;
				current.next = newLocation;
				tail = newLocation; // update the tail
			}

		}
		count++;

	}

	public boolean ifExistLocation(String locationName) {// return true if exist
		if (head == null) {
			return false;
		} else {
			locationNode current = head;
			do {
				if (locationName.equalsIgnoreCase(current.locationName)) {// check match
					return true;
				}
				current = current.next;
			} while (current != head);// end list
			return false;
		}
	}

	public locationNode exist(String namelocation) {// return node from list if exist (by name)
		if (head != null) {
			locationNode current = head;
			while (current.next != head) {
				if (namelocation.equalsIgnoreCase(current.locationName)) {
					return current;
				}
				current = current.next;
			}

		}
		return head;
	}

	public void display() {
		if (head == null) {
			System.out.println("The location list is empty.");
			return;
		}

		locationNode current = head;

		do {
			System.out.println(current.locationName);
			// Move to the next node
			current = current.next;
		} while (current != head);
	}

	public void updateLocation(String oldLocationName, String newLocationName) {// update location name
		if (head == null) {
			return; // No districts to update
		}

		locationNode current = head;
		do {
			if (current.locationName.equalsIgnoreCase(oldLocationName)) {// check if match with oldLocationName
				current.updateLocationName(newLocationName);
				return;
			}
			current = current.next;
		} while (current != head);
	}

	public void deleteLocation(String locationName) {// delete location from list
		if (head == null) {
			return; // No data to delete
		}
		// if the node is head
		if (head.locationName.equals(locationName)) {
			head = head.next;
			count--;
			return;

		}
		locationNode prev = head;
		locationNode current = head.next;
		do {
			if (current.locationName.equalsIgnoreCase(locationName)) {// check if match with LocationName
				prev.next = current.next;
				count--;
				return;
			}
			// update current and prev
			current = current.next;
			prev = prev.next;
		} while (current != head);

	}

}
