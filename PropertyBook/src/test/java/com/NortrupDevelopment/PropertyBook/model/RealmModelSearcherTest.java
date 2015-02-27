package com.NortrupDevelopment.PropertyBook.model;

import android.app.Activity;
import android.os.Bundle;
import android.test.InstrumentationTestCase;

import com.NortrupDevelopment.PropertyBook.view.DefaultMainActivity;

import io.realm.Realm;

public class RealmModelSearcherTest extends InstrumentationTestCase {

  private ModelSearcher searcher;
  Activity mContext;

  public RealmModelSearcherTest() {
    super();
  }

  @Override
  public void setUp() throws Exception {
    mContext = launchActivity("com.NortrupDevelopment.PropertyBook.view",
        DefaultMainActivity.class,
        new Bundle());

    Realm realm = Realm.getInstance(mContext.getFilesDir());
    searcher = new RealmModelSearcher(realm);
  }

  public void testSearchLineNumber() throws Exception {
    assertEquals(0, searcher.searchLineNumber("Z12345").size());
    assertEquals(1, searcher.searchLineNumber("70210N").size());
    assertEquals(2, searcher.searchLineNumber("A3").size());
    assertEquals(6, searcher.searchLineNumber("").size());
  }

  public void testSearchStockNumber() throws Exception {
    assertEquals(1, searcher.searchStockNumber("6665-01-552-2704").size());
    assertEquals(2, searcher.searchStockNumber("7010").size());
    assertEquals(0, searcher.searchStockNumber("9999-99-999-9999").size());
  }

  public void testSearchSerialNumber() throws Exception {
    assertEquals(1, searcher.searchSerialNumber("FTX1139A3A1").size());
    assertEquals(12, searcher.searchSerialNumber("M403").size());
    assertEquals(0, searcher.searchSerialNumber("NO_SUCH_SERIAL_NUMBER").size());
    assertEquals(41, searcher.searchSerialNumber("").size());
  }
}