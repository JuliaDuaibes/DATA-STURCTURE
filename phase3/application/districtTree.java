package application;

import java.util.ArrayList;

public class districtTree {
	districtNode root;
	private stack<districtNode> stack;

	public districtTree() {
		stack = new stack<>();
		root = null;
	}

	// insert unique districts sorted
	public void insert(String data) {
		root = insertDistrict(root, data);
	}

	private districtNode insertDistrict(districtNode root, String data) {
		if (root == null) {
			return new districtNode(data);
		}

		if (data.compareToIgnoreCase(root.districtName) < 0) {
			root.left = insertDistrict(root.left, data);
		} else if (data.compareToIgnoreCase(root.districtName) > 0) {
			root.right = insertDistrict(root.right, data);
		}

		return root;
	}

	// chick if contains district by name
	public boolean contains(String data) {
		return containsDistrict(root, data);
	}

	private boolean containsDistrict(districtNode root, String data) {
		if (root == null)
			return false;

		if (root.districtName.equalsIgnoreCase(data)) {
			return true;
		}

		if (data.compareToIgnoreCase(root.districtName) < 0) {
			return containsDistrict(root.left, data);
		} else {
			return containsDistrict(root.right, data);
		}
	}

	// find the node of district using name
	public districtNode find(String data) {
		return findDistrict(root, data);
	}

	private districtNode findDistrict(districtNode root, String data) {
		if (root == null)
			return null;

		if (root.districtName.equalsIgnoreCase(data))
			return root;

		if (data.compareToIgnoreCase(root.districtName) < 0) {
			return findDistrict(root.left, data);
		} else {
			return findDistrict(root.right, data);
		}

	}

	// delete node by Successor
	public void delete(String data) {
		deleteDistrict(root, data);
	}

	public String minValue(districtNode root) {
		String min = root.districtName;
		while (root.left != null) {
			min = root.left.districtName;
			root = root.left;
		}
		return min;

	}

	public districtNode deleteDistrict(districtNode root, String data) {
		if (root == null) {
			return root;
		}
		if (data.compareToIgnoreCase(root.districtName) < 0) {
			root.left = deleteDistrict(root.left, data);
		} else if (data.compareToIgnoreCase(root.districtName) > 0) {
			root.right = deleteDistrict(root.right, data);
		} else {
			if (root.left != null && root.right != null) {
				root.districtName = minValue(root.right);// min Value from right
				root.right = deleteDistrict(root.right, root.districtName);
			} else {
				if (root.right != null)
					return root.right;
				else
					return root.left;
			}
		}
		return root;
	}

	// print InOrder
	public void print() {
		printInOrder(root);
	}

	private void printInOrder(districtNode node) {
		if (node == null) {
			return;
		}
		printInOrder(node.left);
		System.out.print(node.districtName + " ");
		printInOrder(node.right);
	}

	// calculate total martyr in District (districts-locations-dates-martyrs)
	public int calculateMartyrs(districtNode node) {
		if (node == null) {
			return 0;
		}

		int locationsInThisNode = calculateMartyrsInLocationTree(node.locations.root);

		return locationsInThisNode;
	}

	// go to locations in district
	private int calculateMartyrsInLocationTree(locationNode locationNode) {
		if (locationNode == null) {
			return 0;
		}

		int datesInLeftSubtree = calculateMartyrsInLocationTree(locationNode.left);
		int datesInThislocationNode = calculateMartyrsInDatesTree(locationNode.dates.root);
		int datesInRightSubtree = calculateMartyrsInLocationTree(locationNode.right);

		return datesInLeftSubtree + datesInRightSubtree + datesInThislocationNode;
	}

	// go to dates in location
	private int calculateMartyrsInDatesTree(datesTreeNode datesNode) {
		if (datesNode == null) {
			return 0;
		}

		int martyrsInLeftSubtree = calculateMartyrsInDatesTree(datesNode.left);
		int martyrsInThislocationNode = calculateMartyrsInMartyrsList(datesNode.allmartyrIndate);
		int martyrsInRightSubtree = calculateMartyrsInDatesTree(datesNode.right);

		return martyrsInLeftSubtree + martyrsInThislocationNode + martyrsInRightSubtree;
	}

	// go to martyr list in date
	private int calculateMartyrsInMartyrsList(martyrList martyrsList) {
		int totalMartyrs = 0;
		martyrNode current = martyrsList.head;
		if (current == null) {
			return 0;
		}

		do {
			totalMartyrs++;
			current = current.next;
		} while (current != martyrsList.head);

		return totalMartyrs;
	}

	// updates district name (districts-locations-dates-martyrs)
	public void updateDistrictName(districtNode node, String newName) {
		if (root == null) {
			return;
		}
		if (node != null) {
			node.districtName = newName;
			updateupdateDistrictNameInlocations(node.locations.root, newName);
		}
	}

	// go to locations in district
	private void updateupdateDistrictNameInlocations(locationNode locationsIndistrict, String newName) {
		if (locationsIndistrict == null) {
			return;
		}
		updatedateDistrictNameInDates(locationsIndistrict.dates.root, newName);
		updateupdateDistrictNameInlocations(locationsIndistrict.left, newName);
		updateupdateDistrictNameInlocations(locationsIndistrict.right, newName);
	}

	// go to dates in location
	private void updatedateDistrictNameInDates(datesTreeNode datesInlocation, String newName) {
		if (datesInlocation == null) {
			return;
		}
		updatedateDistrictNameInList(datesInlocation.allmartyrIndate, newName);
		updatedateDistrictNameInDates(datesInlocation.left, newName);
		updatedateDistrictNameInDates(datesInlocation.right, newName);

	}

	// go to martyr list in date
	private void updatedateDistrictNameInList(martyrList martyrsInDates, String newName) {
		if (martyrsInDates.head == null) {
			return;
		}
		martyrNode current = martyrsInDates.head;

		do {
			current.District = newName;
			current = current.next;
		} while (current != martyrsInDates.head);

	}

	// navigate next
	public districtNode goNext() {
		if (stack.isEmpty() && root == null) {
			System.out.println("District tree is empty.");
			return null;
		}

		if (stack.isEmpty()) {
			pushLeft(root);
		} else {
			districtNode node = stack.pop();
			pushLeft(node.right);
		}

		if (!stack.isEmpty()) {
			return stack.peek();
		} else {
			return null;
		}
	}

	// navigate previous
	public districtNode goPrevious() {
		if (stack.isEmpty() && root == null) {
			System.out.println("District tree is empty.");
			return null;
		}

		if (!stack.isEmpty()) {
			districtNode node = stack.pop();
			pushRight(node.left);
		} else {
			pushRight(root);
		}

		if (!stack.isEmpty()) {
			return stack.peek();
		} else {
			return null;
		}

	}

	private void pushLeft(districtNode node) {
		while (node != null) {
			stack.push(node);
			node = node.getLeft();
		}
	}

	private void pushRight(districtNode node) {
		while (node != null) {
			stack.push(node);
			node = node.right;
		}
	}


	public void inorderDistrict(districtNode root, ArrayList<String> districtsnames) {
		if (root != null) {
			inorderDistrict(root.left, districtsnames);
			districtsnames.add(root.districtName);
			inorderDistrict(root.right, districtsnames);
		}
	}

	public ArrayList<String> getNamesOfDistrict() {
		ArrayList<String> districtsnames = new ArrayList<>();
		inorderDistrict(root, districtsnames);
		return districtsnames;
	}
}
