package com.NortrupDevelopment.PropertyBook;

import android.app.Application;
import android.util.Log;

import com.NortrupDevelopment.PropertyBook.dagger_modules.AndroidModule;
import com.NortrupDevelopment.PropertyBook.dagger_modules.ModelSearchModule;
import com.NortrupDevelopment.PropertyBook.dagger_modules.PresenterModule;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 * Provides the Application context to Dagger.
 * Created by andy on 3/16/14.
 */
public class App extends Application {

  Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

  private ObjectGraph graph;

  public void onCreate() {
    Log.d("App.java", "Starting Application");
    //super.onCreate();

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

    graph = ObjectGraph.create(getModules().toArray());
  }

  protected List<Object> getModules() {
    return Arrays.asList(
        new AndroidModule(this),
        new ModelSearchModule(),
        new PresenterModule());
  }

  public void inject(Object object) {
    graph.inject(object);
  }

}
