package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import exceptions.InvalidNumberOfPlayersException;
import exceptions.NegativePriceException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import model.Animal;
import model.BoardGame;
import model.Figure;
import model.Puzzle;
import model.Toy;

public class ToyManagerInterface {
	/**
	 * This class handles all processes in the program.
	 */

	// Variables
	private final String FILE_PATH = "res/toys.txt"; // Location of database file
	ArrayList<Toy> toys;
//	ToyGUIManager toyGUIMg;

	@FXML
	private Button btnDisplayGift;

	@FXML
	private Button btnClearGift;

	@FXML
	private Button btnPurchaseGift;

	@FXML
	private Button btnRemove;

	@FXML
	private Button btnSave;

	@FXML
	private ComboBox<String> cbToyTypeDropDown;

	@FXML
	private Label lblRemoveMsg;

	@FXML
	private Label lblGiftMsg;

	@FXML
	private ListView<Toy> lvRemoveItem;

	@FXML
	private ListView<Toy> lvSuggest;

	@FXML
	private Tab tabHome;

	@FXML
	private TextField tfSerialNum;

	@FXML
	private TextField tfAge;

	@FXML
	private TextField tfMaxPrice;

	@FXML
	private TextField tfMinPrice;

	@FXML
	private MenuButton menuSelectType;
	
	@FXML
  	private TextField tfSerialNumber, tfName, tfBrand, tfPrice, tfAvailableCount, tfAgeAppropriate;
    
	@FXML
        private TextField tfDynamic1, tfDynamic2, tfDynamic3;
  
	@FXML
        private Label lblDynamic1, lblDynamic2, lblDynamic3, lblMessage;
  
	@FXML
        private Button btnAddToy;
	
	/**
	 * Constructor
	 * 
	 * @throws IOException
	 */
	public ToyManagerInterface() throws IOException {
		toys = new ArrayList<Toy>(); // Empty arrayList for the toys.
		loadToyData(); // Loads toy data into program.
//		toyGUIMg = new ToyGUIManager();
//		launchToyApp(); // Launches the program.
//		printAllToys(toys); // Printing to console for testing.
	}

	// <-----------------------PRELIMINARY METHODS---------------------------->
	/**
	 * Loads the information about the toys on startup.
	 * 
	 * @throws IOException
	 */
	public void loadToyData() {
		// Path to the file
		try {
			File file = new File(FILE_PATH);
			String toyline;
			String[] splittedToyLine;

			// Checks if the file exists and reads the file using FileReader and
			// BufferedReader.
			if (file.exists()) {
				FileReader fileReader = new FileReader(file);
				BufferedReader bufferedFile = new BufferedReader(fileReader);

				// Iterating through the file
				toyline = bufferedFile.readLine(); // Priming read
				while (toyline != null) { // Checks if the toy exist.
					splittedToyLine = toyline.split(";"); // Splits the line with semmi-colon.
					Toy toyType = convertToyType(splittedToyLine); // Converts the appropriate data types
					toys.add(toyType); // Adds each toy to the Toy arrayList.
					toyline = bufferedFile.readLine(); // Moves to next line.
				}
				bufferedFile.close(); // Closes the BufferedReader object.
			}
		} catch (IOException e) {
			System.out.println("File does not exit!");
		}
	}

	/**
	 * Converts each toy to one of the four Toy types, along with the appropriate
	 * data types.
	 * 
	 * @param splittedToyLine A String array with no delimiter.
	 * @return Each of the different Toy types or null if the toy does not exist.
	 */
	private Toy convertToyType(String[] splittedToyLine) {
		// Converting all the common attributes amongst all Toy types
		String serialNumber = splittedToyLine[0].trim();
		String name = splittedToyLine[1].trim();
		String brand = splittedToyLine[2].trim();
		double price = Double.parseDouble(splittedToyLine[3].trim());
		int availableCount = Integer.parseInt(splittedToyLine[4].trim());
		int ageAppropriate = Integer.parseInt(splittedToyLine[5].trim());

		// Classifying each Toy type using the first digit of the serial number
		char firstSerialDigit = serialNumber.charAt(0);

		// Implementing polymorphism to reference the different Toy types.
		// For Figures
		if (firstSerialDigit == '0' || firstSerialDigit == '1') {
			char classification = splittedToyLine[6].trim().toUpperCase().charAt(0);
			Toy figureToy = new Figure(serialNumber, name, brand, price, availableCount, ageAppropriate, classification);
			return figureToy;

			// For Animals
		} else if (firstSerialDigit == '2' || firstSerialDigit == '3') {
			String material = splittedToyLine[6].trim();
			char size = splittedToyLine[7].trim().toUpperCase().charAt(0);
			Toy animalToy = new Animal(serialNumber, name, brand, price, availableCount, ageAppropriate, material, size);
			return animalToy;

			// For Puzzles
		} else if (firstSerialDigit == '4' || firstSerialDigit == '5' || firstSerialDigit == '6') {
			char puzzleType = splittedToyLine[6].trim().toUpperCase().charAt(0);
			Toy puzzleToy = new Puzzle(serialNumber, name, brand, price, availableCount, ageAppropriate, puzzleType);
			return puzzleToy;

			// For BoardGames
		} else if (firstSerialDigit == '7' || firstSerialDigit == '8' || firstSerialDigit == '9') {
			String[] numOfPlayersRange = splittedToyLine[6].split("-");
			int minNumOfPlayers = Integer.parseInt(numOfPlayersRange[0].trim());
			int maxNumOfPlayers = Integer.parseInt(numOfPlayersRange[1].trim());
			String designers = splittedToyLine[7].trim();
			Toy boardGameToy = new BoardGame(serialNumber, name, brand, price, availableCount, ageAppropriate, minNumOfPlayers,
					maxNumOfPlayers, designers);
			return boardGameToy;
		} else {
			return null;
		}
	}

	/**
	 * Launches the toy application.
	 * 
	 * @throws IOException
	 */
	private void launchToyApp() throws IOException {
		boolean flag = true;
		int option;

		while (flag) {
			option = toyMenu.showMainMenu();

			switch (option) {
			case 1:
				searchToy(); // Searches for toy.
				break;
			case 2:
				// createToyFromInput(); // Adding new toys.
				break;
			case 3:
				removeToy(); // Removes a toy from the database.
				break;
			case 4:
				giftSuggestion(); // Provides a suggested list of toys.
				break;
			case 5:
				saveExit(); // Saves and exits the program.
				flag = false;
				break;
			}
		}
	}

	// <----------------------MENU METHODS------------------------->
	// All methods that corresponds to the menu options goes here.

	// Method to search and purchase toys
	public void searchToy() {
		// Prompt user for search term
		String searchQuery = toyMenu.askSearchQuery();

		// Temporary List to hold search results
		ArrayList<Toy> searchResults = new ArrayList<>();

		// Search through toys List
		for (Toy toy : toys) {
			if (toy.getName().equalsIgnoreCase(searchQuery) ||
					toy.getName().toLowerCase().contains(searchQuery.toLowerCase()) ||
					toy.getBrand().toLowerCase().contains(searchQuery.toLowerCase()) ||
					searchResults.add(toy))
				;
		}
	}

	/**
	 * Allows user to remove a toy from the database, based on the serial number.
	 */
	public void removeToy() {
		// Receives serial number.
		String toySerialStr = tfSerialNum.getText();

		// Looping through the toys list
		Toy removeThisToy = null;
		for (Toy toy : toys) {
			if (toy.getSerialNumber().equals(toySerialStr)) {
				removeThisToy = toy;
				break;
			}
		}

		// Displaying information and removing toy.
		if (removeThisToy != null) {
			lvRemoveItem.getItems().clear();
			lvRemoveItem.getItems().addAll(removeThisToy);
			toys.remove(removeThisToy);
			lblRemoveMsg.setText("The toy has been removed successfully!");
		} else {
			lvRemoveItem.getItems().clear();
			lblRemoveMsg.setText("The toy you are trying to remove does not exist.");
		}
	}

	@FXML
	void btnRemoveHandler(ActionEvent event) {
		removeToy();
	}

	// public Toy createToyFromInput() {
	// System.out.print("Enter Serial Number: ");
	// String serialNumber = toyMenu.askSerialNum();
	// System.out.print("Enter Toy Name: ");
	// String name = input.nextLine().trim();
	// while (name.isEmpty()) {
	// System.out.println("Please enter a valid name.");
	// System.out.print("Enter Toy Name: ");
	// name = input.nextLine().trim();
	// }
	// System.out.print("Enter Brand: ");
	// String brand = input.nextLine().trim();
	// while (brand.isEmpty()) {
	// System.out.println("Please enter a valid brand.");
	// System.out.print("Enter Brand: ");
	// brand = input.nextLine().trim();
	// }
	// double price = -1;

	// while (price <= 0) {
	// System.out.print("Enter Price: ");
	// if (input.hasNextDouble()) {
	// price = input.nextDouble();
	// if (price <= 0) {
	// System.out.println("Price must be positive. Enter a valid price.");
	// }
	// } else {
	// System.out.println("Enter a numeric value for price.");
	// input.next(); // Consume invalid input
	// }
	// }
	// int availableCount = -1;
	// while (availableCount < 0) {
	// System.out.print("Enter Available Count: ");
	// if (input.hasNextInt()) {
	// availableCount = input.nextInt();
	// if (availableCount < 0) {
	// System.out.println("Please enter a valid count.");
	// }
	// } else {
	// System.out.println("Invalid input.");
	// input.next(); // Consume invalid input
	// }
	// }
	// input.nextLine(); // Consume newline after number input
	// System.out.print("Enter Minimum Age: ");
	// int ageAppropriate = input.nextInt();
	// input.nextLine(); // Consume newline
	// System.out.print("Enter Minimum Players: ");
	// int minimumPlayers = input.nextInt();
	// input.nextLine(); // Consume newline
	// System.out.print("Enter Maximum Players: ");
	// int maximumPlayers = input.nextInt();
	// input.nextLine(); // Consume newline
	// System.out.print("Enter Designer Names: ");
	// String designerNames = input.nextLine().trim();
	// // Now you can create a Toy object using the collected information
	// Toy toy = new Toy(serialNumber, name, brand, price, availableCount,
	// ageAppropriate, minimumPlayers, maximumPlayers, designerNames);
	// return toy;
	// }

	@FXML
	/**
	 * Retrieves toy type from the drop down list.
	 * 
	 * @param event when user selects a toy type.
	 */
	void menuSelectTypeHandler(ActionEvent event) {
		MenuItem selectedType = (MenuItem) event.getSource();
		menuSelectType.setText(selectedType.getText());
	}

	@FXML
	/**
	 * Displays list of suggest gift toys when user clicks the display button.
	 * 
	 * @param event when the user clicks the display button.
	 */
	void btnDisplayGiftHandler(ActionEvent event) {
		giftSuggestion();
	}

	@FXML
	void btnClearGiftHandler(ActionEvent event) {
		lvSuggest.getItems().clear();
		lblGiftMsg.setText("");
	}

	@FXML
	/**
	 * Allows the user to purchase a gift from the suggested list.
	 * 
	 * @param event when user clicks the purchase button.
	 */
	void btnPurchaseGiftHandler(ActionEvent event) {
		int newCount;
		Toy giftToy = lvSuggest.getSelectionModel().getSelectedItem();
		if (toys.contains(giftToy)) { // Checks if toy exist in the toys arrayList.
			if (giftToy.getAvailableCount() > 0) { // Checks if toy is available.
				newCount = giftToy.getAvailableCount() - 1; // Subtract 1 from the available count for that toy.
				giftToy.setAvailableCount(newCount); // Sets a new available count for the toy.
				lvSuggest.getItems().clear();
				lvSuggest.getItems().add(giftToy);
			} else {

			}
		}
	}

	/**
	 * Provides a list of gift suggestions based on the details provided by user.
	 * 
	 * @throws IOException
	 */
	public void giftSuggestion() {
		// Request details from the user.
		String strAge = tfAge.getText().trim();
		String type = menuSelectType.getText().trim().toLowerCase();
		String strMinPrice = tfMinPrice.getText();
		String strMaxPrice = tfMaxPrice.getText();

		// Empty arrayList to hold all suggested toys.
		ArrayList<Toy> giftList = new ArrayList<Toy>();

		if ((strAge != null || strAge == null) && (type != null || type == null)
				&& (strMinPrice != null || strMinPrice == null || strMaxPrice != null || strMaxPrice == null)) {

			// Converts string age to integer age.
			int age = 0;
			if (!strAge.isEmpty()) {
				age = Integer.parseInt(strAge);
			}

			// Converts the price string array into minimum and maximum prices
			double minPrice = 0;
			double maxPrice = 0;
			if (!strMinPrice.isEmpty() && !strMaxPrice.isEmpty()) {
				minPrice = Double.parseDouble(strMinPrice.trim());
				maxPrice = Double.parseDouble(strMaxPrice.trim());
			}

			// Filtering toys based on the given details.
			for (Toy toy : toys) {
				// Searching with age
				boolean ageFilter = false; // Variable used for filtering age.
				if (toy.getAgeAppropriate() == age) {
					ageFilter = true;
				}

				// Filtering with toy type through polymorphism.
				boolean typeFilter = false; // Variable used for filtering the toy type.
				if (!type.isEmpty()) {
					switch (type) {
					case "figure":
						if (toy instanceof Figure)
							typeFilter = true;
						break;
					case "animal":
						if (toy instanceof Animal)
							typeFilter = true;
						break;
					case "puzzle":
						if (toy instanceof Puzzle)
							typeFilter = true;
						break;
					case "board game":
						if (toy instanceof BoardGame)
							typeFilter = true;
						break;
					case "none":
						if (!strAge.isEmpty() || !strMinPrice.isEmpty() || !strMaxPrice.isEmpty()) {
							typeFilter = true;
						}
						break;
					default:
						typeFilter = false;
					}
				}

				// Filtering with price range
				boolean priceFilter = false;
				if (minPrice >= 0 && maxPrice >= 0) {
					if (toy.getPrice() >= minPrice && toy.getPrice() <= maxPrice) {
						priceFilter = true;
					}
				}

				// Adding toy to the suggested list based on the user details
				if ((strAge.isEmpty() || ageFilter) && (typeFilter)
						&& (strMinPrice.isEmpty() || strMaxPrice.isEmpty() || priceFilter)) {
					giftList.add(toy);
				}
			}

			// Displaying suggested toy list.
			if (giftList.size() > 0) {
				lvSuggest.getItems().clear();
				lvSuggest.getItems().addAll(giftList); // Prints all the suggested toys.
				lblGiftMsg.setText("Select a toy");
			} else {
				lvSuggest.getItems().clear();
				lblGiftMsg.setText("No details were provided"); // No provided details message.
			}
		}
	}

	@FXML
	/*
	 * Saves to the database when the user clicks the save button.
	 */
	void btnSaveHandler(ActionEvent event) {
		saveExit();
	}

	/**
	 * Saves and prints to file
	 */
	private void saveExit() {
		// File handling
		try {
			File toyFile = new File(FILE_PATH);
			PrintWriter pw = new PrintWriter(toyFile);

			// Looping through the toys list and writing to file.
			for (Toy t : toys) {
				pw.println(t.format());
			}
			pw.close(); // Closes the PrintWriter object.
		} catch (IOException e) {
			System.out.println("File does not exist!");
		}
	}

	// <--------------------------EXCEPTION METHODS---------------------------->
	// All exception methods used for validation can go here.
	/**
	 * 
	 * @param price the price of the toy.
	 * @return true for positive price
	 * @throws NegativePriceException shows an error message about the negative
	 *                                price.
	 */
	public static boolean validateToyPrice(double price) throws NegativePriceException {

		// Checks if the price is negative. An exception will be thrown if true.
		if (price < 0) {
			throw new NegativePriceException("The price cannot be negative! Only positive values are permitted.");
		}

		return true; // Returns true if price is positive and bypasses the exception.
	}

	/**
	 * 
	 * @param minPlys the minimum # of players to play the game.
	 * @param maxPlys the maximum # of players to play the game.
	 * @return true if no exception is thrown.
	 * @throws InvalidNumberOfPlayersException shows an error message about the
	 *                                         number of players.
	 */
	public static boolean validateBoardgamePlayers(int minPlys, int maxPlys) throws InvalidNumberOfPlayersException {

		// Compares both number of players and throws an exception if true.
		if (minPlys > maxPlys) {
			throw new InvalidNumberOfPlayersException("The minimum number of players cannot exceed the maximum number of players!");
		}

		return true; // Returns true if the above condition is false or no exception is thrown.
	}

	// <------------------MISCELLANEOUS METHODS------------------>
	// All non-specific or extra methods can go here.

	/**
	 * Printing to console for testing.
	 * 
	 * @param toys an arrayList of all the toys.
	 */
	public void printAllToys(ArrayList<Toy> toys) {
		for (Toy toy : toys) {
			System.out.println(toy.toString());
		}
	}

}
