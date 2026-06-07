package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainClass extends Application {
	private File selectedFile;
	martyrList martyrList = new martyrList();
	locationList locationList = new locationList();
	DistrictList districtList = new DistrictList();

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		Button loadFileButton = new Button("Load File");
		loadFileButton.setOnAction(e -> {
			try {
				loadFile(primaryStage);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		VBox loadStack = new VBox(10);
		loadStack.setAlignment(Pos.CENTER);
		loadStack.getChildren().add(loadFileButton);
		Scene loadFileScene = createStyledScene(loadStack, 300, 100);

		primaryStage.setScene(loadFileScene);
		primaryStage.setTitle("Choose File");
		primaryStage.show();
	}

	private void loadFile(Stage stage) throws FileNotFoundException, IOException {// to load file
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open File");
		fileChooser.setInitialDirectory(new File("c:\\"));
		selectedFile = fileChooser.showOpenDialog(stage);
		if (selectedFile != null) {
			// read data from file and store the data in links
			try (BufferedReader buffer = new BufferedReader(new FileReader(selectedFile))) {
				String line;
				// split lines
				while ((line = buffer.readLine()) != null) {
					int string1 = line.indexOf(',');
					int string2 = line.indexOf(',', string1 + 1);
					int string3 = line.indexOf(',', string2 + 1);
					int string4 = line.indexOf(',', string3 + 1);
					int string5 = line.indexOf(',', string4 + 1);

					if (string1 != -1 && string2 != -1 && string3 != -1 && string4 != -1 && string5 != -1) {
						String name = line.substring(0, string1).trim();
						String event = line.substring(string1 + 1, string2).trim();
						String age = line.substring(string2 + 1, string3).trim();
						String location = line.substring(string3 + 1, string4).trim();
						String district = line.substring(string4 + 1, string5).trim();
						String gender = line.substring(string5 + 1).trim();

						districtList.insertDis(district);// insert district to district list
						districtNode dis = districtList.exist(district);// district node
						dis.AllLocations.insert(location);// insert locations to the district with name (district)
						locationList.insert(location);
						// return location node from location list to add martyr to the location with
						// name (location)
						locationNode loc = locationList.exist(location);
						loc.Allmartyrs.insert(name, event, age, location, district, gender);
						martyrList.insert(name, event, age, location, district, gender);// added martyr to martyr
																						// list(all martyr)

					} else {
						// Handle invalid line format
						System.out.println("Invalid line: " + line);
					}
				}
				martyrList.display();
//				rewriteFile(selectedFile, martyrList);// rewrite the martyr list (stored in file)

				// show new screens
				screen(stage);
			} catch (IOException ex) {
				System.out.println(ex);
			}
		} else {

			System.out.println("Choose file to load...");
		}
	}

	public void screen(Stage stage) {// display district
		VBox vScreen = new VBox(20);
		vScreen.setAlignment(Pos.CENTER);

		Button districtScreen = new Button("District Screen");
		districtScreen.setOnAction(e -> disOpertion());

		vScreen.getChildren().addAll(districtScreen);
		Scene s2 = createStyledScene(vScreen, 300, 200);
		stage.setScene(s2);
		stage.setTitle("Screens");
		stage.setResizable(false);
		stage.show();
	}

	public void disOpertion() {// district operation
		districtList.display();
		Stage thirdStage = new Stage();
		VBox vDistrict = new VBox(20);
		vDistrict.setAlignment(Pos.CENTER);
		BorderPane root = new BorderPane();
		MenuBar menuBar = new MenuBar();
		Menu operation = new Menu("Operation");
		menuBar.getMenus().add(operation);

		MenuItem insert = new MenuItem("Insert New District");
		MenuItem update = new MenuItem("Update District");
		MenuItem delete = new MenuItem("Delete District");

		root.setTop(menuBar);
		operation.getItems().addAll(insert, update, delete);

		insert.setOnAction(e -> {// insert new district to the district list
			insertDistrict();
		});
		update.setOnAction(e -> {// update old district (name)
			updateDistrict();

		});
		delete.setOnAction(e -> {// delete old district using (name)& all locations in this district also all
			deleteDistrict(); // martyrs in the same district
		});

		Label lbln = new Label("Current District");
		Label tfName = new Label();
		GridPane gp = new GridPane();
		gp.setHgap(10);
		gp.setVgap(10);
		gp.setAlignment(Pos.CENTER);
		// two button to navigate throw district list and show all statistics foe
		// current district
		HBox h1 = new HBox(10);
		h1.setAlignment(Pos.CENTER);
		Button btnext = new Button("Next");
		Button btprev = new Button("Previous");
		h1.getChildren().addAll(btnext, btprev);
		// to store the statistics
		Label lbt = new Label("Total num of martyr");
		Label txtt = new Label();
		Label lbF = new Label("Total num Female martyr");
		Label txtf = new Label();
		Label lbM = new Label("Total num male martyr");
		Label txtm = new Label();
		Label lba = new Label("Average martyr Age");
		Label txta = new Label();
		Label lbD = new Label("Date has max martyr ");
		Label txtd = new Label();

		gp.add(lbln, 0, 0);
		gp.add(tfName, 1, 0);
		gp.add(h1, 1, 1);
		gp.add(lbt, 0, 2);
		gp.add(txtt, 1, 2);
		gp.add(lbF, 0, 3);
		gp.add(txtf, 1, 3);
		gp.add(lbM, 0, 4);
		gp.add(txtm, 1, 4);
		gp.add(lba, 0, 5);
		gp.add(txta, 1, 5);
		gp.add(lbD, 0, 6);
		gp.add(txtd, 1, 6);

		Button locationScreen = new Button("Location Screen");// load the location screen in current district
		Button btTotal = new Button("Total number of martyrs (in date)");
		if (districtList.head != null) {// if list not empty
			// set the first district in the district list without click on button
			tfName.setText(districtList.head.districtName);
			// to load the location screen (first location in first district)
			locationScreen.setOnAction(e -> locOpertion(districtList.head.districtName));
			// statistics to the first district
			txtt.setText("" + martyrList.TotalMartyrInDistrict(districtList.head.districtName));
			txtm.setText("" + martyrList.numOfMaleIndistrict(districtList.head.districtName));
			txtf.setText("" + martyrList.numOfFemaleIndistrict(districtList.head.districtName));
			txta.setText("" + martyrList.avergeAgeIndistrict(districtList.head.districtName));
			if (martyrList.dateMaxMartyr(districtList.head.districtName) != null) {
				txtd.setText("" + martyrList.dateMaxMartyr(districtList.head.districtName));
			} else {
				txtd.setText("There is no maximum date");
			}

			btTotal.setOnAction(e -> {// number of martyr in date(in the first district)
				numOfMartyrforDate(districtList.head.districtName);
			});
			// to move next
			btnext.setOnAction(e1 -> {

				districtNode current = districtList.head;
				if (current.next != districtList.head) {
					tfName.setText(current.next.districtName);
					districtList.head = current.next; // Update the current reference
				}
				// load location screen (locations in current.next.districtName)
				locationScreen.setOnAction(e -> locOpertion(current.next.districtName));
				// statistics to the current.next.districtName
				txtt.setText("" + martyrList.TotalMartyrInDistrict(current.next.districtName));
				txtm.setText("" + martyrList.numOfMaleIndistrict(current.next.districtName));
				txtf.setText("" + martyrList.numOfFemaleIndistrict(current.next.districtName));
				txta.setText("" + martyrList.avergeAgeIndistrict(current.next.districtName));
				if (martyrList.dateMaxMartyr(current.next.districtName) != null) {
					txtd.setText("" + martyrList.dateMaxMartyr(current.next.districtName));
				} else {
					txtd.setText("There is no maximum date");

				}
				// number of martyr in date
				btTotal.setOnAction(e -> {
					numOfMartyrforDate(current.next.districtName);
				});
			});
			// to move previous
			btprev.setOnAction(e2 -> {
				districtNode current = districtList.head; // Retrieve the current node
				if (current.prev != districtList.head) {
					tfName.setText(current.prev.districtName);
					districtList.head = current.prev; // Update the current reference
				}
				// load location screen (locations in current.prev.districtName)
				locationScreen.setOnAction(e -> locOpertion(current.prev.districtName));
				// statistics to the current.prev.districtName
				txtt.setText("" + martyrList.TotalMartyrInDistrict(current.prev.districtName));
				txtm.setText("" + martyrList.numOfMaleIndistrict(current.prev.districtName));
				txtf.setText("" + martyrList.numOfFemaleIndistrict(current.prev.districtName));
				txta.setText("" + martyrList.avergeAgeIndistrict(current.prev.districtName));
				if (martyrList.dateMaxMartyr(current.prev.districtName) != null) {
					txtd.setText("" + martyrList.dateMaxMartyr(current.prev.districtName));
				} else {
					txtd.setText("There is no maximum date");
				}
				// number of martyr in date
				btTotal.setOnAction(e -> {
					numOfMartyrforDate(current.prev.districtName);
				});
			});
		} else {
			System.out.println("District list is empty.");
		}
		vDistrict.getChildren().addAll(gp, btTotal, locationScreen);
		root.setCenter(vDistrict);
		Scene s3 = createStyledScene(root, 400, 350);
		thirdStage.setScene(s3);
		thirdStage.setTitle("District Operation");
		thirdStage.setResizable(false);
		thirdStage.show();
	}

	private void insertDistrict() {// insert new district to the list
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
			String districtName = txtin.getText();
			if (!districtName.isEmpty()) {
				boolean exist = districtList.ifExistDistrict(districtName);// check if the district exist
				if (exist) {
					System.out.println("district already exist");
				} else {
					districtList.insertDis(districtName);
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

	private void updateDistrict() {// update exist district
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

		btupdate.setOnAction(event -> {
			String oldDistrictName = txtUp.getText();// old name
			String newDistrictName = txtUpTo.getText();// new name
			if (!oldDistrictName.isEmpty() && !newDistrictName.isEmpty()) {
				boolean exist = districtList.ifExistDistrict(oldDistrictName);// if exist update
				if (exist) {
					boolean exsitNew = districtList.ifExistDistrict(newDistrictName);
					if (!exsitNew) {// check if the new name exist (if not exist update)
						districtList.updateDistrict(oldDistrictName, newDistrictName);// update in district list
						martyrList.updateDistrict(oldDistrictName, newDistrictName);// update in martyr list
						rewriteFile(selectedFile, martyrList);// update in file
					} else {
						System.out.println("The new name " + newDistrictName + " already exists...");
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

	private void deleteDistrict() {// delete exist district
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
				boolean exist = districtList.ifExistDistrict(districtName);// check if exist
				if (exist) {
					martyrList.deleteDistrict(districtName);// delete from martyr list
					rewriteFile(selectedFile, martyrList);// update in file
					districtList.deleteDistrict(districtName);// remove from list
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

	private void numOfMartyrforDate(String districtName) {// number of martyr for a given date (in specific district)
		Stage stage4 = new Stage();
		VBox v = new VBox(20);
		v.setAlignment(Pos.CENTER);
		GridPane gp = new GridPane();
		gp.setAlignment(Pos.CENTER);
		gp.setHgap(10);
		gp.setVgap(10);
		gp.setAlignment(Pos.CENTER);
		StackPane sp = new StackPane();
		sp.setAlignment(Pos.CENTER);

		Label lblDate = new Label("Date:");
		TextField tfdate = new TextField();
		Label lblNum = new Label("Number of Martyr:");
		Text tfnum = new Text();
		Button btc = new Button("Calculate");
		HBox h1 = new HBox(10);
		h1.setAlignment(Pos.CENTER);
		HBox h2 = new HBox(10);
		h2.setAlignment(Pos.CENTER);
		sp.getChildren().add(btc);

		gp.add(lblDate, 0, 0);
		gp.add(tfdate, 1, 0);
		gp.add(sp, 2, 0);
		gp.add(lblNum, 0, 1);
		gp.add(tfnum, 1, 1);

		btc.setOnAction(e -> {
			String date = tfdate.getText();// get date
			tfnum.setText("" + martyrList.getTotalMartyrsForDate(date, districtName));// to get total martyr
		});
		// to clear
		tfnum.setText("");
		tfdate.setText("");

		v.getChildren().addAll(gp);
		Scene s4 = createStyledScene(v, 350, 200);
		stage4.setScene(s4);
		stage4.setResizable(false);
		stage4.setTitle("Num of Martyr");
		stage4.show();
	}

	public void locOpertion(String districtName) {// location operation
		Stage thirdStage = new Stage();
		VBox vDistrict = new VBox(20);
		vDistrict.setAlignment(Pos.CENTER);
		BorderPane root = new BorderPane();
		MenuBar menuBar = new MenuBar();
		Menu operation = new Menu("Operation");
		MenuItem insert = new MenuItem("insert new Location");
		MenuItem update = new MenuItem("update  Location");
		MenuItem delete = new MenuItem("delete Location");
		operation.getItems().addAll(insert, update, delete);
		menuBar.getMenus().add(operation);

		root.setTop(menuBar);
		insert.setOnAction(e -> {// insert new location to exist district
			insertLocation(districtName);
		});
		update.setOnAction(e -> {// update exist location be name
			updateLocation(districtName);
		});
		delete.setOnAction(e -> {// delete exist location
			deleteLocation(districtName);
		});
		Label lbln = new Label("Current Location");
		Label tfName = new Label();
		GridPane gp = new GridPane();
		gp.setHgap(10);
		gp.setVgap(10);
		gp.setAlignment(Pos.CENTER);

		HBox h1 = new HBox(10);
		h1.setAlignment(Pos.CENTER);
		Button btnext = new Button("Next");
		h1.getChildren().addAll(btnext);
		// to store statistics in location
		Label lbt = new Label("Total num of martyr");
		Label txtt = new Label();
		Label lbF = new Label("Total num Female martyr");
		Label txtf = new Label();
		Label lbM = new Label("Total num male martyr");
		Label txtm = new Label();
		Label lba = new Label("Average martyr Age");
		Label txta = new Label();
		Label lby = new Label("youngest martyr ");
		Label txty = new Label();
		Label lbO = new Label("oldest martyr ");
		Label txto = new Label();

		gp.add(lbln, 0, 0);
		gp.add(tfName, 1, 0);
		gp.add(h1, 1, 1);
		gp.add(lbt, 0, 2);
		gp.add(txtt, 1, 2);
		gp.add(lbF, 0, 3);
		gp.add(txtf, 1, 3);
		gp.add(lbM, 0, 4);
		gp.add(txtm, 1, 4);
		gp.add(lba, 0, 5);
		gp.add(txta, 1, 5);
		gp.add(lby, 0, 6);
		gp.add(txty, 1, 6);
		gp.add(lbO, 0, 7);
		gp.add(txto, 1, 7);

		Button btOP = new Button("update/delete martyr");// update and delete martyr form specific location
		Button btIn = new Button("insert New martyr");// insert martyr to specific location
		Button btSe = new Button("Search martyr (part of name)");// search martyr by part of name in specific location

		districtNode district = districtList.exist(districtName);
		locationList locations = district.AllLocations;// locations in district with name (districtName)

		if (locations.head != null) {// move next
			tfName.setText(locations.head.locationName);// set first location
			// all statistic to the first location in district with name (districtName)
			txtt.setText("" + martyrList.TotalMartyrInLocation(locations.head.locationName));
			txtf.setText("" + martyrList.numOfFemale(locations.head.locationName));
			txtm.setText("" + martyrList.numOfMale(locations.head.locationName));
			txta.setText("" + martyrList.avergeAge(locations.head.locationName));

			if (martyrList.youngestMartyr(locations.head.locationName) != null) {
				txty.setText("" + martyrList.youngestMartyr(locations.head.locationName).Name);
			} else {
				txty.setText("There's no martyr");
			}
			if (martyrList.oldestMartyr(locations.head.locationName) != null) {
				txto.setText("" + martyrList.oldestMartyr(locations.head.locationName).Name);
			} else {
				txto.setText("There's no martyr");
			}
			// update & delete martyr from first location
			btOP.setOnAction(e -> {
				martyrOpertaion(locations.head.locationName);
			});
			// insert new martyr to first location
			btIn.setOnAction(e -> {
				insertMartyr(locations.head.locationName, districtName);
			});
			// search martyr by part of name in first location
			btSe.setOnAction(e -> {
				sreachBypartOfName(locations.head.locationName);
			});

			// To move next
			btnext.setOnAction(e1 -> {
				locationNode current = locations.head;
				if (current.next != locations.head) {
					tfName.setText(current.next.locationName);
					locations.head = current.next; // Update the current reference
				}
				// update & delete martyr from current.next.locationName location
				btOP.setOnAction(e -> {
					martyrOpertaion(current.next.locationName);
				});
				// insert new martyr to current.next.locationName location
				btIn.setOnAction(e -> {
					insertMartyr(current.next.locationName, districtName);
				});
				// search martyr by part of name in current.next.locationName location
				btSe.setOnAction(e -> {
					sreachBypartOfName(current.next.locationName);

				});
				// all statistic in current.next.locationName location
				txtt.setText("" + martyrList.TotalMartyrInLocation(current.next.locationName));
				txtf.setText("" + martyrList.numOfFemale(current.next.locationName));
				txtm.setText("" + martyrList.numOfMale(current.next.locationName));
				txta.setText("" + martyrList.avergeAge(current.next.locationName));

				if (martyrList.youngestMartyr(current.next.locationName) != null) {
					txty.setText("" + martyrList.youngestMartyr(current.next.locationName).Name);
				} else {
					txty.setText("There's no martyr");
				}

				if (martyrList.oldestMartyr(current.next.locationName) != null) {
					txto.setText("" + martyrList.oldestMartyr(current.next.locationName).Name);
				} else {
					txto.setText("There's no martyr");
				}

			});

		} else {
			tfName.setText("There's no locations in:" + districtName);
			System.out.println("Location list is empty.");
		}

		VBox vCenter = new VBox(10);
		vCenter.setAlignment(Pos.CENTER);
		vCenter.getChildren().addAll(gp, btIn, btOP, btSe);
		root.setCenter(vCenter);

		Scene s3 = createStyledScene(root, 400, 450);
		thirdStage.setScene(s3);
		thirdStage.setTitle("Location Operation");
		thirdStage.setResizable(false);
		thirdStage.show();
	}

	private void insertLocation(String districtName) {// insert new location to exist district
		districtNode dis = districtList.exist(districtName);
		locationList locations = dis.AllLocations;
		locations.display();

		System.out.println();
		Stage stage4 = new Stage();
		VBox v = new VBox(20);
		v.setAlignment(Pos.CENTER);
		HBox h2 = new HBox(10);
		h2.setAlignment(Pos.CENTER);

		Label lblN = new Label("Location name");
		TextField tfin = new TextField();
		tfin.setPromptText("Location Name");

		h2.getChildren().addAll(lblN, tfin);

		Button btinsert = new Button("Insert");
		btinsert.setOnAction(event -> {
			String LocationName = tfin.getText();// location name
			if (!LocationName.isEmpty()) {
				districtNode district = districtList.exist(districtName);// district node (from district list)
				boolean locationExist = district.AllLocations.ifExistLocation(LocationName);// check if the
																							// location exist
				if (locationExist) {
					System.out.println("Location already exists");
				} else {
					district.AllLocations.insert(LocationName);// insert location in specific district
					locationList.insert(LocationName);// insert new location to location list
					System.out.println("Added succsessfully....");
				}
			} else {
				System.out.println("You should write the location name before clicking the button.");
			}
			// to clear
			tfin.setText("");

		});

		v.getChildren().addAll(h2, btinsert);
		Scene s4 = createStyledScene(v, 300, 200);
		stage4.setScene(s4);
		stage4.setTitle("Insert location");
		stage4.setResizable(false);
		stage4.show();
	}

	private void updateLocation(String districtName) {// update exist location
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
				districtNode dis = districtList.exist(districtName);
				locationList loc = dis.getAllLocations();// all locations in the district
				boolean locationexist = loc.ifExistLocation(oldLocationName);// check if location exists
				if (locationexist) {
					boolean newlocationExist = loc.ifExistLocation(newLocationName);// check if exist the new name
					if (!newlocationExist) {
						loc.updateLocation(oldLocationName, newLocationName);// update name
						locationList.updateLocation(oldLocationName, newLocationName);// update name in list
						// update name in martyr list
						martyrList.updateLocationName(oldLocationName, newLocationName);
						System.out.println("updated successfully..");
						rewriteFile(selectedFile, martyrList);// rewrite the updated martyr list to file
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

	private void deleteLocation(String districtName) {// delete exist location
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
				districtNode dis = districtList.exist(districtName);
				boolean exist = dis.AllLocations.ifExistLocation(locationName);// check if exist to delete
				if (exist) {
					martyrList.deleteLocation(locationName);// delete all martyr in this location
					locationList.deleteLocation(locationName);// delete location from list
					dis.AllLocations.deleteLocation(locationName);
					rewriteFile(selectedFile, martyrList);// rewrite updated martyr list to the file
					System.out.println("updated file");
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

	private void insertMartyr(String locationName, String districtName) {
		// insert new martyr to the location(location Name) & district(districtName)
		Stage stage4 = new Stage();
		BorderPane bp = new BorderPane();
		GridPane gp = new GridPane();
		StackPane sp = new StackPane();
		bp.setPadding(new Insets(20));
		gp.setHgap(10);
		gp.setVgap(10);
		gp.setAlignment(Pos.CENTER);
		// all info about martyr
		Label lbN = new Label("Name");
		TextField tfN = new TextField();
		Label lbE = new Label("Event");
		TextField tfE = new TextField();
		Label lbA = new Label("Age");
		TextField tfA = new TextField();
		Label lbG = new Label("Gender");
		TextField tfG = new TextField();
		gp.add(lbN, 0, 0);
		gp.add(tfN, 1, 0);
		gp.add(lbE, 0, 1);
		gp.add(tfE, 1, 1);
		gp.add(lbA, 0, 2);
		gp.add(tfA, 1, 2);
		gp.add(lbG, 0, 3);
		gp.add(tfG, 1, 3);
		bp.setCenter(gp);
		Button btin = new Button("Insert");
		sp.getChildren().add(btin);
		bp.setBottom(sp);

		btin.setOnAction(e -> {
			String name = tfN.getText();
			String event = tfE.getText();
			String age = tfA.getText();
			String gender = tfG.getText();
			try {
				if (!name.isEmpty() && !event.isEmpty() && !gender.isEmpty()) {
					martyrList.insert(name, event, age, locationName, districtName, gender);// insert in martyr list
					locationNode loc = locationList.exist(locationName);// location node (from location list)
					loc.Allmartyrs.insert(name, event, age, locationName, districtName, gender);// insert to the martyr
					// list
					// in the location
					rewriteFile(selectedFile, martyrList);// rewrite the martyr list update to the file

				} else {
					System.out.println("Mising information try again..........");
				}

			} catch (IllegalArgumentException ex) {
				System.out.println(ex);
			}
			// to clear
			tfN.setText("");
			tfE.setText("");
			tfA.setText("");
			tfG.setText("");
		});

		Scene s4 = createStyledScene(bp, 350, 300);
		stage4.setScene(s4);
		stage4.setResizable(false);
		stage4.setTitle("Insert New martyr");
		stage4.show();
	}

	private void martyrOpertaion(String locationName) {// updated and delete martyr from lists
		Stage stage4 = new Stage();
		HBox h = new HBox(20);
		h.setAlignment(Pos.CENTER);

		Button btup = new Button("Update");
		Button btde = new Button("Delete");
		btde.setOnAction(e -> {
			deletemartyr(locationName);
		});
		btup.setOnAction(e -> {
			updateMartyr(locationName);
		});
		h.getChildren().addAll(btup, btde);

		Scene s4 = createStyledScene(h, 300, 100);
		stage4.setScene(s4);
		stage4.setResizable(false);
		stage4.setTitle("Operation martyr");
		stage4.show();
	}

	private void deletemartyr(String locationName) {// to delete martyr by name
		Stage stage4 = new Stage();
		HBox h = new HBox(20);
		h.setAlignment(Pos.CENTER);
		StackPane sp = new StackPane();
		sp.setAlignment(Pos.CENTER);
		VBox v = new VBox(20);
		v.setAlignment(Pos.CENTER);

		Button btde = new Button("Delete");
		Label l = new Label("Martyr Name:");
		TextField tfn = new TextField();
		h.getChildren().addAll(l, tfn);
		sp.getChildren().add(btde);
		v.getChildren().addAll(h, sp);
		btde.setOnAction(e -> {
			String name = tfn.getText();
			if (!name.isEmpty()) {
				locationNode loc = locationList.exist(locationName);// to get the node from list
				martyrList martyInLoc = loc.Allmartyrs;
				boolean exist = martyInLoc.ifExistMartyr(name);// check if exist to delete
				if (exist) {
					martyInLoc.removeMartyr(name);
					martyrList.removeMartyr(name);// delete martyr from list
					rewriteFile(selectedFile, martyrList);// rewrite the date in file (updated martyr list)
					System.out.println("remove successfully...");
				} else {
					System.out.println("There's no martyr with name:" + name);
				}
			} else {
				System.out.println("you enter name of martyr to delete..");
			}
			tfn.setText("");// to clear
		});
		Scene s4 = createStyledScene(v, 300, 150);
		stage4.setScene(s4);
		stage4.setResizable(false);
		stage4.setTitle("Delete martyr");
		stage4.show();
	}

	private void updateMartyr(String locationName) {// update martyr using (name,age,gender,event)
		Stage stage4 = new Stage();
		Label label = new Label("Selected option:to update martyr");
		// ways to update
		RadioButton option1 = new RadioButton("Update name");
		RadioButton option2 = new RadioButton("Update age");
		RadioButton option3 = new RadioButton("Update date");
		RadioButton option4 = new RadioButton("Update gender");

		ToggleGroup group = new ToggleGroup();
		option1.setToggleGroup(group);
		option2.setToggleGroup(group);
		option3.setToggleGroup(group);
		option4.setToggleGroup(group);

		option1.setOnAction(event -> {// update name
			Stage stage = new Stage();
			GridPane gp = new GridPane();
			gp.setHgap(10);
			gp.setVgap(10);
			gp.setAlignment(Pos.CENTER);

			Button btn = new Button("update");
			Label lblName = new Label("Martyr Name:");
			TextField tfn = new TextField();
			Label lblNew = new Label("Martyr new Name:");
			TextField tfnew = new TextField();
			gp.add(lblName, 0, 0);
			gp.add(tfn, 1, 0);
			gp.add(lblNew, 0, 1);
			gp.add(tfnew, 1, 1);
			gp.add(btn, 1, 2);

			btn.setOnAction(e -> {
				String name = tfn.getText();// old name
				String newName = tfnew.getText();// new name
				if (!name.isEmpty() && !newName.isEmpty()) {
					locationNode loc = locationList.exist(locationName);// get node from list
					martyrList martyrInLoc = loc.Allmartyrs;// martyr list in loc
					boolean exist = martyrInLoc.ifExistMartyr(name);// check if exist to update
					if (exist) {
						martyrInLoc.setName(name, newName);
						martyrList.setName(name, newName);// update name
						rewriteFile(selectedFile, martyrList);// rewrite on file
						System.out.println("updated successfully...");
					} else {

						System.out.println("There's no martyr with name:" + name);
					}
				} else {
					System.out.println("information missing ...");
				}
				// to clear
				tfn.setText("");
				tfnew.setText("");
			});
			Scene s4 = createStyledScene(gp, 300, 200);
			stage.setScene(s4);
			stage.setResizable(false);
			stage.setTitle("Update by name");
			stage.show();
		});
		option2.setOnAction(e -> {// update age
			Stage stage = new Stage();
			GridPane gp = new GridPane();
			gp.setHgap(10);
			gp.setVgap(10);
			gp.setAlignment(Pos.CENTER);

			Button btn = new Button("update");
			Label lblName = new Label("Martyr Name:");
			TextField tfn = new TextField();
			Label lblNew = new Label("Martyr new Age:");
			TextField tfnew = new TextField();

			gp.add(lblName, 0, 0);
			gp.add(tfn, 1, 0);
			gp.add(lblNew, 0, 1);
			gp.add(tfnew, 1, 1);
			gp.add(btn, 1, 2);

			btn.setOnAction(e1 -> {
				try {
					String name = tfn.getText();// name
					String newAge = tfnew.getText();// new age
					if (!name.isEmpty() && !newAge.isEmpty()) {
						locationNode loc = locationList.exist(locationName);// get node from list
						martyrList martyrInLoc = loc.Allmartyrs;// martyr list in loc
						boolean exist = martyrInLoc.ifExistMartyr(name);// check if exist to update
						if (exist) {
							martyrInLoc.setAge(name, newAge);
							martyrList.setAge(name, newAge);// update age
							rewriteFile(selectedFile, martyrList);// rewrite on file
							System.out.println("updated successfully...");
						} else {
							System.out.println("There's no martyr with name:" + name);
						}
					} else {
						System.out.println("information missing ...");
					}
				} catch (IllegalArgumentException ex) {
					System.out.println(ex);
				}
				// to clear
				tfn.setText("");
				tfnew.setText("");
			});
			Scene s4 = createStyledScene(gp, 300, 200);
			stage.setScene(s4);
			stage.setResizable(false);
			stage.setTitle("Update by Age");
			stage.show();
		});
		option3.setOnAction(event -> {// update event
			Stage stage = new Stage();
			GridPane gp = new GridPane();
			gp.setHgap(10);
			gp.setVgap(10);
			gp.setAlignment(Pos.CENTER);

			Button btn = new Button("update");
			Label lblName = new Label("Martyr Name:");
			TextField tfn = new TextField();
			Label lblNew = new Label("Martyr new Event:");
			TextField tfnew = new TextField();
			gp.add(lblName, 0, 0);
			gp.add(tfn, 1, 0);
			gp.add(lblNew, 0, 1);
			gp.add(tfnew, 1, 1);
			gp.add(btn, 1, 2);

			btn.setOnAction(e1 -> {
				String name = tfn.getText();// name
				String newEvent = tfnew.getText();// new event
				if (!name.isEmpty() && !newEvent.isEmpty()) {
					locationNode loc = locationList.exist(locationName);
					martyrList martyrInLoc = loc.Allmartyrs;
					boolean exist = martyrInLoc.ifExistMartyr(name);// check if exist to update
					if (exist) {
						martyrInLoc.setEvent(name, newEvent);
						martyrList.setEvent(name, newEvent);// update event
						rewriteFile(selectedFile, martyrList);// rewrite on file
						System.out.println("updated successfully...");
					} else {
						System.out.println("There's no martyr with name:" + name);
					}
				} else {
					System.out.println("information missing ...");
				}
				tfn.setText("");// to clear
				tfnew.setText("");
			});
			Scene s4 = createStyledScene(gp, 300, 200);
			stage.setScene(s4);
			stage.setResizable(false);
			stage.setTitle("Update by event");
			stage.show();
		});
		option4.setOnAction(event -> {// update gender
			Stage stage = new Stage();
			GridPane gp = new GridPane();
			gp.setHgap(10);
			gp.setVgap(10);
			gp.setAlignment(Pos.CENTER);

			Button btn = new Button("update");
			Label lblName = new Label("Martyr Name:");
			TextField tfn = new TextField();
			Label lblNew = new Label("Martyr new Gender:");
			TextField tfnew = new TextField();
			gp.add(lblName, 0, 0);
			gp.add(tfn, 1, 0);
			gp.add(lblNew, 0, 1);
			gp.add(tfnew, 1, 1);
			gp.add(btn, 1, 2);

			btn.setOnAction(e1 -> {
				try {
					String name = tfn.getText();// name
					String newGender = tfnew.getText();// new gender
					if (!name.isEmpty() && !newGender.isEmpty()) {
						locationNode loc = locationList.exist(locationName);
						martyrList martyrInLoc = loc.Allmartyrs;
						boolean exist = martyrInLoc.ifExistMartyr(name);// check if exist to update
						if (exist) {
							martyrInLoc.setGender(name, newGender);
							martyrList.setGender(name, newGender);// update gender
							rewriteFile(selectedFile, martyrList);// rewrite on file
							System.out.println("updated successfully...");
						} else {
							System.out.println("There's no martyr with name:" + name);
						}

					} else {
						System.out.println("information missing ...");
					}
				} catch (IllegalArgumentException ex) {
					System.out.println(ex);
				}
				// to clear
				tfn.setText("");
				tfnew.setText("");
			});
			Scene s4 = createStyledScene(gp, 300, 200);
			stage.setScene(s4);
			stage.setResizable(false);
			stage.setTitle("Update by event");
			stage.show();
		});

		VBox vbox = new VBox(10);
		vbox.setPadding(new Insets(20));
		vbox.getChildren().addAll(label, option1, option2, option3, option4);

		Scene s4 = createStyledScene(vbox, 250, 250);
		stage4.setScene(s4);
		stage4.setResizable(false);
		stage4.setTitle("Update martyr(options)");
		stage4.show();
	}

	private void sreachBypartOfName(String locationName) {// search martyr by part of name in locationName
		Stage stage4 = new Stage();
		HBox h = new HBox(20);
		h.setAlignment(Pos.CENTER);
		StackPane sp = new StackPane();
		sp.setAlignment(Pos.CENTER);
		VBox v = new VBox(20);
		v.setAlignment(Pos.CENTER);

		Button bts = new Button("Search");
		Label l = new Label("Part of Martyr Name:");
		TextField tfn = new TextField();
		h.getChildren().addAll(l, tfn);
		Text t = new Text();
		sp.getChildren().add(bts);
		v.getChildren().addAll(h, sp, t);

		bts.setOnAction(e -> {
			String part = tfn.getText();// get part
			locationNode loc = locationList.exist(locationName);// get node from list
			martyrList martyrInLoc = loc.Allmartyrs;// martyr list in loc
			MartyrNode searhMarytr = martyrInLoc.searchMartyrByName(part, locationName);// to search return martyrNode
			if (searhMarytr != null) {
				t.setText("" + searhMarytr.Name);
				System.out.println("martyr found");
			} else {
				t.setText("martyr not found");
				System.out.println("martyr not found");
			}

		});
		// to clear
		tfn.setText("");
		Scene s4 = createStyledScene(v, 400, 200);
		stage4.setScene(s4);
		stage4.setResizable(false);
		stage4.setTitle("Search martyr(part of name)");
		stage4.show();

	}

	private void rewriteFile(File selectedFile, martyrList martyrs) {// rewrite the updated martyr list to the file
		try (FileWriter rewrite = new FileWriter(selectedFile)) {
			MartyrNode current = martyrList.head;
			if (martyrList.head != null) {// if list not empty
				while (current.next != martyrList.head) {
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

	private Scene createStyledScene(Parent root, double width, double height) {
		double enlargedWidth = Math.max(width * 1.35, 500);
		double enlargedHeight = Math.max(height * 1.35, 220);
		Scene scene = new Scene(root, enlargedWidth, enlargedHeight);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		return scene;
	}

	public static void main(String[] args) {
		launch(args);

	}

}
