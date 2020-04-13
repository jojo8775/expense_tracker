package expensetracker.webapp;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class TransactionInformation {
    public DateTime transactionDateTime;
    public TransactionCategory category;
    public String originalStatement;
    public double value;
    public StatementSource source;
    
    @Override
    public String toString() {
        var dateTimeStr = transactionDateTime == null ? "null" : transactionDateTime.toString(DateTimeFormat.mediumDate());
        return "TransactionInformation [transactionDateTime=" + dateTimeStr + ", category=" + category
                + ", originalStatement=" + originalStatement + ", value=" + value + ", source=" + source + "]";
    }
}
