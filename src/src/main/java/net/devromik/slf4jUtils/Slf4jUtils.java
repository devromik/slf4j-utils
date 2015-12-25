package net.devromik.slf4jUtils;

import org.slf4j.*;
import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.ArrayUtils.*;
import static org.apache.commons.lang3.StringUtils.countMatches;
import static org.apache.commons.lang3.exception.ExceptionUtils.*;

/**
 * @author Shulnyaev Roman
 */
public final class Slf4jUtils {

    public static final String DEFAULT_EXCEPTION_ACCOMPANYING_MESSAGE = "Exception";

    // ****************************** //

    /**
     * @throws NullPointerException when logger == null or exception == null.
     */
    public static void logException(Logger logger, Exception exception) {
        logException(logger, exception, DEFAULT_EXCEPTION_ACCOMPANYING_MESSAGE);
    }

    /**
     * Example:
     *
     * {@code
     * logException(
     *     logger,
     *     new Exception("Error description"),
     *     "Accompanying message arguments are: {}, {}, and {}",
     *     "arg_1", "arg_2", "arg_3");
     * }
     *
     * gives the following output:
     *
     *<pre>
     *[2015.09.23 20:08:34.769] [ERROR] [main] [net.devromik.slf4jUtils.Slf4jUtils.logException() at line 53]		Accompanying message arguments are: arg_1, arg_2, and arg_3
     *java.lang.Exception: Error description
     *    at net.devromik.someProject.SomeClass.someMethod(SomeClass.java:42)
     *    ...
     *</pre>
     *
     * You can see the place where the exception was thrown next to the first "at" in the exception stack trace.
     *
     * @throws NullPointerException when:
     *             logger == null or
     *             exception == null or
     *             accompanyingMessagePattern == null or
     *             accompanyingMessagePatternArgs == null.
     */
    public static void logException(
        Logger logger,
        Exception exception,
        String accompanyingMessagePattern,
        Object... accompanyingMessagePatternArgs) {

        checkNotNull(logger);
        checkNotNull(exception);
        checkNotNull(accompanyingMessagePattern);
        checkNotNull(accompanyingMessagePatternArgs);

        // Logging on the ERROR level should be a rare operation.
        // So we can permit ourselves the following useful check.
        checkArgument(countMatches(accompanyingMessagePattern, "{}") == accompanyingMessagePatternArgs.length);

        logger.error(
            accompanyingMessagePattern + "\n{}",
            add(accompanyingMessagePatternArgs, getStackTrace(exception)));
    }
}