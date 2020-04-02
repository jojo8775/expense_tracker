package expensetracker.webapp;

import org.apache.commons.lang3.tuple.Pair;

public abstract class BaseDoubleExtractor implements DoubleExtractor{
	private boolean debugFlag = true;
	
	public Pair<StatementRowType, Double> tryGetDollarValue(String strValue) {
		if (strValue == null || strValue.isBlank()) {
			return null;
		}

		var amount = extractDouble(strValue);
		if(amount != null) {
			return Pair.of(StatementRowType.AMOUNT_ONLY, amount);
		}
		
		var statementWords = strValue.split("\\s+");
		
		amount = extractDouble(statementWords[statementWords.length - 1]);
		if(amount != null) {
			return Pair.of(StatementRowType.AMOUNT_WITH_DETAILS, amount);
		}
		
		return null;
		
//		StatementRowType rowType = null;
//		if (strValue.charAt(0) == '$') {
//			var pair = stripSpecialCharaterIfAny(strValue);
//			if (!pair.getLeft()) {
//				return null;
//			}
//			strValue = pair.getRight();
//			rowType = StatementRowType.AMOUNT_ONLY;
//			
//			return Pair.of(rowType, parseDougble(strValue));
//		} else {
//			String[] strs = strValue.split("\\s+");
//			if (strs.length > 1) {
//				rowType = StatementRowType.AMOUNT_WITH_DETAILS;
//				var pair = stripSpecialCharaterIfAny(strs[strs.length - 1]);
//				if (!pair.getLeft()) {
//					return null;
//				}
//				strValue = pair.getRight();
//				
//				return Pair.of(rowType, parseDougble(strValue)); 
//			}
//		}
//
//		return null;
	}

	private Double extractDouble (String str) {
		if(!hasInvalidCharacter(str)) {
			var ss = stripSpecialCharaterIfAny(str);
			return parseDouble(ss);
		}
		
		return null;
	}
	
	protected boolean hasInvalidCharacter(String str) {
		if(str  == null || str.isBlank()) {
			return true;
		}
		
		boolean foundDecimal = false, foundSign = false;
		
		for(char ch : str.toCharArray()) {
			if(!Character.isDigit(ch)) {
				if(ch == '.' && !foundDecimal) {
					foundDecimal = true;
				}
				else if((ch == '-' || ch == '+') && !foundSign) {
					foundSign = true;
				}
				else if(ch == ',') {
					// Nothing to do. 
				}
				else {
					return true;
				}
			}
		}
		
		return false;
	}
	
	private Double parseDouble(String str) {
		if(str.isEmpty()) {
			return null;
		}
		
		try {
			return Double.parseDouble(str);
		}
		catch (NumberFormatException e) {
			trace(str + " is not a double.");
			return null;
		}
	}
	
	private void trace(String msg) {
		if (debugFlag) {
			System.out.println(msg);
		}
	}
	
	protected abstract String stripSpecialCharaterIfAny(String str);
}
