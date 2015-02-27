package com.NortrupDevelopment.PropertyBook.model;

import java.util.AbstractList;

import io.realm.RealmList;
import io.realm.RealmObject;

public class RealmStockNumber extends RealmObject implements StockNumber {

  private String nsn; //NSN
  private String ui; //Unit of Issue
  private long unitPrice; //Unit Price
  private String nomenclature; //NSN Nomenclature
  private String llc; //LLC code
  private String ecs; //ECS code
  private String srrc; //SRRC code
  private String uiiManaged;
  private String ciic;
  private String dla;
  private String pubData; //publication data
  private int onHand; //quantity on hand

  //reference to the LIN id to which this NSN belongs.
  private RealmLineNumber parentLineNumber;

  private RealmList<RealmSerialNumber> serialNumbers;

  @Override
  public String getNsn() {
    return nsn;
  }

  @Override
  public void setNsn(String nsn) {
    this.nsn = nsn;
  }

  @Override
  public String getUi() {
    return ui;
  }

  @Override
  public void setUi(String ui) {
    this.ui = ui;
  }

  @Override
  public long getUnitPrice() {
    return unitPrice;
  }

  @Override
  public void setUnitPrice(long unitPrice) {
    this.unitPrice = unitPrice;
  }

  @Override
  public String getNomenclature() {
    return nomenclature;
  }

  @Override
  public void setNomenclature(String nomenclature) {
    this.nomenclature = nomenclature;
  }

  @Override
  public String getLlc() {
    return llc;
  }

  @Override
  public void setLlc(String llc) {
    this.llc = llc;
  }

  @Override
  public String getEcs() {
    return ecs;
  }

  @Override
  public void setEcs(String ecs) {
    this.ecs = ecs;
  }

  @Override
  public String getSrrc() {
    return srrc;
  }

  @Override
  public void setSrrc(String srrc) {
    this.srrc = srrc;
  }

  @Override
  public String getUiiManaged() {
    return uiiManaged;
  }

  @Override
  public void setUiiManaged(String uiiManaged) {
    this.uiiManaged = uiiManaged;
  }

  @Override
  public String getCiic() {
    return ciic;
  }

  @Override
  public void setCiic(String ciic) {
    this.ciic = ciic;
  }


  @Override
  public String getDla() {
    return dla;
  }


  @Override
  public void setDla(String dla) {
    this.dla = dla;
  }


  @Override
  public String getPubData() {
    return pubData;
  }


  @Override
  public void setPubData(String pubData) {
    this.pubData = pubData;
  }


  @Override
  public int getOnHand() {
    return onHand;
  }


  @Override
  public void setOnHand(int onHand) {
    this.onHand = onHand;
  }


  @Override
  public LineNumber getParentLineNumber() {
    return parentLineNumber;
  }


  @Override
  public void setParentLineNumber(LineNumber parentLineNumber) {
    if (!(parentLineNumber instanceof RealmLineNumber)) {
      throw new IllegalStateException("Not a realm object.");
    }
    this.parentLineNumber = (RealmLineNumber) parentLineNumber;
  }


  @Override
  public AbstractList<SerialNumber> getSerialNumbers() {
    return (AbstractList) serialNumbers;
  }


  @Override
  public void setSerialNumbers(AbstractList<SerialNumber> serialNumbers) {
    if (!(serialNumbers.get(0) instanceof RealmSerialNumber)) {
      throw new IllegalStateException("Objects are not Realm Objects");
    }
    for (SerialNumber sn : serialNumbers) {
      this.serialNumbers.add((RealmSerialNumber) sn);
    }
  }

}
