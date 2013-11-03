package se.krats.explorelogs;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Combine the convenience of formatting messages with efficiency and the
 * possibility of using different logger categories in different classes.
 */
public class LogWrapper {

    private final Logger logger;

    public static LogWrapper forCaller() {
        try {
            StackTraceElement[] stack = new Throwable().getStackTrace();
            return new LogWrapper(stack[1].getClassName());
        } catch (Throwable t) {
            return new LogWrapper("unknown");
        }
    }

    public LogWrapper(String category) {
        logger = Logger.getLogger(category);
    }

    public void error(String format, Object... args) {
        if (logger.isEnabledFor(Level.ERROR)) {
            logger.error(String.format(format, args));
        }
    }

    public void error(Throwable throwable, String format, Object... args) {
        if (logger.isEnabledFor(Level.ERROR)) {
            logger.error(String.format(format, args), throwable);
        }
    }

    public void warn(String format, Object... args) {
        if (logger.isEnabledFor(Level.WARN)) {
            logger.warn(String.format(format, args));
        }
    }

    public void warn(Throwable throwable, String format, Object... args) {
        if (logger.isEnabledFor(Level.WARN)) {
            logger.warn(String.format(format, args), throwable);
        }
    }

    public void info(String format, Object... args) {
        if (logger.isEnabledFor(Level.INFO)) {
            logger.info(String.format(format, args));
        }
    }

    public void debug(String format, Object... args) {
        if (logger.isEnabledFor(Level.DEBUG)) {
            logger.debug(String.format(format, args));
        }
    }
}
