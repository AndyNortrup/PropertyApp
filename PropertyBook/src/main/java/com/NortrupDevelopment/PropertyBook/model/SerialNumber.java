package com.NortrupDevelopment.PropertyBook.model;


import io.realm.RealmObject;

/**
 * This is the basic data structure to track a serial numbered property book
 * item.
 * Created by andy on 5/16/13.
 */
public class SerialNumber extends RealmObject {


  private String serialNumber;
  private String sysNo;
  private StockNumber stockNumber;

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public String getSysNo() {
    return sysNo;
  }

  public void setSysNo(String sysNo) {
    this.sysNo = sysNo;
  }

  public StockNumber getStockNumber() {
    return stockNumber;
  }

  public void setStockNumber(StockNumber stockNumber) {
    this.stockNumber = stockNumber;
  }

}
