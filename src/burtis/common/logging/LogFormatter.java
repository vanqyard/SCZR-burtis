package burtis.common.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Console logging formatter.
 * 
 * The pattern is: &lt;LEVEL&gt; &lt;Source class&gt;: &lt;Message&gt;
 * 
 * @author Mikołaj Sowiński
 *
 */
public class LogFormatter extends Formatter
{
    @Override
    public String format(LogRecord record)
    {
        String resultString = "\n" + record.getLevel() + ": "
                + record.getMessage() + "\n";
        if (record.getThrown() != null)
        {
            StringWriter errors = new StringWriter();
            record.getThrown().printStackTrace(new PrintWriter(errors));
            resultString += errors.toString() + "\n";
        }
        return resultString;
    }
}
