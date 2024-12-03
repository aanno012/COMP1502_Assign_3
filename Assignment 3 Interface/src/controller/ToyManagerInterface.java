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
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

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

	public Toy createToyFromInput() {
	    Scanner scanner = new Scanner(System.in);
	    Toy newToy = null;

	    try {
	        // Collecting common toy attributes
	        System.out.println("Enter Serial Number:");
	        String serialNumber = scanner.nextLine();

	        System.out.println("Enter Toy Name:");
	        String name = scanner.nextLine();

	        System.out.println("Enter Toy Brand:");
	        String brand = scanner.nextLine();

	        System.out.println("Enter Toy Price:");
	        double price = Double.parseDouble(scanner.nextLine());

	        System.out.println("Enter Available Count:");
	        int availableCount = Integer.parseInt(scanner.nextLine());

	        System.out.println("Enter Appropriate Age:");
	        int ageAppropriate = Integer.parseInt(scanner.nextLine());

	        char firstSerialDigit = serialNumber.charAt(0);

	        // Determine toy type based on the serial number's first digit
	        switch (firstSerialDigit) {
	            case '0':
	            case '1': // Figure
	                System.out.println("Enter Classification (A for Action, D for Doll, H for Historic):");
	                char classification = scanner.nextLine().toUpperCase().charAt(0);
	                newToy = new Figure(serialNumber, name, brand, price, availableCount, ageAppropriate, classification);
	                break;

	            case '2':
	            case '3': // Animal
	                System.out.println("Enter Material (Plush, Wood, Plastic):");
	                String material = scanner.nextLine();
	                System.out.println("Enter Size (Small, Medium, Large):");
	                char size = scanner.nextLine();
	                newToy = new Animal(serialNumber, name, brand, price, availableCount, ageAppropriate, material, size);
	                break;

	            case '4':
	            case '5':
	            case '6': // Puzzle
	            	System.out.println("Enter Puzzle Type (3D, Jigsaw, Crossword):");
	               String puzzleType = scanner.nextLine();
	                System.out.println("Enter Number of Pieces:");
	                int numberOfPieces = Integer.parseInt(scanner.nextLine());
	                newToy = new Puzzle(serialNumber, name, brand, price, availableCount, ageAppropriate, numberOfPieces, puzzleType);
	                break;

	            case '7':
	            case '8':
	            case '9': // Board Game
	                System.out.println("Enter Minimum Number of Players:");
	                int minPlayers = Integer.parseInt(scanner.nextLine());
	                System.out.println("Enter Maximum Number of Players:");
	                int maxPlayers = Integer.parseInt(scanner.nextLine());
	                System.out.println("Enter Designers (comma-separated):");
	                String designers = scanner.nextLine();
	                newToy = new BoardGame(serialNumber, name, brand, price, availableCount, ageAppropriate, minPlayers, maxPlayers, designers);
	                break;

	            default:
	                return null;
	        }

	        // Validate and add to the toy list
	        if (newToy != null) {
	            newToy.validate(); // Ensure toy data meets requirements
	            toys.add(newToy);
	            System.out.println("Toy added successfully!");
	        }

	    } catch (NumberFormatException e) {
	        System.out.println("Invalid input! Please enter the correct data type.");
	    } catch (ToyValidationException e) {
	        System.out.println("Validation Error: " + e.getMessage());
	    } catch (Exception e) {
	        System.out.println("An unexpected error occurred: " + e.getMessage());
	    }

	    return newToy;
	}

	
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
	private ComboBox<String> cbCategory;

	@FXML
	private GridPane figurePane, animalPane, puzzlePane, boardGamePane;
	
	@FXML
	private ComboBox<String> cbCategory;

	@FXML
	private GridPane figurePane, animalPane, puzzlePane, boardGamePane;
	
	void btnAddToyHandler(ActionEvent event) {
	    // Retrieve the basic toy details
	    String toyName = tfToyName.getText();
	    String selectedCategory = cbCategory.getValue();
	    double toyPrice;
	    int toyCount;

	    try {
	        toyPrice = Double.parseDouble(tfToyPrice.getText());
	        toyCount = Integer.parseInt(tfToyCount.getText());
	    } catch (NumberFormatException e) {
	        lblGiftMsg.setText("Invalid price or count. Please enter valid numbers.");
	        return;
	    }

	    if (toyName == null || toyName.trim().isEmpty()) {
	        lblGiftMsg.setText("Please enter a valid toy name.");
	        return;
	    }

	    // Declare the Toy object to hold the created toy
	    Toy newToy = null;

	    // Handle category-specific details
	    switch (selectedCategory) {
	        case "Figure":
	            String classification = tfClassification.getText();
	            newToy = new Figure(toyName, toyPrice, toyCount, classification);
	            break;

	        case "Animal":
	            String material = tfMaterial.getText();
	            String size = tfSize.getText();
	            newToy = new Animal(toyName, toyPrice, toyCount, material, size);
	            break;

	        case "Puzzle":
	            String puzzleType = tfPuzzleType.getText();
	            newToy = new Puzzle(toyName, toyPrice, toyCount, puzzleType);
	            break;

	        case "Board Game":
	            int minPlayers, maxPlayers;
	            String designers = tfDesigners.getText();
	            try {
	                minPlayers = Integer.parseInt(tfMinPlayers.getText());
	                maxPlayers = Integer.parseInt(tfMaxPlayers.getText());
	            } catch (NumberFormatException e) {
	                lblGiftMsg.setText("Invalid player count. Please enter valid numbers.");
	                return;
	            }
	            newToy = new BoardGame(toyName, toyPrice, toyCount, minPlayers, maxPlayers, designers);
	            break;

	        default:
	            lblGiftMsg.setText("Please select a valid category.");
	            return;
	    }

	    // Add the toy to the list if it doesn't already exist
	    if (newToy != null && !toys.contains(newToy)) {
	        toys.add(newToy);
	        lvToys.getItems().add(newToy);
	        lblGiftMsg.setText("Toy added successfully!");
	    } else {
	        lblGiftMsg.setText("Toy already exists in the inventory.");
	    }

	    // Clear input fields after adding
	    tfToyName.clear();
	    tfToyPrice.clear();
	    tfToyCount.clear();
	    tfClassification.clear();
	    tfMaterial.clear();
	    tfSize.clear();
	    tfPuzzleType.clear();
	    tfMinPlayers.clear();
	    tfMaxPlayers.clear();
	    tfDesigners.clear();
	    
	    cbCategory.valueProperty().addListener((obs, oldVal, newVal) -> {
	    // Hide all panes by default
	    figurePane.setVisible(false);
	    animalPane.setVisible(false);
	    puzzlePane.setVisible(false);
	    boardGamePane.setVisible(false);

	    // Show the appropriate pane based on the selected category
	    switch (newVal) {
	        case "Figure":
	            figurePane.setVisible(true);
	            break;
	        case "Animal":
	            animalPane.setVisible(true);
	            break;
	        case "Puzzle":
	            puzzlePane.setVisible(true);
	            break;
	        case "Board Game":
	            boardGamePane.setVisible(true);
	            break;
	        default:
	            // No need to do anything as all panes are hidden by default
	            break;
	    }
	    }
	};
	    
	//Saving the toy 
	private void saveToy() {
	    String category = cbCategory.getValue();
	    if (category == null) {
	        System.out.println("Please select a category.");
	        return;
	    }

	    switch (category) {
	        case "Figure":
	            String classification = tfClassification.getText();
	            break;
	        case "Animal":
	            String material = tfMaterial.getText();
	            String size = tfSize.getText();
	            break;
	        case "Puzzle":
	            String type = tfPuzzleType.getText();
	            break;
	        case "Board Game":
	            int minPlayers = Integer.parseInt(tfMinPlayers.getText());
	            int maxPlayers = Integer.parseInt(tfMaxPlayers.getText());
	            String designers = tfDesigners.getText();
	            break;
	    }
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
	
	public static boolean ToyValidationException (String category) throws handleCategoryException() {
	    String category = cbCategory.getValue(); // Get selected category
	    if (category == null || category.isEmpty()) {
	        System.out.println("Please select a category.");
	        return;
	    }
	    System.out.println("Selected Category: " + category);
	}
	
	public Toy createToyFromInput1() throws ToyValidationException {
	    Scanner scanner = new Scanner(System.in);
	    Toy newToy = null;

	    try {
	        // Collecting common toy attributes
	        if (serialNumber.isEmpty()) {
	            throw new ToyValidationException("Serial number cannot be empty.");
	        }
	        
	        if (name.isEmpty()) {
	            throw new ToyValidationException("Toy name cannot be empty.");
	        }

	        if (brand.isEmpty()) {
	            throw new ToyValidationException("Toy brand cannot be empty.");
	        }

	       
	        if (price <= 0) {
	            throw new ToyValidationException("Price must be greater than zero.");
	        }

	       
	        if (availableCount < 0) {
	            throw new ToyValidationException("Available count cannot be negative.");
	        }

	       
	        if (ageAppropriate < 0 || ageAppropriate > 18) {
	            throw new ToyValidationException("Appropriate age must be between 0 and 18.");
	        }

	        char firstSerialDigit = serialNumber.charAt(0);

	        // Category-specific attributes
	        switch (firstSerialDigit) {
	            case '0':
	            case '1': // Figure
	                System.out.println("Enter Classification (A for Action, D for Doll, H for Historic):");
	                char classification = scanner.nextLine().toUpperCase().charAt(0);
	                if (classification != 'A' && classification != 'D' && classification != 'H') {
	                    throw new ToyValidationException("Invalid classification for a figure. Must be 'A', 'D', or 'H'.");
	                }
	                newToy = new Figure(serialNumber, name, brand, price, availableCount, ageAppropriate, classification);
	                break;

	            case '2':
	            case '3': // Animal
	                System.out.println("Enter Material (Plush, Wood, Plastic):");
	                String material = scanner.nextLine();
	                if (!material.equalsIgnoreCase("Plush") && !material.equalsIgnoreCase("Wood") && !material.equalsIgnoreCase("Plastic")) {
	                    throw new ToyValidationException("Invalid material for an animal toy. Must be 'Plush', 'Wood', or 'Plastic'.");
	                }
	                System.out.println("Enter Size (Small, Medium, Large):");
	                String size = scanner.nextLine();
	                if (!size.equalsIgnoreCase("Small") && !size.equalsIgnoreCase("Medium") && !size.equalsIgnoreCase("Large")) {
	                    throw new ToyValidationException("Invalid size for an animal toy. Must be 'Small', 'Medium', or 'Large'.");
	                }
	                newToy = new Animal(serialNumber, name, brand, price, availableCount, ageAppropriate, material, size);
	                break;

	            case '4':
	            case '5':
	            case '6': // Puzzle
	                System.out.println("Enter Puzzle Type (3D, Jigsaw, Crossword):");
	                String puzzleType = scanner.nextLine();
	                if (!puzzleType.equalsIgnoreCase("3D") && !puzzleType.equalsIgnoreCase("Jigsaw") && !puzzleType.equalsIgnoreCase("Crossword")) {
	                    throw new ToyValidationException("Invalid puzzle type. Must be '3D', 'Jigsaw', or 'Crossword'.");
	                }
	                System.out.println("Enter Number of Pieces:");
	                int numberOfPieces = Integer.parseInt(scanner.nextLine());
	                if (numberOfPieces <= 0) {
	                    throw new ToyValidationException("Number of pieces must be greater than zero.");
	                }
	                newToy = new Puzzle(serialNumber, name, brand, price, availableCount, ageAppropriate, numberOfPieces, puzzleType);
	                break;

	            case '7':
	            case '8':
	            case '9': // Board Game
	                System.out.println("Enter Minimum Number of Players:");
	                int minPlayers = Integer.parseInt(scanner.nextLine());
	                System.out.println("Enter Maximum Number of Players:");
	                int maxPlayers = Integer.parseInt(scanner.nextLine());
	                if (minPlayers <= 0 || maxPlayers <= 0 || minPlayers > maxPlayers) {
	                    throw new ToyValidationException("Invalid number of players. Minimum and maximum players must be greater than zero, and minPlayers <= maxPlayers.");
	                }
	                System.out.println("Enter Designers (comma-separated):");
	                String designers = scanner.nextLine();
	                if (designers.isEmpty()) {
	                    throw new ToyValidationException("Designers cannot be empty for a board game.");
	                }
	                newToy = new BoardGame(serialNumber, name, brand, price, availableCount, ageAppropriate, minPlayers, maxPlayers, designers);
	                break;

	            default:
	                throw new ToyValidationException("Invalid serial number. Unable to determine toy category.");
	        }

	    } catch (NumberFormatException e) {
	        throw new ToyValidationException("Invalid input format. Please enter numbers where required.");
	    }

	    return newToy;
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
