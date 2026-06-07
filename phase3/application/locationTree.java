
package application;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class locationTree {
	locationNode root;
	int size;
	private Queue<locationNode> queue;

	public locationTree() {
		queue = new LinkedList<>();
		root = null;
		size = 0;
	}

	// Insert unique sorted location
	public void insert(String data) {
		root = insertLocation(root, data);
	}

	private locationNode insertLocation(locationNode root, String data) {
		if (root == null)
			return new locationNode(data);

		if (data.compareToIgnoreCase(root.locationName) < 0) {
			root.left = insertLocation(root.left, data);
		} else if (data.compareToIgnoreCase(root.locationName) > 0) {
			root.right = insertLocation(root.right, data);
		}
		return root;
	}

	// Check if contains location by name
	public boolean contains(String data) {
		return containsLocation(root, data);
	}

	public boolean containsLocation(locationNode root, String data) {
		if (root == null)
			return false;

		if (root.locationName.equalsIgnoreCase(data))
			return true;

		if (data.compareToIgnoreCase(root.locationName) < 0) {
			return containsLocation(root.left, data);
		} else {
			return containsLocation(root.right, data);
		}
	}

	// Find node using name
	public locationNode find(String data) {
		return Find(root, data);
	}

	public locationNode Find(locationNode root, String data) {
		if (root == null)
			return root;

		if (root.locationName.equalsIgnoreCase(data))
			return root;

		if (data.compareToIgnoreCase(root.locationName) < 0) {
			return Find(root.left, data);
		} else {
			return Find(root.right, data);
		}
	}

	// Min location name
	public String minValue(locationNode root) {
		String min = root.locationName;
		while (root.left != null) {
			min = root.left.locationName;
			root = root.left;
		}
		return min;
	}

	// Delete location using Successor
	public void delete(String data) {
		deleteLocation(root, data);
	}

	public locationNode deleteLocation(locationNode root, String data) {
		if (root == null) {
			return root;
		}
		if (data.compareToIgnoreCase(root.locationName) < 0) {
			root.left = deleteLocation(root.left, data);
		} else if (data.compareToIgnoreCase(root.locationName) > 0) {
			root.right = deleteLocation(root.right, data);
		} else {
			if (root.left != null && root.right != null) {
				root.locationName = minValue(root.right); // Min value from right
				root.right = deleteLocation(root.right, root.locationName);
			} else {
				if (root.right != null)
					return root.right;
				else
					return root.left;
			}
		}
		return root;
	}

	// update location name
	public void updateLocation(String oldName, String newName) {
		updateLocation(root, oldName, newName);
	}

	public void updateLocation(locationNode root, String oldName, String newName) {
		locationNode node = find(oldName);
		if (node != null) {
			node.locationName = newName;
			System.out.println("update successfully");
		} else {
			System.out.println("location does not exist");
		}
	}

	public void updateLocationName(locationNode node, String oldName, String newName) {
		if (root == null) {
			return;
		}

		updateLocation(oldName, newName);
		updatedateLocationNameInDates(node.dates.root, newName);
	}

	// go to dates in locations
	private void updatedateLocationNameInDates(datesTreeNode datesInlocation, String newName) {
		if (datesInlocation == null) {
			return;
		}
		updatedateLocationNameInList(datesInlocation.allmartyrIndate, newName);
		updatedateLocationNameInDates(datesInlocation.left, newName);
		updatedateLocationNameInDates(datesInlocation.right, newName);
	}

	// go martyr list in date
	private void updatedateLocationNameInList(martyrList martyrsInDates, String newName) {
		if (martyrsInDates.head == null) {
			return;
		}
		martyrNode current = martyrsInDates.head;
		do {
			current.location = newName;
			current = current.next;
		} while (current != martyrsInDates.head);
	}

	// Locations in order
	public void inorderLocation(locationNode root, ArrayList<String> loactionsnames) {
		if (root != null) {
			inorderLocation(root.left, loactionsnames);
			loactionsnames.add(root.locationName);
			inorderLocation(root.right, loactionsnames);
		}
	}

	// Get names of locations
	public ArrayList<String> getNamesOfLocation() {
		ArrayList<String> loactionsnames = new ArrayList<>();
		inorderLocation(root, loactionsnames);
		return loactionsnames;
	}

	public void printlevel() {
		printLevelByLevel(root);
	}

	private void printLevelByLevel(locationNode root) {
		if (root == null) {
			return;
		}

		int height = getHeight(root);
		for (int level = 1; level <= height; level++) {
			printLevel(root, level, 1); // Start from level 1, current node depth 1
			System.out.println();
		}
	}

	private void printLevel(locationNode node, int targetLevel, int currentLevel) {
		if (node == null) {
			return;
		}

		if (currentLevel == targetLevel) {
			System.out.print(node.locationName + " ");
		} else {
			printLevel(node.left, targetLevel, currentLevel + 1);
			printLevel(node.right, targetLevel, currentLevel + 1);
		}
	}

	public int getHeight(locationNode root) {
		if (root == null) {
			return 0; // Empty tree has height 0
		}

		int leftHeight = getHeight(root.left); // Height of left subtree
		int rightHeight = getHeight(root.right); // Height of right subtree

		return Math.max(leftHeight, rightHeight) + 1;
	}

	// Navigate to the next node level by level
	public locationNode goNext() {
		if (queue.isEmpty() && root == null) {
			System.out.println("Location tree is empty.");
			return null;
		}

		if (queue.isEmpty()) {
			queue.add(root); // Initialize the queue with the root node
		}

		locationNode currentNode = queue.poll();
		if (currentNode.left != null) {
			queue.add(currentNode.left);
		}
		if (currentNode.right != null) {
			queue.add(currentNode.right);
		}

		return currentNode;
	}

}
