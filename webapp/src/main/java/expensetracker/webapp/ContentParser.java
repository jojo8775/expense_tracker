package expensetracker.webapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

public class ContentParser {
    private final Map<String, Integer> validMonths = initializeValidMonths();
    private final TransactionCategoryDetector categoryDetector;
    public ContentParser(TransactionCategoryDetector categoryDetector) {
    	this.categoryDetector = categoryDetector;
    }
    
    public List<TransactionInformation> parse(List<Pair<String, Double>> entries, StatementSource source) {
        List<TransactionInformation> transactionDetailList = new ArrayList<TransactionInformation>();
        for(var entry : entries) {
            transactionDetailList.add(extractTransactionInformation(entry, source));
        }
        
        
        
        return transactionDetailList;
    }
    
    private TransactionInformation extractTransactionInformation(Pair<String, Double> entry, StatementSource source) {
        String[] strArr = entry.getLeft().split("\\s+");
        
        var transactionInformation = new TransactionInformation();
        transactionInformation.category = categoryDetector.detect(entry.getLeft()).category;
        transactionInformation.transactionDateTime = getTransactedDatetime(strArr);
        transactionInformation.value = entry.getRight();
        transactionInformation.originalStatement = entry.getLeft();
        transactionInformation.source = source;
                
        return transactionInformation;
    }
    
    private DateTime getTransactedDatetime(String[] strArr) {
        var monthFound = false;
        int month = -1;

        for (String str : strArr) {
            str = str.toLowerCase();
            if (!monthFound && validMonths.containsKey(str)) {
                monthFound = true;
                month = validMonths.get(str);
            } else if (monthFound && isInt(str)) {
                return new DateTime(2020, month, Integer.parseInt(str), 0, 0);
            }
        }
        
        return null;
    }

    // private

    private boolean isInt(String str) {
        boolean isNan = false;
        try {
            Integer.parseInt(str);
            isNan = true;
        } catch (NumberFormatException e) {
            isNan = false;
        }

        return isNan;
    }

    private Map<String, Integer> initializeValidMonths() {
        var months = new HashMap<String, Integer>();
        months.put("jan", Integer.valueOf(DateTimeConstants.JANUARY));
        months.put("feb", Integer.valueOf(DateTimeConstants.FEBRUARY));
        months.put("mar", Integer.valueOf(DateTimeConstants.MARCH));
        months.put("apr", Integer.valueOf(DateTimeConstants.APRIL));
        months.put("may", Integer.valueOf(DateTimeConstants.MAY));
        months.put("jun", Integer.valueOf(DateTimeConstants.JUNE));
        months.put("jul", Integer.valueOf(DateTimeConstants.JULY));
        months.put("aug", Integer.valueOf(DateTimeConstants.AUGUST));
        months.put("sep", Integer.valueOf(DateTimeConstants.SEPTEMBER));
        months.put("oct", Integer.valueOf(DateTimeConstants.OCTOBER));
        months.put("nov", Integer.valueOf(DateTimeConstants.NOVEMBER));
        months.put("dec", Integer.valueOf(DateTimeConstants.DECEMBER));

        return months;
    }
}
