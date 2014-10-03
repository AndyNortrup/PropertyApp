package com.NortrupDevelopment.PropertyBook.model;

import java.math.BigDecimal;

import io.realm.RealmList;
import io.realm.RealmObject;

public class StockNumber extends RealmObject {

  private String nsn; //NSN
  private String ui; //Unit of Issue
  private BigDecimal unitPrice; //Unit Price
  private String nomencalture; //NSN Nomenclature
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

  private static String queryString = TableContractNSN._ID + " = ?";

  private static String DASH = "-";

  RealmList<SerialNumber> mSerialNumbers;

  /**
   * @return the nsn
   */
  public String getNsn() {
    return nsn;
  }

  public static String getFormattedNSN(String nsn) {
    final String DASH = "-";
    StringBuilder sb = new StringBuilder(nsn);
    sb.insert(4, DASH);
    sb.insert(7, DASH);
    sb.insert(11, DASH);
    return sb.toString();
  }

  public String getFormatedNSN() {
    StringBuilder sb = new StringBuilder(nsn);
    sb.insert(4, DASH);
    sb.insert(7, DASH);
    sb.insert(11, DASH);
    return sb.toString();

  }

  /**
   * @param nsn the nsn to set
   */
  public void setNsn(String nsn) {
    this.nsn = nsn;
  }

  /**
   * @return the ui
   */
  public String getUi() {
    return ui;
  }

  /**
   * @param ui the ui to set
   */
  public void setUi(String ui) {
    this.ui = ui;
  }

  /**
   * @return the unitPrice
   */
  public BigDecimal getUnitPrice() {
    return unitPrice;
  }

  /**
   * @param unitPrice the unitPrice to set
   */
  public void setUnitPrice(BigDecimal unitPrice) {
    this.unitPrice = unitPrice;
  }

  /**
   * @return the nomencalture
   */
  public String getNomencalture() {
    return nomencalture;
  }

  /**
   * @param nomencalture the nomencalture to set
   */
  public void setNomencalture(String nomencalture) {
    this.nomencalture = nomencalture;
  }

  /**
   * @return the llc
   */
  public String getLlc() {
    return llc;
  }

  /**
   * @param llc the llc to set
   */
  public void setLlc(String llc) {
    this.llc = llc;
  }

  /**
   * @return the ecs
   */
  public String getEcs() {
    return ecs;
  }

  /**
   * @param ecs the ecs to set
   */
  public void setEcs(String ecs) {
    this.ecs = ecs;
  }

  /**
   * @return the srrc
   */
  public String getSrrc() {
    return srrc;
  }

  /**
   * @param srrc the srrc to set
   */
  public void setSrrc(String srrc) {
    this.srrc = srrc;
  }

  /**
   * @return the uiiManaged
   */
  public String getUiiManaged() {
    return uiiManaged;
  }

  /**
   * @param uiiManaged the uiiManaged to set
   */
  public void setUiiManaged(String uiiManaged) {
    this.uiiManaged = uiiManaged;
  }

  /**
   * @return the ciic
   */
  public String getCiic() {
    return ciic;
  }

  /**
   * @param ciic the ciic to set
   */
  public void setCiic(String ciic) {
    this.ciic = ciic;
  }

  /**
   * @return the dla
   */
  public String getDla() {
    return dla;
  }

  /**
   * @param dla the dla to set
   */
  public void setDla(String dla) {
    this.dla = dla;
  }

  /**
   * @return the pubData
   */
  public String getPubData() {
    return pubData;
  }

  /**
   * @param pubData the pubData to set
   */
  public void setPubData(String pubData) {
    this.pubData = pubData;
  }

  /**
   * @return the On Hand quantity
   */
  public int getOnHand() {
    return onHand;
  }

  /**
   * @param onHand the onHand to set
   */
  public void setOnHand(int onHand) {
    this.onHand = onHand;
  }

  /**
   * Sets the parent LIN for this NSN
   *
   * @param parentLineNumber Parent LIN
   */
  public void setParentLineNumber(LineNumber parentLineNumber) {
    this.parentLineNumber = parentLineNumber;
  }

  public LineNumber getParentLineNumber() {
    return parentLineNumber;
  }
  //endregion

}
