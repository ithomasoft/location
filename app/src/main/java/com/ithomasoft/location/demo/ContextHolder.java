package com.ithomasoft.location.demo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;

import androidx.annotation.Nullable;

import java.lang.reflect.Field;

public class ContextHolder {
    static volatile Context ApplicationContext;

    @Nullable
    public static Activity getActivity(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context.getClass().getName().contains("com.android.internal.policy.DecorContext")) {
            try {
                Field declaredField = context.getClass().getDeclaredField("mPhoneWindow");
                declaredField.setAccessible(true);
                Object obj = declaredField.get(context);
                return (Activity) obj.getClass().getMethod("getContext", new Class[0]).invoke(obj, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (context instanceof ContextWrapper) {
                return getActivity(((ContextWrapper) context).getBaseContext());
            }
            return null;
        }
        return null;
    }

    public static Context getContext() {
        if (ApplicationContext != null) {
            return ApplicationContext;
        }
        try {
            Application application = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null, (Object[]) null);
            if (application != null) {
                ApplicationContext = application;
                return application;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Application application2 = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null, (Object[]) null);
            if (application2 != null) {
                ApplicationContext = application2;
                return application2;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        throw new IllegalStateException("ContextHolder is not initialed, it is recommend to init with application context.");
    }

    public static void init(Context context) {
        ApplicationContext = context;
    }
}