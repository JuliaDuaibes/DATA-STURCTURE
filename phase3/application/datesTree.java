package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class datesTree {
	datesTreeNode root;
	private stack<datesTreeNode> stack;

	public datesTree() {
		stack = new stack<>();
		root = null;

	}

	// insert unique sorted date
	public void insert(String date) {
		root = insertDates(root, date);
	}

	private datesTreeNode insertDates(datesTreeNode root, String date) {
		if (root == null) {
			root = new datesTreeNode(date);
			return root;
		}
		if (compare(date, root.date) < 0)
			root.left = insertDates(root.left, date);
		else if (compare(date, root.date) > 0)
			root.right = insertDates(root.right, date);
		return root;
	}

	// compare date using array
	private int compare(String date1, String date2) {
		String[] parts1 = date1.split("/");
		String[] parts2 = date2.split("/");

		int month1 = Integer.parseInt(parts1[0]);
		int day1 = Integer.parseInt(parts1[1]);
		int year1 = Integer.parseInt(parts1[2]);

		int month2 = Integer.parseInt(parts2[0]);
		int day2 = Integer.parseInt(parts2[1]);
		int year2 = Integer.parseInt(parts2[2]);

		if (year1 != year2)
			return year1 - year2;
		if (month1 != month2)
			return month1 - month2;
		return day1 - day2;
	}

	public datesTreeNode find(String date) {
		if (root == null) {
			return null;
		}

		try {// find node by format
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy"); // Assuming expected format
			Date searchDate = format.parse(date);

			return findDate(root, searchDate);
		} catch (ParseException e) {
			// (invalid date format in input file)
			System.out.println("Error parsing date: " + date);
			return null;
		}
	}

	// find date node
	private datesTreeNode findDate(datesTreeNode root, Date searchDate) {
		if (root == null) {
			return null;
		}

		Date nodeDate;
		try {
			nodeDate = new SimpleDateFormat("MM/dd/yyyy").parse(root.date); // Parse node's date
		} catch (ParseException e) {
			// Handle potential parsing errors in the data file (invalid dates)
			System.out.println("Error parsing date in tree: " + root.date);
			return null;
		}

		if (searchDate.compareTo(nodeDate) == 0) {
			return root;
		} else if (searchDate.before(nodeDate)) {
			return findDate(root.left, searchDate);
		} else {
			return findDate(root.right, searchDate);
		}
	}

	// check if date exist
	public boolean contains(String date) {
		return contains(root, date);
	}

	private boolean contains(datesTreeNode root, String date) {
		if (root == null)
			return false;

		if (root.date.equalsIgnoreCase(date))
			return true;

		if (date.compareTo(root.date) < 0) {
			return contains(root.left, date);
		} else if (date.compareTo(root.date) > 0) {
			return contains(root.right, date);
		}
		return false;
	}

	// min date
	public String minDate() {
		return min(root);
	}

	private String min(datesTreeNode root) {
		if (root == null) {
			return null;
		} else {
			datesTreeNode current = root;
			while (current.left != null) {
				current = current.left;
			}
			return current.date;
		}
	}

	// max date
	public String maxDate() {
		return max(root);
	}

	private String max(datesTreeNode root) {
		if (root == null) {
			return null;
		} else {
			datesTreeNode current = root;
			while (current.right != null) {
				current = current.right;
			}
			return current.date;
		}
	}

	// print dates in order
	public void print() {
		printInOrder(root);
	}

	private void printInOrder(datesTreeNode node) {
		if (node == null) {
			return;
		}
		printInOrder(node.left);
		System.out.print(node.date + " ");
		printInOrder(node.right);
	}

	// max martyr in date
	public String maxMartyr() {
		return maxMartyrinDate(root);
	}

	public String maxMartyrinDate(datesTreeNode root) {
		if (root == null) {
			return null; // Base case
		}

		String date = root.date;
		int maxMartyrCount = root.allmartyrIndate.martyrCount;

		// Check if the left subtree has a date with more martyrs
		String leftMaxDate = maxMartyrinDate(root.left);
		if (leftMaxDate != null) {
			int leftMaxMartyrCount = root.left.allmartyrIndate.martyrCount;
			if (leftMaxMartyrCount > maxMartyrCount) {
				maxMartyrCount = leftMaxMartyrCount;
				date = leftMaxDate;
			}
		}

		// Check if the right subtree has a date with more martyrs
		String rightMaxDate = maxMartyrinDate(root.right);
		if (rightMaxDate != null) {
			int rightMaxMartyrCount = root.right.allmartyrIndate.martyrCount;
			if (rightMaxMartyrCount > maxMartyrCount) {
				maxMartyrCount = rightMaxMartyrCount;
				date = rightMaxDate;
			}
		}

		return date;
	}

	public datesTreeNode goNext() {
		if (stack.isEmpty() && root == null) {
			System.out.println("Date tree is empty.");
			return null;
		}

		if (stack.isEmpty()) {
			pushLeftNodes(root);
		} else {
			datesTreeNode node = stack.pop();
			pushLeftNodes(node.right);
		}

		if (!stack.isEmpty()) {
			return stack.peek();
		} else {
			return null;
		}
	}

	public datesTreeNode goPrevious() {
		if (stack.isEmpty() && root == null) {
			System.out.println("Date tree is empty.");
			return null;
		}

		if (!stack.isEmpty()) {
			datesTreeNode node = stack.pop();
			pushRightNodes(node.left);
		} else {
			pushRightNodes(root);
		}

		if (!stack.isEmpty()) {
			return stack.peek();
		} else {
			return null;
		}
	}

	private void pushLeftNodes(datesTreeNode node) {
		while (node != null) {
			stack.push(node);
			node = node.getLeft();
		}
	}

	private void pushRightNodes(datesTreeNode node) {
		while (node != null) {
			stack.push(node);
			node = node.right;
		}
	}

	// delete node by(Successor)
	public void delete(String data) {
		deleteDate(root, data);
	}

	public datesTreeNode deleteDate(datesTreeNode root, String data) {
		if (root == null) {
			return root;
		}
		if (data.compareToIgnoreCase(root.date) < 0) {
			root.left = deleteDate(root.left, data);
		} else if (data.compareToIgnoreCase(root.date) > 0) {
			root.right = deleteDate(root.right, data);
		} else {
			if (root.left != null && root.right != null) {
				root.date = min(root.right);// min value from right
				root.right = deleteDate(root.right, root.date);
			} else {
				if (root.right != null)
					return root.right;
				else
					return root.left;
			}
		}
		return root;
	}

}
