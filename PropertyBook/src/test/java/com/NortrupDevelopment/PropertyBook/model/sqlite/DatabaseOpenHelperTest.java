package com.NortrupDevelopment.PropertyBook.model.sqlite;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public class DatabaseOpenHelperTest extends AndroidTestCase {

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();

  }

  public void testOnCreate() throws Exception {
    RenamingDelegatingContext renameContext =
        new RenamingDelegatingContext(getContext(), "testPrefix");

    DatabaseOpenHelper helper = new DatabaseOpenHelper(renameContext);

  }
}