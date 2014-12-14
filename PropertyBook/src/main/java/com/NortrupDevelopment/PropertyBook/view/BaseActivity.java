package com.NortrupDevelopment.PropertyBook.view;

import android.app.Activity;
import android.os.Bundle;

import com.NortrupDevelopment.PropertyBook.R;

/**
 *  This activity acts as a container for all of the other views in the app
 *  utilizing a fragment oriented design
 *  Created by andy on 12/14/14.
 */
public class BaseActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.base_layout);

    //Create a browser fragment and put it in the container
  }
}
