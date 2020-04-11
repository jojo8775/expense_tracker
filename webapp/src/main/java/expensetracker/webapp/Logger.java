package expensetracker.webapp;

import java.util.ArrayList;
import java.util.List;

public class Logger {
	private List<String> logLines = new ArrayList<>();
	public Logger() {
		
	}
	
	public void info(String message) {
		System.out.println(message);
		logLines.add(message);
	}
	
	public List<String> getLogs(){
		return logLines;
	}
}
