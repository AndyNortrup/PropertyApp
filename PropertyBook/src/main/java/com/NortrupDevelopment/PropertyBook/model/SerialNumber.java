package com.NortrupDevelopment.PropertyBook.model;

/**
 * Serial Number interface defines the public requirements for a serial number.
 * Created by andy on 2/25/15.
 */
public interface SerialNumber {
  String getSerialNumber();

  void setSerialNumber(String serialNumber);

  String getSysNo();

  void setSysNo(String sysNo);

  StockNumber getStockNumber();

  void setStockNumber(StockNumber stockNumber);
}
