package com.NortrupDevelopment.PropertyBook.model;

import com.NortrupDevelopment.PropertyBook.dao.LineNumber;
import com.NortrupDevelopment.PropertyBook.dao.PropertyBook;
import com.NortrupDevelopment.PropertyBook.dao.SerialNumber;
import com.NortrupDevelopment.PropertyBook.dao.StockNumber;

import java.math.BigDecimal;

/**
 * Implements the ModelFactory for Realm objects
 * Created by andy on 2/25/15.
 */
public class ModelFactoryImpl implements ModelFactory {

  public ModelFactoryImpl() {

  }


  @Override
  public PropertyBook createOrphanPropertyBook(String pbic, String pbicDescription, String uic, String unitName) {
    PropertyBook pb = new PropertyBook();
    pb.setPbic(pbic);
    pb.setUic(uic);
    pb.setDescription(pbicDescription);
    pb.setUnitName(unitName);
    return pb;
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
    LineNumber lin = new LineNumber();
    lin.setLin(lineNumberText);
    lin.setSubLin(subLineNumber);
    lin.setSri(sri);
    lin.setErc(erc);
    lin.setNomenclature(nomenclature);
    lin.setAuthDoc(authDoc);
    lin.setAuthorized(authorized);
    lin.setRequired(required);
    lin.setDueIn(dueIn);

    return lin;
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
    StockNumber nsn = new StockNumber();
    nsn.setNsn(stockNumberText);
    nsn.setUi(unitOfIssue);
    nsn.setNomenclature(nomenclature);
    nsn.setLlc(llc);
    nsn.setEcs(ecs);
    nsn.setSrrc(srrc);
    nsn.setUiiManaged(uiiManaged);
    nsn.setCiic(ciic);
    nsn.setDla(dla);
    nsn.setPubData(pubData);
    nsn.setOnHand(onHand);
    nsn.setUnitPrice((int) (unitPrice.longValue() * 100));

    return nsn;
  }

  @Override
  public SerialNumber createOrphanSerialNumber(String serialNumber,
                                               String systemNumber) {
    SerialNumber sn = new SerialNumber();
    sn.setSerialNumber(serialNumber);
    sn.setSystemNumber(systemNumber);

    return sn;
  }
}
