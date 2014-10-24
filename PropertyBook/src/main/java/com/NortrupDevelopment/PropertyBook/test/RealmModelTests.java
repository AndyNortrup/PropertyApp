package com.NortrupDevelopment.PropertyBook.test;

import android.test.AndroidTestCase;

import com.NortrupDevelopment.PropertyBook.control.ModelUtils;
import com.NortrupDevelopment.PropertyBook.io.PrimaryHandReceiptReader;
import com.NortrupDevelopment.PropertyBook.model.LineNumber;
import com.NortrupDevelopment.PropertyBook.model.PropertyBook;
import com.NortrupDevelopment.PropertyBook.model.RealmDefinition;
import com.NortrupDevelopment.PropertyBook.model.SerialNumber;
import com.NortrupDevelopment.PropertyBook.model.StockNumber;

import java.io.InputStream;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by andy on 10/13/14.
 */
public class RealmModelTests extends AndroidTestCase {

  /**
   * Tests the functionality of the PropertyBookReader class
   * @throws Exception Throws any found exceptions.
   */
  public void testPropertyBookReader() throws Exception {

    final int[] TEST_SHEETS = {0, 1};

    InputStream inStream =
        getContext().getAssets().open("TestPhrPrint.xls");

    //Read the hand receipt file
    PrimaryHandReceiptReader.readHandReceipt(inStream,
        TEST_SHEETS,
        getContext(),
        RealmDefinition.TEASTING_REALM);

    //Check the contents of the realm

    //We should have two PBICs
    final int DESIRED_PROPERTY_BOOKS = 2;
    Realm realm = RealmDefinition.getRealm(getContext(),
        RealmDefinition.TEASTING_REALM);

    RealmQuery<PropertyBook> propertyBookQuery = realm.where(PropertyBook.class);

    RealmResults<PropertyBook> propertyBooks = propertyBookQuery.findAll();

    assertEquals(DESIRED_PROPERTY_BOOKS, propertyBooks.size());

    //We should have 6 LINs
    final int DESIRED_NUM_LINS = 6;

    RealmQuery<LineNumber> lineNumberQuery = realm.where(LineNumber.class);
    RealmResults<LineNumber> lineNumbers = lineNumberQuery.findAll();

    assertEquals(DESIRED_NUM_LINS, lineNumbers.size());

    //We should have 7 StockNumbers
    final int DESIRED_STOCK_NUMBERS = 7;
    RealmQuery<StockNumber> stockNumberQuery = realm.where(StockNumber.class);
    RealmResults<StockNumber> stockNumbers = stockNumberQuery.findAll();

    assertEquals(DESIRED_STOCK_NUMBERS, stockNumbers.size());

    //We should have 41 Serial Numbers
    final int DESIRED_SERIAL_NUMBERS = 41;
    RealmQuery<SerialNumber> serialNumberQuery =
        realm.where(SerialNumber.class);
    RealmResults<SerialNumber> serialNumbers = serialNumberQuery.findAll();

    assertEquals(DESIRED_SERIAL_NUMBERS, serialNumbers.size());

    //In depth check of our LINs.  LIN 70210N should have three Stock Numbers
    lineNumberQuery.equalTo("lin", "70210N");
    lineNumbers = lineNumberQuery.findAll();

    assertEquals(1, lineNumbers.size());
    assertEquals(3, lineNumbers.get(0).getStockNumbers().size());

    //NSN 5810015236682 should have 2 serial numbers
    stockNumberQuery.equalTo("nsn", "5810015236682");
    stockNumbers = stockNumberQuery.findAll();

    assertEquals(1, stockNumbers.size());
    assertEquals(2, stockNumbers.get(0).getSerialNumbers().size());

  }

  /**
   * Test the functionality of the deleteAllPropertyBooks method.
   * @throws Exception
   */
  public void testDeleteAllPropertyBooks() throws Exception {
    ModelUtils.deleteAllPropertyBooks(getContext(),
        RealmDefinition.TEASTING_REALM);

    Realm realm = RealmDefinition.getRealm(getContext(),
        RealmDefinition.TEASTING_REALM);

    RealmQuery<PropertyBook> query = realm.where(PropertyBook.class);
    RealmResults<PropertyBook> results = query.findAll();

    assertEquals(results.size(), (int)0);
  }
}
