package application;

import java.util.ArrayList;
import java.util.Stack;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MartyrsAVLTree {
	martyrNode root;
	ObservableList<martyrNode> martyrList = FXCollections.observableArrayList();

	public void insert(martyrNode martyr) {// insert new martyr
		if (martyr != null && search(martyr.getName()) == null) {
			root = insertMartyr(root, martyr);
		}
	}

	private martyrNode insertMartyr(martyrNode root, martyrNode martyr) {
		if (root == null) {
			return martyr;
		}
		// compare by name and district
		int compare = compareMartyrs(root, martyr);
		if (compare < 0) {
			root.left = insertMartyr(root.left, martyr);
		} else if (compare > 0) {
			root.right = insertMartyr(root.right, martyr);
		}
		int balance = getBalance(root);

		// Update height of the current node
		root.height = 1 + Math.max(getHeight(root.left), getHeight(root.right));

		// Left Left Case
		if (balance > 1 && compareMartyrs(martyr, root.left) < 0) {
			return rightRotate(root);
		}
		// Right Right Case
		if (balance < -1 && compareMartyrs(martyr, root.right) > 0) {
			return leftRotate(root);
		}
		// Left Right Case
		if (balance > 1 && compareMartyrs(martyr, root.left) > 0) {
			leftRightRotate(root);
		}
		// Right Left Case
		if (balance < -1 && compareMartyrs(martyr, root.right) < 0) {
			rightLeftRotate(root);
		}

		return root;
	}

	// Search for a martyr in the AVL tree
	public boolean exists(martyrNode martyr) {
		return exists(root, martyr);
	}

	private boolean exists(martyrNode node, martyrNode martyr) {
		if (node == null) {
			return false;
		}

		if (node.equals(martyr)) {
			return true;
		}

		if (compareMartyrs(node, martyr) < 0) {
			return exists(node.right, martyr);
		} else {
			return exists(node.left, martyr);
		}
	}

	martyrNode rightRotate(martyrNode x) {// right rotate
		martyrNode y = x.left;
		if (y != null) {
			x.left = y.right;
			y.right = x;
			x.height = Math.max(getHeight(x.right), getHeight(x.left));
			y.height = Math.max(getHeight(y.right), getHeight(y.left));
		}
		return y;
	}

	martyrNode leftRotate(martyrNode x) {// left rotate
		martyrNode y = x.right;
		if (y != null) {
			x.right = y.left;
			y.left = x;
			x.height = Math.max(getHeight(x.right), getHeight(x.left));
			y.height = Math.max(getHeight(y.right), getHeight(y.left));
		}
		return y;
	}

	martyrNode rightLeftRotate(martyrNode z) {// right left rotate
		z.right = rightRotate(z.right);
		return leftRotate(z);
	}

	martyrNode leftRightRotate(martyrNode z) {// left right rotate
		z.left = leftRotate(z.left);
		return rightRotate(z);
	}

	public int getHeight() { // tree height
		return getHeight(root);
	}

	private int getHeight(martyrNode node) {
		if (node == null) {
			return -1; // Empty tree has height -1
		}
		return 1 + Math.max(getHeight(node.left), getHeight(node.right));
	}

	public int getSize() { // tree size
		return getSize(root);
	}

	private int getSize(martyrNode node) {
		if (node == null) {
			return 0; // Empty tree
		}
		return 1 + getSize(node.left) + getSize(node.right);
	}

	private int compareMartyrs(martyrNode martyrOne, martyrNode martyrTwo) {// compare martyr by district then by name
		int districtCompare = martyrOne.getDistrict().compareTo(martyrTwo.getDistrict());
		if (districtCompare != 0) {// not in the same district
			return districtCompare;
		}
		return martyrOne.getName().compareTo(martyrTwo.getName());
	}

	private int getBalance(martyrNode node) {// Balance left-right=1/0
		if (node == null) {
			return 0;
		}
		int leftHeight = (node.left == null) ? 0 : node.left.height;
		int rightHeight = (node.right == null) ? 0 : node.right.height;
		return leftHeight - rightHeight;
	}

	// Search for a martyr in the AVL tree
	public martyrNode search(String name) {
		return search(root, name);
	}

	private martyrNode search(martyrNode node, String name) {
		if (node == null) {
			return null;
		}

		martyrNode temp = search(node.getLeft(), name);
		if (temp != null) {
			return temp;
		}

		if (node.getName().equalsIgnoreCase(name)) {
			return node;
		}

		return search(node.getRight(), name);
	}

	public void delete(String name) {// delete exists martyr
		root = deleteMartyr(root, name);
	}

	private martyrNode deleteMartyr(martyrNode node, String name) {
		if (node == null) {
			return node;
		}
		martyrNode martyr = search(name);

		int compare = compareMartyrs(node, martyr);

		if (compare < 0) {
			node.left = deleteMartyr(node.left, name);
		} else if (compare > 0) {
			node.right = deleteMartyr(node.right, name);
		} else {
			// Node with only one child or no child
			if (node.left == null) {
				return node.right;
			} else if (node.right == null) {
				return node.left;
			}

			node.Name = minValue(node.right);

			node.right = deleteMartyr(node.right, node.Name);
		}

		node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));

		int balance = getBalance(node);

		if (balance > 1 && getBalance(node.left) >= 0) {
			return rightRotate(node);
		}

		if (balance > 1 && getBalance(node.left) < 0) {
			leftRightRotate(node);
		}

		if (balance < -1 && getBalance(node.right) <= 0) {
			return leftRotate(node);
		}

		if (balance < -1 && getBalance(node.right) > 0) {
			rightLeftRotate(node);
		}

		return node;
	}

	private String minValue(martyrNode node) {// get min
		String minv = node.Name;
		while (node.left != null) {
			minv = node.left.Name;
			node = node.left;
		}
		return minv;
	}

	public martyrNode update(martyrNode node, String name, String newName) {
		if (node == null) {
			return null; // Martyr not found
		}

		int compare = name.compareToIgnoreCase(node.Name);
		if (compare < 0) {
			node.left = update(node.left, name, newName);
		} else if (compare > 0) {
			node.right = update(node.right, name, newName);
		} else { // Found the node to update
			node.Name = newName;
		}

		node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));

		int balance = getBalance(node);

		if (balance > 1 && getBalance(node.left) >= 0) {
			return rightRotate(node);
		}
		if (balance > 1 && getBalance(node.left) < 0) {
			leftRightRotate(node);
		}
		if (balance < -1 && getBalance(node.right) <= 0) {
			return leftRotate(node);
		}
		if (balance < -1 && getBalance(node.right) > 0) {
			rightLeftRotate(node);
		}

		return node;
	}

	public void updateDate(String newDate) {// update date
		updateDate(root, newDate);
	}

	// Method to update the date in the AVL tree
	private void updateDate(martyrNode node, String newDate) {
		if (root == null) {
			return;
		}

		if (node != null) {
			node.event = newDate;

			updateDate(node.left, newDate);
			updateDate(node.right, newDate);
		}

	}

	public void print() {
		printMartyrsAVLTree(root);
	}

	private void printMartyrsAVLTree(martyrNode root) {
		if (root != null) {
			printMartyrsAVLTree(root.left); // Traverse left subtree
			System.out.println(root); // Print the current node
			printMartyrsAVLTree(root.right); // Traverse right subtree
		}
	}

	public int avregeAge() {// get martyr avrege
		int sum = calulateSumAge(root);
		int count = calculateCount(root);
		int avreg = 0;
		if (count != 0) {
			avreg = sum / count;
		}
		return avreg;
	}

	public int calulateSumAge(martyrNode node) {
		if (node == null) {
			return 0;
		}
		int sum = 0;
		if (node.Age != null && !node.Age.isEmpty()) {
			if (!node.Age.equals("Age")) {
				sum = Integer.parseInt(node.Age);
			}
		}

		sum += calulateSumAge(node.left);
		sum += calulateSumAge(node.right);
		return sum;
	}

	public int calculateCount(martyrNode node) {
		if (node == null) {
			return 0;
		}
		int count = 1;
		count += calculateCount(node.left);
		count += calculateCount(node.right);
		return count;
	}

	public String DistricthasMaxMartyrs() {// get district have max martyr in AVL
		if (root == null) {
			return null;
		}
		Stack<martyrNode> stack = new Stack<>();
		martyrNode current = root;
		// insilize
		String currentDistrict = null;
		int currentCount = 0;
		String maxDistrict = null;
		int maxCount = 0;

		while (current != null || !stack.isEmpty()) {
			while (current != null) {// push all lefts
				stack.push(current);
				current = current.left;
			}

			current = stack.pop();
			if (currentDistrict == null || !current.District.equals(currentDistrict)) {
				if (currentDistrict != null && currentCount > maxCount) {// compare
					maxCount = currentCount;
					maxDistrict = currentDistrict;
				}
				currentDistrict = current.District;
				currentCount = 1;
			} else {
				currentCount++;
			}

			current = current.right;// get right node
		}

		// Final check for the last district in the traversal
		if (currentCount > maxCount) {
			maxDistrict = currentDistrict;
		}

		return maxDistrict;
	}

	public String LocationhasMaxMartyrs() {// get location that have max martyr in AVL tree
		if (root == null) {
			return null;
		}

		Stack<martyrNode> stackLocation = new Stack<>();
		martyrNode currentNode = root;

		String currentLocation = null;
		int currentCount = 0;
		String maxLocation = null;
		int maxCount = 0;

		while (currentNode != null || !stackLocation.isEmpty()) {
			while (currentNode != null) {// push all lefts
				stackLocation.push(currentNode);
				currentNode = currentNode.left;
			}

			currentNode = stackLocation.pop();
			if (currentLocation == null || !currentNode.location.equals(currentLocation)) {
				if (currentLocation != null && currentCount > maxCount) {// compare
					maxCount = currentCount;
					maxLocation = currentLocation;
				}
				currentLocation = currentNode.location;
				currentCount = 1;
			} else {
				currentCount++;
			}

			currentNode = currentNode.right;// get right
		}

		// Final check for the last location in the traversal
		if (currentCount > maxCount) {
			maxLocation = currentLocation;
		}

		return maxLocation;
	}

	// Method to delete all nodes with a specific date
	public void deleteByDate(String date) {
		root = deleteByDate(root, date);
	}

	// Recursive function to delete nodes with a specific date
	private martyrNode deleteByDate(martyrNode node, String date) {
		if (node == null)
			return node;

		node.left = deleteByDate(node.left, date);
		node.right = deleteByDate(node.right, date);

		// If the current node's date matches the target date, delete this node
		if (node.event.equals(date)) {
			return deleteMartyr(node, node.Name);
		}

		return node;
	}

	public void printlevel() {
		martyrList.clear(); // Clear the previous data
		printLevelByLevel(root, martyrList);
	}

	public void printLevelByLevel(martyrNode root, ObservableList<martyrNode> martyrList) {
		if (root == null) {
			System.out.println("Tree empty");
			return;
		}

		ArrayList<martyrNode> currentLevel = new ArrayList<>();
		currentLevel.add(root);

		while (!currentLevel.isEmpty()) {
			ArrayList<martyrNode> nextLevel = new ArrayList<>();
			for (int i = currentLevel.size() - 1; i >= 0; i--) { // Traverse from right to left
				martyrNode node = currentLevel.get(i);
				if (node != null) {
					martyrList.add(node); // Add the node to the list for TableView
					nextLevel.add(node.right); // Add right child first
					nextLevel.add(node.left); // Then add left child
				}
			}
			currentLevel = nextLevel;
		}
	}

	public void insertToHeap(MinHeap heap) {// insert all martyr to heap
		if (root == null) {
			System.out.println("Tree is Empty");
			return;
		} else {
			insertToHeap(heap, root);
		}
	}

	private void insertToHeap(MinHeap heap, martyrNode root) {
		heap.insertSortedAge(root);// Sorted by age

		if (root.left != null) {
			insertToHeap(heap, root.left);
		}
		if (root.right != null) {
			insertToHeap(heap, root.right);
		}

	}

}
//public void printlevel() {// print level by level (right-left)
//System.out.println();
//printLevelByLevel(root);
//}
//
//public void printLevelByLevel(martyrNode root) {
//if (root == null) {
//	System.out.println("Tree empty");
//	return;
//}
//
//int height = getHeight(root);
//for (int level = 0; level <= height; level++) {
//	printLevel(root, level, 0); // Start from level 0
//}
//}
//
//private void printLevel(martyrNode node, int targetLevel, int currentLevel) {
//if (node == null) {
//	return;
//}
//
//if (currentLevel == targetLevel) {
//	System.out.println(node.toString() + " ");
//} else {
//	printLevel(node.right, targetLevel, currentLevel + 1);
//	printLevel(node.left, targetLevel, currentLevel + 1);
//}
//}
