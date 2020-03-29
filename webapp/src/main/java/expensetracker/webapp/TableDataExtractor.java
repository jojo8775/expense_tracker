package expensetracker.webapp;

import java.io.File;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

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

    private final boolean debugFalg = true;

    public static void main(String[] args) {
        new TableDataExtractor().read();
    }

    public void read() {
        readPdf("src/resources/test_file.pdf");
    }

    private void readPdf(String fileName) {
        Deque<String> deque = new LinkedList<>();
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
                            String rowStr = row.getText();
                            if (debugFalg) {
                                System.out.println("------------ number of cells : " + row.getTextElements().size());
                                System.out.println(rowStr);
                                System.out.println("--------------------------------");
//                                if (rowStr.isBlank()) {
//                                    System.out.println("found a blank row skiping ...");
//                                }
                            }

//                            Double val = tryGetDoubleValue(rowStr);
//                            if (val != null) {
//                                String prev = deque.isEmpty() ? "<Blank>" : deque.pollFirst();
//                                System.out.println(prev + " => " + val);
//                            } else {
//                                deque.offerFirst(rowStr);
//                            }
//
//                            if (deque.size() > 10) {
//                                deque.pollLast();
//                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Double tryGetDoubleValue(String strValue) {
        if (strValue == null || strValue.isBlank()) {
            return null;
        }

        if (strValue.charAt(0) == '$') {
            strValue = strValue.substring(1);
        }

        Double val = null;
        try {
            val = Double.parseDouble(strValue);
        } catch (NumberFormatException e) {

        }

        return val;
    }
}
