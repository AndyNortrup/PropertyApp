package com.NortrupDevelopment.PropertyBook.model;

import java.util.AbstractList;

/**
 * Public interface for LineNumber objects
 * Created by andy on 2/25/15.
 */
public interface LineNumber {
  String getLin();

  void setLin(String lin);

  String getSubLin();

  void setSubLin(String subLin);

  String getSri();

  void setSri(String sri);

  String getErc();

  void setErc(String erc);

  String getNomenclature();

  void setNomenclature(String nomencalture);

  String getAuthDoc();

  void setAuthDoc(String authDoc);

  int getRequired();

  void setRequired(int required);

  int getAuthorized();

  void setAuthorized(int authorized);

  int getDueIn();

  void setDueIn(int dueIn);

  PropertyBook getPropertyBook();

  void setPropertyBook(PropertyBook propertyBook);

  AbstractList<StockNumber> getStockNumbers();

  void setStockNumbers(AbstractList<StockNumber> stockNumbers);
}
