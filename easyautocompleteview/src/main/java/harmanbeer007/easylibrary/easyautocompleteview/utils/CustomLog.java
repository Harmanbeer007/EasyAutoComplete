package harmanbeer007.easylibrary.easyautocompleteview.utils;

public class CustomLog {

    private static boolean DEBUG_MODE = false;
    private static String UNIQUE_CODE;

    /**
     * Setting the debug mode here
     * to manage printing android.util.Logs if it's in release mode
     *
     * @param debugMode:  BuildConfig.DEBUG should be provided here, ideally
     * @param uniqueCode: To identify or filter out your android.util.Logs
     */
    public static void initialize(boolean debugMode, String uniqueCode) {

        if (uniqueCode.length() > 20) {
            android.util.Log.e("LogHere", "Unique code must be less than 20 characters");
            return;
        }
        DEBUG_MODE = debugMode;
        UNIQUE_CODE = uniqueCode;
    }

    // Single param functions
    public static void d(String message) {
        if (!DEBUG_MODE) {
            return;
        }
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        String fileStack = getOneStack(stack[3]);
        android.util.Log.d(UNIQUE_CODE, fileStack + ": " + message);
    }

    public static void e(String message) {
        if (!DEBUG_MODE) {
            return;
        }
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        String fileStack = getOneStack(stack[3]);
        android.util.Log.e(UNIQUE_CODE, fileStack + ": " + message);
    }

    public static void i(String message) {
        if (!DEBUG_MODE) {
            return;
        }
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        String fileStack = getOneStack(stack[3]);
        android.util.Log.i(UNIQUE_CODE, fileStack + ": " + message);
    }

    public static void v(String message) {
        if (!DEBUG_MODE) {
            return;
        }
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        String fileStack = getOneStack(stack[3]);
        android.util.Log.v(UNIQUE_CODE, fileStack + ": " + message);
    }

    // Double param
    public static void d(String tag, String message) {
        if (!DEBUG_MODE) {
            return;
        }
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        String fileStack = getOneStack(stack[3]);
        android.util.Log.d(tag, fileStack + ": " + message);
    }

    public static void e(String tag, String message) {
        if (!DEBUG_MODE) {
            return;
        }
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        String fileStack = getOneStack(stack[3]);
        android.util.Log.e(tag, fileStack + ": " + message);
    }

    public static void i(String tag, String message) {
        if (!DEBUG_MODE) {
            return;
        }
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        String fileStack = getOneStack(stack[3]);
        android.util.Log.i(tag, fileStack + ": " + message);
    }

    public static void v(String tag, String message) {
        if (!DEBUG_MODE) {
            return;
        }
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        String fileStack = getOneStack(stack[3]);
        android.util.Log.v(tag, fileStack + ": " + message);
    }

    public static void e(String message, Exception e) {
        if (!DEBUG_MODE) {
            return;
        }
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        String fileStack = getOneStack(stack[3]);
        android.util.Log.e(UNIQUE_CODE, fileStack + ": " + message, e);
    }

    public static void e(String tag, String message, Exception e) {
        if (!DEBUG_MODE) {
            return;
        }
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        String fileStack = getOneStack(stack[3]);
        android.util.Log.e(tag, fileStack + ": " + message, e);
    }

    public static void e(String tag, String message, Throwable throwable) {
        if (!DEBUG_MODE) {
            return;
        }
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        String fileStack = getOneStack(stack[3]);
        android.util.Log.e(tag, fileStack + ": " + message, throwable);
    }

    public static void e(String message, Throwable throwable) {
        if (!DEBUG_MODE) {
            return;
        }
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        String fileStack = getOneStack(stack[3]);
        android.util.Log.e(UNIQUE_CODE, fileStack + ": " + message, throwable);
    }

    public static void e(Throwable throwable) {
        if (!DEBUG_MODE) {
            return;
        }
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        String fileStack = getOneStack(stack[3]);
        android.util.Log.e(UNIQUE_CODE, fileStack, throwable);
    }

    public static void e(String message, int parentCount) {
        sPrint(message, parentCount, "error");
    }

    public static void d(String message, int parentCount) {
        sPrint(message, parentCount, "debug");
    }

    public static void v(String message, int parentCount) {
        sPrint(message, parentCount, "verbose");
    }

    public static void i(String message, int parentCount) {
        sPrint(message, parentCount, "info");
    }

    /**
     * @param message:     message string to show
     * @param parentCount: 0, 1, 2
     */
    private static void sPrint(String message, int parentCount, String type) {

        if (!DEBUG_MODE)
            return;
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        String fileStack;
        switch (parentCount) {
            case 0:
                fileStack = getFileStack(stack[3]);
                break;
            case 1:
                fileStack = getFileStack(stack[4], stack[3]);
                break;
            case 2:
                if (stack.length < 6) {
                    android.util.Log.e(UNIQUE_CODE, "no stack trace available as there are no more parents");
                    return;
                }
                fileStack = getFileStack(stack[5], stack[4], stack[3]);
                break;
            default:
                android.util.Log.e(UNIQUE_CODE, "parentCount param must be 0, 1 or 2");
                return;
        }

        switch (type) {
            case "error":
                android.util.Log.e(UNIQUE_CODE, fileStack + " << " + message);
                break;
            case "verbose":
                android.util.Log.e(UNIQUE_CODE, fileStack + " << " + message);
                break;
            case "debug":
                android.util.Log.e(UNIQUE_CODE, fileStack + " << " + message);
                break;
            default:
                android.util.Log.e(UNIQUE_CODE, fileStack + " << " + message);
                break;
        }
    }

    private static String getOneStack(StackTraceElement... stackTraceElement) {

        String trace = "";
        int i = stackTraceElement.length - 1;
        StackTraceElement element = stackTraceElement[i];
        if (!element.isNativeMethod()) {

            String fullClassName = element.getClassName();
            String className = fullClassName.substring(fullClassName
                    .lastIndexOf(".") + 1);
            String methodName = element.getMethodName();
            String lineNumber = String
                    .valueOf(element.getLineNumber());
            trace = methodName + "("
                    + className + ".java:" + lineNumber + ")";
        }
        return trace.replaceAll("\\$\\d+", "");
    }

    private static String getFileStack(StackTraceElement... stackTraceElement) {

        StringBuilder trace = new StringBuilder();
        for (int i = stackTraceElement.length - 1; i >= 0; i--) {
            StackTraceElement element = stackTraceElement[i];
            if (!element.isNativeMethod()) {

                String fileName = element.getFileName();
                if (fileName.contains("."))
                    fileName = fileName.substring(0, fileName.indexOf("."));

                int lineNumber = element.getLineNumber();
                String methodName = element.getMethodName();

                trace.append(fileName);
                trace.append(".");
                trace.append(methodName);
                trace.append("(");
                trace.append(lineNumber);
                trace.append(")");
                if (i != 0)
                    trace.append(" << ");
            }
        }
        return trace.toString().replaceAll("\\$\\d+", "");
    }
}