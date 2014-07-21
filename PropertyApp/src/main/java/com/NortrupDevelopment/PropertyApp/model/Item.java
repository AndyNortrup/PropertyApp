package com.NortrupDevelopment.PropertyApp.model;


import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * This is the basic data structure to track a serial numbered property book
 * item.
 * Created by andy on 5/16/13.
 */
public class Item implements Parcelable{

  private int itemId;
  private String serialNumber;
  private String sysNo;
  private NSN nsn;

  private String queryString = TableContractItem._ID + " = ?";

  //Default ID value
  public static final int DEFAULT_ID = -1;

  private Item(
      String serialNumber,
      String sysNo,
      int itemId,
      NSN nsn
  ) {
    this.serialNumber = serialNumber;
    this.sysNo = sysNo;
    this.itemId = itemId;
    this.nsn = nsn;
  }

  public Item(String serialNumber,
              String sysNo,
              NSN nsn) {
    this(serialNumber, sysNo, DEFAULT_ID, nsn);
  }

  public Item(Parcel in) {
    serialNumber = in.readString();
    sysNo = in.readString();
    itemId = in.readInt();
  }

  public static Item itemFromCursor(Cursor data) {
    return new Item(
        data.getString(data.getColumnIndex(TableContractItem.columnSerialNumber)),
        data.getString(data.getColumnIndex(TableContractItem.columnSysNo)),
        data.getInt(data.getColumnIndex(TableContractItem._ID)),
        null);
  }

  public int getItemId() {
    return itemId;
  }

  public void setItemId(int itemId) {
    this.itemId = itemId;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public String getSysNo() {
    return sysNo;
  }

  public void setSysNo(String sysNo) {
    this.sysNo = sysNo;
  }

  public NSN getNsn() {
    return nsn;
  }

  public void setNsn(NSN nsn) {
    this.nsn = nsn;
  }

  /**
   * Generates either an Insert or an Update action based on if the item has
   * an id already.
   *
   * @return ContentProviderOperation to insert this NSN into the database or
   * update the record in the database.
   */
  public ContentProviderOperation getWriteAction(int backReference) {

    Builder updateAction;
    if (itemId == -1) {
      updateAction = ContentProviderOperation.newInsert(
          PropertyBookContentProvider.CONTENT_URI_ITEM);
    } else {
      updateAction = ContentProviderOperation.newUpdate(
          ContentUris.withAppendedId(
              PropertyBookContentProvider.CONTENT_URI_ITEM,
              itemId)
      );

      String queryValues[] = {String.valueOf(itemId)};
      updateAction.withSelection(queryString, queryValues);
    }


    ContentValues values = new ContentValues();
    updateAction.withValue(TableContractItem.columnSerialNumber, serialNumber)
        .withValue(TableContractItem.columnSysNo, sysNo)
        .withValueBackReference(TableContractItem.columnNsnId,
            backReference);

    return updateAction.build();
  }


  /**
   * Generates a ContentProvider Delete Operation to delete this item.
   *
   * @return ContentProviderOperation for this item.
   */
  public ContentProviderOperation getDeleteAction() {
    Builder deleteAction = ContentProviderOperation.newDelete(
        PropertyBookContentProvider.CONTENT_URI_ITEM);

    String queryValues[] = {String.valueOf(itemId)};

    deleteAction.withSelection(queryString, queryValues);

    return deleteAction.build();
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
    dest.writeString(serialNumber);
    dest.writeString(sysNo);
    dest.writeInt(itemId);
  }

  public static class ItemCreator implements Parcelable.Creator<Item> {

    /**
     * Create a new instance of the Parcelable class, instantiating it
     * from the given Parcel whose data had previously been written by
     * {@link android.os.Parcelable#writeToParcel Parcelable.writeToParcel()}.
     *
     * @param source The Parcel to read the object's data from.
     * @return Returns a new instance of the Parcelable class.
     */
    @Override
    public Item createFromParcel(Parcel source) {
      return new Item(source);
    }

    /**
     * Create a new array of the Parcelable class.
     *
     * @param size Size of the array.
     * @return Returns an array of the Parcelable class, with every entry
     * initialized to null.
     */
    @Override
    public Item[] newArray(int size) {
      return new Item[0];
    }
  }
}
