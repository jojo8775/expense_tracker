package expensetracker.webapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

public class TransactionCategoryDetector {
	private final boolean debug = false;

	private final Set<String> groceryCategory = initializeGroceryKeywords();
	private final Set<String> resturantCategory = initializeRestaurantKeywords();
	private final Set<String> onlineShoppingCategory = initializeOnlineShoppingKeywords();
	private final Set<String> liquorCategory = initializeLiquorKeywords();
	private final Set<String> gasCategory = initializeGasKeywords();
	private final Set<String> automotiveCategory = initializeAutomotiveKeywords();
	private final Set<String> healthyLivingCategory = initializeHealthyLivingKeywords();
	private final Set<String> monthlyBillCategory = initializeMonthlyBillKeywords();

	public TransactionCategoryModel detect(String statement) {
		String[] strArr = statement.split("\\s+");

		var categoryHitMap = new HashMap<TransactionCategory, List<String>>();

		for (var str : strArr) {
			str = str.toLowerCase();
			if (groceryCategory.contains(str)) {
				populateMap(TransactionCategory.GROCERY, str, categoryHitMap);
			}

			if (resturantCategory.contains(str)) {
				populateMap(TransactionCategory.BARS_AND_RESTURANT, str, categoryHitMap);
			}

			if (onlineShoppingCategory.contains(str)) {
				populateMap(TransactionCategory.ONLINE, str, categoryHitMap);
			}

			if (liquorCategory.contains(str)) {
				populateMap(TransactionCategory.LIQUOR, str, categoryHitMap);
			}

			if (gasCategory.contains(str)) {
				populateMap(TransactionCategory.GAS, str, categoryHitMap);
			}

			if (automotiveCategory.contains(str)) {
				populateMap(TransactionCategory.AUTOMOTIVE, str, categoryHitMap);
			}
			
			if (healthyLivingCategory.contains(str)) {
				populateMap(TransactionCategory.HEALTHY_LIVING, str, categoryHitMap);
			}
			
			if (monthlyBillCategory.contains(str)) {
				populateMap(TransactionCategory.MONTHLY_BILL, str, categoryHitMap);
			}
		}

		PriorityQueue<Map.Entry<TransactionCategory, List<String>>> pQueue = new PriorityQueue<>(
				(a, b) -> b.getValue().size() - a.getValue().size());
		for (var entry : categoryHitMap.entrySet()) {
			pQueue.offer(entry);
		}

		var result = new TransactionCategoryModel();
		result.originalStatement = statement;

		if (pQueue.isEmpty()) {
			result.category = TransactionCategory.UNKNOWN;
			return result;
		}

		result.category = pQueue.peek().getKey();
		while (!pQueue.isEmpty()) {
			var top = pQueue.poll();
			result.hitDiagnostics.add(Pair.of(top.getKey(), top.getValue()));
		}

		trace(result);

		return result;
	}

	private Map<TransactionCategory, List<String>> populateMap(TransactionCategory category, String keyWord,
			Map<TransactionCategory, List<String>> map) {
		if (!map.containsKey(category)) {
			map.put(category, new ArrayList<String>());
		}

		map.get(category).add(keyWord);
		return map;
	}

	private Set<String> initializeGroceryKeywords() {
		var keyWords = new HashSet<String>();
		keyWords.add("cotsco");
		keyWords.add("wholesale");
		keyWords.add("wal-mart");
		keyWords.add("spice");
		keyWords.add("foods");
		keyWords.add("dollarama");
		keyWords.add("fruiticana");

		return keyWords;
	}

	private Set<String> initializeRestaurantKeywords() {
		var keyWords = new HashSet<String>();
		keyWords.add("shawrma");
		keyWords.add("kitchen");
		keyWords.add("tang");
		keyWords.add("brewing");
		keyWords.add("bar");
		keyWords.add("grill");
		keyWords.add("irish");
		keyWords.add("burrito");
		keyWords.add("saravanaa");
		keyWords.add("tavern");

		return keyWords;
	}
	
	private Set<String> initializeOnlineShoppingKeywords() {
		var keyWords = new HashSet<String>();
		keyWords.add("amzn");

		return keyWords;
	}

	private Set<String> initializeLiquorKeywords() {
		var keyWords = new HashSet<String>();
		keyWords.add("liquor");

		return keyWords;
	}

	private Set<String> initializeGasKeywords() {
		var keyWords = new HashSet<String>();
		keyWords.add("esso");

		return keyWords;
	}

	private Set<String> initializeAutomotiveKeywords() {
		var keyWords = new HashSet<String>();
		keyWords.add("lube");

		return keyWords;
	}
	
	private Set<String> initializeHealthyLivingKeywords(){
		var keyWords = new HashSet<String>();
		keyWords.add("drug");
		keyWords.add("eyecare");

		return keyWords;
	}
	
	private Set<String> initializeMonthlyBillKeywords(){
		var keyWords = new HashSet<String>();
		keyWords.add("shaw");
		keyWords.add("compass");

		return keyWords;
	}

	private void trace(TransactionCategoryModel result) {
		if (debug) {
			var sb = new StringBuilder();
			sb.append("statement : [").append(result.originalStatement).append("] ");
			sb.append("category : [").append(result.category).append("] ");
			sb.append("hitCount : [").append(result.hitDiagnostics.toString()).append("] ");

			System.out.println(sb.toString());
		}
	}
}
