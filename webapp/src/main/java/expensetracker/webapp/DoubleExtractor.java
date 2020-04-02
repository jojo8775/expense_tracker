package expensetracker.webapp;

import org.apache.commons.lang3.tuple.Pair;

interface DoubleExtractor {
	public Pair<StatementRowType, Double> tryGetDollarValue(String strValue);
}
