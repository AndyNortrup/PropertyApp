package com.NortrupDevelopment.PropertyBook.model;

import java.math.BigDecimal;

/**
 * Implements the ModelFactory for Realm objects
 * Created by andy on 2/25/15.
 */
public class ModelFactoryImpl implements ModelFactory {

  public ModelFactoryImpl() {

  }

  @Override
  public PropertyBook createOrphanPropertyBook(String pbic,
                                               String uic,
                                               String description) {

    return new PropertyBookImpl(pbic, uic, description, null);
  }

  @Override
  public LineNumber createOrphanLineNumber(String lineNumberText,
                                           String subLineNumber,
                                           String sri,
                                           String erc,
                                           String nomenclature,
                                           String authDoc,
                                           int authorized,
                                           int required,
                                           int dueIn) {
    return new LineNumberImpl(lineNumberText,
        subLineNumber,
        sri,
        erc,
        nomenclature,
        authDoc,
        required,
        authorized,
        dueIn,
        null,
        null);
  }

  @Override
  public StockNumber createOrphanStockNumber(String stockNumberText,
                                             String unitOfIssue,
                                             BigDecimal unitPrice,
                                             String nomenclature,
                                             String llc,
                                             String ecs,
                                             String srrc,
                                             String uiiManaged,
                                             String ciic,
                                             String dla,
                                             String pubData,
                                             int onHand) {
    return new StockNumberImpl(stockNumberText,
        unitOfIssue,
        unitPrice,
        nomenclature,
        llc,
        ecs,
        srrc,
        uiiManaged,
        ciic,
        dla,
        pubData,
        onHand,
        null,
        null);

  }

  @Override
  public SerialNumber createOrphanSerialNumber(String serialNumber,
                                               String systemNumber) {
    return new SerialNumberImpl(serialNumber, systemNumber, null);
  }
}
