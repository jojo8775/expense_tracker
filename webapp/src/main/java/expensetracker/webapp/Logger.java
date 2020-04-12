package expensetracker.webapp;

import java.util.ArrayList;
import java.util.List;

public class Logger {
    private final boolean debugFlag = false;

    private List<String> logLines = new ArrayList<>();

    public Logger() {

    }

    public void info(String message) {
        if (debugFlag) {
            System.out.println(message);
        }

        logLines.add(message);
    }

    public List<String> getLogs() {
        return logLines;
    }
}
