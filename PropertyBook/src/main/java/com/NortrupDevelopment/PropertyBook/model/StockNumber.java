package com.NortrupDevelopment.PropertyBook.model;

import java.math.BigDecimal;
import java.util.AbstractList;

/**
 * Interface to define a stock number
 * Created by andy on 2/25/15.
 */
public interface StockNumber {
  String getNsn();

  void setNsn(String nsn);

  String getUi();

  void setUi(String ui);

  BigDecimal getUnitPrice();

  void setUnitPrice(BigDecimal unitPrice);

  String getNomenclature();

  void setNomenclature(String nomenclature);

  String getLlc();

  void setLlc(String llc);

  String getEcs();

  void setEcs(String ecs);

  String getSrrc();

  void setSrrc(String srrc);

  String getUiiManaged();

  void setUiiManaged(String uiiManaged);

  String getCiic();

  void setCiic(String ciic);

  String getDla();

  void setDla(String dla);

  String getPubData();

  void setPubData(String pubData);

  int getOnHand();

  void setOnHand(int onHand);

  LineNumber getParentLineNumber();

  void setParentLineNumber(LineNumber parentLineNumber);

  AbstractList<SerialNumber> getSerialNumbers();

  void setSerialNumbers(AbstractList<SerialNumber> serialNumbers);
}
