package com.NortrupDevelopment.PropertyBook.dao;

import io.realm.RealmList;
import io.realm.RealmObject;

public class StockNumber extends RealmObject {

  private String nsn; //NSN
  private String ui; //Unit of Issue
  private int unitPrice; //Unit Price
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
  private LineNumber parentLineNumber;

  private RealmList<SerialNumber> serialNumbers;

  public StockNumber() {
    super();
    if (serialNumbers == null) {
      serialNumbers = new RealmList<>();
    }
  }

  public String getNsn() {
    return nsn;
  }

  public void setNsn(String nsn) {
    this.nsn = nsn;
  }

  public String getUi() {
    return ui;
  }

  public void setUi(String ui) {
    this.ui = ui;
  }

  public int getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(int unitPrice) {
    this.unitPrice = unitPrice;
  }

  public String getNomenclature() {
    return nomenclature;
  }

  public void setNomenclature(String nomenclature) {
    this.nomenclature = nomenclature;
  }

  public String getLlc() {
    return llc;
  }

  public void setLlc(String llc) {
    this.llc = llc;
  }

  public String getEcs() {
    return ecs;
  }

  public void setEcs(String ecs) {
    this.ecs = ecs;
  }

  public String getSrrc() {
    return srrc;
  }

  public void setSrrc(String srrc) {
    this.srrc = srrc;
  }

  public String getUiiManaged() {
    return uiiManaged;
  }

  public void setUiiManaged(String uiiManaged) {
    this.uiiManaged = uiiManaged;
  }

  public String getCiic() {
    return ciic;
  }

  public void setCiic(String ciic) {
    this.ciic = ciic;
  }

  public String getDla() {
    return dla;
  }

  public void setDla(String dla) {
    this.dla = dla;
  }

  public String getPubData() {
    return pubData;
  }

  public void setPubData(String pubData) {
    this.pubData = pubData;
  }

  public int getOnHand() {
    return onHand;
  }

  public void setOnHand(int onHand) {
    this.onHand = onHand;
  }

  public LineNumber getParentLineNumber() {
    return parentLineNumber;
  }

  public void setParentLineNumber(LineNumber parentLineNumber) {
    this.parentLineNumber = parentLineNumber;
  }

  public RealmList<SerialNumber> getSerialNumbers() {
    return serialNumbers;
  }

  public void setSerialNumbers(RealmList<SerialNumber> serialNumbers) {
    this.serialNumbers = serialNumbers;
  }
}
