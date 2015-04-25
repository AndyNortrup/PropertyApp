package com.NortrupDevelopment.PropertyBook.dao;

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
  private int dueIn;
  private PropertyBook propertyBook;
  private RealmList<StockNumber> stockNumbers;

  public String getLin() {
    return lin;
  }

  public void setLin(String lin) {
    this.lin = lin;
  }

  public String getSubLin() {
    return subLin;
  }

  public void setSubLin(String subLin) {
    this.subLin = subLin;
  }

  public String getSri() {
    return sri;
  }

  public void setSri(String sri) {
    this.sri = sri;
  }

  public String getErc() {
    return erc;
  }

  public void setErc(String erc) {
    this.erc = erc;
  }

  public String getNomenclature() {
    return nomenclature;
  }

  public void setNomenclature(String nomenclature) {
    this.nomenclature = nomenclature;
  }

  public String getAuthDoc() {
    return authDoc;
  }

  public void setAuthDoc(String authDoc) {
    this.authDoc = authDoc;
  }

  public int getRequired() {
    return required;
  }

  public void setRequired(int required) {
    this.required = required;
  }

  public int getAuthorized() {
    return authorized;
  }

  public void setAuthorized(int authorized) {
    this.authorized = authorized;
  }

  public int getDueIn() {
    return dueIn;
  }

  public void setDueIn(int dueIn) {
    this.dueIn = dueIn;
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
}
