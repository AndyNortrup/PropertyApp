package com.NortrupDevelopment.PropertyBook.model;

import com.NortrupDevelopment.PropertyBook.dao.PropertyBook;

/**
 * Created by andy on 2/27/15.
 */
public interface ModelUtils {

  public void deleteAllPropertyBooks();

  void insertPropertyBook(PropertyBook pb);
}
