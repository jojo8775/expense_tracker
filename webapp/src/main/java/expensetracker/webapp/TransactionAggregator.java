package expensetracker.webapp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionAggregator {
	public void aggregate(List<TransactionInformation> transactionInformationList) {
		var transactionGroup = new HashMap<TransactionCategory, List<TransactionInformation>>();
		for (var transaction : transactionInformationList) {
			populateMap(transactionGroup, transaction);
		}
		
		for(var entry : transactionGroup.entrySet()) {
			computeSumAndPrint(entry.getKey(), entry.getValue());
		}
		
		transactionInformationList.sort(transactionSortFuntion());
		var linesToPrint = new ArrayList<String>();
		linesToPrint.add("raw data -- ");
		for(var transaction : transactionInformationList) {
			linesToPrint.add(transaction.originalStatement + ": " + transaction.value + " [" + transaction.source + "]");
		}
		print(linesToPrint);
	}
	
	private Map<TransactionCategory, List<TransactionInformation>> populateMap(
			Map<TransactionCategory, List<TransactionInformation>> map, TransactionInformation transactionInformation) {
		if(!map.containsKey(transactionInformation.category)) {
			map.put(transactionInformation.category, new ArrayList<TransactionInformation>());
		}
		
		map.get(transactionInformation.category).add(transactionInformation);
		return map;
	}
	
	private void computeSumAndPrint(TransactionCategory category, List<TransactionInformation> transactions) {
		transactions.sort(transactionSortFuntion());
		
		double sum = 0;
		var linesToPrint = new ArrayList<String>();
		for(var transaction : transactions) {
			linesToPrint.add(transaction.originalStatement + ": " + transaction.value + " [" + transaction.source + "]");
			sum += (transaction.value > 0) ? transaction.value : 0;
		}
		linesToPrint.add("Total spent on [" + category + "] = " + sum);
		
		print(linesToPrint);
	}

	private Comparator<? super TransactionInformation> transactionSortFuntion() {
		return (a, b) -> {
			if(a.transactionDateTime != null && b.transactionDateTime != null) {
				return a.transactionDateTime.compareTo(b.transactionDateTime);
			}
			else if(a.transactionDateTime == null) {
				return -1;
			}
			else {
				return 1;
			}
		};
	}
	
	private void print(List<String> messageList) {
		System.out.println("==================================================================================");
		messageList.forEach(a -> System.out.println(a));
		System.out.println("----------------------------------------------------------------------------------");
	}
}
