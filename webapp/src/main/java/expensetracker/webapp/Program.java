package expensetracker.webapp;

public class Program {
	public static void main(String[] args) {
		new TableDataExtractor().readPdf(
				"src/resources/test_file_scotia.pdf", 
				new DataScrubberImpl(StatementSource.SCOTIA_CREDIT), 
				new SimpleDoubleExtractor());
	}
}
