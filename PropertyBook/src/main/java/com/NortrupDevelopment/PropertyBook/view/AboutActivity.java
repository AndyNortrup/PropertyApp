package com.NortrupDevelopment.PropertyBook.view;

import android.app.Activity;
import android.os.Bundle;

import com.NortrupDevelopment.PropertyBook.R;

/**
 * About us activity is a simple activity that tells the user about the company
 * that make is and gives a link to te website. DeepRanger217
 *
 * Created by andy on 8/20/14.
 */
public class AboutActivity extends Activity {

  public AboutActivity() {
    super();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_about_us);
  }
}
