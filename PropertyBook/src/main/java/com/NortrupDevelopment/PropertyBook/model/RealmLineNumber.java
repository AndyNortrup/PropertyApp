package com.NortrupDevelopment.PropertyBook.model;

import java.util.AbstractList;

import io.realm.RealmList;
import io.realm.RealmObject;

public class RealmLineNumber extends RealmObject implements LineNumber {

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
  private RealmList<RealmStockNumber> stockNumbers;

  /**
   * @return the lin
   */
  @Override
  public String getLin() {
    return lin;
  }


  /**
   * @param lin Text Line Number (A12345)
   */

  @Override
  public void setLin(String lin) {
    this.lin = lin;
  }


  /**
   * @return the subLin
   */
  @Override
  public String getSubLin() {
    return subLin;
  }


  /**
   * @param subLin the subLin to set
   */
  @Override
  public void setSubLin(String subLin) {
    this.subLin = subLin;
  }


  /**
   * @return the sri
   */
  @Override
  public String getSri() {
    return sri;
  }


  /**
   * @param sri the sri to set
   */
  @Override
  public void setSri(String sri) {
    this.sri = sri;
  }


  /**
   * @return the erc
   */
  @Override
  public String getErc() {
    return erc;
  }


  /**
   * @param erc the erc to set
   */
  @Override
  public void setErc(String erc) {
    this.erc = erc;
  }


  /**
   * @return the nomenclature
   */
  @Override
  public String getNomenclature() {
    return nomenclature;
  }


  /**
   * @param nomencalture set the nomenclature
   */
  @Override
  public void setNomenclature(String nomencalture) {
    this.nomenclature = nomencalture;
  }


  /**
   * @return the authDoc
   */
  @Override
  public String getAuthDoc() {
    return authDoc;
  }


  /**
   * @param authDoc the authDoc to set
   */
  @Override
  public void setAuthDoc(String authDoc) {
    this.authDoc = authDoc;
  }


  /**
   * @return the required
   */
  @Override
  public int getRequired() {
    return required;
  }


  /**
   * @param required the required to set
   */
  @Override
  public void setRequired(int required) {
    this.required = required;
  }


  /**
   * @return the authorized
   */
  @Override
  public int getAuthorized() {
    return authorized;
  }


  /**
   * @param authorized the authorized to set
   */
  @Override
  public void setAuthorized(int authorized) {
    this.authorized = authorized;
  }

  /**
   * @return the dueIn
   */
  @Override
  public int getDueIn() {
    return dueIn;
  }

  /**
   * @param dueIn the dueIn to set
   */
  @Override
  public void setDueIn(int dueIn) {
    this.dueIn = dueIn;
  }

  @Override
  public PropertyBook getPropertyBook() {
    return propertyBook;
  }

  @Override
  public void setPropertyBook(PropertyBook propertyBook) {
    this.propertyBook = propertyBook;
  }

  @Override
  public AbstractList<StockNumber> getStockNumbers() {
    return (AbstractList) stockNumbers;
  }

  @Override
  public void setStockNumbers(AbstractList<StockNumber> stockNumbers) {
    if (stockNumbers.size() > 0) {
      if (!(stockNumbers.get(0) instanceof RealmStockNumber)) {
        throw new IllegalStateException("Not a Realms Stock Number");
      }
      for (StockNumber nsn : stockNumbers) {
        this.stockNumbers.add((RealmStockNumber) nsn);
      }
    }
  }
}
