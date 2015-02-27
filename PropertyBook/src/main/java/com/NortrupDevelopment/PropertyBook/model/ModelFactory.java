package com.NortrupDevelopment.PropertyBook.model;

/**
 * Model Factory defines an interface for the creation of model objects.
 * Created by andy on 2/25/15.
 */
public interface ModelFactory {

  public PropertyBook createPropertyBook(String pbic,
                                         String uic,
                                         String description);

  public LineNumber createLineNumber(String lineNumber,
                                     String subLineNumber,
                                     String sri,
                                     String erc,
                                     String nomenclature,
                                     String authDoc,
                                     int authorized,
                                     int required,
                                     int dueIn);

  public StockNumber createStockNumber(String stockNumber,
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
                                       int onHand);

  public SerialNumber createSerialNumber(String serialNumber,
                                         String systemNumber);
}
