package expensetracker.webapp;

public class SimpleDoubleExtractor extends BaseDoubleExtractor{

	@Override
	protected String stripSpecialCharaterIfAny(String str) {
		if(!str.isBlank()) {
			return str.replaceAll(",", "");
		}
		
		return str;
	}
}
