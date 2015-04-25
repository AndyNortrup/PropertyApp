package com.NortrupDevelopment.PropertyBook.model;

import com.NortrupDevelopment.PropertyBook.dao.LineNumber;
import com.NortrupDevelopment.PropertyBook.dao.SerialNumber;
import com.NortrupDevelopment.PropertyBook.dao.StockNumber;

import java.util.AbstractList;

/**
 * Created by andy on 2/8/15.
 */
public interface ModelSearcher {

  /**
   * Search the model for LineNumbers containing the keyword.
   *
   * @param keyword Keyword to be searched for
   * @return An AbstractList instance that contains LineNumber objects
   * matching the criteria
   */
  AbstractList<LineNumber> searchLineNumber(String keyword);

  AbstractList<LineNumber> getAllLineNumbers();

  /**
   * Search the model for stock numbers containing the keyword.
   *
   * @param keyword Keyword to be searched for
   * @return An AbstractList instance that contains StockNumber objects
   * matching the criteria
   */
  AbstractList<StockNumber> searchStockNumber(String keyword);

  AbstractList<StockNumber> getAllStockNumbers();

  /**
   * Search the model for serial numbers containing the keyword.
   *
   * @param keyword Keyword to be searched for
   * @return An AbstractList instance that contains SerialNumber objects
   * matching the criteria
   */
  AbstractList<SerialNumber> searchSerialNumber(String keyword);

}
