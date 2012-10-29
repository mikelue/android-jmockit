package org.no_ip.mikelue.android.jmockit;

import android.util.Log;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.UsingMocksAndStubs;
import mockit.Verifications;
import mockit.integration.logging.Slf4jMocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Tests the correction of mapping of levels between these two logging
 * frameworks.<p>
 */
@UsingMocksAndStubs(Slf4jMocks.class)
public class MockLogToSlf4jTest {
	@Mocked
	private LoggerFactory mockLoggerFactory;
	@Mocked
	private Logger mockLogger;

	public MockLogToSlf4jTest() {}

	private final static String testMessage = "Test message for logging";
	private final static Throwable testException = new Exception("Test exception for logging");

	/**
	 * Tests the output of {@link Log#e}.<p>
	 */
	@Test
	public void e()
	{
		final String testTag = "UT-E";

		setupMockSlf4j(testTag);

		Log.e(testTag, testMessage);
		Log.e(testTag, testMessage, testException);

		new Verifications() {{
			mockLoggerFactory.getLogger(testTag);
			times = 2;

			mockLogger.error(anyString);
			times = 1;
			mockLogger.error(anyString, (Throwable)any);
			times = 1;
		}};
	}
	/**
	 * Tests the output of {@link Log#wtf}.<p>
	 */
	@Test
	public void wtf()
	{
		final String testTag = "UT-WTF";

		setupMockSlf4j(testTag);

		Log.wtf(testTag, testException);
		Log.wtf(testTag, testMessage);
		Log.wtf(testTag, testMessage, testException);

		new Verifications() {{
			mockLoggerFactory.getLogger(testTag);
			times = 3;

			mockLogger.error(anyString, anyString, anyString);
			times = 2;
			mockLogger.error(anyString, anyString, anyString, (Throwable)any);
			times = 1;
		}};
	}
	/**
	 * Tests the output of {@link Log#w}.<p>
	 */
	@Test
	public void w()
	{
		final String testTag = "UT-W";

		setupMockSlf4j(testTag);

		Log.w(testTag, testException);
		Log.w(testTag, testMessage);
		Log.w(testTag, testMessage, testException);

		new Verifications() {{
			mockLoggerFactory.getLogger(testTag);
			times = 3;

			mockLogger.warn(anyString);
			times = 2;
			mockLogger.warn(anyString, (Throwable)any);
			times = 1;
		}};
	}
	/**
	 * Tests the output of {@link Log#i}.<p>
	 */
	@Test
	public void i()
	{
		final String testTag = "UT-I";

		setupMockSlf4j(testTag);

		Log.i(testTag, testMessage);
		Log.i(testTag, testMessage, testException);

		new Verifications() {{
			mockLoggerFactory.getLogger(testTag);
			times = 2;

			mockLogger.info(anyString);
			times = 1;
			mockLogger.info(anyString, (Throwable)any);
			times = 1;
		}};
	}
	/**
	 * Tests the output of {@link Log#d}.<p>
	 */
	@Test
	public void d()
	{
		final String testTag = "UT-D";

		setupMockSlf4j(testTag);

		Log.d(testTag, testMessage);
		Log.d(testTag, testMessage, testException);

		new Verifications() {{
			mockLoggerFactory.getLogger(testTag);
			times = 2;

			mockLogger.debug(anyString);
			times = 1;
			mockLogger.debug(anyString, (Throwable)any);
			times = 1;
		}};
	}
	/**
	 * Tests the output of {@link Log#v}.<p>
	 */
	@Test
	public void v()
	{
		final String testTag = "UT-V";

		setupMockSlf4j(testTag);

		Log.v(testTag, testMessage);
		Log.v(testTag, testMessage, testException);

		new Verifications() {{
			mockLoggerFactory.getLogger(testTag);
			times = 2;

			mockLogger.trace(anyString);
			times = 1;
			mockLogger.trace(anyString, (Throwable)any);
			times = 1;
		}};
	}

	@BeforeClass
	private void setupMockLogging()
	{
		new MockLogToSlf4j();
	}
	private void setupMockSlf4j(final String testTag)
	{
		new NonStrictExpectations() {{
			mockLoggerFactory.getLogger(testTag);
			result = mockLogger;
		}};
	}
}
