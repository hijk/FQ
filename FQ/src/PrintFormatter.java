import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class PrintFormatter extends SimpleFormatter {
	@Override
	public String format(LogRecord record) {
		if (record.getMessage().equals("\t")) {
			return record.getMessage();
		} else {
			return record.getMessage() + "\n";
		}
	}
}
