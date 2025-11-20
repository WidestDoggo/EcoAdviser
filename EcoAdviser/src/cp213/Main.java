package cp213;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private String category;
    private double amount;
    private String date;

    public Main(String category, double amount, String date) {
	this.category = category;
	this.amount = amount;
	this.date = date;
    }

    public static void main(String[] args) {
	Scanner scanner = new Scanner(System.in);
	List<Main> expenses = new ArrayList<>();

	while (true) {
	    System.out.println("\n=== Expense Tracker ===");
	    System.out.println("1. Add Expense");
	    System.out.println("2. View Expenses");
	    System.out.println("3. Exit");
	    System.out.print("Choose option: ");
	    int choice = scanner.nextInt();
	    scanner.nextLine();

	    if (choice == 1) {
		System.out.print("Category: ");
		String category = scanner.nextLine();
		System.out.print("Amount: ");
		double amount = scanner.nextDouble();
		scanner.nextLine();
		System.out.print("Date (YYYY-MM-DD): ");
		String date = scanner.nextLine();

		Main e = new Main(category, amount, date);
		expenses.add(e);
		System.out.println("Expense added!");

	    } else if (choice == 2) {
		System.out.println("\n--- All Expenses ---");
		for (Main e : expenses) {
		    System.out.println(e);
		}

	    } else if (choice == 3) {
		System.out.println("Goodbye!");
		break;

	    } else {
		System.out.println("Invalid option.");
	    }

	}

    }

    public String getCategory() {
	return category;
    }

    public void setCategory(String category) {
	this.category = category;
    }

    public double getAmount() {
	return amount;
    }

    public void setAmount(double amount) {
	this.amount = amount;
    }

    public String getDate() {
	return date;
    }

    public void setDate(String date) {
	this.date = date;
    }

    @Override
    public String toString() {
	return "Category: " + category + ", Amount: $" + amount + ", Date: " + date;
    }
}
