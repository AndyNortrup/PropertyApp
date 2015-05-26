package com.NortrupDevelopment.PropertyBook.model;

import com.NortrupDevelopment.PropertyBook.dao.LineNumber;
import com.NortrupDevelopment.PropertyBook.dao.PropertyBook;
import com.NortrupDevelopment.PropertyBook.dao.SerialNumber;
import com.NortrupDevelopment.PropertyBook.dao.StockNumber;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Utility methods to support the manipulation of the model
 * Created by andy on 10/13/14.
 */
public class ModelUtilsImpl implements ModelUtils {

  public Realm mRealm;

  public ModelUtilsImpl(Realm realm) {
    mRealm = realm;
  }

  ;

  /**
   * Deletes all the entire contents of the database.
   */
  public void deleteAllPropertyBooks() {
    RealmResults<PropertyBook> pbicList = mRealm.where(PropertyBook.class)
        .findAll();

    RealmResults<LineNumber> lineNumbers = mRealm.where(LineNumber.class).findAll();
    RealmResults<StockNumber> stockNumbers = mRealm.where(StockNumber.class).findAll();
    RealmResults<SerialNumber> serialNumbers = mRealm.where(SerialNumber.class).findAll();
    mRealm.beginTransaction();
    serialNumbers.clear();
    stockNumbers.clear();
    lineNumbers.clear();
    pbicList.clear();
    mRealm.commitTransaction();
  }

  @Override public void insertPropertyBook(PropertyBook pb) {
    mRealm.beginTransaction();
    mRealm.copyToRealm(pb);
    mRealm.commitTransaction();
  }

}
