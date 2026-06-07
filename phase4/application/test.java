package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class test extends Application {

	private ObservableList<Product> products = FXCollections.observableArrayList();

	@Override
	public void start(Stage primaryStage) throws Exception {

		// Create the tabs
		TabPane tabPane = new TabPane();
		Tab productsTab = new Tab("Product Tab");
		Tab newTab = new Tab("New Tab");
		Tab helpTab = new Tab("Help");

		// Create the table
		TableView<Product> productTable = new TableView<>();
		TableColumn<Product, String> productNameColumn = new TableColumn<>("Product Name");
		TableColumn<Product, Double> productPriceColumn = new TableColumn<>("Product Price");
		TableColumn<Product, Integer> productQuantityColumn = new TableColumn<>("Quantity");

		// Set the cell value factories
		productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
		productQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

		// Add the columns to the table
		productTable.getColumns().addAll(productNameColumn, productPriceColumn, productQuantityColumn);

		// Add the table to the products tab
		productsTab.setContent(productTable);

		// Create the add product form
		TextField productNameField = new TextField();
		TextField productPriceField = new TextField();
		TextField productQuantityField = new TextField();

		Button addProductButton = new Button("Add Product");
		addProductButton.setOnAction(event -> {
			// Get the values from the fields
			String name = productNameField.getText();
			double price = Double.parseDouble(productPriceField.getText());
			int quantity = Integer.parseInt(productQuantityField.getText());

			// Create a new product object
			Product newProduct = new Product(name, price, quantity);

			// Add the product to the table
			products.add(newProduct);

			// Clear the fields
			productNameField.clear();
			productPriceField.clear();
			productQuantityField.clear();
		});

		// Create the layout for the form
		HBox addProductForm = new HBox(10, productNameField, productPriceField, productQuantityField, addProductButton);
		addProductForm.setAlignment(Pos.CENTER);
		addProductForm.setPadding(new Insets(10));

		// Add the form to the new tab
		newTab.setContent(addProductForm);

		// Create the layout for the tabs
		VBox root = new VBox(10, tabPane);
		root.setAlignment(Pos.CENTER);
		root.setPadding(new Insets(10));

		// Add the tabs to the tab pane
		tabPane.getTabs().addAll(productsTab, newTab, helpTab);

		// Create the scene
		Scene scene = new Scene(root, 600, 400);

		// Set the stage
		primaryStage.setTitle("Inventory Management System");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	// Product class
	public static class Product {
		private String name;
		private double price;
		private int quantity;

		public Product(String name, double price, int quantity) {
			this.name = name;
			this.price = price;
			this.quantity = quantity;
		}

		public String getName() {
			return name;
		}

		public double getPrice() {
			return price;
		}

		public int getQuantity() {
			return quantity;
		}
	}
}