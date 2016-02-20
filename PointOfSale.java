/*
*	Brandon Saunders
*	2/19/2016
*	Liferay Coding Problem: Sales Taxes
*
*	Basic Sales Tax 10%: All Goods - ( Books, Food, and Medical Products )
*	Import Duty 5%: All Goods
*
*	Input: Items from the shopping basket. Including their price, name, tax, and total cost of items. 
*	Output: Receipt with a list of all items and prices (including tax).
*
*	Areas To Improve: Convert to use an SQL database, add GUI for easier use, and add expanded
*	functionality for further additions of items. 
*/
// Package Members
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Math;

// Class to represent and apply tax values upon item.
class PointOfSale {
	public PointOfSale() {
	}
	// Main
	public static void main(String args[]) {
		Inventory stock = new Inventory();
		stock.selectAndPrintInventory();
	}
}

// Class to represent each item.
class Item {
	private String product_name;
	private double product_price;
	private double product_sales_tax_rate;
	private double product_import_rate;

	// Default Constructor in the event variables are not initialized.
	public Item() {
		this.product_name = null;
		this.product_price = -1;
		this.product_sales_tax_rate = -1;
		this.product_import_rate = -1;
	}

	// Constructor to set variable members when describing out Item object.
	public Item(String item_name, double item_price, double item_sales_tax_rate, double item_import_tax_rate) {
		this.product_name = item_name;
		this.product_price = item_price;
		this.product_sales_tax_rate = item_sales_tax_rate;
		this.product_import_rate = item_import_tax_rate;
	}
	// Setters
	public void setItemName(String new_name) {
		this.product_name = new_name;
	}
	public void setItemPrice(double new_price) {
		this.product_price = new_price;
	}
	public void setItemSalesTaxRate(double new_tax_rate) {
		this.product_sales_tax_rate = new_tax_rate;
	}
	public void setItemImportTaxRate(double new_tax_rate) {
		this.product_import_rate = new_tax_rate;
	}
	// Getters
	public String getItemName() {
		return product_name;
	}
	public double getItemPrice() {
		return product_price;
	}
	public double getItemSalesTaxRate() {
		return product_sales_tax_rate;
	}	
	public double getItemImportTaxRate() {
		return product_import_rate;
	}
}

// Class to represent the shopping cart.
class ShoppingCart {
	// Stores the Item objects we'll have in our virtual cart.
	private ArrayList<Item> shopping_cart = new ArrayList<>();

	public ShoppingCart() {
		// Empty default constructor.
	}

	// Add an item to the virtual shopping cart (array list).
	public void addItem(Item product){
		shopping_cart.add(product);
	}

	// Getter to obtain item from the current shopping cart.
	public void getItem(int index){
		try {
			shopping_cart.get(index);
		}
		catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	// Allows for user input in the console. TO-DO: Change to GUI user input.
	public int getUserInput(){
		int input;
		System.out.println("Please select your item: ");
		Scanner reader = new Scanner(System.in);  // Reading from System.in
		input = reader.nextInt();
		return input;
	}

	// Displays the shopping cart to the user after the items have been selected.
	public void printShoppingCart() {
		double total_tax = 0.0;
		double running_total = 0.0;
		double imported_item_price = 0.0;
		double current_tax = 0.0;
		double current_item_price = 0.0;
		try {
			// Displays the contents of the shopping cart.
			System.out.println("\nYour shopping cart contains: ");
			for ( int i = 0; i < shopping_cart.size(); i++) {
				// Store the current item's tax rate and item price into local variables.
				current_tax = applyTaxRate(shopping_cart.get(i));
				current_item_price = shopping_cart.get(i).getItemPrice();

				// Determine the overall cost of the imported good. Round up to the 0.01 place.
				imported_item_price = (current_item_price + current_tax);
				imported_item_price = roundToOneCents(imported_item_price);

				// Display the item's name and price in the proper output format.
				System.out.println("1 " + shopping_cart.get(i).getItemName() 
								 + " $" + imported_item_price);

				// Round the current tax to the 0.05 place, sum the total tax and total price.
				current_tax = roundToFiveCents(current_tax);
				total_tax += current_tax;
				running_total += (current_item_price + current_tax);
			}

			// Round to the 0.01 place for the total and tax.
			running_total = roundToOneCents(running_total);
			total_tax = roundToOneCents(total_tax);

			// Print the tax total and sales total at the end of the receipt.
			System.out.println("Sales Taxes: $" + total_tax);
			System.out.println("Total: $" + running_total);
		}
		// Catch any ".get(i)" that could be out of bounds.
		catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	// Apply the tax rate. If there is no sales tax or import duty tax it won't matter because we're
	// utilizing addition. 
	private double applyTaxRate(Item product) {
		double DENOMINATOR = 100.0;
		double total_tax;
		total_tax = (((product.getItemSalesTaxRate() + product.getItemImportTaxRate()) * product.getItemPrice()) / DENOMINATOR);
		return total_tax;
	}

	// Round to the 0.05 place because 0.05 is the same as 1/20. 
	private double roundToFiveCents(double input) {
		double DENOMINATOR = 20.0;
		double value = input;
		try {
			value = value * DENOMINATOR;
			value = Math.round(value);
			value = value / DENOMINATOR;
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return value;
	}

	// Round to the 0.01 place because 0.01 is the same as 1/100.
	private double roundToOneCents(double input) {
		double DENOMINATOR = 100.0;
		double value = input;
		try {
			value = value * DENOMINATOR;
			value = Math.round(value);
			value = value / DENOMINATOR;
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return value;
	}
}

// Inventory class to add all current store stock to the inventory list.
class Inventory {
	// TO-DO: Convert entries to a database so nothing is hard-coded.
	private double SALES_TAX = 10.00;
	private double IMPORT_DUTY = 05.00;
	private double EXEMPT = 0.0;
	private double BOOK_PRICE = 12.49;
	private double MUSIC_CD_PRICE = 14.99;
	private double DOMESTIC_CHOCOLATE_BAR_PRICE = 0.85;
	private double IMPORTED_CHOCOLATE_BAR_PRICE = 10.00;
	private double IMPORTED_CHOCOLATE_BAR_PRICE_ALT = 11.25;
	private double IMPORTED_PERFUME = 47.50;
	private double IMPORTED_PERFUME_ALT = 27.99;
	private double DOMESTIC_PERFUME = 18.99;
	private double HEADACHE_PILLS = 9.75;

	// Our virtual inventory array list.
	private ArrayList<Item> inventory = new ArrayList<>();

	// Adding items to the inventory.
	public Inventory() {
		addItemsToInventory();
	}
	// Display all items in the inventory currently and 
	// prompt the user for which items to purchase.
	protected void selectAndPrintInventory(){
		int selection_index;
		int selection = -1;
		int inventory_size = inventory.size();

		try {
			System.out.println("--------------------------");
			System.out.println("Please select your items or enter 9 to check out: ");
			for ( int i = 0; i < inventory.size(); i++) {
				System.out.println( " [" + i + "] 1 "
								 + inventory.get(i).getItemName()
								 + " at " + inventory.get(i).getItemPrice());
			}
		}
		// Once again to verify that "get(i)" does not go out of bounds.
		catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		// Out escape method.
		System.out.println(" [9] Done");

		// Pick up a shopping cart on the way into the store.
		ShoppingCart cart = new ShoppingCart();

		// Allow the customer to add as many items as they want, including dupicate items.
		while (selection < 9){
			selection = cart.getUserInput();
			try {
				// Break out of the while loop when we're done shopping.
				if (selection == 9){
					break;
				}
				// Display the item the user most recently selected.
				System.out.println(inventory.get(selection).getItemName());
				// Add this item to the virtual cart.
				cart.addItem(inventory.get(selection));
			}
			catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
		}
		// Once we're done shopping show the customer the items in the cart and 
		// print the sales/import duty tax and total.
		cart.printShoppingCart();
	}
	// Add an item to the inventory.
	private void addItem(Item product){
		inventory.add(product);
	}
	// Remove an item from the inventory.
	private void removeItem(Item product){
		inventory.remove(product);
	}
	// Adding all current store stock to the inventory. This can be expanded to store these values in 
	// a database with the JDBC.
	private void addItemsToInventory(){
		try {
		// Cart Configuration 1
		Item book = new Item("book", BOOK_PRICE, EXEMPT, EXEMPT);
		Item music_CD = new Item("music CD", MUSIC_CD_PRICE, SALES_TAX, EXEMPT);
		Item chocolate_bar = new Item("chocolate bar", DOMESTIC_CHOCOLATE_BAR_PRICE, EXEMPT, EXEMPT);

		// Cart Configuration 2
		Item imported_chocolates = new Item("imported box of chocolates", IMPORTED_CHOCOLATE_BAR_PRICE, EXEMPT, IMPORT_DUTY);
		Item imported_bottle_perfume = new Item("imported bottle of perfume", IMPORTED_PERFUME, SALES_TAX, IMPORT_DUTY);

		// Cart Congiguration 3
		Item imported_bottle_perfume_alt = new Item("imported bottle of perfume",IMPORTED_PERFUME_ALT, SALES_TAX, IMPORT_DUTY);
		Item domestic_bottle_perfume = new Item("bottle of perfume",DOMESTIC_PERFUME, SALES_TAX, EXEMPT);
		Item pack_headache_pills = new Item("packet of headache pills", HEADACHE_PILLS, EXEMPT, EXEMPT);
		Item imported_chocolates_alt = new Item("imported box of chocolates", IMPORTED_CHOCOLATE_BAR_PRICE_ALT, EXEMPT, IMPORT_DUTY);

		// Cart Configuration 1
		inventory.add(book);
		inventory.add(music_CD);	
		inventory.add(chocolate_bar);

		// Cart Configuration 2
		inventory.add(imported_chocolates);
		inventory.add(imported_bottle_perfume);

		// Cart Congiguration 3
		inventory.add(imported_bottle_perfume_alt);
		inventory.add(domestic_bottle_perfume);
		inventory.add(pack_headache_pills);
		inventory.add(imported_chocolates_alt);
		}
		catch (Exception e) {
		}
	}
}


