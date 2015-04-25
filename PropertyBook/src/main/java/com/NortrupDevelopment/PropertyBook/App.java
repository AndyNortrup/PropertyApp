package com.NortrupDevelopment.PropertyBook;

import android.app.Application;
import android.util.Log;

/**
 * Provides the Application context to Dagger.
 * Created by andy on 3/16/14.
 */
public class App extends Application {


  public void onCreate() {
    Log.d("App.java", "Starting Application");
    super.onCreate();
  }

  public void inject(Object object) {
  }

}
