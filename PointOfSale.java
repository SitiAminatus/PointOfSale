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

	public Item() {
		this.product_name = null;
		this.product_price = -1;
		this.product_sales_tax_rate = -1;
		this.product_import_rate = -1;
	}

	public Item(String item_name, double item_price, double item_sales_tax_rate, double item_import_tax_rate) {
		this.product_name = item_name;
		this.product_price = item_price;
		this.product_sales_tax_rate = item_sales_tax_rate;
		this.product_import_rate = item_import_tax_rate;
	}

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
	private ArrayList<Item> shopping_cart = new ArrayList<>();

	public ShoppingCart() {
	}

	public void printShoppingCart() {
		double total_tax = 0.0;
		double running_total = 0.0;
		double imported_item_price = 0.0;
		double current_import_tax = 0.0;
		double current_sales_tax = 0.0;
		double current_item_price = 0.0;
		try {
			// Displays the contents of the shopping cart.
			System.out.println("\nYour shopping cart contains: ");
			for ( int i = 0; i < shopping_cart.size(); i++) {

				current_sales_tax = applySalesTaxRate(shopping_cart.get(i));
				current_import_tax = applyImportDutyRate(shopping_cart.get(i));
				current_item_price = shopping_cart.get(i).getItemPrice();

				imported_item_price = (current_item_price + current_import_tax + current_sales_tax);
				imported_item_price = roundValue(imported_item_price);

				System.out.println("1 " + shopping_cart.get(i).getItemName() 
								 + " $" + imported_item_price);

				total_tax += (current_sales_tax + current_import_tax);
				running_total += (current_item_price + current_import_tax + current_sales_tax);
			}

			//running_total = roundValue(running_total);

			System.out.println("Sales Taxes: $" + total_tax);
			System.out.println("Total: $" + running_total);
		}
		catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	private double applySalesTaxRate(Item product) {
		double DENOMINATOR = 100.0;
		double total_tax;
		total_tax = (product.getItemSalesTaxRate() * product.getItemPrice()) / DENOMINATOR;
		//total_tax = roundValue(total_tax);
		return total_tax;
	}

	private double applyImportDutyRate(Item product) {
		double DENOMINATOR = 100.0;
		double total_tax;
		total_tax = (product.getItemImportTaxRate() * product.getItemPrice()) / DENOMINATOR;
		//total_tax = roundValue(total_tax);
		return total_tax;
	}

	private double roundValue(double input) {
		double DENOMINATOR = 100.0;
		double value = input;
		try {
			input = input * DENOMINATOR;
			input = Math.round(input);
			input = input / DENOMINATOR;
		}
		catch (NumberFormatException e) {}
		return input;
	}

	public void addItem(Item product){
		shopping_cart.add(product);
	}

	public void getItem(int index){
		try {
			shopping_cart.get(index);
		}
		catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	public int getUserInput(){
		int input;
		System.out.println("Please select your item: ");
		Scanner reader = new Scanner(System.in);  // Reading from System.in
		input = reader.nextInt();
		return input;
	}
}


class Inventory {
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

	private ArrayList<Item> inventory = new ArrayList<>();


	public Inventory() {
		addItemsToInventory();
	}

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
		catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}

		System.out.println(" [9] Done");

		ShoppingCart cart = new ShoppingCart();

		while (selection < 9){
			selection = cart.getUserInput();

			try {
				if (selection == 9){
					break;
				}

				inventory.get(selection);

				System.out.println(inventory.get(selection).getItemName());
				cart.addItem(inventory.get(selection));
			}
			catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
		}
		
		cart.printShoppingCart();
	}

	private void addItem(Item product){
		inventory.add(product);
	}

	private void removeItem(Item product){
		inventory.remove(product);
	}

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


