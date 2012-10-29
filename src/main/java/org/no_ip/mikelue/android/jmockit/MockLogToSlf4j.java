package org.no_ip.mikelue.android.jmockit;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.util.Log;

import mockit.Mock;
import mockit.MockUp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bridges the {@link Log} on Android to corresponding functions of {@link Logger} on SLF4j.<p>
 *
 * The tag defined in Android's {@link Log} becomes the name of SLF4j's logger.
 *
 * The mappings on levels of logging:
 * <ol>
 * 	<li>Android({@link Log#ERROR} and {@link Log#ASSERT}) maps to SLF4j({@link Logger#error ERROR})</li>
 * 	<li>Android({@link Log#WARN}) maps to SLF4j({@link Logger#warn ERROR})</li>
 * 	<li>Android({@link Log#INFO}) maps to SLF4j({@link Logger#info INFO})</li>
 * 	<li>Android({@link Log#DEBUG}) maps to SLF4j({@link Logger#debug DEBUG})</li>
 * 	<li>Android({@link Log#VERBOSE}) maps to SLF4j({@link Logger#trace TRACE})</li>
 * </ol>
 *
 * In addition, the message outputted at {@link Log#ASSERT ASSERT} level would
 * be preffixed with "[Terrible Failure]".<p>
 *
 * Example in TestNG:
 * <pre><code>
 * {@code @BeforeClass}
 * private void setupMockLog()
 * {
 * 		new MockLogToSlf4j();
 * }
 * </code></pre>
 */
public class MockLogToSlf4j extends MockUp<Log> {
	private final static Logger logger = LoggerFactory.getLogger(MockLogToSlf4j.class);
	private final static String ASSERT_PREFIX = "[Terrible Failure]";
	private final static String PATTERN_UNKNOWN_ANDROID_LEVEL = "<<Unknown Android Log Level:[{}]>>";

	public MockLogToSlf4j()
	{
		logger.trace("[jmockit] Mock android.util.Log");
	}

	@Mock
	public boolean isLoggable(String tag, int level)
	{
		Logger logger = LoggerFactory.getLogger(tag);

		switch (level) {
			case Log.ASSERT:
			case Log.ERROR:
				return logger.isErrorEnabled();
			case Log.WARN:
				return logger.isWarnEnabled();
			case Log.INFO:
				return logger.isInfoEnabled();
			case Log.DEBUG:
				return logger.isDebugEnabled();
			case Log.VERBOSE:
				return logger.isTraceEnabled();
		}

		logger.warn("Unknown Android Level[{}] for isLoggable(\"{}\", {})", tag, level);
		return true;
	}

	@Mock
	public int println(int priority, String tag, String msg)
	{
		Logger logger = LoggerFactory.getLogger(tag);

		switch (priority) {
			case Log.ASSERT:
				logger.error("{} {}", ASSERT_PREFIX, msg);
				break;
			case Log.ERROR:
				logger.error(msg);
				break;
			case Log.WARN:
				logger.warn(msg);
				break;
			case Log.INFO:
				logger.info(msg);
				break;
			case Log.DEBUG:
				logger.debug(msg);
				break;
			case Log.VERBOSE:
				logger.trace(msg);
				break;
			default:
				logger.trace(PATTERN_UNKNOWN_ANDROID_LEVEL + " {}", priority, msg);
				break;
		}

		return priority;
	}

	@Mock
	public String getStackTraceString(Throwable tr)
	{
		StringWriter stringOutput = new StringWriter();
		tr.printStackTrace(new PrintWriter(stringOutput));
		return stringOutput.toString();
	}

	@Mock
	public int e(String tag, String msg)
	{
		return println(Log.ERROR, tag, msg);
	}
	@Mock
	public int e(String tag, String msg, Throwable tr)
	{
		return outputLog(Log.ERROR, tag, msg, tr);
	}
	@Mock
	public int w(String tag, String msg)
	{
		return println(Log.WARN, tag, msg);
	}
	@Mock
	public int w(String tag, String msg, Throwable tr)
	{
		return outputLog(Log.WARN, tag, msg, tr);
	}
	@Mock
	public int w(String tag, Throwable tr)
	{
		return w(tag, getStackTraceString(tr));
	}
	@Mock
	public int wtf(String tag, String msg)
	{
		return println(Log.ASSERT, tag, msg);
	}
	@Mock
	public int wtf(String tag, String msg, Throwable tr)
	{
		return outputLog(Log.ASSERT, tag, msg, tr);
	}
	@Mock
	public int wtf(String tag, Throwable tr)
	{
		return wtf(tag, getStackTraceString(tr));
	}
	@Mock
	public int i(String tag, String msg)
	{
		return println(Log.INFO, tag, msg);
	}
	@Mock
	public int i(String tag, String msg, Throwable tr)
	{
		return outputLog(Log.INFO, tag, msg, tr);
	}
	@Mock
	public int d(String tag, String msg)
	{
		return println(Log.DEBUG, tag, msg);
	}
	@Mock
	public int d(String tag, String msg, Throwable tr)
	{
		return outputLog(Log.DEBUG, tag, msg, tr);
	}
	@Mock
	public int v(String tag, String msg)
	{
		return println(Log.VERBOSE, tag, msg);
	}
	@Mock
	public int v(String tag, String msg, Throwable tr)
	{
		return outputLog(Log.VERBOSE, tag, msg, tr);
	}

	private int outputLog(int priority, String tag, String msg, Throwable tr)
	{
		Logger logger = LoggerFactory.getLogger(tag);

		switch (priority) {
			case Log.ASSERT:
				logger.error("{} {} {}", ASSERT_PREFIX, msg, tr);
				break;
			case Log.ERROR:
				logger.error(msg, tr);
				break;
			case Log.WARN:
				logger.warn(msg, tr);
				break;
			case Log.INFO:
				logger.info(msg, tr);
				break;
			case Log.DEBUG:
				logger.debug(msg, tr);
				break;
			case Log.VERBOSE:
				logger.trace(msg, tr);
				break;
			default:
				logger.error(PATTERN_UNKNOWN_ANDROID_LEVEL + " {} {}", priority, msg, tr);
				break;
		}

		return priority;
	}
}
