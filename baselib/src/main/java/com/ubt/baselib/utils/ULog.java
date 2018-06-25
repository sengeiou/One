package com.ubt.baselib.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ubt.baselib.BuildConfig;
import com.vise.log.ViseLog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertTrue;

/**
 * 日志采用的使用是异步日志，必须先初始化才能使用
 */
public class ULog {

    private static final String TAG = "ULog";
    private static final long FLUSH_TIME_IN_MILLIS = 1000; // Flush log every second.
    public static final int MAX_LENGTH = 1024 * 3;
    public static final int LIMIT_FILE_SIZE = 20 * 1024 * 1024;// 20MB

    public static final String TYPE_VERBOSE = "V";
    public static final String TYPE_DEBUG = "D";
    public static final String TYPE_INFO = "I";
    public static final String TYPE_WARNING = "W";
    public static final String TYPE_ERROR = "E";
    public static final String TYPE_ASSERT = "A";

    private static Context mContext = null;
    private static Thread mLogThread = null;

    private static File mLogFile = null;
    private static String mLogFilePath = null;
    private static String mLogDirPath = null;
    private static List<String> mAllLogFilePathList = new LinkedList<String>();
    private static FileWriter mLogWriter = null;

    private static SimpleDateFormat mTimeFormatter = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
    private static SimpleDateFormat mLogFileNameFormatter = new SimpleDateFormat("yy.MM.dd.HH");

    private static String mProcessName = "";
    private static int mProcessId = 0;
    private static String mBuildVersion = "";
    private static boolean mColor = BuildConfig.DEBUG;
    private static boolean isDebug = false;
    private static LinkedBlockingDeque<LogInfo> mLogQueue = new LinkedBlockingDeque<LogInfo>();

    private static long mNextLogHourInMillis = 0;
    private static long mLastFlushTimeInMillis = 0;
    private static long mOutOfDateDay = 6;

    private static ThreadLocal<StringBuilder> stringBuilderThreadLocal = new ThreadLocal<StringBuilder>() {
        @Override
        protected StringBuilder initialValue() {
            return new StringBuilder();
        }
    };

    private ULog() {
    }

    public static void setOutOfDateDay(long mOutOfDateDay) {
        ULog.mOutOfDateDay = mOutOfDateDay;
    }

    public static String getLogDirPath() {
        return mLogDirPath;
    }

    public static void init(Context context, boolean debug) {

        assertTrue(context != null);
        mContext = context;
        isDebug = debug;
        // Get process name by enumerate pid.
        mProcessId = Process.myPid();

        // Create the director if it isn't exist.
        if (mLogDirPath == null) {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {
                String SDCardPath = Environment
                        .getExternalStorageDirectory().getPath();
                String file_path = SDCardPath + "/ubt_enalpha1e/log/";
                mLogDirPath = file_path;
            } else {
                // 如果没有外部存储器，则使用data/data目录下的位置保存日志
                mLogDirPath = mContext.getFilesDir().getAbsolutePath() + "/Log";
            }
        }
        // 清理一下log目录
        cleanEmptyAndOutOfDateFiles(mLogDirPath);

        ActivityManager actMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appList = actMgr.getRunningAppProcesses();
        if (null != appList) {
            for (ActivityManager.RunningAppProcessInfo info : appList) {
                if (info.pid == mProcessId) {
                    mProcessName = info.processName;
                    break;
                }
            }
        }

        // Create working thread
        Runnable logWorkingThread = new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                boolean toFlush = false;
                while (true) {

                    long currentTimeMillis = System.currentTimeMillis();

                    // Definitely execute initLogFile() at first time.
                    if (currentTimeMillis > mNextLogHourInMillis) {
                        initLogFile();
                        mNextLogHourInMillis = getNextHourMillion();
                    }

                    LogInfo logMessage = null;
                    try {
                        logMessage = mLogQueue.pollFirst(FLUSH_TIME_IN_MILLIS, TimeUnit.MILLISECONDS);

                        if (logMessage != null) {
                            mLogWriter.write(constructLogMsg(logMessage));
                            toFlush = true;
                        }

                        if (toFlush && currentTimeMillis - mLastFlushTimeInMillis > FLUSH_TIME_IN_MILLIS) {
                            flush();
                            mLastFlushTimeInMillis = currentTimeMillis;
                            toFlush = false;
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.e(TAG, "logWorkingThread", e);
                        assertTrue(false);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "logWorkingThread", e);
                        assertTrue(false);
                    }
                }
            }
        };

        initLogFile();
        mLogThread = new Thread(logWorkingThread, "ULogThread");
        mLogThread.start();
    }

    /**
     * 敏感信息, 屏蔽10位
     */
    public static String security(@Nullable String msg) {
        return security(msg, 10);
    }

    @Nullable
    public static String security(@Nullable String msg, int bits) {
        if (msg == null) {
            return null;
        }
        int length = msg.length();
        if (length <= bits) {
            return generateRepeatCharArray('*', length);
        }
        if (bits < length / 2) {
            bits = length / 2;
        }
        int index1 = (length - bits) / 2;
        int index2 = index1 + bits;
        String start = msg.substring(0, index1);
        String middle = generateRepeatCharArray('*', bits);
        String end = msg.substring(index2, length);
        return start + middle + end;
    }

    /**
     * 0,1,2,3,4,8,12
     */
    public static String generateRepeatCharArray(char c, int count) {
        switch (count) {
            case 0:
                return "";
            case 1:
                return "*";
            case 2:
                return "**";
            case 3:
                return "***";
            case 4:
                return "****";
            default:
                return "****(" + count + ")****";
        }
        /*
        if(count > 50) {
            count = 50;
        }
        StringBuilder builder = new StringBuilder();
        for(int i=0; i<count; ++i) {
            builder.append(c);
        }
        return builder.toString();
        */
    }

    public static void d(String tag, String msg) {
        Log.d(tag, msg);
        appendLog(TYPE_DEBUG, tag, msg, null);
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (tr != null) {
            Log.d(tag, msg, tr);
        } else {
            Log.d(tag, msg);
        }

        appendLog(TYPE_DEBUG, tag, msg, tr);
    }

    public static void d(String tag, String format, Object... args) {
        int bufferSize = format.length() + (args == null ? 0 : args.length * 10);
        Formatter f = new Formatter(new StringBuilder(bufferSize));
        String msg = f.format(format, args).toString();
        d(tag, msg);
    }

    public static void e(String tag, String msg) {
        e(tag, msg, null);
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (tr != null) {
            Log.e(tag, msg, tr);
        } else {
            Log.e(tag, msg);
        }

        appendLog(TYPE_ERROR, tag, msg, tr);
    }

    public static void i(String tag, String msg) {
        Log.i(tag, msg);
        appendLog(TYPE_INFO, tag, msg, null);
    }

    public static void i(String tag, String format, Object... args) {
        int bufferSize = format.length() + (args == null ? 0 : args.length * 10);
        Formatter f = new Formatter(new StringBuilder(bufferSize));
        String msg = f.format(format, args).toString();
        i(tag, msg);
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (tr != null) {
            Log.i(tag, msg, tr);
        } else {
            Log.i(tag, msg);
        }

        appendLog(TYPE_INFO, tag, msg, tr);
    }

    public static void v(String tag, String msg) {
        if (isDebug) {
            v(tag, msg, null);
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (isDebug) {
            if (tr != null) {
                Log.v(tag, msg, tr);
            } else {
                Log.v(tag, msg);
            }

            appendLog(TYPE_VERBOSE, tag, msg, tr);
        }
    }

    public static void w(String tag, Throwable tr) {
        w(tag, "", tr);
    }

    public static void w(String tag, String msg) {
        Log.w(tag, msg);

        appendLog(TYPE_WARNING, tag, msg, null);
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (tr != null) {
            Log.w(tag, msg, tr);
        } else {
            Log.w(tag, msg);
        }

        appendLog(TYPE_WARNING, tag, msg, tr);
    }

    public static void w(String tag, String format, Object... args) {
        int bufferSize = format.length() + (args == null ? 0 : args.length * 10);
        Formatter f = new Formatter(new StringBuilder(bufferSize));
        String msg = f.format(format, args).toString();
        w(tag, msg);
    }

    public static void a(String tag, String msg) {
        a(tag, msg, null);
    }

    public static void a(String tag, String msg, Throwable tr) {
        if (tr != null) {
            Log.e(tag, "<ASSERT>" + msg, tr);
        } else {
            Log.e(tag, "<ASSERT>" + msg);
        }

        appendLog(TYPE_ASSERT, tag, msg, tr);

//        // 进行断点
//        if (isDebug) {
//            AssertUtils.fail(tag + msg);
//        }
    }

    public static void flush() {
        if (mLogWriter == null) {
            return;
        }

        try {
            mLogWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();

            Log.e(TAG, "flush", e);
            assertTrue(false);
        }
    }

    public static void flushRemainLogs() {

        // 1.循环等待队列中所有日志写到磁盘上。
        while (true) {
            if (mLogQueue.size() == 0) {
                break;
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();

                Log.e(TAG, "flushRemainLogs", e);
                assertTrue(false);
            }
        }

        // 2.flush一次
        flush();
    }

    public static String getCurrentLogFilePath() {
        return mLogFilePath;
    }

    public static String getLogFilePath(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        return mLogFileNameFormatter.format(calendar.getTime());
    }

    public static void setbuildVersion(String buildVersion) {
        mBuildVersion = buildVersion;
    }

    public static List<String> getAllLogFilePathList() {
        return mAllLogFilePathList;
    }

//    public static void updateUserColor(String currentUserUIN) {
//
//        String onlineValue = StatConfig.getCustomProperty("logColorUIN", "");
//        String[] uidList = onlineValue.split(";");
//
//        for (String colorUid :uidList){
//            if (colorUid.equalsIgnoreCase(currentUserUIN)) {
//                setColor(true);
//                return;
//            }
//        }
//
//        setColor(false);
//    }


    public static boolean isColor() {
        return mColor;
    }

    public static void setColor(boolean color) {
        mColor = color;
    }

    public static String getTimeString(long timeMilis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMilis);

        return mTimeFormatter.format(calendar.getTime());
    }

    public static void closeLogFile() {
        try {
            mLogWriter.close();
        } catch (IOException e) {
            e.printStackTrace();

            Log.e(TAG, "closeLogFile", e);
            assertTrue(false);

        } finally {
            mLogWriter = null;
        }
    }

    /**
     * 对于长消息的内容，通过增加换行来保证显示完整
     */
    public static void printEnterForLongMsg(String tag, String msg) {

        if (msg.length() < MAX_LENGTH) {
            d(tag, msg);
            return;
        }

        d(tag, "begin");
        int length = msg.length();
        for (int i = 0; i < length; ) {
            int end = i + MAX_LENGTH;
            if (end > length) {
                end = length;
            }
            d(tag, msg.substring(i, end));
            i = end;
        }
        d(tag, "end");
    }

    private static String getLogFileName() {
        long currentMillis = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentMillis);

        return mLogFileNameFormatter.format(calendar.getTime());
    }

    private static long getNextHourMillion() {
        long currentMillis = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentMillis);

        calendar.add(Calendar.HOUR, 1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTimeInMillis();
    }

    private static void initLogFile() {

        File logDir = new File(mLogDirPath);
        // Create the log file . The file name is the current time.
        mLogFilePath = mLogDirPath + getCurProcessName(mContext) + "_" + getLogFileName() + ".log";
        mAllLogFilePathList.add(0, mLogFilePath);

        boolean createFileSucceed = createLogFile(logDir);
        if (!createFileSucceed) {
            ULog.e(TAG, "Create log file error in " + mLogFilePath);

            // 如果没有外部存储器，上创建目录失败。（如vivo x5f上将手机改为usb存储）
            // 则使用data/data目录下的位置保存日志
            mLogDirPath = mContext.getFilesDir().getAbsolutePath() + "/Log";

            logDir = new File(mLogDirPath);
            // Create the log file . The file name is the current time.
            mLogFilePath = mLogDirPath + getCurProcessName(mContext) + "_" + getLogFileName() + ".log";
            mAllLogFilePathList.add(0, mLogFilePath);

            ULog.e(TAG, "Try to create log file again in " + mLogFilePath);
            createLogFile(logDir);
        }

    }

    private static boolean createLogFile(File logDir) {
        mLogFile = new File(mLogFilePath);
        ViseLog.d("创建日志文件==="+mLogFilePath);
        try {
            if (!mLogFile.exists()) {

                if (!logDir.exists()) {
                    logDir.mkdirs();
                }

                boolean bRetCode = mLogFile.createNewFile();
                ViseLog.d("创建日志文件=bRetCode=="+bRetCode);
                assertTrue(bRetCode);
            }

            // Flush log to previous log file(if exist) and than reopen a new
            writeAppVersion();
            flush();

            if (mLogWriter != null) {
                mLogWriter.close();
            }

            mLogWriter = new FileWriter(mLogFile, true);
        } catch (IOException e) {
            e.printStackTrace();

            // 如果日志初始化失败了，请务必段下来看看这里是什么错误
            Log.e(TAG, "initLogFile", e);
            assertTrue(false);
            return false;
        }

        return true;
    }

    private static void cleanEmptyAndOutOfDateFiles(String logDirPath) {

        File logDir = new File(logDirPath);
        long currentDay = System.currentTimeMillis() / (1000 * 60 * 60 * 24);

        File[] files = logDir.listFiles();
        if (files == null) {
            return;
        }

        TreeMap<Long, File> fileTreeMap = new TreeMap<Long, File>();

        // 清除七天前的日志
        for (File file : files) {
            long lastModifiedDay = file.lastModified() / (1000 * 60 * 60 * 24);
            if (currentDay - lastModifiedDay > mOutOfDateDay) {
                file.delete();
                continue;
            }

            // 将文件以修改时间为key放入到TreeMap中
            // 根据修改时间逆序排序，最新修改的会在最前面
            fileTreeMap.put(new Long(-file.lastModified()), file);
        }

        Set<Long> set = fileTreeMap.keySet();
        Iterator<Long> it = set.iterator();
        long totalSize = 0;
        while (it.hasNext()) {
            Object key = it.next();
            Object objValue = fileTreeMap.get(key);

            File file = (File) objValue;

            // 如果总大小超出上线，则删除
            // 最新一个日志永远保留，不删除。
            if (totalSize > LIMIT_FILE_SIZE) {
                file.delete();
            }

            totalSize += file.length();
        }
    }


    private static String getCurProcessName(Context context) {

        String procName = "";
        int pid = Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                procName = appProcess.processName;
            }
        }
        procName = procName.replace(':', '.');

        return procName;
    }

    private static void writeAppVersion() {
        if (null == mLogWriter || "".equals(mBuildVersion)) {
            return;
        }

        try {
            mLogWriter.write(getTimeString(System.currentTimeMillis()) + "|" + mProcessName + "|D|" + "|Version: " + mBuildVersion + "\r\n");
        } catch (IOException e) {
            e.printStackTrace();

            // 如果日志初始化失败了，请务必段下来看看这里是什么错误
            Log.e(TAG, "writeAppVersion", e);
            assertTrue(false);

        }
    }

    private static void appendLog(String type, String tag, String msg, Throwable tr) {

        //assertTrue(mContext != null);
        if (mContext == null) {
            Log.e("ULog", "You should init ULog first");
            return;
        }

        assertTrue(type != null);
        assertTrue(tag != null);
        assertTrue(msg != null);

        mLogQueue.add(new LogInfo(System.currentTimeMillis(), type, tag, msg, tr));
    }

    private static String constructLogMsg(LogInfo logInfo) {
        StringBuilder message = stringBuilderThreadLocal.get();
        assertTrue(message != null);

        message.delete(0, message.length());

        message.append(getTimeString(logInfo.timeMilis));
        message.append("|");

        message.append(mProcessName);
        message.append("|");

        message.append("P ");
        message.append(mProcessId);
        message.append("|");

        message.append("T ");
        message.append(String.format("%05d", logInfo.tid));
        message.append("|");

        message.append(logInfo.type);
        message.append("| ");

        message.append(logInfo.tag);
        message.append(": ");

        message.append(logInfo.msg);
        message.append("\n");

        if (null != logInfo.tr) {
            message.append("\n");
            message.append(Log.getStackTraceString(logInfo.tr));
            message.append("\n");
        }

        return message.toString();
    }

    private static class LogInfo {
        public final long tid;
        public final long timeMilis;
        public final String type;
        public final String tag;
        public final String msg;
        public final Throwable tr;

        public LogInfo(long timeMilis, String type, String tag, String msg, Throwable tr) {
            this.tid = Thread.currentThread().getId();
            this.timeMilis = timeMilis;
            this.type = type;
            this.tag = tag;
            this.msg = msg;
            this.tr = tr;
        }
    }
}
