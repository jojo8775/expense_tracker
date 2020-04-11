package expensetracker.webapp;

import java.util.List;

import com.google.gson.Gson;

public class Program {
	public static void main(String[] args) {
		new Program().execute();
	}

	public List<String> execute() {
		var logger = new Logger();

//	    new TableDataExtractorMultiline().readPdf(
//      "/test_file_scotia.pdf", 
//      new DataScrubberImpl(StatementSource.SCOTIA_CREDIT), 
//      new SimpleDoubleExtractor());

		new TableDataExtractor().readPdf("/test_file.pdf",
				new DataScrubberImpl(StatementSource.RBC_CREDIT, logger), 
				new DoubleExtractorWithDollarSign());

		var result = new Gson().toJson(logger.getLogs());
		System.out.println(result);

		return logger.getLogs();
	}
}
