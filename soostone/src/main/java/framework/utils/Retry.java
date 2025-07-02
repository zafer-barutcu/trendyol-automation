package framework.utils;

import framework.context.BaseTextContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for executing retry logic on flaky operations.
 * <p>
 * This class provides a simple mechanism to retry a given {@link Runnable}
 * in case of exceptions or assertion failures.
 * It is especially useful for handling intermittent UI or backend failures in tests.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * Retry.run(() -> {
 * someUnstableOperation();
 * }, 3, 1000); // 3 attempts, 1000 m.second sleep between
 */
public final class Retry {

    private Retry(){}

    private static final Logger log = LoggerFactory.getLogger(Retry.class);

        public static void run(Runnable runnable, int maxAttempts, long sleepMillis) {
            int attempts = 0;
            while (attempts < maxAttempts) {
                try {
                    runnable.run();
                    return;
                } catch (Exception | AssertionError e) {
                    log.warn("Caught exception on attempt: {}", e.toString());
                    attempts++;
                    if (attempts >= maxAttempts) {
                        log.warn("Caught exception on attempt: {}", e.toString());
                        throw e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
                    }
                    WrappedSleep.sleep(sleepMillis);
                }
            }
        }
    }


