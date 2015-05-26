package com.NortrupDevelopment.PropertyBook.model;

import com.NortrupDevelopment.PropertyBook.dao.LineNumber;
import com.NortrupDevelopment.PropertyBook.dao.SerialNumber;
import com.NortrupDevelopment.PropertyBook.dao.StockNumber;

import java.util.AbstractList;

import javax.inject.Inject;

import io.realm.Realm;


/**
 * Class used to search the Realm object database for objects that match the
 * criteria provided.
 * Created by andy on 2/8/15.
 */
public class ModelSearcherImpl implements ModelSearcher {

  private Realm realm;

  @Inject
  public ModelSearcherImpl(Realm realm) {
    this.realm = realm;
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

    return realm.where(LineNumber.class)
        .contains("lin", keyword)
        .or()
        .contains("nomenclature", keyword)
        .findAll();

  }

  @Override
  public AbstractList<LineNumber> getAllLineNumbers() {
    return realm.where(LineNumber.class)
        .findAllSorted("lin");
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
    return realm.where(StockNumber.class)
        .contains("nsn", keyword)
        .or()
        .contains("nomenclature", keyword)
        .findAll();
  }

  @Override
  public AbstractList<StockNumber> getAllStockNumbers() {
    return realm.where(StockNumber.class)
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
    return realm.where(SerialNumber.class)
        .contains("serialNumber", keyword)
        .or()
        .contains("systemNumber", keyword)
        .findAll();
  }
}
