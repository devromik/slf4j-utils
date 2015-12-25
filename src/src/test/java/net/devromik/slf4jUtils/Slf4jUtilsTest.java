package net.devromik.slf4jUtils;

import org.junit.Test;
import org.slf4j.*;
import static net.devromik.slf4jUtils.Slf4jUtils.logException;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.mockito.Mockito.*;

/**
 * @author Shulnyaev Roman
 */
public class Slf4jUtilsTest {

    @Test
    public void canLogExceptionAddingParametrizedAccompanyingMessage() throws Exception {
        Logger logger = mock(Logger.class);

        Exception exception = new Exception("Error description");
        logException(logger, exception);
        verify(logger).error("Exception\n{}", new Object[] {getStackTrace(exception)});

        logException(logger, exception, "Accompanying message");
        verify(logger).error("Accompanying message\n{}", new Object[] {getStackTrace(exception)});

        logException(
            logger,
            exception,
            "Accompanying message arguments are: {}, {}, and {}",
            "arg_1", "arg_2", "arg_3");

        verify(logger).error(
            "Accompanying message arguments are: {}, {}, and {}\n{}",
            "arg_1", "arg_2", "arg_3", getStackTrace(exception));
    }

    @Test(expected = NullPointerException.class)
    public void loggerShouldBeNotNull() throws Exception {
        logException(null, new Exception());
    }

    @Test(expected = NullPointerException.class)
    public void exceptionShouldBeNotNull() throws Exception {
        logException(mock(Logger.class), null);
    }

    @Test(expected = NullPointerException.class)
    public void accompanyingMessagePatternShouldBeNotNull() throws Exception {
        logException(mock(Logger.class), new Exception(), null);
    }

    @Test(expected = NullPointerException.class)
    public void accompanyingMessagePatternArgsShouldBeNotNull() throws Exception {
        logException(mock(Logger.class), new Exception(), "Accompanying message", (Object[])null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void accompanyingMessagePatternParamCountShouldBeEqualToArgCount() throws Exception {
        logException(
            mock(Logger.class),
            new Exception(),
            "Accompanying message arguments are: {}, {} and {}",
            "arg_1", "arg_2");
    }
}