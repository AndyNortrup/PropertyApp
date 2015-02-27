package com.NortrupDevelopment.PropertyBook.model;


import io.realm.RealmObject;

/**
 * This is the basic data structure to track a serial numbered property book
 * item.
 * Created by andy on 5/16/13.
 */
public class RealmSerialNumber extends RealmObject implements SerialNumber {


  private String serialNumber;
  private String sysNo;
  private RealmStockNumber stockNumber;

  @Override
  public String getSerialNumber() {
    return serialNumber;
  }

  @Override
  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  @Override
  public String getSysNo() {
    return sysNo;
  }

  @Override
  public void setSysNo(String sysNo) {
    this.sysNo = sysNo;
  }

  @Override
  public StockNumber getStockNumber() {
    return stockNumber;
  }

  @Override
  public void setStockNumber(StockNumber stockNumber) {
    if (!(stockNumber instanceof RealmStockNumber)) {
      throw new IllegalStateException("Not a Realm StockNumber");
    }
    this.stockNumber = (RealmStockNumber) stockNumber;
  }
}
