package expensetracker.webapp;

import java.io.IOException;
import java.util.ArrayList;
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
		var entries = readPdf("/test_file.pdf", new DoubleExtractorWithDollarSign());
		new DataScrubberImpl(StatementSource.RBC_CREDIT, new Logger()).getScrubbedEntries(entries);
	}

	public List<Pair<String, Double>> readPdf(String fileName, DoubleExtractor doubleExtractor) {
		LinkedList<String> list = new LinkedList<String>();
		var entries = new ArrayList<Pair<String, Double>>();
		try {
			PDDocument pdfDocument = PDDocument.load(getClass().getResourceAsStream(fileName));

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
									entries.add(Pair.of(rowStr, val.getRight()));
									
								} else {
									String prev = list.isEmpty() ? "<Blank>" : list.get(0);
									
									entries.add(Pair.of(prev, val.getRight()));
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
		
		return entries;
	}


	private void trace(String msg) {
		if (debugFalg) {
			System.out.println(msg);
		}
	}
}
