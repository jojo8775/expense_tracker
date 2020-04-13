package expensetracker.webapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class Program {
	private final boolean debug = false;
	
	public static void main(String[] args) {
		new Program().execute();
	}

	public List<String> execute() {
		var logger = new Logger();

		List<TransactionInformation> transactions = new ArrayList<>(); 
		transactions.addAll(executeScotiaStatement(logger));
		transactions.addAll(executeRbcStatement(logger));
		
		new TransactionAggregator().aggregate(transactions);

//		var result = new Gson().toJson(logger.getLogs());
//		System.out.println(result);

		return logger.getLogs();
	}

	private List<TransactionInformation> executeScotiaStatement(Logger logger) {
		var entries = new TableDataExtractorMultiline().readPdf("/test_file_scotia.pdf", new SimpleDoubleExtractor());
		var scrubbedEntries = new DataScrubberImpl(StatementSource.SCOTIA_CREDIT, logger).getScrubbedEntries(entries);
		var transactionList = new ContentParser(new TransactionCategoryDetector()).parse(scrubbedEntries, StatementSource.SCOTIA_CREDIT);
//		print(transactionList);
		return transactionList;
	}

	private List<TransactionInformation> executeRbcStatement(Logger logger) {
		var entries = new TableDataExtractor().readPdf("/test_file.pdf", new DoubleExtractorWithDollarSign());
		trace(entries);
		var scrubbedEntries = new DataScrubberImpl(StatementSource.RBC_CREDIT, logger).getScrubbedEntries(entries);
		var transactionList = new ContentParser(new TransactionCategoryDetector()).parse(scrubbedEntries, StatementSource.RBC_CREDIT);
//		print(transactionList);
		return transactionList;
	}

	private void print(List<TransactionInformation> transactionInfoList) {
		transactionInfoList.forEach(x -> System.out.println(x.toString()));
	}

	private void trace(List<Pair<String, Double>> entries) {
		if (debug) {
			entries.forEach(x -> System.out.println(x.getLeft() + " -> " + x.getRight()));
		}
	}
}
