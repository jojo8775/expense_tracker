package expensetracker.webapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class TransactionCategoryModel {
	public TransactionCategory category;
	public String originalStatement;
	public List<Pair<TransactionCategory, List<String>>> hitDiagnostics = new ArrayList<Pair<TransactionCategory,List<String>>>();
}
