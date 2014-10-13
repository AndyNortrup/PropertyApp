package com.NortrupDevelopment.PropertyBook.test;

import android.test.AndroidTestCase;

import com.NortrupDevelopment.PropertyBook.model.PropertyBook;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Test cases for realm LineNumbers
 * Created by andy on 10/12/14.
 */
public class RealmLineNumberTests extends AndroidTestCase {

  public void testCreatePropertyBook() throws Exception
  {
    Realm realm = Realm.getInstance(getContext());

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

  public void testDeleteAllPropertyBooks() throws Exception {
    ModelUtils.deleteAllPropertyBooks();

    Realm realm = Realm.getInstance(getContext());
    RealmQuery<PropertyBook> query = realm.where(PropertyBook.class);

    RealmResults<PropertyBook> results = query.findAll();

    assertEquals(results.size(), (int)0);
  }
}
