package com.NortrupDevelopment.PropertyBook.dao;


import io.realm.RealmObject;

/**
 * This is the basic data structure to track a serial numbered property book
 * item.
 * Created by andy on 5/16/13.
 */
public class SerialNumber extends RealmObject {


  private String serialNumber;
  private String systemNumber;
  private StockNumber stockNumber;

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public String getSystemNumber() {
    return systemNumber;
  }

  public void setSystemNumber(String systemNumber) {
    this.systemNumber = systemNumber;
  }

  public StockNumber getStockNumber() {
    return stockNumber;
  }

  public void setStockNumber(StockNumber stockNumber) {
    this.stockNumber = stockNumber;
  }
}
