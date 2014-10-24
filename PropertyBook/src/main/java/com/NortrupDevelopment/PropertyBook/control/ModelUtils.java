package com.NortrupDevelopment.PropertyBook.control;

import android.content.Context;

import com.NortrupDevelopment.PropertyBook.model.LineNumber;
import com.NortrupDevelopment.PropertyBook.model.PropertyBook;
import com.NortrupDevelopment.PropertyBook.model.RealmDefinition;
import com.NortrupDevelopment.PropertyBook.model.SerialNumber;
import com.NortrupDevelopment.PropertyBook.model.StockNumber;

import io.realm.Realm;
import io.realm.RealmQuery;

/**
 * Utility methods to support the manipulation of the model
 * Created by andy on 10/13/14.
 */
public class ModelUtils {

  /**
   * Deletes all the entire contents of a realm.
   * @param context Application context with the given realm.
   */
  public static void deleteAllPropertyBooks(Context context, String realmFile) {
    Realm realm = RealmDefinition.getRealm(context, realmFile);

    realm.beginTransaction();

    //Delete all of the Property Books
    RealmQuery<PropertyBook> query = realm.where(PropertyBook.class);
    query.findAll().clear();

    //Delete all of the Line Numbers
    RealmQuery<LineNumber> lineNumberQuery = realm.where(LineNumber.class);
    lineNumberQuery.findAll().clear();

    //Delete all of the Stock Numbers
    realm.where(StockNumber.class).findAll().clear();

    //Delete all of the serial numbers
    realm.where(SerialNumber.class).findAll().clear();

    realm.commitTransaction();
  }

}
