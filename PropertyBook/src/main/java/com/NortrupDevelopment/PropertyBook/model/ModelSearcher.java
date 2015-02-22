package com.NortrupDevelopment.PropertyBook.model;

import java.util.AbstractList;

/**
 * Created by andy on 2/8/15.
 */
public interface ModelSearcher {

  /**
   * Search the model for LineNumbers containing the keyword.
   * @param keyword Keyword to be searched for
   * @return An AbstractList instance that contains LineNumber objects
   * matching the criteria
   */
  AbstractList<LineNumber> searchLineNumber(String keyword);

  /**
   * Search the model for stock numbers containing the keyword.
   * @param keyword Keyword to be searched for
   * @return An AbstractList instance that contains StockNumber objects
   * matching the criteria
   */
  AbstractList<StockNumber> searchStockNumber(String keyword);

  /**
   * Search the model for serial numbers containing the keyword.
   * @param keyword Keyword to be searched for
   * @return An AbstractList instance that contains SerialNumber objects
   * matching the criteria
   */
  AbstractList<SerialNumber> searchSerialNumber(String keyword);

}
