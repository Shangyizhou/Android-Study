package com.shangyizhou.develop.log;

import android.os.Process;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.util.StatusPrinter;

public class SLog {
    public static void initialize(String logHome) {
        Log.e("DuLog", "[DuLog_initialize]" + logHome);
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.reset();
        //  日志分割的策略是： 按照每小时 和日志文件大小生成日志文件，
        //  每小时滚动生成日志文件， 在每小时时间段内，如果文件大小大于20M，则生成新的日志文件
        //  RollingFileAppender 滚动记录文件，先将日志记录到指定文件，当符合某个条件时，将日志记录到其他文件
        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender();
        rollingFileAppender.setName("FILE");
        rollingFileAppender.setContext(context);
        rollingFileAppender.setAppend(true);

//        rollingFileAppender.setFile(logHome + "/" + "distribution" + ".log.temp");

        TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new TimeBasedRollingPolicy();

        rollingPolicy.setFileNamePattern(logHome + "/%d{yyyyMMdd, aux}/%d{yyyyMMdd_HH}-%i.log.zip");
        rollingPolicy.setCleanHistoryOnStart(true);
        rollingPolicy.setMaxHistory(480);
        rollingPolicy.setTotalSizeCap(FileSize.valueOf("800MB"));

        SizeAndTimeBasedFNATP<ILoggingEvent> fnatp = new SizeAndTimeBasedFNATP();
        fnatp.setMaxFileSize(FileSize.valueOf("50mb"));
        rollingPolicy.setTimeBasedFileNamingAndTriggeringPolicy(fnatp);
        rollingPolicy.setParent(rollingFileAppender);
        rollingPolicy.setContext(context);
        rollingPolicy.start();

        rollingFileAppender.setRollingPolicy(rollingPolicy);

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setPattern(
                "%date{yyyy-MM-dd HH:mm:ss.SSS} %mdc{pid} %mdc{tid} %.-1level %logger{48}: "
                        + "[%mdc{uid}][%thread] %msg%n");
        encoder.setContext(context);
        encoder.start();
        rollingFileAppender.setEncoder(encoder);
        rollingFileAppender.start();

        AsyncAppender asyncAppender = new AsyncAppender() {
            protected boolean isDiscardable(ILoggingEvent event) {
                return event != null && "main".equals(event.getThreadName()) ? true
                        : super.isDiscardable(event);
            }
        };
        asyncAppender.setContext(context);
        asyncAppender.setName("ASYNC");
        asyncAppender.setQueueSize(1024);
        asyncAppender.setDiscardingThreshold(25);
        asyncAppender.addAppender(rollingFileAppender);
        asyncAppender.start();
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("ROOT");
        root.setLevel(Level.INFO);
        root.addAppender(asyncAppender);
        prepareCustomConverter();
        StatusPrinter.print(context);
    }

    private static final boolean ENABLE_LOG = true; // 打印日志的开关
    //    private static final boolean ENABLE_LOG = BuildConfigHelper.getCurrentHelper().isDebug(); // 打印日志的开关
    public static final int LARGE_STRING_LIMIT = 1000; // logcat每条日志的最大字符数

    public static int v(String tag, String msg) {
        return println(Log.VERBOSE, tag, msg);
    }

    public static int v(String tag, String msg, Throwable tr) {
        return println(Log.VERBOSE, tag, msg + '\n' + Log.getStackTraceString(tr));
    }

    public static int d(String tag, String msg) {
        return println(Log.DEBUG, tag, msg);
    }

    public static int d(String tag, String msg, Throwable tr) {
        return println(Log.DEBUG, tag, msg + '\n' + Log.getStackTraceString(tr));
    }

    public static int i(String tag, String msg) {
        return println(Log.INFO, tag, msg);
    }

    public static int i(String tag, String msg, Throwable tr) {
        return println(Log.INFO, tag, msg + '\n' + Log.getStackTraceString(tr));
    }

    public static int w(String tag, String msg) {
        return println(Log.WARN, tag, msg);
    }

    public static int w(String tag, String msg, Throwable tr) {
        return println(Log.WARN, tag, msg + '\n' + Log.getStackTraceString(tr));
    }

    public static int e(String tag, String msg) {
        return println(Log.ERROR, tag, msg);
    }

    public static int e(String tag, String msg, Throwable tr) {
        return println(Log.ERROR, tag, msg + '\n' + Log.getStackTraceString(tr));
    }

    private static int println(int priority, String tag, String msg) {
        if ((ENABLE_LOG || Log.isLoggable(tag, priority))
                && (tag != null && msg != null)) {
            final Logger logger = LoggerFactory.getLogger(tag);
            prepareCustomConverter();
            switch (priority) {
                case Log.VERBOSE:
                    if (logger.isTraceEnabled()) {
                        logger.trace(msg);
                    }
                    // Not in use increase priority, use "adb shell setprop log.tag.<LOG_TAG> <LEVEL>" instead
                    //                    priority = Log.INFO;
                    break;
                case Log.DEBUG:
                    if (logger.isDebugEnabled()) {
                        logger.debug(msg);
                    }
                    // Not in use increase priority, use "adb shell setprop log.tag.<LOG_TAG> <LEVEL>" instead
                    //                    priority = Log.INFO;
                    break;
                case Log.INFO:
                    if (logger.isInfoEnabled()) {
                        logger.info(msg);
                    }
                    break;
                case Log.WARN:
                    if (logger.isWarnEnabled()) {
                        logger.warn(msg);
                    }
                    break;
                case Log.ERROR:
                    if (logger.isErrorEnabled()) {
                        logger.error(msg);
                    }
                    break;
                case Log.ASSERT:
                    logger.error(msg); // no same level in org.slf4j.Logger
                    break;
                default:
                    throw new IllegalArgumentException("Unknown log priority!");
            }
            msg = "[" + Thread.currentThread().getName() + "]" + msg;
            return printlnLargeString(priority, tag, msg);
        }
        return 0;
    }

    // There is a fixed size buffer in logcat for binary logs (/dev/log/events) and this limit is 1024 bytes.
    // For the non-binary logs there is also a limit:
    // #define LOGGER_ENTRY_MAX_LEN        (4*1024)
    // #define LOGGER_ENTRY_MAX_PAYLOAD (LOGGER_ENTRY_MAX_LEN - sizeof(struct logger_entry))
    private static int printlnLargeString(int priority, String tag, String msg) {
        if (msg.length() > LARGE_STRING_LIMIT) {
            final int written = Log.println(priority, tag, msg.substring(0, LARGE_STRING_LIMIT));
            return written + printlnLargeString(priority, tag, msg.substring(LARGE_STRING_LIMIT));
        } else {
            return Log.println(priority, tag, msg);
        }
    }

    private static void prepareCustomConverter() {
        MDC.put("pid", String.valueOf(Process.myPid()));
        MDC.put("tid", String.valueOf(Process.myTid()));
        MDC.put("uid", String.valueOf(Process.myUid()));
    }

    /**
     * 原e级别日志输出, 没有消息主体, 直接打印Throwable堆栈
     */
    public static int error(@NonNull String tag, @Nullable Throwable e) {
        return println(Log.ERROR, tag, Log.getStackTraceString(e));
    }

    /**
     * 为便于声明式编程和避免字符串拼接, 使用参数格式化日志记录方式作为封装, 此处使用String.format实现<br>
     * Example: <br>
     * DuLog.error("Tag", (Throwable)null, "%s -> %d", "age", 18);<br>
     * // will print: age -> 18
     */
    public static int error(@NonNull String tag, @Nullable Throwable e, @Nullable String msg,
                            @Nullable Object... args) {
        if (msg == null) {
            return 0;
        }
        return println(Log.ERROR, tag, String.format(msg, args) + '\n' + Log.getStackTraceString(e));
    }

    /**
     * 为便于声明式编程和避免字符串拼接, 使用参数格式化日志记录方式作为封装, 此处使用String.format实现<br>
     * Example: <br>
     * DuLog.warn("Tag", "%s -> %d", "age", 18);<br>
     * // will print: age -> 18
     */
    public static int warn(@NonNull String tag, @Nullable String msg, @Nullable Object... args) {
        if (msg == null) {
            return 0;
        }
        return println(Log.WARN, tag, String.format(msg, args));
    }

    /**
     * 为便于声明式编程和避免字符串拼接, 使用参数格式化日志记录方式作为封装, 此处使用String.format实现<br>
     * Example: <br>
     * DuLog.info("Tag", "%s -> %d", "age", 18);<br>
     * // will print: age -> 18
     */
    public static int info(@NonNull String tag, @Nullable String msg, @Nullable Object... args) {
        if (msg == null) {
            return 0;
        }
        return println(Log.INFO, tag, String.format(msg, args));
    }
}
