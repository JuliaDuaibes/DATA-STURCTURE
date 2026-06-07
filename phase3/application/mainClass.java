
package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class mainClass extends Application {
	private File selectedFile;
	districtTree disTree = new districtTree();
	martyrList martList = new martyrList();
	ArrayList<String> districtNames;

	private Scene createStyledScene(Parent root, double width, double height) {
		double enlargedWidth = Math.max(width * 1.25, 500);
		double enlargedHeight = Math.max(height * 1.25, 300);
		Scene scene = new Scene(root, enlargedWidth, enlargedHeight);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		return scene;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		Button loadFileButton = new Button("Load File");
		loadFileButton.setOnAction(e -> {
			try {
				loadFile(primaryStage);

			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		VBox loadStack = new VBox(10);
		loadStack.setAlignment(Pos.CENTER);
		loadStack.getChildren().add(loadFileButton);
		Scene loadFileScene = createStyledScene(loadStack, 440, 220);

		primaryStage.setScene(loadFileScene);
		primaryStage.setTitle("Choose File");
		primaryStage.show();
	}

	private void loadFile(Stage stage) throws IOException {// choose file
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open File");
		fileChooser.setInitialDirectory(new File("c:\\"));
		selectedFile = fileChooser.showOpenDialog(stage);
		if (selectedFile != null) {
			try (BufferedReader buffer = new BufferedReader(new FileReader(selectedFile))) {

				String line;// split lines to make trees
				while ((line = buffer.readLine()) != null) {
					String[] parts = line.split(",");
					if (parts.length == 6) {
						String name = parts[0].trim();
						String event = parts[1].trim();
						String age = parts[2].trim();
						String location = parts[3].trim();
						String district = parts[4].trim();
						String gender = parts[5].trim();

						districtNode node = disTree.find(district);
						if (node == null) {
							node = new districtNode(district);
							disTree.insert(node.districtName);
						}

						relation(node, location, event, name, age, gender);
						// insert to martyr list sorted
						martList.insert(name, event, age, location, district, gender);
					}
				}
				disTree.print();
				rewriteFile(selectedFile, martList);
				districtNames = disTree.getNamesOfDistrict();
				disScreen(stage);
			} catch (IOException ex) {
				System.out.println("Error reading file: " + ex.getMessage());
			}
		} else {
			System.out.println("Choose file to load...");
		}
	}

	// make districts-locations-dates-martyrs
	private void relation(districtNode dis, String location, String date, String name, String age, String gender) {
		if (dis != null) {
			dis.locations.insert(location);
			locationNode loc = dis.locations.find(location);
			if (loc != null) {
				loc.dates.insert(date); // as expected
				datesTreeNode dateIn = loc.dates.find(date); // Search for the exact date
				if (dateIn != null) {
					if (dateIn.allmartyrIndate == null) {// if null Creat one
						dateIn.allmartyrIndate = new martyrList();
					}
					dateIn.allmartyrIndate.insert(name, date, age, location, dis.districtName, gender);

				} else {
					System.out.println("Date " + date + " not found!"); // Print the date if not found
				}
			} else {
				System.out.println("Location not found!");
			}
		} else {
			System.out.println("District not found!");
		}
	}

	public void disScreen(Stage stage) {// district screen all districts with total of martyr in
		disTree.print();
		Stage thirdStage = new Stage();
		VBox vDistrict = new VBox(20);
		vDistrict.setAlignment(Pos.CENTER);
		BorderPane root = new BorderPane();
		MenuBar menuBar = new MenuBar();
		Menu operation = new Menu("Operation");
		menuBar.getMenus().add(operation);
		// operation on hole tree
		MenuItem insert = new MenuItem("Insert New District");
		MenuItem update = new MenuItem("Update District");
		MenuItem delete = new MenuItem("Delete District");

		root.setTop(menuBar);
		operation.getItems().addAll(insert, update, delete);

		GridPane gp = new GridPane();
		gp.setHgap(10);
		gp.setVgap(10);
		gp.setAlignment(Pos.CENTER);

		HBox h1 = new HBox(10);
		h1.setAlignment(Pos.CENTER);
		// to navigate in-Order traversal
		Button btnext = new Button("Next");
		Button btprev = new Button("Previous");
		h1.getChildren().addAll(btnext, btprev);

		Label lbln = new Label("Current District:");
		Label tfName = new Label();
		tfName.setStyle("-fx-font-weight: bold");
		gp.add(lbln, 0, 0);
		gp.add(tfName, 1, 0);
		gp.add(h1, 1, 1);

		Label lbt = new Label("Total num of martyrs:");
		Label txtt = new Label();
		gp.add(lbt, 0, 2);
		gp.add(txtt, 1, 2);

		Button locationScreen = new Button("Location Screen");

		districtNode currentDistrict = disTree.goNext();
		if (currentDistrict != null) {
			// update statistics according to district
			statisticesDistrict(currentDistrict, tfName, txtt, locationScreen);
			btnext.setOnAction(e -> {
				districtNode nextDistrict = disTree.goNext();
				statisticesDistrict(nextDistrict, tfName, txtt, locationScreen);
			});

			btprev.setOnAction(e -> {
				districtNode prevDistrict = disTree.goPrevious();
				statisticesDistrict(prevDistrict, tfName, txtt, locationScreen);
			});
		} else {
			System.out.println("District tree is empty.");
		}
		insert.setOnAction(e -> insertDistrict(disTree, tfName, txtt, locationScreen));
		update.setOnAction(e -> updateDistrict(disTree, tfName, txtt, locationScreen));
		delete.setOnAction(e -> deleteDistrict(disTree, tfName, txtt, locationScreen));

		vDistrict.getChildren().addAll(gp, locationScreen);
		root.setCenter(vDistrict);
		Scene s3 = createStyledScene(root, 500, 400);
		thirdStage.setScene(s3);
		thirdStage.setTitle("District Screen");
		thirdStage.setResizable(false);
		thirdStage.show();
	}

	// update statistics according to district
	private void statisticesDistrict(districtNode node, Label tfName, Label txtt, Button locationScreen) {
		if (node != null) {
			tfName.setText(node.districtName);
			txtt.setText(disTree.calculateMartyrs(node) + " ");
			locationScreen.setOnAction(e1 -> {
				locScreen(node.locations.root, node.locations, node.districtName);
			});
		} else {
			tfName.setText("");
			txtt.setText("");
		}
	}

	// insert new district
	private void insertDistrict(districtTree districts, Label tfName, Label txtt, Button locationScreen) {
		disTree.print();
		System.out.println();
		Stage stage4 = new Stage();
		VBox v = new VBox(20);
		v.setAlignment(Pos.CENTER);
		HBox h = new HBox(10);
		h.setAlignment(Pos.CENTER);
		Label lblN = new Label("District name");
		TextField txtin = new TextField();
		txtin.setPromptText("DISTRICT NAME");
		h.getChildren().addAll(lblN, txtin);
		Button btinsert = new Button("Insert");

		btinsert.setOnAction(event -> {
			String districtName = txtin.getText();// get the name
			if (!districtName.isEmpty()) {
				boolean exist = disTree.contains(districtName);// check if already exist
				if (exist) {
					System.out.println("district already exist");
				} else {
					disTree.insert(districtName);// if not exist insert
					districtNode node = disTree.find(districtName);// find the node to update statistics
					statisticesDistrict(node, tfName, txtt, locationScreen);
					System.out.println("added succsesfully");
				}
			} else {
				System.out.println("You should write the name before clicking the button.");
			}
			txtin.setText("");// to clear
		});
		v.getChildren().addAll(h, btinsert);
		Scene s4 = createStyledScene(v, 300, 200);
		stage4.setScene(s4);
		stage4.setTitle("Insert District");
		stage4.setResizable(false);
		stage4.show();
	}

	// update exist district name
	private void updateDistrict(districtTree districts, Label tfName, Label txtt, Button locationScreen) {
		Stage stage4 = new Stage();
		VBox v = new VBox(20);
		v.setAlignment(Pos.CENTER);
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20));

		Label lblN = new Label("District Name");
		TextField txtUp = new TextField();
		txtUp.setPromptText("DISTRICT NAME");
		TextField txtUpTo = new TextField();
		Label lblNE = new Label("District New Name");
		txtUpTo.setPromptText("DISTRICT new NAME");
		Button btupdate = new Button("Update");

		gridPane.add(lblN, 0, 0);
		gridPane.add(txtUp, 1, 0);
		gridPane.add(lblNE, 0, 1);
		gridPane.add(txtUpTo, 1, 1);

		btupdate.setOnAction(e -> {
			String oldDistrictName = txtUp.getText();// old name
			String newDistrictName = txtUpTo.getText();// new name
			if (!oldDistrictName.isEmpty() && !newDistrictName.isEmpty()) {
				boolean exist = districts.contains(oldDistrictName);// check if exist to update
				if (exist) {
					boolean existNew = districts.contains(newDistrictName);// check if the new name exist
					if (!existNew) {// if not exist(the new name)
						// update
						districtNode node = districts.find(oldDistrictName);
						districts.updateDistrictName(node, newDistrictName);
						martList.updateDistrict(oldDistrictName, newDistrictName);// update in list
						districtNode nodeN = districts.find(newDistrictName);
						rewriteFile(selectedFile, martList);// rewrite the data updated to file
						statisticesDistrict(nodeN, tfName, txtt, locationScreen);// updated statistics

						System.out.println();
						System.out.println("update successfully");
					} else {
						System.out.println();
						System.out.println("The new district name already exists.");
					}
				} else {

					System.out.println("District does not exist");
				}
			} else {
				System.out.println("you should insert old name and new name to update");
			}
			// to clear
			txtUp.setText("");
			txtUpTo.setText("");
		});
		v.getChildren().addAll(gridPane, btupdate);
		Scene s4 = createStyledScene(v, 350, 200);
		stage4.setScene(s4);
		stage4.setTitle("Update District");
		stage4.setResizable(false);
		stage4.show();
	}

	// delete exist district
	private void deleteDistrict(districtTree districts, Label tfName, Label txtt, Button locationScreen) {
		Stage stage4 = new Stage();
		VBox v = new VBox(20);
		v.setAlignment(Pos.CENTER);
		HBox h = new HBox(10);
		h.setAlignment(Pos.CENTER);
		Label lblN = new Label("District name");
		TextField txtde = new TextField();
		txtde.setPromptText("DISTRICT NAME");
		Button btDelete = new Button("Delete");
		h.getChildren().addAll(lblN, txtde);

		btDelete.setOnAction(event -> {
			String districtName = txtde.getText();// district name
			if (!districtName.isEmpty()) {
				boolean exist = districts.contains(districtName);// check if exist
				if (exist) {
					districts.delete(districtName);
					martList.deleteDistrict(districtName);// delete from martyr list
					disTree.delete(districtName);// remove from list
					statisticesDistrict(disTree.goNext(), tfName, txtt, locationScreen);// update statistics
					rewriteFile(selectedFile, martList);// update in file

					System.out.println("delete successfully...");
				} else {
					System.out.println("district not exist");
				}
			} else {
				System.out.println("You should write the name before clicking the button.");
			}
			// to clear
			txtde.setText("");
		});

		v.getChildren().addAll(h, btDelete);
		Scene s4 = createStyledScene(v, 300, 200);
		stage4.setScene(s4);
		stage4.setTitle("Delete District");
		stage4.setResizable(false);
		stage4.show();
	}

	// location screen
	public void locScreen(locationNode location, locationTree locations, String districtName) {
		Stage thirdStage = new Stage();
		thirdStage.setTitle("Location Operation");
		BorderPane root = new BorderPane();

		MenuBar menuBar = new MenuBar();
		Menu operationMenu = new Menu("Operation");
		MenuItem insertItem = new MenuItem("Insert New Location");
		MenuItem updateItem = new MenuItem("Update Location");
		MenuItem deleteItem = new MenuItem("Delete Location");
		operationMenu.getItems().addAll(insertItem, updateItem, deleteItem);
		menuBar.getMenus().add(operationMenu);

		root.setTop(menuBar);

		VBox centerBox = new VBox(20);
		centerBox.setAlignment(Pos.CENTER);
		centerBox.setPadding(new Insets(20));

		Label currentLocationLabel = new Label("Current Location:");
		Label locationNameLabel = new Label();
		locationNameLabel.setStyle("-fx-font-weight: bold");

		HBox navigationButtons = new HBox(20);
		navigationButtons.setAlignment(Pos.CENTER);
		Button previousButton = new Button("Previous");
		Button nextButton = new Button("Next");
		navigationButtons.getChildren().addAll(previousButton, nextButton);

		GridPane statsGrid = new GridPane();
		statsGrid.setAlignment(Pos.CENTER);
		statsGrid.setHgap(10);
		statsGrid.setVgap(10);

		Label earliestLabel = new Label("Earliest Date:");
		Label earliestDateLabel = new Label();
		Label latestLabel = new Label("Latest Date:");
		Label latestDateLabel = new Label();
		Label maxMartyrsLabel = new Label("Date with Maximum Martyrs:");
		Label maxMartyrsDateLabel = new Label();

		// to change district and location
		ComboBox<String> districtBox = new ComboBox<>();
		ObservableList<String> districtItems = FXCollections.observableArrayList(districtNames);
		districtBox.setItems(districtItems);

		ComboBox<String> locationBox = new ComboBox<>();
		ObservableList<String> locationItems = FXCollections.observableArrayList();
		locationBox.setItems(locationItems);

		statsGrid.addRow(0, earliestLabel, earliestDateLabel);
		statsGrid.addRow(1, latestLabel, latestDateLabel);
		statsGrid.addRow(2, maxMartyrsLabel, maxMartyrsDateLabel);

		Button martyrScreenButton = new Button("Martyr Screen");

		centerBox.getChildren().addAll(currentLocationLabel, locationNameLabel, navigationButtons, statsGrid,
				martyrScreenButton, districtBox, locationBox);
		root.setCenter(centerBox);

		if (location != null) {
			statisticsOfLocations(location, locationNameLabel, earliestDateLabel, latestDateLabel, maxMartyrsDateLabel,
					martyrScreenButton, districtName);
			nextButton.setOnAction(e -> {
				locationNode next = locations.goNext();
				statisticsOfLocations(next, locationNameLabel, earliestDateLabel, latestDateLabel, maxMartyrsDateLabel,
						martyrScreenButton, districtName);

			});
			previousButton.setOnAction(e -> {
				// locationNode prev = locations.goPrevious();
				// statisticsOfLocations(prev, locationNameLabel, earliestDateLabel,
				// latestDateLabel, maxMartyrsDateLabel,
				// martyrScreenButton, districtName);

			});
		} else {
			System.out.println("Location tree is empty.");
		}
		// insert ,update ,delete location
		insertItem.setOnAction(e -> insertLocation(locations, districtName, locationNameLabel, earliestDateLabel,
				latestDateLabel, maxMartyrsDateLabel, martyrScreenButton));
		updateItem.setOnAction(e -> updateLocation(locations, districtName, locationNameLabel, earliestDateLabel,
				latestDateLabel, maxMartyrsDateLabel, martyrScreenButton));
		deleteItem.setOnAction(e -> deleteLocation(locations, districtName, locationNameLabel, earliestDateLabel,
				latestDateLabel, maxMartyrsDateLabel, martyrScreenButton));

		districtBox.setOnAction(e -> {
			String district = districtBox.getValue();
			districtNode disNode = disTree.find(district);
			if (disNode != null) {
				locationTree loctionsIn = disNode.locations;
				ArrayList<String> locationsName = loctionsIn.getNamesOfLocation();
				locationItems.clear();
				locationItems.addAll(locationsName);
			}
		});

		locationBox.setOnAction(e -> {
			String distName = districtBox.getValue();
			districtNode districtNode = disTree.find(distName);
			String locName = locationBox.getValue();
			if (locName != null) {
				locationNode loc = districtNode.locations.find(locName);
				locScreen(loc, districtNode.locations, distName);
			}
		});
		Scene scene = createStyledScene(root, 500, 400);
		thirdStage.setScene(scene);
		thirdStage.setTitle("Location Operation");
		thirdStage.setResizable(false);
		thirdStage.show();
	}

	// update statistics
	private void statisticsOfLocations(locationNode node, Label locationNameLabel, Label earliestDateLabel,
			Label latestDateLabel, Label maxMartyrsDateLabel, Button martyrScreenButton, String districtName) {
		if (node != null) {
			datesTree dates = node.dates;
			if (dates != null) {
				locationNameLabel.setText(node.locationName);
				earliestDateLabel.setText("" + dates.minDate());
				latestDateLabel.setText("" + dates.maxDate());
				maxMartyrsDateLabel.setText("" + dates.maxMartyr());
				martyrScreenButton.setOnAction(e1 -> martyrScreen(dates, districtName, node.locationName));
			}
		}

	}

	// insert location
	private void insertLocation(locationTree locations, String districtName, Label locationNameLabel,
			Label earliestDateLabel, Label latestDateLabel, Label maxMartyrsDateLabel, Button martyrScreenButton) {
		locations.printlevel();
		Stage stage4 = new Stage();
		VBox v = new VBox(20);
		v.setAlignment(Pos.CENTER);
		HBox h2 = new HBox(10);
		h2.setAlignment(Pos.CENTER);

		Label lblN = new Label("Location name");
		TextField tfin = new TextField();
		tfin.setPromptText("Location Name");

		HBox h3 = new HBox(10);
		h3.setAlignment(Pos.CENTER);
		Button btinsert = new Button("Insert");
		btinsert.setOnAction(event -> {
			String LocationName = tfin.getText();// get location name

			if (!LocationName.isEmpty()) {
				boolean locationExist = locations.contains(LocationName);// check if exist
				if (locationExist) {// if exist
					System.out.println("Location already exists");
				} else {// if not exist

					locations.insert(LocationName);// insert location to locations (tree in district)
					System.out.println("Added succsessfully....");
					// update statistics
					statisticsOfLocations(locations.root, locationNameLabel, earliestDateLabel, latestDateLabel,
							maxMartyrsDateLabel, martyrScreenButton, districtName);
				}
			} else {
				System.out.println("You should write the location name before clicking the button.");
			}
			tfin.setText("");
		});
		h2.getChildren().addAll(lblN, tfin);
		h3.getChildren().addAll(btinsert);
		v.getChildren().addAll(h2, h3);
		Scene s4 = createStyledScene(v, 300, 200);
		stage4.setScene(s4);
		stage4.setTitle("Insert location");
		stage4.setResizable(false);
		stage4.show();
	}

	// update location name exist location
	private void updateLocation(locationTree locations, String districtName, Label locationNameLabel,
			Label earliestDateLabel, Label latestDateLabel, Label maxMartyrsDateLabel, Button martyrScreenButton) {
		Stage stage4 = new Stage();
		VBox v = new VBox(20);
		v.setAlignment(Pos.CENTER);
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(30));

		Label lblN = new Label("Location Name");
		TextField txtUp = new TextField();
		txtUp.setPromptText("LOCATION NAME");
		Label lblNe = new Label("Location New Name");
		TextField txtUpTo = new TextField();
		txtUpTo.setPromptText("LOCATION new NAME");
		Button btupdate = new Button("Update");

		gridPane.add(lblN, 0, 0);
		gridPane.add(txtUp, 1, 0);
		gridPane.add(lblNe, 0, 1);
		gridPane.add(txtUpTo, 1, 1);

		btupdate.setOnAction(event -> {
			String oldLocationName = txtUp.getText();// old name
			String newLocationName = txtUpTo.getText();// new name
			if (!newLocationName.isEmpty() && !oldLocationName.isEmpty()) {
				boolean locationexist = locations.contains(oldLocationName);// check if old exist to update
				if (locationexist) {
					locationNode node = locations.find(oldLocationName);
					boolean newlocationExist = locations.contains(newLocationName);// check if new exist to update
					if (!newlocationExist) {
						// update
						locations.updateLocationName(node, oldLocationName, newLocationName);

						martList.updateLocationName(oldLocationName, newLocationName);
						statisticsOfLocations(node, locationNameLabel, earliestDateLabel, latestDateLabel,
								maxMartyrsDateLabel, martyrScreenButton, districtName);

						rewriteFile(selectedFile, martList);
						System.out.println("updated successfully..");
					} else {
						System.out.println("The new name " + newLocationName + " is already exist");
					}
				} else {
					System.out.println("Location not exist...");
				}

			} else {
				System.out.println("you should enter all info to update the location");
			}
			// to clear
			txtUp.setText("");
			txtUpTo.setText("");
		});
		v.getChildren().addAll(gridPane, btupdate);
		Scene s4 = createStyledScene(v, 350, 250);
		stage4.setScene(s4);
		stage4.setTitle("Update Location");
		stage4.setResizable(false);
		stage4.show();
	}

	// delete exist location
	private void deleteLocation(locationTree locations, String disName, Label locationNameLabel,
			Label earliestDateLabel, Label latestDateLabel, Label maxMartyrsDateLabel, Button martyrScreenButton) {
		Stage stage4 = new Stage();
		VBox v = new VBox(20);
		v.setAlignment(Pos.CENTER);

		HBox h2 = new HBox(10);
		h2.setAlignment(Pos.CENTER);

		Label lblN = new Label("Location name");
		TextField txtde = new TextField();
		txtde.setPromptText("Location Name");
		Button btDelete = new Button("Delete");

		h2.getChildren().addAll(lblN, txtde);

		btDelete.setOnAction(event -> {
			String locationName = txtde.getText();// location name
			if (!locationName.isEmpty()) {
				boolean exist = locations.contains(locationName);// check if exist to delete
				if (exist) {
					// delete from list and tree
					martList.deleteLocation(locationName);
					locations.delete(locationName);
					rewriteFile(selectedFile, martList);
					System.out.println("updated file");
					// update statistic
					statisticsOfLocations(locations.goNext(), locationNameLabel, earliestDateLabel, latestDateLabel,
							maxMartyrsDateLabel, martyrScreenButton, disName);
				} else {
					System.out.println("Location does not exist");
				}
			} else {
				System.out.println("You should write the name before clicking the button.");

			}
			txtde.setText("");// to clear

		});
		v.getChildren().addAll(h2, btDelete);
		Scene s4 = createStyledScene(v, 300, 200);
		stage4.setScene(s4);
		stage4.setTitle("Delete Location");
		stage4.setResizable(false);
		stage4.show();
	}

	// martyr Screen
	public void martyrScreen(datesTree dates, String disName, String locationName) {
		Stage thirdStage = new Stage();
		thirdStage.setTitle("Martyr Operation");

		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setAlignment(Pos.CENTER);

		HBox dateContainer = new HBox(10);
		dateContainer.setAlignment(Pos.CENTER);
		Label currentDateLabel = new Label("Current Date:");
		Label currentDateText = new Label();
		currentDateText.setStyle("-fx-font-weight: bold");
		dateContainer.getChildren().addAll(currentDateLabel, currentDateText);

		HBox buttonBar = new HBox(20);
		buttonBar.setAlignment(Pos.CENTER);
		Button nextButton = new Button("Next");
		Button prevButton = new Button("Previous");
		buttonBar.getChildren().addAll(nextButton, prevButton);

		VBox statsContainer = new VBox(10);
		statsContainer.setAlignment(Pos.CENTER);
		Label averageAgeLabel = new Label("Average martyrs age:");
		Label averageAgeText = new Label();
		averageAgeText.setStyle("-fx-font-weight: bold");
		Label youngestMartyrLabel = new Label("Youngest martyrs name:");
		Label youngestMartyrText = new Label();
		youngestMartyrText.setStyle("-fx-font-weight: bold");
		Label oldestMartyrLabel = new Label("Oldest martyrs name:");
		Label oldestMartyrText = new Label();
		oldestMartyrText.setStyle("-fx-font-weight: bold");

		Button btListOf = new Button("list of martyr in current Date");

		statsContainer.getChildren().addAll(new HBox(10, averageAgeLabel, averageAgeText),
				new HBox(10, youngestMartyrLabel, youngestMartyrText),
				new HBox(10, oldestMartyrLabel, oldestMartyrText), btListOf);

		HBox actionButtonBar = new HBox(20);
		actionButtonBar.setAlignment(Pos.CENTER);
		Button insertButton = new Button("Insert new martyr");
		Button updateButton = new Button("Update martyr");
		Button deleteButton = new Button("Delete martyr");
		Button searchButton = new Button("Search by name");
		actionButtonBar.getChildren().addAll(insertButton, updateButton, deleteButton, searchButton);

		HBox hbox = new HBox(10);
		hbox.setAlignment(Pos.CENTER);
		Button btSaveData = new Button("Save updated data to anthor file");
		hbox.getChildren().addAll(btSaveData);

		ComboBox<String> districtBox = new ComboBox<>();
		ObservableList<String> districtItems = FXCollections.observableArrayList(districtNames);
		districtBox.setItems(districtItems);

		ComboBox<String> locationBox = new ComboBox<>();
		ObservableList<String> locationItems = FXCollections.observableArrayList();
		locationBox.setItems(locationItems);

		gridPane.add(dateContainer, 0, 0);
		gridPane.add(buttonBar, 0, 1);
		gridPane.add(statsContainer, 0, 2);
		gridPane.add(actionButtonBar, 0, 3);
		gridPane.add(hbox, 0, 4);
		gridPane.add(districtBox, 0, 5);
		gridPane.add(locationBox, 0, 6);

		districtBox.setOnAction(e -> {
			String districtName = districtBox.getValue();
			if (districtName != null) {
				locationBox.getItems().clear();
				districtNode dis = disTree.find(districtName);
				if (dis != null) {
					locationTree locationsInDis = dis.locations;

					ArrayList<String> locations = locationsInDis.getNamesOfLocation();
					locationBox.getItems().addAll(locations);
				}
			}
		});
		locationBox.setOnAction(e -> {
			String districtName = districtBox.getValue();
			String location = locationBox.getValue();
			districtNode dis = disTree.find(districtName);
			locationTree locationsInDis = dis.locations;
			locationNode loc = locationsInDis.find(location);
			datesTree datesInLocation = loc.dates;
			martyrScreen(datesInLocation, disName, locationName);

		});
		// navigate in dates tree
		if (dates != null) {
			System.out.println();
			dates.print();
			datesTreeNode currentDate = dates.goNext();
			if (currentDate != null) {
				System.out.println(currentDate.date);
				currentDateText.setText(currentDate.date);
				staticties(currentDate, averageAgeText, youngestMartyrText, oldestMartyrText, currentDateText);
				btListOf.setOnAction(e3 -> showListMartyr(currentDate));

				nextButton.setOnAction(e -> {
					datesTreeNode next = dates.goNext();
					if (next != null) {
						currentDateText.setText(next.date);
						staticties(next, averageAgeText, youngestMartyrText, oldestMartyrText, currentDateText);
						btListOf.setOnAction(e3 -> showListMartyr(next));
					}
				});

				prevButton.setOnAction(e -> {
					datesTreeNode prev = dates.goPrevious();
					if (prev != null) {
						currentDateText.setText(prev.date);
						staticties(prev, averageAgeText, youngestMartyrText, oldestMartyrText, currentDateText);
						btListOf.setOnAction(e3 -> showListMartyr(prev));
					}
				});

			}
		}

		insertButton.setOnAction(e -> insertMartyr(dates, disName, locationName, averageAgeText, youngestMartyrText,
				oldestMartyrText, currentDateText));
		searchButton.setOnAction(e -> SearchMartyr(dates, disName, locationName));
		deleteButton.setOnAction(e2 -> deleteMartyr(dates, disName, locationName, averageAgeText, youngestMartyrText,
				oldestMartyrText, currentDateText));
		updateButton.setOnAction(e1 -> updateMartyr(dates, disName, locationName, averageAgeText, youngestMartyrText,
				oldestMartyrText, currentDateText));
		btSaveData.setOnAction(e -> loadToAnotherFile(martList));

		Scene scene = createStyledScene(gridPane, 900, 400);
		thirdStage.setScene(scene);
		thirdStage.show();
	}

	// update statistics
	private void staticties(datesTreeNode date, Label avrege, Label youngest, Label oldest, Label currentDateText) {
		if (date != null) {
			martyrList martyr = date.allmartyrIndate;
			if (martyr != null) {
				System.out.println(date.date);
				currentDateText.setText(date.date);
				avrege.setText(martyr.averageAge() + "");
				youngest.setText(martyr.youngestMartyr() + "");
				oldest.setText(martyr.oldestMartyr() + " ");
			}
		} else {
			currentDateText.setText("");
			avrege.setText("");
			youngest.setText("");
			oldest.setText(" ");

		}
	}

	// insert new martyr
	private void insertMartyr(datesTree dates, String districtName, String locationName, Label averageAgeText,
			Label youngestMartyrText, Label oldestMartyrText, Label currentDateText) {
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
		Label lbG = new Label("Gender:");
		TextField tfG = new TextField();

		gp.add(lbN, 0, 0);
		gp.add(tfN, 1, 0);
		gp.add(lbA, 0, 1);
		gp.add(tfA, 1, 1);
		gp.add(lbG, 0, 2);
		gp.add(tfG, 1, 2);

		Button btin = new Button("Insert ");
		sp.getChildren().add(btin);
		bp.setBottom(sp);

		DatePicker dp = new DatePicker();
		gp.add(new Label("Date:"), 0, 3);
		gp.add(dp, 1, 3);
		bp.setCenter(gp);

		btin.setOnAction(e1 -> {
			// get info
			String name = tfN.getText();
			String age = tfA.getText();
			String gender = tfG.getText();
			LocalDate date = dp.getValue();
			String formattedDate = null;
			if (date != null) {
				formattedDate = dp.getValue().format(DateTimeFormatter.ofPattern("M/d/yyyy"));// format of date
			}
			try {
				if (!age.isEmpty() && !formattedDate.isEmpty() && !name.isEmpty() && !gender.isEmpty()) {
					boolean contains = dates.contains(formattedDate);// if date exist append martyr to it

					if (contains) {
						datesTreeNode dDate = dates.find(formattedDate);
						if (dDate != null) {
							dDate.allmartyrIndate.insert(name, formattedDate, age, locationName, districtName, gender);
							martList.insert(name, formattedDate, age, locationName, districtName, gender);
							rewriteFile(selectedFile, martList);// rewrite file
							// update statistics
							staticties(dates.find(formattedDate), averageAgeText, youngestMartyrText, oldestMartyrText,
									currentDateText);
						}

					} else {// if not exist create new date
						dates.insert(formattedDate);
						datesTreeNode dDate = dates.find(formattedDate);
						if (dDate != null) {
							dDate.allmartyrIndate.insert(name, formattedDate, age, locationName, districtName, gender);
							martList.insert(name, formattedDate, age, locationName, districtName, gender);
							rewriteFile(selectedFile, martList);// rewrite in file
							// update statistics
							staticties(dates.find(formattedDate), averageAgeText, youngestMartyrText, oldestMartyrText,
									currentDateText);
						}

					}

				} else {
					System.out.println("Missing information, try again...");
				}

			} catch (IllegalArgumentException ex) {
				System.out.println(ex);
			}

			// Clear fields
			tfN.setText("");
			tfA.setText("");
			tfG.setText("");
			dp.setValue(null); // Clear the DatePicker

		});

		Scene s4 = createStyledScene(bp, 350, 300);
		stage4.setScene(s4);
		stage4.setResizable(false);
		stage4.setTitle("Insert New Martyr");
		stage4.show();
	}

	// delete exist martyr
	private void deleteMartyr(datesTree dates, String districtName, String locationName, Label average, Label youngest,
			Label oldest, Label currentDateText) {
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
			String name = txtUp.getText();

			if (!name.isEmpty()) {
				boolean deleted = false;
				martyrNode exist = searchMartyrFull(dates, name);// search martyr in all dates
				if (exist != null) {
					datesTreeNode date = dates.find(exist.event);// find date to delete martyr from it
					if (date != null) {
						date.allmartyrIndate.removeMartyr(exist.Name);// remove martyr from list martyr in date
						if (date.allmartyrIndate != null) {// if there still the date have martyr
							staticties(date, average, youngest, oldest, currentDateText);// update statistics
						} else {// if not have anthor martyr
							dates.delete(exist.event);// delete date
							staticties(null, average, youngest, oldest, currentDateText);// update statistics
						}
						martList.removeMartyr(name);
						rewriteFile(selectedFile, martList);
						deleted = true;
					}
				}
				if (deleted) {
					System.out.println("Martyr with name " + name + " deleted successfully.");
				} else {
					System.out.println("Martyr with name " + name + " does not exist.");
				}

			} else {
				System.out.println("Enter all info to delete.");
			}
			txtUp.setText("");
		});

		v.getChildren().addAll(gridPane1, btUpdate);
		Scene s4 = createStyledScene(v, 350, 250);
		stage4.setScene(s4);
		stage4.setTitle("Delete Martyr");
		stage4.setResizable(false);
		stage4.show();
	}

	// update martyr by name,age,gender and event
	private void updateMartyr(datesTree dates, String districtName, String locationName, Label avrege, Label youngest,
			Label oldest, Label currentDateText) {
		Stage stage4 = new Stage();
		Label label = new Label("Selected option: to update martyr");

		ComboBox<String> comboBox = new ComboBox<>();// to select witch update
		comboBox.getItems().addAll("Update name", "Update age", "Update event", "Update gender");

		comboBox.setOnAction(event -> {
			String selectedOption = comboBox.getSelectionModel().getSelectedItem();
			if (selectedOption != null) {
				switch (selectedOption) {
				case "Update name":
					updateName(dates, districtName, locationName, avrege, youngest, oldest, currentDateText);

					break;
				case "Update age":
					updateAge(dates, districtName, locationName, avrege, youngest, oldest, currentDateText);
					break;
				case "Update event":
					updateEvent(dates, districtName, locationName, avrege, youngest, oldest, currentDateText);
					break;
				case "Update gender":
					updateGender(dates, districtName, locationName, avrege, youngest, oldest, currentDateText);
					break;
				}
			}
		});

		VBox vbox = new VBox(10);
		vbox.setPadding(new Insets(20));
		vbox.getChildren().addAll(label, comboBox);

		Scene s4 = createStyledScene(vbox, 250, 250);
		stage4.setScene(s4);
		stage4.setResizable(false);
		stage4.setTitle("Update martyr(options)");
		stage4.show();
	}

	// update martyr by name
	private void updateName(datesTree dates, String districtName, String locationName, Label avrege, Label youngest,
			Label oldest, Label currentDateText) {
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
				martyrNode exist = searchMartyrFull(dates, oldName);// search my name in all dates
				if (exist != null) {
					datesTreeNode date = dates.find(exist.event);// find date to update list martyr in this date
					if (date != null) {// if exist
						date.allmartyrIndate.setName(oldName, newName);// update name in list
						martList.setName(oldName, newName);// update name in martyr list
						rewriteFile(selectedFile, martList);// rewrite the updated in file
						staticties(date, avrege, youngest, oldest, currentDateText);// update statistics
					}
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
		Scene s4 = createStyledScene(v, 350, 250);
		stage4.setScene(s4);
		stage4.setTitle("Update Martyr");
		stage4.setResizable(false);
		stage4.show();
	}

	// update martyr by age
	private void updateAge(datesTree dates, String districtName, String locationName, Label avrege, Label youngest,
			Label oldest, Label currentDateText) {
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
				String oldName = txtUp.getText();// get name
				String newAge = txtUpTo.getText();// get new age
				if (!oldName.isEmpty() && !newAge.isEmpty()) {
					martyrNode exist = searchMartyrFull(dates, oldName);// search martyr in dates
					if (exist != null) {
						datesTreeNode date = dates.find(exist.event);// get martyr date to update
						if (date != null) {// if exist update in list martyr on this date
							date.allmartyrIndate.setAge(oldName, newAge);
							martList.setAge(oldName, newAge);
							rewriteFile(selectedFile, martList);// rewrite file
							staticties(date, avrege, youngest, oldest, currentDateText);// update statistics
						}
					} else {
						System.out.println("martyr with name :" + oldName + "doesnot exist");
					}

				} else {
					System.out.println("Enter all info to update");

				}
			} catch (IllegalArgumentException ex) {
				System.out.println(ex);
			}
			txtUp.setText("");
			txtUpTo.setText("");
		});

		v.getChildren().addAll(gridPane, btupdate);
		Scene s4 = createStyledScene(v, 350, 250);
		stage4.setScene(s4);
		stage4.setTitle("Update martyr");
		stage4.setResizable(false);
		stage4.show();
	}

	// update martyr by event
	private void updateEvent(datesTree dates, String districtName, String locationName, Label avrege, Label youngest,
			Label oldest, Label currentDateText) {
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
		Label lblNe = new Label("martyr New Event Date");
		DatePicker dpUpTo = new DatePicker();
		dpUpTo.setPromptText("Martyr new EVENT DATE");
		Button btupdate = new Button("Update");

		gridPane.add(lblN, 0, 0);
		gridPane.add(txtUp, 1, 0);
		gridPane.add(lblNe, 0, 1);
		gridPane.add(dpUpTo, 1, 1);

		btupdate.setOnAction(e -> {
			String oldName = txtUp.getText();// get name

			String dateNew = dpUpTo.getValue().format(DateTimeFormatter.ofPattern("M/d/yyyy"));// get date

			if (!oldName.isEmpty() && dateNew != null) {
				martyrNode exist = searchMartyrFull(dates, oldName);// search martyr if exist
				if (exist != null) {
					datesTreeNode oldDate = dates.find(exist.event);// find old date to remove martyr from this date
					if (oldDate != null) {// remove martyr from old date
						oldDate.allmartyrIndate.removeMartyr(oldName);
						datesTreeNode newDate = dates.find(dateNew);
						if (newDate != null) {// update event(if the new date already exist)
							oldDate.date = newDate.date;
							newDate.allmartyrIndate.insert(oldName, dateNew, exist.Age, districtName, locationName,
									exist.Gender);
							martList.setEvent(oldName, dateNew.toString());
							rewriteFile(selectedFile, martList);
							staticties(newDate, avrege, youngest, oldest, currentDateText);
						} else {// (if the new date does not exist)Create new date apened the new date to dates
							// tree then insert martyr to it
							dates.insert(dateNew);
							datesTreeNode newdate = dates.find(dateNew);
							if (newdate != null) {
								newdate.allmartyrIndate.insert(oldName, dateNew, exist.Age, districtName, locationName,
										exist.Gender);
								martList.setEvent(oldName, dateNew.toString());
								rewriteFile(selectedFile, martList);
								staticties(newdate, avrege, youngest, oldest, currentDateText);
							}
						}
					}
				} else {
					System.out.println("martyr with name :" + oldName + "doesnot exist");
				}

			} else {
				System.out.println("Enter all info to update");

			}
			txtUp.setText("");
			dpUpTo.setValue(null);

		});

		v.getChildren().addAll(gridPane, btupdate);
		Scene s4 = createStyledScene(v, 350, 250);
		stage4.setScene(s4);
		stage4.setTitle("Update martyr");
		stage4.setResizable(false);
		stage4.show();
	}

	// update martyr by gender
	private void updateGender(datesTree dates, String districtName, String locationName, Label avrege, Label youngest,
			Label oldest, Label currentDateText) {
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
		TextField txtUpTo = new TextField();
		txtUpTo.setPromptText("Martyr new GENDER");
		Button btupdate = new Button("Update");

		gridPane.add(lblN, 0, 0);
		gridPane.add(txtUp, 1, 0);
		gridPane.add(lblNe, 0, 1);
		gridPane.add(txtUpTo, 1, 1);

		btupdate.setOnAction(e -> {
			String oldName = txtUp.getText();// get name
			String newGender = txtUpTo.getText();// get new Gender
			if (!oldName.isEmpty() && !newGender.isEmpty()) {
				martyrNode exist = searchMartyrFull(dates, oldName);// search martyr in dates
				if (exist != null) {
					datesTreeNode date = dates.find(exist.event);// find the date to updated
					if (date != null) {
						date.allmartyrIndate.setGender(oldName, newGender);// update in list
						martList.setName(oldName, newGender);// update gender
						rewriteFile(selectedFile, martList);// rewrite file
						staticties(date, avrege, youngest, oldest, currentDateText);// update statistics
					}
				} else {
					System.out.println("martyr with name :" + oldName + "doesnot exist");
				}

			} else {
				System.out.println("Enter all info to update");

			}
			txtUp.setText("");
			txtUpTo.setText("");

		});

		v.getChildren().addAll(gridPane, btupdate);
		Scene s4 = createStyledScene(v, 350, 250);
		stage4.setScene(s4);
		stage4.setTitle("Update martyr");
		stage4.setResizable(false);
		stage4.show();
	}

	// search martyr in dates tree by full name
	private martyrNode searchMartyrFull(datesTree datTree, String martyrName) {
		return searchFullName(datTree.root, martyrName);
	}

	private martyrNode searchFullName(datesTreeNode currentNode, String martyrName) {
		if (currentNode == null) {
			return null;
		}
		if (currentNode.allmartyrIndate != null) {
			martyrNode foundMartyr = currentNode.allmartyrIndate.searchMartyrByFullName(martyrName);// search in martyr
																									// list
			if (foundMartyr != null) {
				return foundMartyr;
			}
		}
		// search in left nodes
		martyrNode foundMartyrInLeft = searchFullName(currentNode.left, martyrName);
		if (foundMartyrInLeft != null) {
			return foundMartyrInLeft;
		}
		// search in right nodes
		return searchFullName(currentNode.right, martyrName);
	}

	// show martyr list sorted by age in date in table view
	private void showListMartyr(datesTreeNode date) {
		Stage stage4 = new Stage();
		BorderPane bp = new BorderPane();

		Label lb = new Label("ALL martyr:");
		bp.setTop(lb);

		ObservableList<martyrNode> martyrList = FXCollections.observableArrayList();

		TableView<martyrNode> tableView = new TableView<>();
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

		if (date != null && date.allmartyrIndate != null) {
			martyrList.clear();
			martyrList.addAll(date.allmartyrIndate.listMartyr());// get list in date to print
		} else {
			// If there are no martyrs, display a message
			Label noMartyrLabel = new Label("There are no martyrs for this date.");
			bp.setCenter(noMartyrLabel);
		}

		bp.setCenter(tableView);

		Scene s4 = createStyledScene(bp, 900, 300);
		stage4.setScene(s4);
		stage4.setResizable(false);
		stage4.setTitle("Show Martyr List");
		stage4.show();
	}

	// search martyr by part name in all dates
	private void SearchMartyr(datesTree dates, String districtName, String locationName) {
		Stage stage4 = new Stage();
		BorderPane bp = new BorderPane();

		GridPane gp = new GridPane();
		gp.setHgap(10);
		gp.setVgap(10);
		gp.setAlignment(Pos.CENTER);

		Button btde = new Button("Search");
		Label l = new Label("Martyr Name:");

		TextField tfn = new TextField();

		gp.add(l, 0, 0);
		gp.add(tfn, 1, 0);
		gp.add(btde, 1, 1);
		ObservableList<martyrNode> martyrList = FXCollections.observableArrayList();

		TableView<martyrNode> tableView = new TableView<>();
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

		Label linfo = new Label();

		btde.setOnAction(e -> {
			String name = tfn.getText();// get part to search
			districtNode dis = disTree.find(districtName);// to get locations
			if (dis != null) {
				locationTree locIn = dis.locations;// to get location
				locationNode loc = locIn.find(locationName);
				if (loc != null) {// find martyrs to search on it
					datesTree dateIn = loc.dates;
					martyrList.clear();
					martyrList.addAll(searchMartyr(dateIn, name));
					if (martyrList.isEmpty()) {
						linfo.setText("No martyrs found with the given name.");
					} else {
						linfo.setText(""); // Clear previous message
					}
				}
			}
		});

		bp.setTop(gp);
		bp.setCenter(tableView);

		Scene s4 = createStyledScene(bp, 900, 400);
		stage4.setScene(s4);
		stage4.setResizable(false);
		stage4.setTitle("Search martyr");
		stage4.show();
	}

	// list of all martyr with this part in datTree
	private ObservableList<martyrNode> searchMartyr(datesTree datTree, String martyrName) {
		return search(datTree.root, martyrName);
	}

	private ObservableList<martyrNode> search(datesTreeNode currentNode, String martyrName) {
		ObservableList<martyrNode> foundMartyr = FXCollections.observableArrayList();
		if (currentNode == null) {
			return foundMartyr;
		}
		if (currentNode.allmartyrIndate != null) {
			ArrayList<martyrNode> searchResult = currentNode.allmartyrIndate.searchMartyrByName(martyrName);
			if (searchResult != null) {
				foundMartyr.addAll(searchResult); // search part of name in list
			}
		}
		foundMartyr.addAll(search(currentNode.left, martyrName)); // Search on left nodes
		foundMartyr.addAll(search(currentNode.right, martyrName)); // Search on right nodes
		return foundMartyr;
	}
	

	// load the updated data structure to outputFile
	private void loadToAnotherFile(martyrList martyrs) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("outputFile.csv"))) {
			martyrNode current = martyrs.head;
			while (current != null) {
				writer.write(current.getName() + "," + current.getEvent() + "," + current.getAge() + ","
						+ current.getLocation() + "," + current.getDistrict() + "," + current.getGender() + "\n");
				current = current.next;
				if (current == martyrs.head) {
					break;
				}
			}
			System.out.println("Updated File.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// rewrite the updated data structure to selected file
	private void rewriteFile(File selectedFile, martyrList martyrs) {
		try (FileWriter rewrite = new FileWriter(selectedFile)) {
			martyrNode current = martList.head;
			if (martList.head != null) {
				while (current.next != martList.head) {
					rewrite.write(current.getName() + "," + current.getEvent() + "," + current.getAge() + ","
							+ current.getLocation() + "," + current.getDistrict() + "," + current.getGender() + "\n");
					current = current.next;
				}
				System.out.println("updated File.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);

	}

}
