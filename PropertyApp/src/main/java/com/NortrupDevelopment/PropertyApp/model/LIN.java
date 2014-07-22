package com.NortrupDevelopment.PropertyApp.model;

import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentUris;
import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Hashtable;

public class LIN implements Parcelable {

  private int linId;
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
  private Hashtable<String, NSN> mNewNSNs;
  private SparseArray<NSN> mNSNs;

  private static String queryString = TableContractLIN._ID + " = ?";

  //Default ID value
  public static final int DEFAULT_ID = -1;


  /**
   * Creates a LIN object representing information for an individual LIN Number
   *
   * @param linId        Unique identifier for the LIN in the database
   * @param lin          Input LIN
   * @param subLin       Input SubLIN
   * @param sri          Input SRI Code
   * @param erc          Input ERC Code
   * @param nomenclature Input Nomenclature
   * @param authDoc      Input authorizing document
   * @param required     Input quantity required
   * @param authorized   Input quantity authorized
   */
  public LIN(
      int linId,
      String lin,
      String subLin,
      String sri,
      String erc,
      String nomenclature,
      String authDoc,
      int required,
      int authorized,
      int di,
      PropertyBook propertyBook,
      Hashtable<String, NSN> nsnList,
      int propertyBookID) {

    this.linId = linId;
    this.lin = lin;
    this.subLin = subLin;
    this.sri = sri;
    this.erc = erc;
    this.nomenclature = nomenclature;
    this.authDoc = authDoc;
    this.required = required;
    this.authorized = authorized;
    this.di = di;
    this.propertyBook = propertyBook;
    this.mNewNSNs = nsnList;

    mNSNs = new SparseArray<NSN>();
    mPropertyBookID = propertyBookID;

  }

  /**
   * Creates a LIN object representing information for an individual LIN
   * without having a property book object to nest it in.
   *
   * @param lin          Input LIN
   * @param subLin       Input SubLIN
   * @param sri          Input SRI Code
   * @param erc          Input ERC Code
   * @param nomenclature Input Nomenclature
   * @param authDoc      Input authorizing document
   * @param required     Input quantity required
   * @param authorized   Input quantity authorized
   */
  public LIN(
      int _id,
      String lin,
      String subLin,
      String sri,
      String erc,
      String nomenclature,
      String authDoc,
      int required,
      int authorized,
      int di) {

    this(_id,
        lin,
        subLin,
        sri,
        erc,
        nomenclature,
        authDoc,
        required,
        authorized,
        di,
        null,
        new Hashtable<String, NSN>(),
        -1);

  }

  /**
   * Creates a LIN object representing information for an individual LIN
   * without having a property book object to nest it in, but does provide a
   * property book ID for later look up.
   *
   * @param lin          Input LIN
   * @param subLin       Input SubLIN
   * @param sri          Input SRI Code
   * @param erc          Input ERC Code
   * @param nomenclature Input Nomenclature
   * @param authDoc      Input authorizing document
   * @param required     Input quantity required
   * @param authorized   Input quantity authorized
   */
  public LIN(
      int _id,
      String lin,
      String subLin,
      String sri,
      String erc,
      String nomenclature,
      String authDoc,
      int required,
      int authorized,
      int di,
      int propertyBookID) {

    this(_id,
        lin,
        subLin,
        sri,
        erc,
        nomenclature,
        authDoc,
        required,
        authorized,
        di,
        null,
        new Hashtable<String, NSN>(),
        propertyBookID);

  }

  /**
   * Creates a LIN object representing information for an individual LIN
   * Number without having a linID from the database.
   *
   * @param lin          Input LIN
   * @param subLin       Input SubLIN
   * @param sri          Input SRI Code
   * @param erc          Input ERC Code
   * @param nomenclature Input Nomenclature
   * @param authDoc      Input authorizing document
   * @param required     Input quantity required
   * @param authorized   Input quantity authorized
   */
  public LIN(
      String lin,
      String subLin,
      String sri,
      String erc,
      String nomenclature,
      String authDoc,
      int required,
      int authorized,
      int di) {

    this(DEFAULT_ID,
        lin,
        subLin,
        sri,
        erc,
        nomenclature,
        authDoc,
        required,
        authorized,
        di,
        null,
        new Hashtable<String, NSN>(),
        -1);

  }

  public LIN(Parcel out) {
    linId = out.readInt();
    lin = out.readString();
    subLin = out.readString();
    sri = out.readString();
    erc = out.readString();
    nomenclature = out.readString();
    authDoc = out.readString();
    required = out.readInt();
    authorized = out.readInt();
    di = out.readInt();

    //Recreate the mNSN frome pacel
    mNSNs = new SparseArray<NSN>();
    ArrayList<NSN> stockNumberList = new ArrayList<NSN>();
    out.readTypedList(stockNumberList, new NSN.NSNCreator());
    for(NSN nsn : stockNumberList) {
      mNSNs.put((int)nsn.getNsnId(), nsn);
      nsn.setParentLin(this);
    }

    mNewNSNs = new Hashtable<String, NSN>();
    out.readTypedList(stockNumberList, new NSN.NSNCreator());
    for(NSN nsn : stockNumberList) {
      mNewNSNs.put(nsn.getNsn(), nsn);
      nsn.setParentLin(this);
    }
  }

  /**
   * @return the linID
   */
  public int getLinId() {
    return linId;
  }


  /**
   * @param linID the linID to set
   */
  public void setLinID(int linID) {
    this.linId = linID;
  }


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
  public String getNomencalture() {
    return nomenclature;
  }


  /**
   * @param nomencalture set the nomenclature
   */
  public void setNomencalture(String nomencalture) {
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

  public Hashtable<String, NSN> getNewNsnList() {
    return mNewNSNs;
  }

  public NSN getNSNById(int nsn) {
    return mNSNs.get(nsn);
  }

  public SparseArray<NSN> getNSNs() {
    return mNSNs;
  }

  public void addNSN(NSN newNSN) {
    if(newNSN.getNsnId() != NSN.DEFAULT_ID) {
      mNSNs.put((int) newNSN.getNsnId(), newNSN);
    } else {
      mNewNSNs.put(newNSN.getNsn(), newNSN);
    }
  }

  public void deleteNSN(NSN nsn) {
    mNewNSNs.remove(nsn.getNsn());
  }

  public ArrayList<ContentProviderOperation> getWriteAction(
      boolean includeSubElements,
      ArrayList<ContentProviderOperation> result,
      int propertyBookBackReference) {

    ContentValues values = new ContentValues();

    values.put(TableContractLIN.LIN, lin);
    values.put(TableContractLIN.SUB_LIN, subLin);
    values.put(TableContractLIN.AUTH_DOC, authDoc);
    values.put(TableContractLIN.AUTHORIZED, authorized);
    values.put(TableContractLIN.ERC, erc);
    values.put(TableContractLIN.NOMENCLATURE, nomenclature);
    values.put(TableContractLIN.REQUIRED, required);
    values.put(TableContractLIN.DI, di);
    values.put(TableContractLIN.SRI, sri);


    if (propertyBook != null) {
      values.put(TableContractLIN.PROPERTY_BOOK_ID,
          propertyBook.getPropertyBookId());
    }

    Builder builder;

    if (linId > 0) {
      values.put(TableContractLIN._ID, linId);
      builder = ContentProviderOperation.newUpdate(
          PropertyBookContentProvider.CONTENT_URI_LIN);

      String[] selectionArgs = {String.valueOf(linId)};
      builder.withSelection(queryString, selectionArgs);
    } else {
      builder = ContentProviderOperation.newInsert(
          PropertyBookContentProvider.CONTENT_URI_LIN);
    }

    builder.withValueBackReference(TableContractLIN.PROPERTY_BOOK_ID,
        propertyBookBackReference);

    builder.withValues(values);

    result.add(builder.build());

    if (includeSubElements) {
      int linBackReference = result.size() - 1;

      //Add NSNs with IDs
      for(int x=0; x<mNSNs.size(); x++) {
        NSN nsn = mNSNs.valueAt(x);
        result = nsn.getWriteAction(includeSubElements,
            result,
            linBackReference);
      }

      //Add new NSNs to the mix
      for(NSN nsn : mNewNSNs.values()) {
        result = nsn.getWriteAction(includeSubElements,
            result,
            linBackReference);
      }
    }

    return result;
  }

  public ArrayList<ContentProviderOperation> getDeleteAction() {
    ArrayList<ContentProviderOperation> result =
        new ArrayList<ContentProviderOperation>();

    result.add(ContentProviderOperation.newDelete(
        ContentUris.withAppendedId(
            PropertyBookContentProvider.CONTENT_URI_LIN, linId)
    ).build());

    for(NSN nsn : mNewNSNs.values()) {
      result.addAll(nsn.getDeleteAction());
    }

    return result;
  }

  @Override
  public String toString() {
    return getLin() + " " + getNomencalture();
  }

  /**
   * Describe the kinds of special objects contained in this Parcelable's
   * marshalled representation.
   *
   * @return a bitmask indicating the set of special object types marshalled
   * by the Parcelable.
   */
  @Override
  public int describeContents() {
    return 0;
  }

  /**
   * Flatten this object in to a Parcel.
   *
   * @param dest  The Parcel in which the object should be written.
   * @param flags Additional flags about how the object should be written.
   *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
   */
  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(linId);
    dest.writeString(lin);
    dest.writeString(subLin);
    dest.writeString(sri);
    dest.writeString(erc);
    dest.writeString(nomenclature);
    dest.writeString(authDoc);
    dest.writeInt(required);
    dest.writeInt(authorized);
    dest.writeInt(di);

    NSN[] nsns = new NSN[mNSNs.size()];
    for(int x=0; x<mNSNs.size(); x++) {
      nsns[x] = mNSNs.valueAt(x);
    }
    dest.writeTypedArray(nsns, 0);

    dest.writeTypedArray(mNewNSNs.values().toArray(nsns), 0);
  }
}
