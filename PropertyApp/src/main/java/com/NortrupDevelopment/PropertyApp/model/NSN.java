package com.NortrupDevelopment.PropertyApp.model;

import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentUris;
import android.database.Cursor;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

public class NSN {

  private long nsnId; //unique identifier for the NSN, assigned by the database
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
  private LIN parentLin; //reference to the LIN id to which this NSN belongs.

  private static String queryString = TableContractNSN._ID + " = ?";

  private static String DASH = "-";

  private Multimap<Long, Item> mItemList;

  //Default ID value
  public static final int DEFAULT_ID = -1;

  NSN(long nsnId,
      String nsn,
      String ui,
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
    this.nsnId = nsnId;
    this.nsn = nsn;
    this.ui = ui;
    this.unitPrice = unitPrice;
    this.nomencalture = nomenclature;
    this.llc = llc;
    this.ecs = ecs;
    this.srrc = srrc;
    this.uiiManaged = uiiManaged;
    this.ciic = ciic;
    this.dla = dla;
    this.pubData = pubData;
    this.onHand = onHand;

    mItemList = ArrayListMultimap.create();
  }

  NSN(long nsnId,
      String nsn,
      String ui,
      BigDecimal unitPrice,
      String nomenclature,
      String llc,
      String ecs,
      String srrc,
      String uiiManaged,
      String ciic,
      String dla,
      String pubData,
      int onHand,
      LIN parentLin) {
    this.nsnId = nsnId;
    this.nsn = nsn;
    this.ui = ui;
    this.unitPrice = unitPrice;
    this.nomencalture = nomenclature;
    this.llc = llc;
    this.ecs = ecs;
    this.srrc = srrc;
    this.uiiManaged = uiiManaged;
    this.ciic = ciic;
    this.dla = dla;
    this.pubData = pubData;
    this.onHand = onHand;
    this.parentLin = parentLin;

    mItemList = ArrayListMultimap.create();
  }

  public NSN(String nsn,
             String ui,
             BigDecimal unitPrice,
             String nomenclature,
             String llc,
             String ecs,
             String srrc,
             String uiiManaged,
             String ciic,
             String dla,
             String pubData,
             int onHand,
             LIN parentLin) {
    this(DEFAULT_ID,
        nsn,
        ui,
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
        parentLin);
  }


  public static NSN NSNFromCursor(Cursor cursor) {
    return new NSN(
        cursor.getLong(cursor.getColumnIndex(TableContractNSN._ID)),
        cursor.getString(cursor.getColumnIndex(TableContractNSN.columnNSN)),
        cursor.getString(cursor.getColumnIndex(TableContractNSN.columnUI)),
        new BigDecimal(cursor.getDouble(
            cursor.getColumnIndex(TableContractNSN.columnUnitPrice))),
        cursor.getString(
            cursor.getColumnIndex(TableContractNSN.columnNomenclature)),
        cursor.getString(cursor.getColumnIndex(TableContractNSN.columnLLC)),
        cursor.getString(cursor.getColumnIndex(TableContractNSN.columnECS)),
        cursor.getString(cursor.getColumnIndex(TableContractNSN.columnSRRC)),
        cursor.getString(
            cursor.getColumnIndex(TableContractNSN.columnUIIManaged)),
        cursor.getString(cursor.getColumnIndex(TableContractNSN.columnCIIC)),
        cursor.getString(cursor.getColumnIndex(TableContractNSN.columnDLA)),
        cursor.getString(cursor.getColumnIndex(TableContractNSN.columnPubData)),
        cursor.getInt(cursor.getColumnIndex(TableContractNSN.columnOnHand))
    );
  }


  /**
   * @return the nsnId
   */
  public long getNsnId() {
    return nsnId;
  }

  /**
   * @param nsnId the nsnId to set
   */
  public void setNsnId(long nsnId) {
    this.nsnId = nsnId;
  }

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
   * @param parentLin Parent LIN
   */
  public void setParentLin(LIN parentLin) {
    this.parentLin = parentLin;
  }

  public LIN getParentLin() {
    return parentLin;
  }

  public ArrayList<ContentProviderOperation> getWriteAction(boolean includeSubElements,
                                                            ArrayList<ContentProviderOperation> result,
                                                            int linBackReference) {

    Builder thisAction;
    if (nsnId < 0) {
      thisAction = ContentProviderOperation.newInsert(
          PropertyBookContentProvider.CONTENT_URI_NSN);
    } else {
      thisAction = ContentProviderOperation.newUpdate(
          ContentUris.withAppendedId(
              PropertyBookContentProvider.CONTENT_URI_NSN,
              nsnId)
      );
    }

    thisAction.withValue(TableContractNSN.columnCIIC, ciic)
        .withValue(TableContractNSN.columnDLA, dla)
        .withValue(TableContractNSN.columnECS, ecs)
        .withValue(TableContractNSN.columnLLC, llc)
        .withValue(TableContractNSN.columnNomenclature, nomencalture)
        .withValue(TableContractNSN.columnNSN, nsn)
        .withValue(TableContractNSN.columnOnHand, onHand)
        .withValue(TableContractNSN.columnPubData, pubData)
        .withValue(TableContractNSN.columnSRRC, srrc)
        .withValue(TableContractNSN.columnUI, ui)
        .withValue(TableContractNSN.columnUIIManaged, uiiManaged)
        .withValue(TableContractNSN.columnUnitPrice,
            unitPrice.toPlainString());

    thisAction.withValueBackReference(TableContractNSN.linID,
        linBackReference);

    result.add(thisAction.build());

    if (includeSubElements) {
      int backReference = result.size() - 1;
      for(Item item : mItemList.values())
      {
        result.add(item.getWriteAction(backReference));
      }
    }

    return result;
  }

  public ArrayList<ContentProviderOperation> getDeleteAction() {
    ArrayList<ContentProviderOperation> result =
        new ArrayList<ContentProviderOperation>();

    //Delete Nested Items
    for(Item item : mItemList.values()) {
      result.add(item.getDeleteAction());
    }

    Builder deleteAction = ContentProviderOperation.newDelete(
        PropertyBookContentProvider.CONTENT_URI_NSN);

    String selectionArgs[] = {String.valueOf(nsnId)};

    deleteAction.withSelection(queryString, selectionArgs);
    result.add(deleteAction.build());

    return result;
  }


  public void addItem(Item newItem) {
    mItemList.put(newItem.getItemId(), newItem);
  }

  public void removeItem(Item remove) {
    mItemList.remove(remove.getItemId(), remove);
  }

  public ArrayList<Item> getItemList() {
    return new ArrayList<Item>(mItemList.values());
  }


}
