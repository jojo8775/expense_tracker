package expensetracker.webapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class DataScrubberImpl {
    private boolean debugFlag = false;
    private final List<String> exclusionList = new ArrayList<>();
    private final List<Pair<String, Double>> scrubbedEntries = new ArrayList<>();
    private final Logger logger;

    public DataScrubberImpl(StatementSource statementSource, Logger logger) {
        this.logger = logger;

        switch (statementSource) {
        case RBC_DEBIT:
        case RBC_CREDIT:
            exclusionList.add("NEW BALANCE $");
            exclusionList.add("Interest");
            exclusionList.add("Purchases & debits");
            exclusionList.add("Cash Back");
            exclusionList.add("Minimum payment");
            exclusionList.add("PREVIOUS STATEMENT");
            exclusionList.add("Credit limit");
            exclusionList.add("Available credit");
            exclusionList.add("P.O.BOX 4016");
            exclusionList.add("19.99 $");
            exclusionList.add("Previous Statement");
            exclusionList.add("Cash advances");

            break;

        case SCOTIA_CREDIT:
        case SCOTIA_DEBIT:
            exclusionList.add("Statement Period");
            exclusionList.add("SBVREP_");
            exclusionList.add("HRI -");
            exclusionList.add("Statement date");
            exclusionList.add("Account # 4537 ");
            exclusionList.add("Page");
            exclusionList.add("544 SYDNEY AVE");
            exclusionList.add("Payment due date");
            exclusionList.add("MR CHIRANJEEB");
            exclusionList.add("Continued on page");
            exclusionList.add("4537 471 494 457 010");
            exclusionList.add("Ending points");
            exclusionList.add("Points earned");
            exclusionList.add("cheque or money orde");
            exclusionList.add("4 of 4");
            exclusionList.add("Date revised");
            exclusionList.add("Beginning points");

            break;

        default:
            break;
        }
    }

    public List<Pair<String, Double>> getScrubbedEntries(List<Pair<String, Double>> entries) {
        entries.forEach(a -> addEntry(a));
        return scrubbedEntries;
    }

    private void addEntry(Pair<String, Double> entry) {
        if (entry.getLeft().isBlank() || isExcluded(entry.getLeft()) || entry.getRight() == null) {
            return;
        }

        scrubbedEntries.add(entry);
        trace(entry.toString());
    }

    private boolean isExcluded(String str) {
        for (String s : exclusionList) {
            if (str.contains(s)) {
                return true;
            }
        }

        return false;
    }

    private void trace(String msg) {
        if (debugFlag) {
            System.out.println(msg);
        }

        logger.info(msg);
    }

    @Deprecated
    public List<Pair<String, Double>> getScrubbedEntries() {
        return scrubbedEntries;
    }
}
