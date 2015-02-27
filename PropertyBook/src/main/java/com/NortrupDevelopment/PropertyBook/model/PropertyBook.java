package com.NortrupDevelopment.PropertyBook.model;

import java.util.AbstractList;

/**
 * Created by andy on 2/27/15.
 */
public interface PropertyBook {
  String getDescription();

  void setDescription(String description);

  String getUic();

  void setUic(String uic);

  String getPbic();

  void setPbic(String pbic);

  AbstractList<LineNumber> getLineNumbers();

  void setLineNumbers(AbstractList<LineNumber> lineNumbers);
}
