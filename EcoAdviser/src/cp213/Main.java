package cp213;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Abstract parent for both expenses and incomes.
 */
abstract class FinancialRecord {
    private final String category;
    private final double amount;
    private final String date; // stored as String, validated with LocalDate

    public FinancialRecord(String category, double amount, String date) {
	this.category = category;
	this.amount = amount;
	this.date = date;
    }

    public String getCategory() {
	return category;
    }

    public double getAmount() {
	return amount;
    }

    public String getDate() {
	return date;
    }

    // Implemented differently for Expense and Income
    public abstract String getType();

    @Override
    public String toString() {
	return String.format("[%s] %s: $%.2f (%s)", getType(), category, amount, date);
    }
}

class Expense extends FinancialRecord {

    public Expense(String category, double amount, String date) {
	super(category, -Math.abs(amount), date); // store income positive, expenses negative
    }

    @Override
    public String getType() {
	return "Expense";
    }
}

@SuppressWarnings("serial")
class ExpensePanel extends JPanel {

    // Polymorphic list: can hold both Expense and Income
    private final List<FinancialRecord> records = new ArrayList<>();

    private final JTextField categoryField = new JTextField();
    private final JTextField amountField = new JTextField();
    private final JTextField dateField = new JTextField(); // YYYY-MM-DD

    private final JButton addExpenseButton = new JButton("Add Expense");
    private final JButton addIncomeButton = new JButton("Add Income");
    private final JButton viewButton = new JButton("View All");
    private final JTextArea outputArea = new JTextArea(15, 40);

    public ExpensePanel() {
	this.layoutView();
	this.registerListeners();
    }

    private void layoutView() {
	this.setLayout(new BorderLayout(10, 10));
	this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
	inputPanel.add(new JLabel("Category:"));
	inputPanel.add(categoryField);
	inputPanel.add(new JLabel("Amount:"));
	inputPanel.add(amountField);
	inputPanel.add(new JLabel("Date (YYYY-MM-DD):"));
	inputPanel.add(dateField);
	inputPanel.add(addButton);
	inputPanel.add(viewButton);

	outputArea.setEditable(false);
	JScrollPane scrollPane = new JScrollPane(outputArea);

	this.add(inputPanel, BorderLayout.NORTH);
	this.add(scrollPane, BorderLayout.CENTER);
    }

    private void registerListeners() {
	addButton.addActionListener(e -> addExpense());
	viewButton.addActionListener(e -> displayExpenses());
    }

    private void addExpense() {
	String category = categoryField.getText().trim();
	String amountText = amountField.getText().trim();
	String dateText = dateField.getText().trim();

	if (category.isEmpty() || amountText.isEmpty() || dateText.isEmpty()) {
	    outputArea.setText("Please fill out category, amount, and date.\n");
	    System.out.println("GUI: missing fields when adding expense.");
	    return;
	}

	double amount;
	try {
	    amount = Double.parseDouble(amountText);
	} catch (NumberFormatException ex) {
	    outputArea.setText("Amount must be a valid number.\n");
	    System.out.println("GUI: invalid amount entered: " + amountText);
	    return;
	}

	// validate date like your console code
	try {
	    LocalDate.parse(dateText); // just to validate
	} catch (Exception ex) {
	    outputArea.setText("Invalid date. Use YYYY-MM-DD and a real date.\n");
	    System.out.println("GUI: invalid date entered: " + dateText);
	    return;
	}

	Expense expense = new Expense(category, amount, dateText);
	expenses.add(expense);

	// GUI message
	outputArea.setText("Expense added!\n");
	// Console output too
	System.out.println("Added expense: " + expense);

	// clear inputs
	categoryField.setText("");
	amountField.setText("");
	dateField.setText("");
    }

    private void displayExpenses() {
	if (expenses.isEmpty()) {
	    outputArea.setText("No expenses recorded yet.\n");
	    System.out.println("View Expenses: no expenses recorded yet.");
	    return;
	}

	StringBuilder sb = new StringBuilder();
	sb.append("=== All Expenses ===\n\n");

	Map<String, List<Expense>> grouped = new TreeMap<>();

	for (Expense e : expenses) {
	    grouped.computeIfAbsent(e.getCategory(), k -> new ArrayList<>()).add(e);
	}

	for (Map.Entry<String, List<Expense>> entry : grouped.entrySet()) {
	    String category = entry.getKey();
	    List<Expense> catExpenses = entry.getValue();

	    sb.append(category).append(": ");

	    double total = 0.0;

	    for (int i = 0; i < catExpenses.size(); i++) {
		Expense e = catExpenses.get(i);
		total += e.getAmount();

		sb.append(String.format("$%.2f (%s)", e.getAmount(), e.getDate()));
		if (i < catExpenses.size() - 1) {
		    sb.append(", ");
		}
	    }
	    sb.append(String.format(" | Total: $%.2f\n", total));
	}

	String result = sb.toString();
	outputArea.setText(result);

	// Also print to console for your report / screenshots
	System.out.println(result);
    }
}

public class Main {

    public static void main(String[] args) {
	SwingUtilities.invokeLater(() -> {
	    JFrame frame = new JFrame("Expense Tracker");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setContentPane(new ExpensePanel());
	    frame.pack();
	    frame.setLocationRelativeTo(null); // center
	    frame.setVisible(true);
	});
    }
}

/*
 * public class Main { private String category; private double amount; private
 * String date;
 * 
 * public Main(String category, double amount, String date) { this.category =
 * category; this.amount = amount; this.date = date; }
 * 
 * public static void main(String[] args) { Scanner scanner = new
 * Scanner(System.in); List<Main> expenses = new ArrayList<>();
 * 
 * while (true) { System.out.println("\n=== Expense Tracker ===");
 * System.out.println("1. Add Expense"); System.out.println("2. View Expenses");
 * System.out.println("3. Exit"); System.out.print("Choose option: "); int
 * choice = scanner.nextInt(); scanner.nextLine();
 * 
 * if (choice == 1) { System.out.print("Category: "); String category =
 * scanner.nextLine();
 * 
 * System.out.print("Amount: "); double amount = scanner.nextDouble();
 * scanner.nextLine();
 * 
 * String date = readValidDate(scanner);
 * 
 * Main e = new Main(category, amount, date); expenses.add(e);
 * System.out.println("Expense added!");
 * 
 * } else if (choice == 2) { System.out.println("\n--- All Expenses ---");
 * 
 * if (expenses.isEmpty()) { System.out.println("No expenses recorded yet."); }
 * else { // Group by category, sorted alphabetically Map<String, List<Main>>
 * grouped = new TreeMap<>();
 * 
 * for (Main e : expenses) { grouped.computeIfAbsent(e.getCategory(), k -> new
 * ArrayList<>()).add(e); }
 * 
 * // Print each category with amounts, dates, and total for (Map.Entry<String,
 * List<Main>> entry : grouped.entrySet()) { String category = entry.getKey();
 * List<Main> catExpenses = entry.getValue();
 * 
 * System.out.print(category + ": ");
 * 
 * double total = 0.0;
 * 
 * for (int i = 0; i < catExpenses.size(); i++) { Main e = catExpenses.get(i);
 * total += e.getAmount();
 * 
 * System.out.print(String.format("$%.2f (%s)", e.getAmount(), e.getDate()));
 * 
 * if (i < catExpenses.size() - 1) { System.out.print(", "); } }
 * 
 * System.out.println(String.format(" | Total: $%.2f", total)); } }
 * 
 * } else if (choice == 3) { System.out.println("Goodbye!"); break;
 * 
 * } else { System.out.println("Invalid option."); }
 * 
 * }
 * 
 * }
 * 
 * public String getCategory() { return category; }
 * 
 * public void setCategory(String category) { this.category = category; }
 * 
 * public double getAmount() { return amount; }
 * 
 * public void setAmount(double amount) { this.amount = amount; }
 * 
 * public String getDate() { return date; }
 * 
 * public void setDate(String date) { this.date = date; }
 * 
 * @Override public String toString() { return "Category: " + category +
 * ", Amount: $" + amount + ", Date: " + date; }
 * 
 * private static String readValidDate(Scanner scanner) { while (true) {
 * System.out.print("Date (YYYY-MM-DD): "); String input =
 * scanner.nextLine().trim();
 * 
 * try { // Strict parsing of ISO format LocalDate.parse(input); return input;
 * // valid date } catch (Exception e) { System.out.
 * println("Invalid date. Please enter a real date in YYYY-MM-DD format."); } }
 * }
 */
