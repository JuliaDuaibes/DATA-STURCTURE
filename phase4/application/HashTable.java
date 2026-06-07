package application;

public class HashTable {
	private static final int INITIAL_SIZE = 11;// size
	private int size;
	private int halfSize;
	hashNode[] table;

	public HashTable() {// cons
		size = 0;
		halfSize = INITIAL_SIZE / 2;
		table = new hashNode[INITIAL_SIZE];
		for (int i = 0; i < INITIAL_SIZE; i++) {
			table[i] = new hashNode(); // Initialize all slots with empty nodes
		}
	}

	private int hashFunction(String date) {// to get index
		int total = 0;
		if (date == null) {
			return 0;
		}
		String[] dateParts = date.split("/");
		if (dateParts.length == 3) {
			int year = Integer.parseInt(dateParts[0]);
			int month = Integer.parseInt(dateParts[1]);
			int day = Integer.parseInt(dateParts[2]);
			total = year + month + day;
		}
		return Math.abs(total) % table.length;
	}

	public void insert(String date, martyrNode martyr) {// insert date and their martyr
		int index = hashFunction(date);
		int i = 0;
		int hash = index;

		// Check if the date already exists in the hash table
		while (table[index] != null && table[index].flag != 'E' && !table[index].date.equals(date)) {
			index = (hash + i * i) % table.length; // Quadratic Probing
			i++;
		}

		// If the date already exists, insert into the AVL tree at this index(exists)
		if (table[index] != null && table[index].flag == 'F' && table[index].date.equals(date)) {
			table[index].martyrsTree.insert(martyr);
			return;
		}

		// If an empty or deleted slot is found, insert the new date and martyr(not
		// exists)
		if (table[index] == null || table[index].flag != 'F') {
			table[index] = new hashNode(date);
			table[index].flag = 'F'; // Set as full
			size++;

			// Ensure the AVL tree is initialized before insertion
			if (table[index].martyrsTree == null) {
				table[index].martyrsTree = new MartyrsAVLTree();
			}

			// Now, you can insert into the AVL tree associated with this date
			table[index].martyrsTree.insert(martyr);

			if (size >= halfSize) {
				rehash();
			}
		}

	}

	private void rehash() {// resize
		int newSize = getNextPrime(table.length * 2);
		hashNode[] newTable = new hashNode[newSize];
		for (hashNode node : table) {
			if (node != null && node.flag == 'F') {
				int newIndex = hashFunction(node.date);
				int hash = newIndex;
				int i = 0;
				while (newTable[newIndex] != null && newTable[newIndex].flag != 'E') {
					newIndex = (hash + i * i) % newTable.length; // Quadratic Probing
					i++;
				}
				newTable[newIndex] = node;
//				newTable[newIndex].martyrsTree = node.martyrsTree;
			}
		}

		table = newTable;// update size and half size
		halfSize = newSize / 2;
	}

	private int getNextPrime(int n) {
		// Find the next prime number after n
		while (!isPrime(n)) {
			n++;
		}
		return n;
	}

	private boolean isPrime(int n) {
		// Check if a number is prime
		if (n <= 1)
			return false;
		if (n <= 3)
			return true;
		if (n % 2 == 0 || n % 3 == 0)
			return false;
		for (int i = 5; i * i <= n; i += 6) {
			if (n % i == 0 || n % (i + 2) == 0)
				return false;
		}
		return true;
	}

	public void printAllDatesAndmartyrsIn() {// print all dates with their martyrs
		for (int i = 0; i < table.length; i++) {
			if (table[i] != null) {
				System.out.println("Index " + i + ": " + table[i].date);
				table[i].martyrsTree.print();// get martyrs
			}
		}
	}

	public void printAllDates() {// print all dates in hash
		int y = 1;
		for (int i = 0; i < table.length; i++) {
			if (table[i] != null) {
				System.out.println(y + ": Index " + i + ": " + table[i].date);
				y++;
			}
		}
	}

	public void printAllDatesIncludingEmpty() {// print all index in hash with empty
		for (int i = 0; i < table.length; i++) {
			if (table[i] != null) {
				System.out.println("Index " + i + ": " + table[i].date);
			} else {
				System.out.println("Index " + i + ": null");
			}
		}
	}

	private int Index = 0; // Track the current position

	public String DownDate() {// move down
		if (Index < table.length) {
			while (++Index < table.length - 1) {
				if (table[Index] != null && table[Index].flag == 'F') {
					return table[Index].date;
				}
			}
			System.out.println("Invalid down dates");
		}
		return null;
	}

	public String UpDate() {// move up
		if (Index < 0) {
			System.out.println("Invalid Up dates");
			return null;
		}
		if (Index > 0) {
			while (--Index > -1) {
				if (table[Index] != null && table[Index].flag == 'F') {
					return table[Index].date;
				}
			}
			System.out.println("Invalid Up dates");
		}
		return null;

	}

	public hashNode search(String date) {
		int index = hashFunction(date);
		int i = 0;
		int hash = index;

		while (table[index] != null && table[index].flag != 'E') {
			if (table[index].flag == 'F' && table[index].date.equals(date)) {
				return table[index];
			}
			// Skip over deleted or empty nodes
			index = (hash + i * i) % table.length;
			i++;

		}
		return null; // Not found
	}

	public boolean exist(String date) {// if exists
		int index = hashFunction(date);
		int i = 0;
		int hash = index;

		while (table[index] != null) {
			if (table[index].flag == 'F' && table[index].date.equals(date)) {// if exist return true
				return true;
			}
			index = (hash + i * i) % table.length; // Quadratic probing
			i++;
		}
		return false; // Not found
	}

	public void delete(String date) {// delete exist date
		hashNode node = search(date);
		if (node != null) {
			node.flag = 'D'; // Mark as deleted
			node.martyrsTree = null;
			node = new hashNode();

		} else {
			System.out.println("Date does not exist");
		}
	}

	public void Update(String date, String newDate) { // update date
		hashNode node = search(date);
		if (node != null) {
			node.martyrsTree.updateDate(newDate);
			node.date = newDate;
		} else {
			System.out.println("Date does not exist");
		}
	}
	
	// Modify the printAllDates() method to return a String
			public String printAllDatesToString() {
			    StringBuilder sb = new StringBuilder();
			    int y = 1;
			    for (int i = 0; i < table.length; i++) {
			        if (table[i] != null) {
			            sb.append(y + ": Index " + i + ": " + table[i].date + "\n");
			            y++;
			        }
			    }
			    return sb.toString();
			}

			// Modify the printAllDatesIncludingEmpty() method to return a String
			public String printAllDatesIncludingEmptyToString() {
			    StringBuilder sb = new StringBuilder();
			    int y = 1;
			    for (int i = 0; i < table.length; i++) {
			        if (table[i] != null) {
			            sb.append(y + ": Index " + i + ": " + table[i].date + "\n");
			        } else {
			            sb.append(y + ": Index " + i + ": null\n");
			        }
			        y++;
			    }
			    return sb.toString();
			}

}
