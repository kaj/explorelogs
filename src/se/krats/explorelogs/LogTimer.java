package se.krats.explorelogs;
import static java.util.Arrays.asList;

import java.util.List;

public class LogTimer {

    private static final int ITER = 10;
    private static final int ITER_DEBUG = 10;

    private static List<?> extraInfo = asList("Foo", LogTimer.class, Integer.MAX_VALUE);

    public static void main(String[] args) {
        String desc = ITER + " logged and " + ITER*ITER_DEBUG + " filtered messages.";
        if (isEnabled("jul", args))
            System.out.println("java.util: " + measureUtil() + " ms for " + desc);
        if (isEnabled("log4j", args))
            System.out.println("Log4j: " + measureLog4j() + " ms for " + desc);
        if (isEnabled("commons", args))
            System.out.println("Commons: " + measureCommons() + " ms for " + desc);
        if (isEnabled("slf4j", args))
            System.out.println("slf4j: " + measureSlf4j() + " ms for " + desc);
        if (isEnabled("play", args))
            System.out.println("Play: " + measurePlay() + " ms for " + desc);
        if (isEnabled("my", args))
            System.out.println("my wrap: " + measureWrap() + " ms for " + desc);
    }

    private static boolean isEnabled(String string, String[] args) {
        for (String arg : args) {
            if ("all".equalsIgnoreCase(arg) || string.equalsIgnoreCase(arg)) {
                return true;
            }
        }
        return false;
    }

    private static long measureUtil() {
        System.setProperty("java.util.logging.config.file", "conf/logging.properties");
        final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LogTimer.class.getName());
        long start = System.currentTimeMillis();
        for (int i = 0; i < ITER; ++i) {
            logger.log(java.util.logging.Level.INFO, "Message #{0} through java.util logger: {1}", new Object[]{i, extraInfo});
            for (int j = 0; j < ITER_DEBUG; ++j) {
                logger.log(java.util.logging.Level.FINER, "Message #{0},{1} through java.util logger: {2}", new Object[]{i, j, extraInfo});
            }
        }
        return System.currentTimeMillis() - start;
    }

    private static long measureSlf4j() {
        final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(LogTimer.class);
        long start = System.currentTimeMillis();
        for (int i = 0; i < ITER; ++i) {
            logger.info("Message #{} through slf4j logger: {}", i, extraInfo);
            for (int j = 0; j < ITER_DEBUG; ++j) {
                logger.debug("Message #{},{} through slf4j logger: {}", new Object[]{i, j, extraInfo});
            }
        }
        return System.currentTimeMillis() - start;
    }

    private static long measureWrap() {
        final LogWrapper logger = LogWrapper.forCaller();
        long start = System.currentTimeMillis();
        for (int i = 0; i < ITER; ++i) {
            logger.info("Message #%d through wrap logger: %s", i, extraInfo);
            for (int j = 0; j < ITER_DEBUG; ++j) {
                logger.debug("Message #%d,%d through wrapp logger: %s", i, j, extraInfo);
            }
        }
        return System.currentTimeMillis() - start;
    }

    private static long measureLog4j() {
        final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(LogTimer.class);
        long start = System.currentTimeMillis();
        for (int i = 0; i < ITER; ++i) {
            logger.info("Message #" + i + " through log4j logger: " + extraInfo);
            for (int j = 0; j < ITER_DEBUG; ++j) {
                logger.debug("Message #" + i + "," + j + " through log4j logger: " + extraInfo);
            }
        }
        return System.currentTimeMillis() - start;
    }

    private static long measureCommons() {
        final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(LogTimer.class);
        long start = System.currentTimeMillis();
        for (int i = 0; i < ITER; ++i) {
            logger.info("Message #" + i + " through commons logger: " + extraInfo);
            for (int j = 0; j < ITER_DEBUG; ++j) {
                logger.debug("Message #" + i + "," + j + " through commons logger: " + extraInfo);
            }
        }
        return System.currentTimeMillis() - start;
    }

    private static long measurePlay() {
        play.Logger.recordCaller = false;
        play.Logger.log4j = org.apache.log4j.Logger.getLogger("play");

        long start = System.currentTimeMillis();
        for (int i = 0; i < ITER; ++i) {
            play.Logger.info("Message #%d through play logger: %s", i, extraInfo);
            for (int j = 0; j < ITER_DEBUG; ++j) {
                play.Logger.debug("Message #%d,%d through play logger: %s", i, j, extraInfo);
            }
        }
        return System.currentTimeMillis() - start;
    }

}
