package com.NortrupDevelopment.PropertyBook.model;

import io.realm.RealmList;
import io.realm.RealmObject;

public class LineNumber extends RealmObject {

  private String lin;
  private String subLin;
  private String sri;
  private String erc;
  private String nomenclature;
  private String authDoc;
  private int required;
  private int authorized;
  private int di;
  private int mPropertyBookID;

  private PropertyBook propertyBook;
  private RealmList<StockNumber> stockNumbers;

  //Default ID value
  public static final int DEFAULT_ID = -1;


  /**
   * @return the lin
   */
  public String getLin() {
    return lin;
  }


  /**
   * @param lin the lin to set
   */
  public void setLin(String lin) {
    this.lin = lin;
  }


  /**
   * @return the subLin
   */
  public String getSubLin() {
    return subLin;
  }


  /**
   * @param subLin the subLin to set
   */
  public void setSubLin(String subLin) {
    this.subLin = subLin;
  }


  /**
   * @return the sri
   */
  public String getSri() {
    return sri;
  }


  /**
   * @param sri the sri to set
   */
  public void setSri(String sri) {
    this.sri = sri;
  }


  /**
   * @return the erc
   */
  public String getErc() {
    return erc;
  }


  /**
   * @param erc the erc to set
   */
  public void setErc(String erc) {
    this.erc = erc;
  }


  /**
   * @return the nomenclature
   */
  public String getNomenclature() {
    return nomenclature;
  }


  /**
   * @param nomencalture set the nomenclature
   */
  public void setNomenclature(String nomencalture) {
    this.nomenclature = nomencalture;
  }


  /**
   * @return the authDoc
   */
  public String getAuthDoc() {
    return authDoc;
  }


  /**
   * @param authDoc the authDoc to set
   */
  public void setAuthDoc(String authDoc) {
    this.authDoc = authDoc;
  }


  /**
   * @return the required
   */
  public int getRequired() {
    return required;
  }


  /**
   * @param required the required to set
   */
  public void setRequired(int required) {
    this.required = required;
  }


  /**
   * @return the authorized
   */
  public int getAuthorized() {
    return authorized;
  }


  /**
   * @param authorized the authorized to set
   */
  public void setAuthorized(int authorized) {
    this.authorized = authorized;
  }

  /**
   * @return the di
   */
  public int getDi() {
    return di;
  }

  /**
   * @param di the di to set
   */
  public void setDi(int di) {
    this.di = di;
  }

  public void setPropertyBookID(int propertyBookID) {
    mPropertyBookID = propertyBookID;
  }

  public int getPropertyBookID() {
    return mPropertyBookID;
  }

  public PropertyBook getPropertyBook() {
    return propertyBook;
  }

  public void setPropertyBook(PropertyBook propertyBook) {
    this.propertyBook = propertyBook;
  }

  public RealmList<StockNumber> getStockNumbers() {
    return stockNumbers;
  }

  public void setStockNumbers(RealmList<StockNumber> stockNumbers) {
    this.stockNumbers = stockNumbers;
  }

  @Override
  public String toString() {
    return getLin() + " " + getNomenclature();
  }

}
