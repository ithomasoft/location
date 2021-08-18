package com.ithomasoft.location.tools;

import android.util.Log;

/**
 * 日志打印
 *
 * @author ithomasoft
 */
public class SmartLogger {

    private boolean logger;

    public SmartLogger(boolean logger) {
        this.logger = logger;
    }


    public void v(String message, Object... args) {
        if (logger) {
            Log.v(getTag(), formatMessage(message, args));
        }
    }

    public void v(Throwable t, String message, Object... args) {
        if (logger) {
            Log.v(getTag(), formatMessage(message, args), t);
        }
    }

    public void d(String message, Object... args) {
        if (logger) {
            Log.d(getTag(), formatMessage(message, args));
        }
    }

    public void d(Throwable t, String message, Object... args) {
        if (logger) {
            Log.d(getTag(), formatMessage(message, args), t);
        }
    }

    public void i(String message, Object... args) {
        if (logger) {
            Log.i(getTag(), formatMessage(message, args));
        }
    }

    public void i(Throwable t, String message, Object... args) {
        if (logger) {
            Log.i(getTag(), formatMessage(message, args), t);
        }

    }

    public void w(String message, Object... args) {
        if (logger) {
            Log.w(getTag(), formatMessage(message, args));
        }

    }

    public void w(Throwable t, String message, Object... args) {
        if (logger) {
            Log.w(getTag(), formatMessage(message, args), t);
        }

    }

    public void e(String message, Object... args) {
        if (logger) {
            Log.e(getTag(), formatMessage(message, args));
        }

    }

    public void e(Throwable t, String message, Object... args) {
        if (logger) {
            Log.e(getTag(), formatMessage(message, args), t);
        }
    }


    private String getTag() {
        return new Exception().getStackTrace()[3].getMethodName();
    }

    private String formatMessage(String message, Object... args) {
        return args.length == 0 ? message : String.format(message, args);
    }
}
