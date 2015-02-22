package com.NortrupDevelopment.PropertyBook.test;

import android.test.ActivityInstrumentationTestCase2;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.presenter.MainActivity;

/**
 * Created by andy on 2/21/15.
 */
public class SearchUITests extends ActivityInstrumentationTestCase2<MainActivity> {

  public SearchUITests(Class<MainActivity> activityClass) {
    super(activityClass);
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
    getActivity();
  }

  public void testFloatingActionButtonExists() {
    onView(withId(R.id.fab_expand_menu_button)).check(isDisplayed());
  }
}
