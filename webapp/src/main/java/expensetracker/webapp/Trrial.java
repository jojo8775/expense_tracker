package expensetracker.webapp;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;

import jj2000.j2k.roi.encoder.RectROIMaskGenerator;
import technology.tabula.HasText;
import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.PageIterator;
import technology.tabula.Rectangle;
import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;
import technology.tabula.TextElement;
import technology.tabula.detectors.NurminenDetectionAlgorithm;
import technology.tabula.detectors.SpreadsheetDetectionAlgorithm;
import technology.tabula.extractors.BasicExtractionAlgorithm;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

public class Trrial {
	public static void main(String[] args) {
		new Trrial().generateHTMLFromPDF("src/resources/test_file.pdf");
	}

	private void generateHTMLFromPDF(String fileName) {
//		System.out.println("about to convert the file." + filename);
//	    PDDocument pdf;
//		try {
//			pdf = PDDocument.load(new File(filename));
//			Writer output = new PrintWriter("src/output/pdf.html", "utf-8");
//		    new PDFDomTree().writeText(pdf, output);
//		    output.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

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
				System.out.println("table positions in page : " + ++count);
				List<Rectangle> tablesOnPage = detectionAlgorithm.detect(page);

				System.out.println("number of tables detected " + tablesOnPage.size());
				
				List<Table> tables = sea.extract(page);
				int tableCount = 0;
				for (Table table : tables) {
					System.out.println("Printing content of the table " + ++tableCount + " ...");
					for (List<RectangularTextContainer> rows : table.getRows()) {
						for (RectangularTextContainer row : rows) {
							System.out.println("------------ number of cells : " + row.getTextElements().size());
							System.out.println(row.getText());
							System.out.println("--------------------------------");
						}
					}
				}

//				for(Rectangle tablePos : tablesOnPage) {
//					System.out.println("table co-ordinates : " + tablePos.toString());
//					
//					List<Table> tables = sea.extract(page.getArea(tablePos));
//					int tableCount = 0;
//					for(Table table : tables) {
//						System.out.println("Printing content of the table " + ++tableCount + " ...");
//						for(List<RectangularTextContainer> rows : table.getRows()) {
//							for(RectangularTextContainer row : rows) {
//								System.out.println("------------ number of cells : " + row.getTextElements().size());
//								System.out.println(row.getText());
//								System.out.println("--------------------------------");
//							}
//						}
//					}
//				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
