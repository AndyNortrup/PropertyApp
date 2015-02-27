package com.NortrupDevelopment.PropertyBook.test;

import android.test.AndroidTestCase;

import com.NortrupDevelopment.PropertyBook.io.PrimaryHandReceiptReader;
import com.NortrupDevelopment.PropertyBook.io.PrimaryHandReceiptReaderImpl;
import com.NortrupDevelopment.PropertyBook.model.ModelUtils;
import com.NortrupDevelopment.PropertyBook.model.PropertyBook;
import com.NortrupDevelopment.PropertyBook.model.RealmDefinition;
import com.NortrupDevelopment.PropertyBook.model.RealmLineNumber;
import com.NortrupDevelopment.PropertyBook.model.RealmModelFactory;
import com.NortrupDevelopment.PropertyBook.model.RealmSerialNumber;
import com.NortrupDevelopment.PropertyBook.model.RealmStockNumber;

import java.io.InputStream;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by andy on 2/9/15.
 */
public class ModelQueryTest extends AndroidTestCase {

  /**
   * Test cases for realm LineNumbers
   * Created by andy on 10/12/14.
   */
  public static class RealmLineNumberTests extends AndroidTestCase {


    public void testCreatePropertyBook() throws Exception {
      Realm realm = RealmDefinition.getRealm(getContext(),
          RealmDefinition.TEASTING_REALM);

      realm.beginTransaction();

      PropertyBook propertyBook = realm.createObject(PropertyBook.class);
      propertyBook.setPbic("8");
      propertyBook.setUic("WG02AA");
      propertyBook.setDescription("334th Signal Company");

      realm.commitTransaction();

      RealmQuery<PropertyBook> query = realm.where(PropertyBook.class);
      query.equalTo("pbic", "8");
      query.equalTo("uic", "WG02AA");
      query.equalTo("description", "334th Signal Company");

      PropertyBook result = query.findFirst();

      assertEquals(propertyBook.getDescription(), result.getDescription());
      assertEquals(propertyBook.getPbic(), result.getPbic());
      assertEquals(propertyBook.getUic(), result.getUic());
    }

  }

  /**
   * Created by andy on 10/13/14.
   */
  public static class RealmModelTests extends AndroidTestCase {

    /**
     * Tests the functionality of the PropertyBookReader class
     *
     * @throws Exception Throws any found exceptions.
     */
    public void testPropertyBookReader() throws Exception {

      final int[] TEST_SHEETS = {0, 1};

      InputStream inStream =
          getContext().getAssets().open("TestPhrPrint.xls");

      //Read the hand receipt file
      PrimaryHandReceiptReader reader = new PrimaryHandReceiptReaderImpl(
          new RealmModelFactory(
              RealmDefinition.getRealm(getContext(),
                  RealmDefinition.TEASTING_REALM)));

      reader.readHandReceipt(inStream, TEST_SHEETS);

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

      RealmQuery<RealmLineNumber> lineNumberQuery = realm.where(RealmLineNumber.class);
      RealmResults<RealmLineNumber> lineNumbers = lineNumberQuery.findAll();

      assertEquals(DESIRED_NUM_LINS, lineNumbers.size());

      //We should have 7 StockNumbers
      final int DESIRED_STOCK_NUMBERS = 7;
      RealmQuery<RealmStockNumber> stockNumberQuery = realm.where(RealmStockNumber.class);
      RealmResults<RealmStockNumber> stockNumbers = stockNumberQuery.findAll();

      assertEquals(DESIRED_STOCK_NUMBERS, stockNumbers.size());

      //We should have 41 Serial Numbers
      final int DESIRED_SERIAL_NUMBERS = 41;
      RealmQuery<RealmSerialNumber> serialNumberQuery =
          realm.where(RealmSerialNumber.class);
      RealmResults<RealmSerialNumber> serialNumbers = serialNumberQuery.findAll();

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
     *
     * @throws Exception
     */
    public void testDeleteAllPropertyBooks() throws Exception {
      ModelUtils.deleteAllPropertyBooks(getContext(),
          RealmDefinition.TEASTING_REALM);

      Realm realm = RealmDefinition.getRealm(getContext(),
          RealmDefinition.TEASTING_REALM);

      RealmQuery<PropertyBook> query = realm.where(PropertyBook.class);
      RealmResults<PropertyBook> results = query.findAll();

      assertEquals(results.size(), (int) 0);
    }
  }

  /**
   * Created by andy on 2/14/15.
   */
  public static class ViewHolderTest extends AndroidTestCase {
    public void testCreateViewHolder() {


    }

  }
}
