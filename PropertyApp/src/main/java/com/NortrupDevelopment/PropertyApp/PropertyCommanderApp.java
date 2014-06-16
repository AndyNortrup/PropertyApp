package com.NortrupDevelopment.PropertyApp;

import android.app.Application;
import android.util.Log;

/**
 * Created by andy on 3/16/14.
 */
public class PropertyCommanderApp extends Application {

    Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    public void onCreate() {
        Thread.setDefaultUncaughtExceptionHandler(
                new Thread.UncaughtExceptionHandler()

                {
                    private static final String LOG_TAG = "Uncaught Exception";
                    private final Thread.UncaughtExceptionHandler originalHandler =
                            Thread.getDefaultUncaughtExceptionHandler();
                    @Override
                    public void uncaughtException(Thread thread, Throwable ex) {
                        Log.e(LOG_TAG, "Uncaught exception in thread: " + thread.getId());
                        //Log.e(LOG_TAG, ex.getMessage());
                        ex.printStackTrace();

                        originalHandler.uncaughtException(thread, ex);
                    }
                }
        );
    }



}
