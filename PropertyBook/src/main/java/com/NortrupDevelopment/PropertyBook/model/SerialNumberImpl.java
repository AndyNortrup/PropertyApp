package com.NortrupDevelopment.PropertyBook.model;


/**
 * This is the basic data structure to track a serial numbered property book
 * item.
 * Created by andy on 5/16/13.
 */
public class SerialNumberImpl implements SerialNumber {


  private String serialNumber;
  private String sysNo;
  private StockNumber stockNumber;

  public SerialNumberImpl(String serialNumber,
                          String sysNo,
                          StockNumber stockNumber) {
    this.serialNumber = serialNumber;
    this.sysNo = sysNo;
    this.stockNumber = stockNumber;
  }

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
    this.stockNumber = stockNumber;
  }
}
