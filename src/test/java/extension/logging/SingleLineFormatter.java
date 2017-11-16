package extension.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.FieldPosition;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class SingleLineFormatter extends Formatter {
    Date dat = new Date();
    private static final String format = "{0,date} {0,time}";
    private MessageFormat formatter;
    private Object[] args = new Object[1];
    private String lineSeparator = "\n";

    public SingleLineFormatter() {
    }

    public synchronized String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();
        this.dat.setTime(record.getMillis());
        this.args[0] = this.dat;
        StringBuffer text = new StringBuffer();
        if (this.formatter == null) {
            this.formatter = new MessageFormat("{0,date} {0,time}");
        }

        this.formatter.format(this.args, text, (FieldPosition)null);
        sb.append(text);
        sb.append(" ");
        if (record.getSourceClassName() != null) {
            sb.append(record.getSourceClassName());
        } else {
            sb.append(record.getLoggerName());
        }

        if (record.getSourceMethodName() != null) {
            sb.append(" ");
            sb.append(record.getSourceMethodName());
        }

        sb.append(" - ");
        String message = this.formatMessage(record);
        sb.append(record.getLevel().getLocalizedName());
        sb.append(": ");
        sb.append(message);
        sb.append(this.lineSeparator);
        if (record.getThrown() != null) {
            try {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                record.getThrown().printStackTrace(pw);
                pw.close();
                sb.append(sw.toString());
            } catch (Exception var7) {
                ;
            }
        }

        return sb.toString();
    }
}

