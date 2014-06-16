package com.NortrupDevelopment.PropertyApp.view;

import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;

import com.NortrupDevelopment.PropertyApp.model.NSN;
import com.NortrupDevelopment.PropertyApp.model.PropertyBookContentProvider;
import com.NortrupDevelopment.PropertyApp.model.TableContractNSN;

import java.util.ArrayList;

/**
 * NSNLoader loads NSNs from the database
 * Created by andy on 6/13/14.
 */
public class NSNLoader extends AsyncTaskLoader<ArrayList<NSN>> {

  public String LIN_PROJECTION[] = {
      TableContractNSN._ID,
      TableContractNSN.columnPubData,
      TableContractNSN.columnCIIC,
      TableContractNSN.columnUIIManaged,
      TableContractNSN.columnUI,
      TableContractNSN.columnDLA,
      TableContractNSN.columnECS,
      TableContractNSN.columnLLC,
      TableContractNSN.columnNomenclature,
      TableContractNSN.columnNSN,
      TableContractNSN.columnOnHand,
      TableContractNSN.columnSRRC,
      TableContractNSN.columnUnitPrice
  };

  private String SORT_ORDER = TableContractNSN.columnNSN + " ASC";

  private Context mContext;

  private long mLINId;
  private ArrayList<NSN> mData;
  private NSNChangeHandler mObserver;

  public NSNLoader(Context context) {
    super(context);
    mLINId = -1;
  }

  @Override
  public ArrayList<NSN> loadInBackground() {
    if(mLINId != -1) {
      return NSNFromId();
    }
    return null;
  }

  private ArrayList<NSN> NSNFromId() {
    ArrayList<NSN> result = new ArrayList<NSN>();

    String selectionString = TableContractNSN.linID + "?";
    String selectionArgs[] = { Long.toString(mLINId) };

    ContentResolver resolver = mContext.getContentResolver();
    Cursor data = resolver.query(PropertyBookContentProvider.CONTENT_URI_NSN,
        LIN_PROJECTION,
        selectionString,
        selectionArgs,
        SORT_ORDER);

    while(data.moveToNext()) {
      result.add(NSN.NSNFromCursor(data));
    }

    return result;
  }

  @Override
  public void deliverResult(ArrayList<NSN> data) {
    if(isReset()) {
      return;
    }

    ArrayList<NSN> oldData = mData;
    mData = data;

    if(isStarted()) {
      super.deliverResult(data);
    }

    if(oldData != null && oldData != data) {
      //do nothing;
    }
  }

  @Override
  protected void onStartLoading() {
    if(mData != null) {
      deliverResult(mData);
    }

    if(mObserver == null) {
      mObserver = new NSNChangeHandler(this);
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


  public void setLIN(long linId) {
    mLINId = linId;
  }

  class NSNChangeHandler extends Handler
  {

    private AsyncTaskLoader receiver;

    public NSNChangeHandler(AsyncTaskLoader receiver) {
      this.receiver = receiver;
    }

    @Override
    public void handleMessage(Message msg) {
      receiver.onContentChanged();
    }
  }
}
