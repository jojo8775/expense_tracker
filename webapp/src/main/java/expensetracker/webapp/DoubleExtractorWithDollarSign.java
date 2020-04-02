package expensetracker.webapp;

public class DoubleExtractorWithDollarSign extends BaseDoubleExtractor {
	
	@Override
	protected boolean hasInvalidCharacter(String str) {
		if(!(str.startsWith("$") || str.startsWith("-$"))) {
			return true;
		}
		return super.hasInvalidCharacter(stripSpecialCharaterIfAny(str));
	}
	
	@Override
	protected String stripSpecialCharaterIfAny(String str) {

		if (str.startsWith("-$") || str.startsWith("$")) {
			str = str.replaceAll(",", "");
			return str.replace("$", "");
		}

		return str;
	}
}
