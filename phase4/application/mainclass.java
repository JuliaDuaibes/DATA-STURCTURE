package application;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class mainclass extends Application {
	private File selectedFile;
	private HashTable hashTable;
	ArrayList<String> districtNames;
	ArrayList<String> locationNames;
	MinHeap heap;

	@Override
	public void start(Stage primaryStage) throws Exception {
		Button loadFileButton = new Button("Load File");
		loadFileButton.setOnAction(e -> {
			try {
				createDataStructure(); // Initialize data structures before loading the file
				loadFile(primaryStage);
				dateScreen(primaryStage);

			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		VBox loadStack = new VBox(10);
		loadStack.setAlignment(Pos.CENTER);
		loadStack.getChildren().add(loadFileButton);
		loadStack.setPadding(new Insets(35));
		Scene loadFileScene = createStyledScene(loadStack, 520, 260);

		primaryStage.setScene(loadFileScene);
		primaryStage.setTitle("Choose File");
		primaryStage.show();
	}

	private void createDataStructure() {
		hashTable = new HashTable();
		districtNames = new ArrayList<>();
		locationNames = new ArrayList<>();
		heap = new MinHeap();
	}

	private void loadFile(Stage stage) throws IOException {// choose file
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open File");
		fileChooser.setInitialDirectory(new File("c:\\"));
		selectedFile = fileChooser.showOpenDialog(stage);
		if (selectedFile != null) {
			relation();// to get relations
			hashTable.printAllDatesAndmartyrsIn();// print all dates with their martyrs

		} else {
			System.out.println("Choose file to load...");
		}
	}

	private void relation() {
		try (BufferedReader buffer = new BufferedReader(new FileReader(selectedFile))) {// read file file
			String line;// split lines to make trees
			while ((line = buffer.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length == 6) {
					String name = parts[0].trim();
					String date = parts[1].trim();

					String age = parts[2].trim();
					String location = parts[3].trim();
					String district = parts[4].trim();
					String gender = parts[5].trim();

					martyrNode martyr = new martyrNode(name, date, age, location, district, gender);// martyrNode
					String[] dateParts = date.split("/");// spit date to insert
					if (!date.equalsIgnoreCase("event") && dateParts.length == 3) {// insure the format
						hashTable.insert(date, martyr);// insert unique date to hash table
					}

					if (!districtNames.contains(district)) {// get districts names
						districtNames.add(district);
					}
					if (!locationNames.contains(location)) {// get locations names
						locationNames.add(location);
					}
				}
			}
		} catch (IOException ex) {
			System.out.println("Error reading file: " + ex.getMessage());
		} catch (IllegalArgumentException ex) {
			System.out.println("Error reading file: " + ex.getMessage());
		}
	}

	private void dateScreen(Stage primaryStage) {// district Screen
		Stage thirdStage = new Stage();
		VBox vDate = new VBox(20);
		vDate.setAlignment(Pos.CENTER);
		BorderPane root = new BorderPane();
		MenuBar menuBar = new MenuBar();
		Menu operation = new Menu("Operation");
		menuBar.getMenus().add(operation);

		// Create MenuItems for operations
		MenuItem insert = new MenuItem("Insert New Date");
		MenuItem update = new MenuItem("Update Date");
		MenuItem delete = new MenuItem("Delete Date");
		MenuItem printHash = new MenuItem("Print hash table ");
		MenuItem printHashin = new MenuItem("Print hash table (with empty spots)");

		// Add MenuItems to the 'operation' menu
		operation.getItems().addAll(insert, update, delete, printHash, printHashin);

		root.setTop(menuBar);

		GridPane gp = new GridPane();
		gp.setHgap(10);
		gp.setVgap(10);
		gp.setAlignment(Pos.CENTER);

		HBox h1 = new HBox(10);
		h1.setAlignment(Pos.CENTER);

		// To navigate in-Order traversal
		Button btUp = new Button("Up");
		Button btDown = new Button("Down");
		h1.getChildren().addAll(btUp, btDown);

		Label lbln = new Label("Current Date:");
		Label tfName = new Label();
		tfName.setStyle("-fx-font-weight: bold");
		gp.add(lbln, 0, 0);
		gp.add(tfName, 1, 0);
		gp.add(h1, 1, 1);

		Label lbt = new Label("Total num of martyrs:");
		Label txtt = new Label();
		txtt.setStyle("-fx-font-weight: bold");
		Label lbA = new Label("Average martyr age:");
		Label txta = new Label();
		txta.setStyle("-fx-font-weight: bold");
		Label lbd = new Label("District with max martyrs:");
		Label txtd = new Label();
		txtd.setStyle("-fx-font-weight: bold");
		Label lbl = new Label("Location with max martyrs:");
		Label txtl = new Label();
		txtl.setStyle("-fx-font-weight: bold");
		gp.add(lbt, 0, 2);
		gp.add(txtt, 1, 2);
		gp.add(lbA, 0, 3);
		gp.add(txta, 1, 3);
		gp.add(lbd, 0, 4);
		gp.add(txtd, 1, 4);
		gp.add(lbl, 0, 5);
		gp.add(txtl, 1, 5);

		Button MartyrScreen = new Button("Martyr Screen");
		HBox h = new HBox(10);
		h.setAlignment(Pos.CENTER);
		h.getChildren().add(MartyrScreen);

		// naviagte up down
		statisticesDate(hashTable.search(hashTable.DownDate()), tfName, txtt, txta, txtd, txtl, MartyrScreen);
		btDown.setOnAction(e -> {
			String downDate = hashTable.DownDate();
			if (downDate != null) {
				hashNode node = hashTable.search(downDate);
				statisticesDate(node, tfName, txtt, txta, txtd, txtl, MartyrScreen);
			}
		});
		btUp.setOnAction(e -> {
			String upDate = hashTable.UpDate();
			if (upDate != null) {
				hashNode node = hashTable.search(upDate);
				statisticesDate(node, tfName, txtt, txta, txtd, txtl, MartyrScreen);
			}
		});
		// insert ,delete ,update .... date hash
		insert.setOnAction(e -> insertDate(tfName, txtt, txta, txtd, txtl, MartyrScreen));
		update.setOnAction(e -> updateDate(tfName, txtt, txta, txtd, txtl, MartyrScreen));
		delete.setOnAction(e -> deleteDate(tfName, txtt, txta, txtd, txtl, MartyrScreen));
		printHash.setOnAction(e -> {// without null
			rewriteFile(selectedFile);
			hashTable.printAllDates();
		});
		printHashin.setOnAction(e -> {// with null
			rewriteFile(selectedFile);
			hashTable.printAllDatesIncludingEmpty();
		});

		vDate.getChildren().addAll(gp, h, MartyrScreen);
		root.setCenter(vDate);
		Scene s3 = createStyledScene(root, 620, 560);
		thirdStage.setScene(s3);
		thirdStage.setTitle("Date Screen");
		thirdStage.setResizable(false);
		thirdStage.show();
	}

	// to update statistic
	private void statisticesDate(hashNode node, Label tfName, Label txtt, Label txta, Label txtd, Label txtl,
			Button MartyrScreen) {
		if (node != null) {
			tfName.setText(node.date);
			txtt.setText(node.martyrsTree.getSize() + "");
			txta.setText(node.martyrsTree.avregeAge() + "");
			txtd.setText(node.martyrsTree.DistricthasMaxMartyrs());
			txtl.setText(node.martyrsTree.LocationhasMaxMartyrs());
			MartyrScreen.setOnAction(
					e -> martyrScreen(node.martyrsTree, node.date, tfName, txtt, txta, txtd, txtl, MartyrScreen));
			rewriteFile(selectedFile);
		}
	}

	// insert not exists date
	private void insertDate(Label tfName, Label txtt, Label txta, Label txtd, Label txtl, Button MartyrScreen) {
		Stage stage4 = new Stage();
		VBox v = new VBox(20);
		v.setAlignment(Pos.CENTER);
		HBox h = new HBox(10);
		h.setAlignment(Pos.CENTER);
		Label lblN = new Label("Enter date:");
		DatePicker dpD = new DatePicker();
		dpD.setPromptText("Date");
		h.getChildren().addAll(lblN, dpD);
		Button btinsert = new Button("Insert");

		btinsert.setOnAction(event -> {
			if (dpD.getValue() != null) {
				String dateIN = dpD.getValue().format(DateTimeFormatter.ofPattern("M/d/yyyy"));// get date
				if (!dateIN.isEmpty()) {
					relation();
					hashNode exist = hashTable.search(dateIN);// check if already exist
					if (exist != null) {
						System.out.println("Date already exists: " + dateIN);
					} else {
						hashTable.insert(dateIN, null);// if not exist insert
						hashNode node = hashTable.search(dateIN);
						statisticesDate(node, tfName, txtt, txta, txtd, txtl, MartyrScreen);
						System.out.println("Added successfully");
					}
				} else {
					System.out.println("You should select a date before clicking the button.");
				}
			} else {
				System.out.println("You should select a date before clicking the button.");
			}
		});

		v.getChildren().addAll(h, btinsert);

		Scene s4 = createStyledScene(v, 480, 340);
		stage4.setScene(s4);
		stage4.setTitle("Insert date");
		stage4.setResizable(false);
		stage4.show();
	}

	// delete exists date
	private void deleteDate(Label tfName, Label txtt, Label txta, Label txtd, Label txtl, Button MartyrScreen) {
		Stage stage4 = new Stage();
		VBox v = new VBox(20);
		v.setAlignment(Pos.CENTER);
		HBox h = new HBox(10);
		h.setAlignment(Pos.CENTER);
		Label lblN = new Label("Date to delete");
		DatePicker dpD = new DatePicker();

		Button btDelete = new Button("Delete");
		h.getChildren().addAll(lblN, dpD);

		btDelete.setOnAction(event -> {
			String dateIN = dpD.getValue().format(DateTimeFormatter.ofPattern("M/d/yyyy"));// get date
			if (!dateIN.isEmpty()) {
				hashNode exist = hashTable.search(dateIN);// check if exist
				if (exist != null) {
					exist.martyrsTree.deleteByDate(dateIN);
					exist = null;
					statisticesDate(exist, tfName, txtt, txta, txtd, txtl, MartyrScreen);// update statistics
					hashTable.delete(dateIN);

					hashNode nextNode = hashTable.search(hashTable.DownDate());
					statisticesDate(nextNode, tfName, txtt, txta, txtd, txtl, MartyrScreen);// update statistics
					System.out.println("Date deleted successfully...");
				} else {
					System.out.println("Date not found.");
				}
			} else {
				System.out.println("Please enter a date before clicking the button.");
			}
		});

		v.getChildren().addAll(h, btDelete);
		Scene s4 = createStyledScene(v, 480, 340);
		stage4.setScene(s4);
		stage4.setTitle("Delete Date");
		stage4.setResizable(false);
		stage4.show();
	}

	private void updateDate(Label tfName, Label txtt, Label txta, Label txtd, Label txtl, Button MartyrScreen) {
		Stage stage4 = new Stage();
		VBox v = new VBox(20);
		v.setAlignment(Pos.CENTER);
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20));

		Label lblN = new Label("Old Date");
		DatePicker dpD = new DatePicker();

		Label lblNE = new Label("New Date:");
		DatePicker dpDTo = new DatePicker();
		Button btupdate = new Button("Update");

		gridPane.add(lblN, 0, 0);
		gridPane.add(dpD, 1, 0);
		gridPane.add(lblNE, 0, 1);
		gridPane.add(dpDTo, 1, 1);

		btupdate.setOnAction(e -> {
			if (dpD.getValue() != null && dpDTo.getValue() != null) {
				String oldDate = dpD.getValue().format(DateTimeFormatter.ofPattern("M/d/yyyy"));
				String newDate = dpDTo.getValue().format(DateTimeFormatter.ofPattern("M/d/yyyy"));

				if (!oldDate.isEmpty() && !newDate.isEmpty()) {
					hashNode exist = hashTable.search(oldDate);
					if (exist != null) {
						if (exist.date.equals(newDate)) {
							System.out.println("New date is the same as the old date. Cannot update.");
							return;
						}

						hashNode existNew = hashTable.search(newDate);
						if (existNew != null) { // if the new date exists in hash table
							// Add martyrs from old date to new date
							if (exist.martyrsTree.root != null) {
								addedMartyrToNewDate(existNew, exist.martyrsTree.root);
							}
							statisticesDate(existNew, tfName, txtt, txta, txtd, txtl, MartyrScreen);
							System.out.println("Update successfully (merged with existing date)");
						} else { // if the new date doesn't exist in the hash table
							// Insert the new node with martyrs from the old date
							hashTable.insert(newDate, null);
							hashNode newdd = hashTable.search(newDate);
							if (exist.martyrsTree.root != null) {
								addedMartyrToNewDate(newdd, exist.martyrsTree.root);
							}
							statisticesDate(newdd, tfName, txtt, txta, txtd, txtl, MartyrScreen);
							System.out.println("Update successfully (new date created)");
						}
						// Delete the old node from the hash table
						hashTable.delete(oldDate);
					} else {
						System.out.println("Date does not exist");
					}
				} else {
					System.out.println("You should insert old date and new date to update");
				}
			} else {
				System.out.println("You should select both old date and new date before clicking the button");
			}
		});

		v.getChildren().addAll(gridPane, btupdate);

		Scene s4 = createStyledScene(v, 540, 360);
		stage4.setScene(s4);
		stage4.setTitle("Update Date");
		stage4.setResizable(false);
		stage4.show();
	}

	private void addedMartyrToNewDate(hashNode newDate, martyrNode node) {
		node.setEvent(newDate.date);
		newDate.martyrsTree.insert(node);
		if (node.left != null) {
			addedMartyrToNewDate(newDate, node.left);
		}
		if (node.right != null) {
			addedMartyrToNewDate(newDate, node.right);
		}
	}

	public void martyrScreen(MartyrsAVLTree martyr, String date, Label tfName, Label txtt, Label txta, Label txtd,
			Label txtl, Button MartyrScreen) {

		TabPane tabPaneS = new TabPane();
		Tab statsTab = new Tab("Stats");
		Tab operationsTab = new Tab("Operations");
		Tab printTab = new Tab("Print");

		tabPaneS.getTabs().addAll(statsTab, operationsTab, printTab);

		// Stats Tab
		VBox statsVb = new VBox(10);
		statsVb.setAlignment(Pos.CENTER);
		Button bts = new Button("Tree size");
		Label sizeText = new Label();
		sizeText.setStyle("-fx-font-weight: bold");
		HBox h1 = new HBox(20);
		h1.setAlignment(Pos.CENTER);
		h1.getChildren().addAll(bts, sizeText);

		Button bth = new Button("Tree Height");
		Label heightText = new Label();
		heightText.setStyle("-fx-font-weight: bold");
		HBox h2 = new HBox(20);
		h2.setAlignment(Pos.CENTER);
		h2.getChildren().addAll(bth, heightText);

		statsVb.getChildren().addAll(h1, h2);

		statsTab.setContent(statsVb);

		// Operations Tab
		VBox operationsContainer = new VBox(10);
		operationsContainer.setAlignment(Pos.CENTER);
		MenuBar menuBar = new MenuBar();
		Menu operation = new Menu("Operation");
		menuBar.getMenus().add(operation);

		MenuItem btinsert = new MenuItem("Insert New martyr");
		MenuItem btupdate = new MenuItem("Update martyr");
		MenuItem btdelete = new MenuItem("Delete martyr");
		operation.getItems().addAll(btinsert, btupdate, btdelete);

		operationsContainer.getChildren().add(menuBar);

		operationsTab.setContent(operationsContainer);

		// Print Tab
		VBox printvb = new VBox(10);
		printvb.setAlignment(Pos.CENTER);
		Button btprintLevel = new Button("Print level by level");
		Button btprintTable = new Button("Print sorted Table");
		Button btSaveData = new Button("Save updated data to another file");

		// TableView to display sorted martyrs
		TableView<martyrNode> tableView = new TableView<>();
		ObservableList<martyrNode> martyrList = FXCollections.observableArrayList();
		tableView.setItems(martyrList);

		TableColumn<martyrNode, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

		TableColumn<martyrNode, String> dateColumn = new TableColumn<>("Date");
		dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvent()));

		TableColumn<martyrNode, String> ageColumn = new TableColumn<>("Age");
		ageColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAge()));

		TableColumn<martyrNode, String> locationColumn = new TableColumn<>("Location");
		locationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation()));

		TableColumn<martyrNode, String> districtColumn = new TableColumn<>("District");
		districtColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDistrict()));

		TableColumn<martyrNode, String> genderColumn = new TableColumn<>("Gender");
		genderColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGender()));

		tableView.getColumns().addAll(nameColumn, dateColumn, ageColumn, locationColumn, districtColumn, genderColumn);

		printvb.getChildren().addAll(btprintLevel, btprintTable, btSaveData, tableView);

		printTab.setContent(printvb);

		// Event handler to print martyrs sorted by age
		btprintTable.setOnAction(e -> {
			heap.clearHeap();
			martyr.insertToHeap(heap);
			heap.heapSort();
			martyrList.clear();
			martyrList.addAll(heap.getSortedMartyrsAsList());
		});

		// Event Handlers
		btprintLevel.setOnAction(e -> {
			martyrList.clear(); // Clear the previous data
			martyr.printLevelByLevel(martyr.root, martyrList);
		});

		bts.setOnAction(e -> sizeText.setText(martyr.getSize() + "")); // get tree size
		bth.setOnAction(e -> heightText.setText(martyr.getHeight() + "")); // get tree height

		btinsert.setOnAction(e -> {
			insertMartyr(martyr, date, sizeText, heightText, tfName, txtt, txta, txtd, txtl, MartyrScreen);
		});

		btupdate.setOnAction(e -> {
			updateMartyr(martyr, date, sizeText, heightText, tfName, txtt, txta, txtd, txtl, MartyrScreen);
		});

		btdelete.setOnAction(e -> {
			deleteMartyr(martyr, date, sizeText, heightText, tfName, txtt, txta, txtd, txtl, MartyrScreen);
		});

		btSaveData.setOnAction(e -> loadToAnotherFile()); // load updated data to another file

		Scene scene = createStyledScene(tabPaneS, 850, 600);
		Stage thirdStage = new Stage();
		thirdStage.setScene(scene);
		thirdStage.setTitle("Martyr Screen");
		thirdStage.show();
	}

	// insert new martyr
	private void insertMartyr(MartyrsAVLTree martyrs, String date, Label heightText, Label sizeText, Label tfName,
			Label txtt, Label txta, Label txtd, Label txtl, Button MartyrScreen) {
		Stage stage4 = new Stage();
		BorderPane bp = new BorderPane();
		GridPane gp = new GridPane();
		StackPane sp = new StackPane();
		bp.setPadding(new Insets(20));
		gp.setHgap(10);
		gp.setVgap(10);
		gp.setAlignment(Pos.CENTER);

		// All info about martyr
		Label lbN = new Label("Name:");
		TextField tfN = new TextField();
		Label lbA = new Label("Age:");
		TextField tfA = new TextField();
		Label lbl = new Label("Location");
		Label lbd = new Label("District");
		Label lbG = new Label("Gender:");

		ComboBox<String> districtBox = new ComboBox<>();// choose district from combo box
		ObservableList<String> districtItems = FXCollections.observableArrayList(districtNames);
		districtBox.setItems(districtItems);

		ComboBox<String> locationBox = new ComboBox<>();// choose location from combo box
		ObservableList<String> locationItems = FXCollections.observableArrayList(locationNames);
		locationBox.setItems(locationItems);

		// Gender radio buttons
		ToggleGroup genderGroup = new ToggleGroup();
		RadioButton maleRadio = new RadioButton("M");
		maleRadio.setToggleGroup(genderGroup);
		RadioButton femaleRadio = new RadioButton("F");
		femaleRadio.setToggleGroup(genderGroup);

		HBox genderBox = new HBox(10, maleRadio, femaleRadio);
		genderBox.setAlignment(Pos.CENTER_LEFT);

		gp.add(lbN, 0, 0);
		gp.add(tfN, 1, 0);
		gp.add(lbA, 0, 1);
		gp.add(tfA, 1, 1);
		gp.add(lbl, 0, 3);
		gp.add(locationBox, 1, 3);
		gp.add(lbd, 0, 4);
		gp.add(districtBox, 1, 4);
		gp.add(lbG, 0, 5);
		gp.add(genderBox, 1, 5);

		Button btin = new Button("Insert ");
		sp.getChildren().add(btin);
		bp.setBottom(sp);
		bp.setCenter(gp);

		btin.setOnAction(e -> {// get all info
			String name = tfN.getText();
			String age = tfA.getText();
			String location = locationBox.getValue();
			String district = districtBox.getValue();
			RadioButton selectedGenderRadio = (RadioButton) genderGroup.getSelectedToggle();
			String gender = (selectedGenderRadio != null) ? selectedGenderRadio.getText() : "";

			if (!age.isEmpty() && !name.isEmpty() && !gender.isEmpty() && !location.isEmpty() && !district.isEmpty()) {
				martyrNode newMartyr = new martyrNode(name, date, age, location, district, gender);
				martyrs.insert(newMartyr);
//				martyrs.print();
				statisticesDate(hashTable.search(date), tfName, txtt, txta, txtd, txtl, MartyrScreen);
				rewriteFile(selectedFile); // Add this line to update the file

				System.out.println("added successfully");
			} else {
				System.out.println("You should enter all info before inserting.");
			}

			tfN.setText("");
			tfA.setText("");
		});

		Scene s4 = createStyledScene(bp, 560, 500);
		stage4.setScene(s4);
		stage4.setResizable(false);
		stage4.setTitle("Insert New Martyr");
		stage4.show();
	}

	// delete exist martyr
	private void deleteMartyr(MartyrsAVLTree martyrs, String date, Label heightText, Label sizeText, Label tfName,
			Label txtt, Label txta, Label txtd, Label txtl, Button MartyrScreen) {
		Stage stage4 = new Stage();
		VBox v = new VBox(20);
		v.setAlignment(Pos.CENTER);
		GridPane gridPane1 = new GridPane();
		gridPane1.setAlignment(Pos.CENTER);
		gridPane1.setHgap(10);
		gridPane1.setVgap(10);
		gridPane1.setPadding(new Insets(30));

		Label lblN = new Label("Martyr Name");
		TextField txtUp = new TextField();
		txtUp.setPromptText("MARTYR NAME");
		Button btUpdate = new Button("Delete");

		gridPane1.add(lblN, 0, 0);
		gridPane1.add(txtUp, 1, 0);

		btUpdate.setOnAction(e -> {
			String name = txtUp.getText();// get name
			if (!name.isEmpty()) {
				martyrNode exist = martyrs.search(name);
				if (exist != null) {
					martyrs.delete(name);// delete from tree
					statisticesDate(hashTable.search(date), tfName, txtt, txta, txtd, txtl, MartyrScreen);
					rewriteFile(selectedFile);
					System.out.println("Delete successfully");
				} else {
					System.out.println("Martyr with name " + name + " does not exist.");
				}

			} else {
				System.out.println("Enter all info to delete.");
			}
			txtUp.setText("");
		});

		v.getChildren().addAll(gridPane1, btUpdate);
		Scene s4 = createStyledScene(v, 560, 500);
		stage4.setScene(s4);
		stage4.setTitle("Delete Martyr");
		stage4.setResizable(false);
		stage4.show();
	}

	private void updateMartyr(MartyrsAVLTree martyrs, String date, Label heightText, Label sizeText, Label tfName,
			Label txtt, Label txta, Label txtd, Label txtl, Button MartyrScreen) {
		Stage stage4 = new Stage();
		Label label = new Label("Selected option: to update martyr");

		MenuBar menuBar = new MenuBar();

		Menu updateMenu = new Menu("Update");
		menuBar.getMenus().add(updateMenu);

		MenuItem updateNameItem = new MenuItem("Update name");
		updateNameItem.setOnAction(event -> {
			updateName(martyrs, date, heightText, sizeText, tfName, txtt, txta, txtd, txtl, MartyrScreen);
		});
		updateMenu.getItems().add(updateNameItem);

		MenuItem updateAgeItem = new MenuItem("Update age");
		updateAgeItem.setOnAction(event -> {
			updateAge(martyrs, date, heightText, sizeText, tfName, txtt, txta, txtd, txtl, MartyrScreen);
		});
		updateMenu.getItems().add(updateAgeItem);

		MenuItem updateEventItem = new MenuItem("Update event");
		updateEventItem.setOnAction(event -> {
			updateEvent(martyrs, date, heightText, sizeText, tfName, txtt, txta, txtd, txtl, MartyrScreen);
		});
		updateMenu.getItems().add(updateEventItem);

		MenuItem updateGenderItem = new MenuItem("Update gender");
		updateGenderItem.setOnAction(event -> {
			updateGender(martyrs, date, heightText, sizeText, tfName, txtt, txta, txtd, txtl, MartyrScreen);
		});
		updateMenu.getItems().add(updateGenderItem);

		MenuItem updateLocationItem = new MenuItem("Update location");
		updateLocationItem.setOnAction(event -> {
			updatelocation(martyrs, date, heightText, sizeText, tfName, txtt, txta, txtd, txtl, MartyrScreen);
		});
		updateMenu.getItems().add(updateLocationItem);

		MenuItem updateDistrictItem = new MenuItem("Update district");
		updateDistrictItem.setOnAction(event -> {
			updateDistrict(martyrs, date, heightText, sizeText, tfName, txtt, txta, txtd, txtl, MartyrScreen);
		});
		updateMenu.getItems().add(updateDistrictItem);

		VBox vbox = new VBox(10);
		vbox.setPadding(new Insets(20));
		vbox.getChildren().addAll(label, menuBar);

		Scene s4 = createStyledScene(vbox, 480, 420);
		stage4.setScene(s4);
		stage4.setResizable(false);
		stage4.setTitle("Update martyr(options)");
		stage4.show();
	}

	// update martyr by name
	private void updateName(MartyrsAVLTree martyrs, String date, Label heightText, Label sizeText, Label tfName,
			Label txtt, Label txta, Label txtd, Label txtl, Button MartyrScreen) {
		Stage stage4 = new Stage();
		VBox v = new VBox(20);
		v.setAlignment(Pos.CENTER);
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(30));

		Label lblN = new Label("Martyr Name");
		TextField txtUp = new TextField();
		txtUp.setPromptText("Martyr Name");
		Label lblNe = new Label("New Martyr Name");
		TextField txtUpTo = new TextField();
		txtUpTo.setPromptText("New Martyr Name");
		Button btupdate = new Button("Update");

		gridPane.add(lblN, 0, 0);
		gridPane.add(txtUp, 1, 0);
		gridPane.add(lblNe, 0, 1);
		gridPane.add(txtUpTo, 1, 1);

		btupdate.setOnAction(e -> {
			String oldName = txtUp.getText();// get the old name
			String newName = txtUpTo.getText();// get the new name
			if (!oldName.isEmpty() && !newName.isEmpty()) {
				martyrNode exist = martyrs.search(oldName);
				if (exist != null) {
					martyrs.update(exist, oldName, newName);// update in tree
					statisticesDate(hashTable.search(date), tfName, txtt, txta, txtd, txtl, MartyrScreen);
					rewriteFile(selectedFile);
					System.out.println("Martyr name " + oldName + "updated successfully to :" + newName);
				} else {// martyr not exist
					System.out.println("Martyr with name :" + oldName + " does not exist.");
				}
			} else {
				System.out.println("Enter all info to update.");
			}
			txtUp.setText("");
			txtUpTo.setText("");
		});
		v.getChildren().addAll(gridPane, btupdate);
		Scene s4 = createStyledScene(v, 560, 430);
		stage4.setScene(s4);
		stage4.setTitle("Update Martyr");
		stage4.setResizable(false);
		stage4.show();
	}

	// update martyr by age
	private void updateAge(MartyrsAVLTree martyrs, String date, Label heightText, Label sizeText, Label tfName,
			Label txtt, Label txta, Label txtd, Label txtl, Button MartyrScreen) {
		Stage stage4 = new Stage();
		VBox v = new VBox(20);
		v.setAlignment(Pos.CENTER);
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(30));

		Label lblN = new Label("martyr Name");
		TextField txtUp = new TextField();
		txtUp.setPromptText("MARTYR NAME");
		Label lblNe = new Label("martyr New Age");
		TextField txtUpTo = new TextField();
		txtUpTo.setPromptText("Martyr new AGE");
		Button btupdate = new Button("Update");

		gridPane.add(lblN, 0, 0);
		gridPane.add(txtUp, 1, 0);
		gridPane.add(lblNe, 0, 1);
		gridPane.add(txtUpTo, 1, 1);

		btupdate.setOnAction(e -> {
			try {
				String oldName = txtUp.getText(); // get the old name
				String newAge = txtUpTo.getText(); // get the new age
				if (!oldName.isEmpty() && !newAge.isEmpty()) {
					martyrNode exist = martyrs.search(oldName);
					if (exist != null) {
						exist.setAge(newAge); // update the age of the martyr
						statisticesDate(hashTable.search(date), tfName, txtt, txta, txtd, txtl, MartyrScreen);
						rewriteFile(selectedFile);
						System.out.println("Martyr Age " + oldName + " updated successfully to :" + newAge);
					} else { // martyr not exist
						System.out.println("Martyr with name :" + oldName + " does not exist.");
					}
				} else {
					System.out.println("Enter all info to update.");
				}
			} catch (IllegalArgumentException ex) {
				System.out.println(ex);
			}
			txtUp.setText("");
			txtUpTo.setText("");
		});

		v.getChildren().addAll(gridPane, btupdate);
		Scene s4 = createStyledScene(v, 560, 430);
		stage4.setScene(s4);
		stage4.setTitle("Update martyr");
		stage4.setResizable(false);
		stage4.show();
	}

	// update martyr by event
	private void updatelocation(MartyrsAVLTree martyrs, String date, Label heightText, Label sizeText, Label tfName,
			Label txtt, Label txta, Label txtd, Label txtl, Button MartyrScreen) {
		Stage stage4 = new Stage();
		VBox v = new VBox(20);
		v.setAlignment(Pos.CENTER);
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(30));

		ComboBox<String> locationBox = new ComboBox<>();
		ObservableList<String> locationItems = FXCollections.observableArrayList(locationNames);
		locationBox.setItems(locationItems);

		Label lblN = new Label("martyr Name");
		TextField txtUp = new TextField();
		txtUp.setPromptText("MARTYR NAME");
		Label lblNe = new Label("martyr New location");

		Button btupdate = new Button("Update");

		gridPane.add(lblN, 0, 0);
		gridPane.add(txtUp, 1, 0);
		gridPane.add(lblNe, 0, 1);
		gridPane.add(locationBox, 1, 1);

		btupdate.setOnAction(e -> {
			String Name = txtUp.getText();// get name

			String newLocation = locationBox.getValue();

			if (!Name.isEmpty() && newLocation != null) {
				martyrNode exist = martyrs.search(Name);// search martyr if exist
				if (exist != null) {
					exist.setLocation(newLocation);
					statisticesDate(hashTable.search(date), tfName, txtt, txta, txtd, txtl, MartyrScreen);
					rewriteFile(selectedFile);
					System.out.println(Name + " old location  updated successfully to :" + newLocation);
				} else {
					System.out.println("martyr with name :" + Name + "doesnot exist");
				}

			} else {
				System.out.println("Enter all info to update");

			}
			txtUp.setText("");

		});

		v.getChildren().addAll(gridPane, btupdate);
		Scene s4 = createStyledScene(v, 560, 430);
		stage4.setScene(s4);
		stage4.setTitle("Update martyr");
		stage4.setResizable(false);
		stage4.show();
	}

	// update martyr by district
	private void updateDistrict(MartyrsAVLTree martyrs, String date, Label heightText, Label sizeText, Label tfName,
			Label txtt, Label txta, Label txtd, Label txtl, Button MartyrScreen) {
		Stage stage4 = new Stage();
		VBox v = new VBox(20);
		v.setAlignment(Pos.CENTER);
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(30));

		ComboBox<String> districtBox = new ComboBox<>();// get from combo
		ObservableList<String> districtItems = FXCollections.observableArrayList(districtNames);
		districtBox.setItems(districtItems);

		Label lblN = new Label("martyr Name");
		TextField txtUp = new TextField();
		txtUp.setPromptText("MARTYR NAME");
		Label lblNe = new Label("martyr New district:");

		Button btupdate = new Button("Update");

		gridPane.add(lblN, 0, 0);
		gridPane.add(txtUp, 1, 0);
		gridPane.add(lblNe, 0, 1);
		gridPane.add(districtBox, 1, 1);

		btupdate.setOnAction(e -> {
			String Name = txtUp.getText();// get name

			String newdiStrict = districtBox.getValue();

			if (!Name.isEmpty() && newdiStrict != null) {
				martyrNode exist = martyrs.search(Name);// search martyr if exist
				if (exist != null) {
					exist.setDistrict(newdiStrict);
					statisticesDate(hashTable.search(date), tfName, txtt, txta, txtd, txtl, MartyrScreen);
					rewriteFile(selectedFile);
					System.out.println(Name + " old district updated successfully to :" + newdiStrict);
				} else {
					System.out.println("martyr with name :" + Name + "doesnot exist");
				}

			} else {
				System.out.println("Enter all info to update");

			}
			txtUp.setText("");

		});

		v.getChildren().addAll(gridPane, btupdate);
		Scene s4 = createStyledScene(v, 560, 430);
		stage4.setScene(s4);
		stage4.setTitle("Update martyr");
		stage4.setResizable(false);
		stage4.show();
	}

	// update martyr by gender
	private void updateGender(MartyrsAVLTree martyrs, String date, Label heightText, Label sizeText, Label tfName,
			Label txtt, Label txta, Label txtd, Label txtl, Button MartyrScreen) {
		Stage stage4 = new Stage();
		VBox v = new VBox(20);
		v.setAlignment(Pos.CENTER);
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(30));

		Label lblN = new Label("martyr Name");
		TextField txtUp = new TextField();
		txtUp.setPromptText("MARTYR NAME");
		Label lblNe = new Label("martyr New Gender");
		Button btupdate = new Button("Update");

		// Gender radio buttons
		ToggleGroup genderGroup = new ToggleGroup();
		RadioButton maleRadio = new RadioButton("M");
		maleRadio.setToggleGroup(genderGroup);
		RadioButton femaleRadio = new RadioButton("F");
		femaleRadio.setToggleGroup(genderGroup);

		HBox genderBox = new HBox(10, maleRadio, femaleRadio);
		genderBox.setAlignment(Pos.CENTER_LEFT);

		gridPane.add(lblN, 0, 0);
		gridPane.add(txtUp, 1, 0);
		gridPane.add(lblNe, 0, 1);
		gridPane.add(genderBox, 1, 1);

		btupdate.setOnAction(e -> {
			String Name = txtUp.getText();// get name
			RadioButton selectedGenderRadio = (RadioButton) genderGroup.getSelectedToggle();
			String newGender = (selectedGenderRadio != null) ? selectedGenderRadio.getText() : "";
			if (!Name.isEmpty() && !newGender.isEmpty()) {
				martyrNode exist = martyrs.search(Name);// search martyr
				if (exist != null) {
					exist.setGender(newGender);
					statisticesDate(hashTable.search(date), tfName, txtt, txta, txtd, txtl, MartyrScreen);
					rewriteFile(selectedFile);
					System.out.println(Name + " old gender  updated successfully to :" + newGender);

				} else {
					System.out.println("martyr with name :" + Name + "doesnot exist");
				}

			} else {
				System.out.println("Enter all info to update");

			}
			txtUp.setText("");

		});

		v.getChildren().addAll(gridPane, btupdate);
		Scene s4 = createStyledScene(v, 560, 430);
		stage4.setScene(s4);
		stage4.setTitle("Update martyr");
		stage4.setResizable(false);
		stage4.show();
	}

	private void updateEvent(MartyrsAVLTree martyrs, String date, Label heightText, Label sizeText, Label tfName,
			Label txtt, Label txta, Label txtd, Label txtl, Button MartyrScreen) {
		Stage stage4 = new Stage();
		VBox v = new VBox(20);
		v.setAlignment(Pos.CENTER);
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(30));

		Label lblN = new Label("Martyr Name:");
		TextField txtUp = new TextField();
		txtUp.setPromptText("Enter Martyr Name");
		Label lblNe = new Label("New Event Date:");
		DatePicker dpUpTo = new DatePicker();
		dpUpTo.setPromptText("Select New Event Date");
		Button btupdate = new Button("Update");

		gridPane.add(lblN, 0, 0);
		gridPane.add(txtUp, 1, 0);
		gridPane.add(lblNe, 0, 1);
		gridPane.add(dpUpTo, 1, 1);

		btupdate.setOnAction(e -> {
			String name = txtUp.getText().trim(); // get name
			if (name.isEmpty()) {
				System.out.println("Enter martyr name");
				return;
			}

			String dateNew = dpUpTo.getValue() == null ? null
					: dpUpTo.getValue().format(DateTimeFormatter.ofPattern("M/d/yyyy")); // get date
			if (dateNew == null) {
				System.out.println("Select new event date");
				return;
			}

			martyrNode exist = martyrs.search(name);
			if (exist == null) {
				System.out.println("Martyr with name " + name + " does not exist");
				return;
			}

			if (exist.event.equals(dateNew)) {
				System.out.println("New date is the same as the old date. Cannot update.");
				return;
			}

			// Update the event date
			exist.setEvent(dateNew);
			// Remove the martyr from the old date
			hashNode oldDate = hashTable.search(date);
			oldDate.martyrsTree.delete(name);
			// Add the martyr to the new date
			hashNode existNew = hashTable.search(dateNew);
			if (existNew == null) {
				hashTable.insert(dateNew, exist);
			} else {
				existNew.martyrsTree.insert(exist);
			}

			statisticesDate(hashTable.search(dateNew), tfName, txtt, txta, txtd, txtl, MartyrScreen);
			System.out.println("Update successfully");

			txtUp.setText("");
			dpUpTo.setValue(null);
		});

		v.getChildren().addAll(gridPane, btupdate);
		Scene s4 = createStyledScene(v, 560, 430);
		stage4.setScene(s4);
		stage4.setTitle("Update Martyr");
		stage4.setResizable(false);
		stage4.show();
	}

	private Scene createStyledScene(Parent root, double width, double height) {
		Scene scene = new Scene(root, width, height);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		return scene;
	}

	private void rewriteFile(File selectedFile) {// rewrite updated data to file
		try (BufferedWriter rewrite = new BufferedWriter(new FileWriter(selectedFile))) {
			for (int i = 0; i < hashTable.table.length; i++) {
				hashNode node = hashTable.table[i];
				// Check if the entry is not null
				if (node != null && node.flag == 'F') {
					writeMartyrsToFile(node.martyrsTree.root, rewrite);
				}
			}
//			System.out.println("File updated successfully.");
		} catch (IOException e) {
			System.err.println("Error writing to file: " + e.getMessage());
		}
	}

	private void writeMartyrsToFile(martyrNode node, BufferedWriter writer) {// write the AVL martyrs info to file
		try {
			if (node == null) {
				return; // No node to write
			}
			writer.write(node.getName() + "," + node.event + "," + node.getAge() + "," + node.getLocation() + ","
					+ node.getDistrict() + "," + node.getGender() + "\n");
			writeMartyrsToFile(node.right, writer);
			writeMartyrsToFile(node.left, writer);
		} catch (IOException e) {
			System.err.println("Error writing martyr data to file: " + e.getMessage());
		}
	}

	private void loadToAnotherFile() {// write data to anther file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("outputFile.csv"))) {
			for (int i = 0; i < hashTable.table.length; i++) {
				hashNode node = hashTable.table[i];
				if (node != null && node.martyrsTree != null) {
					writeMartyrsToFile(node.martyrsTree.root, writer);
				}
			}
			System.out.println();
			System.out.println("Data loaded to another file successfully.");
		} catch (IOException e) {
			System.err.println("Error writing data to output file: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}

//
//	public void martyrScreen(MartyrsAVLTree martyr, String date, Label tfName, Label txtt, Label txta, Label txtd,
//			Label txtl, Button MartyrScreen) {
//
//		TabPane tabPaneS = new TabPane();
//		Tab statsTab = new Tab("Stats");
//		Tab operationsTab = new Tab("Operations");
//		Tab printTab = new Tab("Print");
//
//		tabPaneS.getTabs().addAll(statsTab, operationsTab, printTab);
//
//		// Stats Tab
//		VBox statsVb = new VBox(10);
//		statsVb.setAlignment(Pos.CENTER);
//		Button bts = new Button("Tree size");
//		Label sizeText = new Label();
//		sizeText.setStyle("-fx-font-weight: bold");
//		HBox h1 = new HBox(20);
//		h1.setAlignment(Pos.CENTER);
//		h1.getChildren().addAll(bts, sizeText);
//
//		Button bth = new Button("Tree Height");
//		Label heightText = new Label();
//		heightText.setStyle("-fx-font-weight: bold");
//		HBox h2 = new HBox(20);
//		h2.setAlignment(Pos.CENTER);
//		h2.getChildren().addAll(bth, heightText);
//
//		statsVb.getChildren().addAll(h1, h2);
//
//		statsTab.setContent(statsVb);
//
//		// Operations Tab
//		VBox operationsContainer = new VBox(10);
//		operationsContainer.setAlignment(Pos.CENTER);
//		MenuBar menuBar = new MenuBar();
//		Menu operation = new Menu("Operation");
//		menuBar.getMenus().add(operation);
//
//		MenuItem btinsert = new MenuItem("Insert New martyr");
//		MenuItem btupdate = new MenuItem("Update martyr");
//		MenuItem btdelete = new MenuItem("Delete marty");
//		operation.getItems().addAll(btinsert, btupdate, btdelete);
//
//		operationsContainer.getChildren().add(menuBar);
//
//		operationsTab.setContent(operationsContainer);
//
//		// Print Tab
//		VBox printvb = new VBox(10);
//		printvb.setAlignment(Pos.CENTER);
//		Button btprintLevel = new Button("print level by level");
//		Button btprintTable = new Button("Print sorted Table");
//		Button btSaveData = new Button("Save updated data to another file");
//
//		printvb.getChildren().addAll(btprintLevel, btprintTable, btSaveData);
//
//		printTab.setContent(printvb);
//
//		// Event Handlers
//		btprintLevel.setOnAction(e -> martyr.printlevel());// print AVL level by level (right - left)
//		bts.setOnAction(e -> sizeText.setText(martyr.getSize() + ""));// get tree size
//		bth.setOnAction(e -> heightText.setText(martyr.getHeight() + ""));// get tree height
//
//		btinsert.setOnAction(e -> {// insert new martyr
//			insertMartyr(martyr, date, sizeText, heightText, tfName, txtt, txta, txtd, txtl, MartyrScreen);
//		});
//
//		btupdate.setOnAction(e -> {// update all info to exists martyr
//			updateMartyr(martyr, date, sizeText, heightText, tfName, txtt, txta, txtd, txtl, MartyrScreen);
//		});
//
//		btdelete.setOnAction(e -> {// delete exists martyr
//			deleteMartyr(martyr, date, sizeText, heightText, tfName, txtt, txta, txtd, txtl, MartyrScreen);
//		});
//
//		btSaveData.setOnAction(e -> loadToAnotherFile());// load updates data to anther file
//
//		btprintTable.setOnAction(e -> {// print martyrs sorted by age using heap sort (MIN Heap)
//			System.out.println();
//			heap.clearHeap();// clear old data
//			martyr.insertToHeap(heap);// insert martyr to head
//			heap.heapSort();// sort
//			heap.printSorted();// then print
//
//		});
//		Scene scene = new Scene(tabPaneS, 400, 400);
//		Stage thirdStage = new Stage();
//		thirdStage.setScene(scene);
//		thirdStage.setTitle("Martyr Screen");
//		thirdStage.show();
//	}
