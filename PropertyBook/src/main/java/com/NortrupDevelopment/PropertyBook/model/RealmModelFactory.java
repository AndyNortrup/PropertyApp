package com.NortrupDevelopment.PropertyBook.model;

import io.realm.Realm;

/**
 * Implements the ModelFactory for Realm objects
 * Created by andy on 2/25/15.
 */
public class RealmModelFactory implements ModelFactory {

  Realm realm;

  public RealmModelFactory(Realm realm) {
    this.realm = realm;
  }

  @Override
  public PropertyBook createPropertyBook(String pbic,
                                         String uic,
                                         String description) {

    PropertyBook propertyBook = realm.createObject(PropertyBook.class);
    propertyBook.setPbic(pbic);
    propertyBook.setUic(uic);
    propertyBook.setDescription(description);

    return propertyBook;
  }

  @Override
  public LineNumber createLineNumber(String lineNumberText,
                                     String subLineNumber,
                                     String sri,
                                     String erc,
                                     String nomenclature,
                                     String authDoc,
                                     int authorized,
                                     int required,
                                     int dueIn) {

    LineNumber lineNumber = realm.createObject(RealmLineNumber.class);
    lineNumber.setLin(lineNumberText);
    lineNumber.setSubLin(subLineNumber);
    lineNumber.setSri(sri);
    lineNumber.setErc(erc);
    lineNumber.setNomenclature(nomenclature);
    lineNumber.setAuthDoc(authDoc);
    lineNumber.setAuthorized(authorized);
    lineNumber.setRequired(required);
    lineNumber.setDueIn(dueIn);
    return lineNumber;
  }

  @Override
  public StockNumber createStockNumber(String stockNumberText,
                                       String unitOfIssue,
                                       double unitPrice,
                                       String nomenclature,
                                       String llc,
                                       String ecs,
                                       String srrc,
                                       String uiiManaged,
                                       String ciic,
                                       String dla,
                                       String pubData,
                                       int onHand) {
    StockNumber stockNumber = realm.createObject(RealmStockNumber.class);
    stockNumber.setNsn(stockNumberText);
    stockNumber.setUi(unitOfIssue);
    stockNumber.setUnitPrice((long) unitPrice * 100);
    stockNumber.setNomenclature(nomenclature);
    stockNumber.setLlc(llc);
    stockNumber.setEcs(ecs);
    stockNumber.setSrrc(srrc);
    stockNumber.setUiiManaged(uiiManaged);
    stockNumber.setCiic(ciic);
    stockNumber.setDla(dla);
    stockNumber.setPubData(pubData);
    stockNumber.setOnHand(onHand);
    return stockNumber;
  }

  @Override
  public SerialNumber createSerialNumber(String serialNumber, String systemNumber) {
    SerialNumber sn = realm.createObject(RealmSerialNumber.class);
    sn.setSerialNumber(serialNumber);
    sn.setSysNo(systemNumber);
    return sn;
  }
}
