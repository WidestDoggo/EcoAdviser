package cp213;

/**
 * Separate file to show inheritance from your own class. Income is another kind
 * of FinancialRecord.
 */
public class Income extends FinancialRecord {

    public Income(String category, double amount, String date) {
	super(category, amount, date);
    }

    @Override
    public String getType() {
	return "Income";
    }
}
