package com.NortrupDevelopment.PropertyBook.model;

import java.util.AbstractList;

import io.realm.Realm;

/**
 * Class used to search the Realm object database for objects that match the
 * criteria provided.
 * Created by andy on 2/8/15.
 */
public class RealmModelSearcher implements ModelSearcher {


  private Realm mRealm;

  public RealmModelSearcher(Realm realm) {
    mRealm = realm;
  }

  private Realm getRealm() {
    return mRealm;
  }

  /**
   * Search the model for LineNumbers containing the keyword in the LIN, SubLIN,
   * or Nomenclature fields.
   *
   * @param keyword Keyword to be searched for
   * @return An AbstractList instance that contains LineNumber objects
   * matching the criteria
   */
  @Override
  public AbstractList<LineNumber> searchLineNumber(String keyword) {

    return (AbstractList) getRealm().where(RealmLineNumber.class)
        .contains("lin", keyword)
        .or()
        .contains("subLin", keyword)
        .or()
        .contains("nomenclature", keyword)
        .findAll();
  }

  /**
   * Search the model for stock numbers containing the keyword.
   *
   * @param keyword Keyword to be searched for
   * @return An AbstractList instance that contains StockNumber objects
   * matching the criteria
   */
  @Override
  public AbstractList<StockNumber> searchStockNumber(String keyword) {
    return (AbstractList) getRealm().where(RealmStockNumber.class)
        .contains("nomenclature", keyword)
        .or()
        .contains("nsn", keyword)
        .or()
        .contains("nsn", NSNFormatter.removeDashesFromNSN(keyword))
        .findAll();
  }

  /**
   * Search the model for serial numbers containing the keyword.
   *
   * @param keyword Keyword to be searched for
   * @return An AbstractList instance that contains SerialNumber objects
   * matching the criteria
   */
  @Override
  public AbstractList<SerialNumber> searchSerialNumber(String keyword) {
    return (AbstractList) getRealm().where(RealmSerialNumber.class)
        .contains("serialNumber", keyword)
        .or()
        .contains("sysNo", keyword)
        .findAll();
  }
}
