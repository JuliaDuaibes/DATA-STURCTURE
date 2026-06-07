package application;

public class DistrictList {
	districtNode head;
	districtNode tail;
	int count;

	public DistrictList() {
		this.head = null;
		this.tail = null;
	}

	public void insertDis(String districtName) {// insert unique district
		districtNode newDistrict = new districtNode(districtName);
		boolean exist = ifExistDistrict(districtName);// check if exist
		if (exist) {
			return; // Return if the district already exists
		}
		if (head == null) {// list is empty
			head = newDistrict;
			tail = newDistrict;
			tail.next = head;
			head.prev = tail;
		} else {// add before head
			if (districtName.compareToIgnoreCase(head.districtName) < 0) {
				tail.next = newDistrict;
				newDistrict.next = head;
				head.prev = newDistrict;
				newDistrict.prev = tail;
				head = newDistrict;
			} else {// Inserting Between Nodes
				districtNode current = head.next;
				while (current != head && districtName.compareToIgnoreCase(current.districtName) >= 0) {
					current = current.next;
				}
				districtNode prevNode = current.prev;
				prevNode.next = newDistrict;
				newDistrict.prev = prevNode;
				newDistrict.next = current;
				current.prev = newDistrict;
				if (current == head) {
					tail = newDistrict;
				}
			}
		}
		count++;
	}

	public void display() {// display district nodes
		districtNode current = head;
		if (head == null) {
			return;
		}
		do {
			System.out.println("District: " + current.districtName);
			current = current.next;
		} while (current != head);// circular
	}

	public boolean ifExistDistrict(String nameDis) {// return true if exist in list
		if (head == null) {
			return false;
		} else {
			districtNode current = head;
			do {
				if (nameDis.equalsIgnoreCase(current.districtName)) {// if match with districtName
					return true;
				}
				current = current.next;
			} while (current != head);
			return false;
		}
	}

	public districtNode exist(String nameDis) {// return node from list if exist
		if (head == null) {
			return null;
		}
		districtNode current = head;
		while (current.next != head) {
			if (nameDis.equalsIgnoreCase(current.districtName)) {// if match with districtName
				return current;
			}
			current = current.next;
		}
		return current;
	}

	public void updateDistrict(String oldDistrictName, String newDistrictName) {// update district by name
		if (head == null) {
			return; // No districts to update
		}
		districtNode current = head;
		while (current != null) {
			if (current.districtName.equalsIgnoreCase(oldDistrictName)) {
				current.updateDistrictName(oldDistrictName, newDistrictName);// method to update node name
				return;

			}
			current = current.next;
		}
	}

	public void deleteDistrict(String districtName) {// delete district from list
		if (head == null) {
			return;
		}
		districtNode current = head;
		do {
			if (current.districtName.equalsIgnoreCase(districtName)) {// check if match
				current.prev.next = current.next;
				current.next.prev = current.prev;
				if (current == tail) {// update tail
					tail = current.prev;
				}
				if (current == head) {// update head
					head = current.next;
				}
				count--;
				return;// if found return
			}
			current = current.next;
		} while (current != head);// end of list

	}
}
