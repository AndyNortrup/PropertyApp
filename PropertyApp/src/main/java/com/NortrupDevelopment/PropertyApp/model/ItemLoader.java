package com.NortrupDevelopment.PropertyApp.model;

import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

/**
 * NSNLoader loads NSNs from the database
 * Created by andy on 6/13/14.
 */
public class ItemLoader extends AsyncTaskLoader<ArrayList<Item>> {


  public String ITEM_PROJECTION[] = {
      TableContractItem._ID,
      TableContractItem.columnNsnId,
      TableContractItem.columnSerialNumber,
      TableContractItem.columnSysNo
  };

  private String SORT_ORDER = TableContractItem.columnSerialNumber + " ASC";

  private Context mContext;

  private long mNSNId;
  private ArrayList<Item> mData;
  private ItemChangeHandler mObserver;

  public ItemLoader(Context context) {
    super(context);
    mContext = context;
    mNSNId = -1;
  }

  @Override
  public ArrayList<Item> loadInBackground() {
    if(mNSNId != -1) {
      return itemFromNSNID();
    }
    return null;
  }

  private ArrayList<Item> itemFromNSNID() {
    ArrayList<Item> result = new ArrayList<Item>();

    String selectionString = TableContractItem.columnNsnId + " = ?";
    String selectionArgs[] = { Long.toString(mNSNId) };

    ContentResolver resolver = mContext.getContentResolver();
    Cursor data = resolver.query(PropertyBookContentProvider.CONTENT_URI_ITEM,
        ITEM_PROJECTION,
        selectionString,
        selectionArgs,
        SORT_ORDER);

    while(data.moveToNext()) {
      result.add(Item.itemFromCursor(data));
    }

    return result;
  }

  @Override
  public void deliverResult(ArrayList<Item> data) {
    if(isReset()) {
      return;
    }

    mData = data;

    if(isStarted()) {
      super.deliverResult(data);
    }
  }

  @Override
  protected void onStartLoading() {
    if(mData != null) {
      deliverResult(mData);
    }

    if(mObserver == null) {
      mObserver = new ItemChangeHandler(this);
    }

    if(takeContentChanged() || mData == null) {
      forceLoad();
    }
  }

  @Override
  protected void onStopLoading() {
    cancelLoad();
  }

  @Override
  protected void onReset() {
    if(mData != null) {
      mData = null;
    }
  }


  /**
   * Sets the NSN Id as a parameter for search.
   * @param nsnId The database id of the NSN that all Items belong too.
   */
  public void setNSNID(long nsnId) {
    mNSNId = nsnId;
  }

  class ItemChangeHandler extends Handler
  {

    private AsyncTaskLoader receiver;

    public ItemChangeHandler(AsyncTaskLoader receiver) {
      this.receiver = receiver;
    }

    @Override
    public void handleMessage(Message msg) {
      receiver.onContentChanged();
    }
  }
}
