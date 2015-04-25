package com.NortrupDevelopment.PropertyBook.model;

import com.NortrupDevelopment.PropertyBook.dao.LineNumber;
import com.NortrupDevelopment.PropertyBook.dao.PropertyBook;
import com.NortrupDevelopment.PropertyBook.dao.SerialNumber;
import com.NortrupDevelopment.PropertyBook.dao.StockNumber;

import java.math.BigDecimal;

/**
 * Model Factory defines an interface for the creation of model objects.
 * Created by andy on 2/25/15.
 */
public interface ModelFactory {

  public PropertyBook createOrphanPropertyBook(String pbic,
                                               String uic,
                                               String description);

  /**
   * Create a LineNumber without parent PropertyBook or child Stock Numbers
   *
   * @param lineNumber
   * @param subLineNumber
   * @param sri
   * @param erc
   * @param nomenclature
   * @param authDoc
   * @param authorized
   * @param required
   * @param dueIn
   * @return
   */
  public LineNumber createOrphanLineNumber(String lineNumber,
                                           String subLineNumber,
                                           String sri,
                                           String erc,
                                           String nomenclature,
                                           String authDoc,
                                           int authorized,
                                           int required,
                                           int dueIn);

  /**
   * Create a StockNumber without parent LineNumber or subordinate
   * SerialNumbers
   *
   * @param stockNumber
   * @param unitOfIssue
   * @param unitPrice
   * @param nomenclature
   * @param llc
   * @param ecs
   * @param srrc
   * @param uiiManaged
   * @param ciic
   * @param dla
   * @param pubData
   * @param onHand
   * @return A new SerialNumberImpl instance
   */
  public StockNumber createOrphanStockNumber(String stockNumber,
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
                                             int onHand);

  /**
   * Create a SerialNumber without a specified Parent LineNumber.
   *
   * @param serialNumber Serial Number
   * @param systemNumber System Number
   * @return a new SerialNumber Instance
   */
  public SerialNumber createOrphanSerialNumber(String serialNumber,
                                               String systemNumber);
}
