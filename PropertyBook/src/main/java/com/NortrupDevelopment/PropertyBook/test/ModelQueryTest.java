package com.NortrupDevelopment.PropertyBook.test;

import android.test.AndroidTestCase;

import com.NortrupDevelopment.PropertyBook.model.ModelSearcher;
import com.NortrupDevelopment.PropertyBook.model.RealmModelSearcher;
import com.NortrupDevelopment.PropertyBook.model.SerialNumber;

import java.util.AbstractList;

/**
 * Created by andy on 2/9/15.
 */
public class ModelQueryTest extends AndroidTestCase {

  public void testRealmPropertyBookQuery() {
    ModelSearcher searcher = new RealmModelSearcher(getContext());
    AbstractList<SerialNumber> serialNumbers
        = searcher.searchSerialNumber("FTX1139A3A1");
    assertEquals(1, serialNumbers.size());

    serialNumbers = searcher.searchSerialNumber("M403");
    assertEquals(12, serialNumbers.size());

    serialNumbers = searcher.searchSerialNumber("NO_SUCH_SERIAL_NUMBER");
    assertEquals(0, serialNumbers.size());
    assertEquals(41, searcher.searchSerialNumber("").size());

    assertEquals(1, searcher.searchStockNumber("6665-01-552-2704").size());
    assertEquals(2, searcher.searchStockNumber("7010").size());
    assertEquals(0, searcher.searchStockNumber("9999-99-999-9999").size());

    assertEquals(0, searcher.searchLineNumber("Z12345").size());
    assertEquals(1, searcher.searchLineNumber("70210N").size());
    assertEquals(2, searcher.searchLineNumber("A3").size());
    assertEquals(6, searcher.searchLineNumber("").size());
  }
}
