package com.NortrupDevelopment.PropertyBook.model;

import java.util.AbstractList;


/**
 * Class used to search the Realm object database for objects that match the
 * criteria provided.
 * Created by andy on 2/8/15.
 */
public class ModelSearcherImpl implements ModelSearcher {


  public ModelSearcherImpl() {

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

    return null;
  }

  @Override
  public AbstractList<LineNumber> getAllLineNumbers() {
    return null;
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
    return null;
  }

  @Override
  public AbstractList<StockNumber> getAllStockNumbers() {
    return null;
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
    return null;
  }
}
