package expensetracker.webapp;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.pdfbox.pdmodel.PDDocument;

import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.PageIterator;
import technology.tabula.Rectangle;
import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;
import technology.tabula.detectors.NurminenDetectionAlgorithm;
import technology.tabula.extractors.BasicExtractionAlgorithm;

public class TableDataExtractor {
	
	private final boolean debugFalg = false;

	public static void main(String[] args) {
		new TableDataExtractor().read();
	}

	public void read() {
		readPdf("src/resources/test_file.pdf", new DataScrubberImpl(StatementSource.RBC_CREDIT), new DoubleExtractorWithDollarSign());
	}

	public void readPdf(String fileName, DataScrubberImpl dataScrubber, DoubleExtractor doubleExtractor) {
		LinkedList<String> list = new LinkedList<String>();
		File file = new File(fileName);
		
		try {
			PDDocument pdfDocument = PDDocument.load(file);

			ObjectExtractor extractor = new ObjectExtractor(pdfDocument);
			NurminenDetectionAlgorithm detectionAlgorithm = new NurminenDetectionAlgorithm();
			PageIterator pages = extractor.extract();

			System.out.println("Detecting tables in each page ...");
			int count = 0;
			BasicExtractionAlgorithm sea = new BasicExtractionAlgorithm();
			while (pages.hasNext()) {
				Page page = pages.next();

				trace("table positions in page : " + ++count);
				List<Rectangle> tablesOnPage = detectionAlgorithm.detect(page);

				trace("number of tables detected " + tablesOnPage.size());

				List<Table> tables = sea.extract(page);
				int tableCount = 0;
				for (Table table : tables) {
					trace("Printing content of the table " + ++tableCount + " ...");
					for (List<RectangularTextContainer> rows : table.getRows()) {
						for (RectangularTextContainer row : rows) {
							String rowStr = row.getText();
							trace("------------ number of cells : " + row.getTextElements().size());
							trace(rowStr);
							trace("--------------------------------");
//                                if (rowStr.isBlank()) {
//                                    System.out.println("found a blank row skiping ...");
//                                }

							Pair<StatementRowType, Double> val = doubleExtractor.tryGetDollarValue(rowStr);
							if (val != null) {

								if (val.getLeft() == StatementRowType.AMOUNT_WITH_DETAILS) {
//									System.out.println(rowStr + " => " + val.getRight());
									dataScrubber.addEntry(Pair.of(rowStr, val.getRight()));
									
								} else {
									String prev = list.isEmpty() ? "<Blank>" : list.get(0);
									
									dataScrubber.addEntry(Pair.of(prev, val.getRight()));
//									System.out.println(prev + " => " + val.getRight());
								}
							} else if (!rowStr.isBlank()) {
								list.add(0, rowStr);
							}

							if (list.size() > 10) {
								list.remove(list.size() - 1);
							}
						}
					}
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	private Pair<StatementRowType, Double> tryGetDollarValue(String strValue) {
//		if (strValue == null || strValue.isBlank()) {
//			return null;
//		}
//
//		StatementRowType rowType = null;
//
//		if (strValue.charAt(0) == '$') {
//			var pair = stripDollarSignIfAny(strValue);
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
//				var pair = stripDollarSignIfAny(strs[strs.length - 1]);
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
//	}
//	
//	private Double parseDougble(String str) {
//		if(str.isEmpty()) {
//			return null;
//		}
//		
//		str = str.replaceAll(",", "");
//		
//		try {
//			return Double.parseDouble(str);
//		}
//		catch (NumberFormatException e) {
//			System.out.println(str + " is not a double.");
//			return null;
//		}
//	}
//
//	private Pair<Boolean, String> stripDollarSignIfAny(String str) {
//		
//		if(str.startsWith("-$") || str.startsWith("$")) {
//			return Pair.of(true, str.replace("$", ""));
//		}
//		
//		return Pair.of(false, str);
//	}

	private void trace(String msg) {
		if (debugFalg) {
			System.out.println(msg);
		}
	}
}
