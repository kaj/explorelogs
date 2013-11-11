package se.krats.explorelogs;


public class LogTimer {

    private static final int ITER = 100;
    private static final int ITER_DEBUG = 100;
    private static final int MAX_FIB = 1;

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
            logger.log(java.util.logging.Level.INFO, "Message #{0} through java.util logger: {1}", new Object[]{i, new LazyFib(i)});
            for (int j = 0; j < ITER_DEBUG; ++j) {
                logger.log(java.util.logging.Level.FINER, "Message #{0},{1} through java.util logger: {2}", new Object[]{i, j, new LazyFib(j)});
            }
        }
        return System.currentTimeMillis() - start;
    }

    private static long measureSlf4j() {
        final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(LogTimer.class);
        long start = System.currentTimeMillis();
        for (int i = 0; i < ITER; ++i) {
            logger.info("Message #{} through slf4j logger: {}", i, new LazyFib(i));
            for (int j = 0; j < ITER_DEBUG; ++j) {
                logger.debug("Message #{},{} through slf4j logger: {}", i, j, new LazyFib(j));
            }
        }
        return System.currentTimeMillis() - start;
    }

    private static long measureWrap() {
        final LogWrapper logger = LogWrapper.forCaller();
        long start = System.currentTimeMillis();
        for (int i = 0; i < ITER; ++i) {
            logger.info("Message #%d through wrap logger: %s", i, new LazyFib(i));
            for (int j = 0; j < ITER_DEBUG; ++j) {
                logger.debug("Message #%d,%d through wrapp logger: %s", i, j, new LazyFib(j));
            }
        }
        return System.currentTimeMillis() - start;
    }

    private static long measureLog4j() {
        final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(LogTimer.class);
        long start = System.currentTimeMillis();
        for (int i = 0; i < ITER; ++i) {
            logger.info("Message #" + i + " through log4j logger: " + new LazyFib(i));
            for (int j = 0; j < ITER_DEBUG; ++j) {
                logger.debug("Message #" + i + "," + j + " through log4j logger: " + new LazyFib(j));
            }
        }
        return System.currentTimeMillis() - start;
    }

    private static long measureCommons() {
        final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(LogTimer.class);
        long start = System.currentTimeMillis();
        for (int i = 0; i < ITER; ++i) {
            logger.info("Message #" + i + " through commons logger: " + new LazyFib(i));
            for (int j = 0; j < ITER_DEBUG; ++j) {
                logger.debug("Message #" + i + "," + j + " through commons logger: " + new LazyFib(j));
            }
        }
        return System.currentTimeMillis() - start;
    }

    private static long measurePlay() {
        play.Logger.recordCaller = false;
        play.Logger.log4j = org.apache.log4j.Logger.getLogger("play");

        long start = System.currentTimeMillis();
        for (int i = 0; i < ITER; ++i) {
            play.Logger.info("Message #%d through play logger: %s", i, new LazyFib(i));
            for (int j = 0; j < ITER_DEBUG; ++j) {
                play.Logger.debug("Message #%d,%d through play logger: %s", i, j, new LazyFib(j));
            }
        }
        return System.currentTimeMillis() - start;
    }

    /**
     * A simple object with a slow toString method.
     */
    private static class LazyFib {
        final int i;

        public LazyFib(int i) {
            this.i = i % MAX_FIB;
        }

        public String toString() {
            return "fib(" + i + ") = " + fib(i);
        }

        private final static int fib(int i) {
            if (i < 2) {
                return 1;
            } else {
                return fib(i-1) + fib(i-2);
            }
        }
    }
}
