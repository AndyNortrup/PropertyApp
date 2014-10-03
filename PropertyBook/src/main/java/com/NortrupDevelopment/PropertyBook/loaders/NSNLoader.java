package com.NortrupDevelopment.PropertyBook.loaders;

import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;

import com.NortrupDevelopment.PropertyBook.model.StockNumber;
import com.NortrupDevelopment.PropertyBook.model.PropertyBookContentProvider;
import com.NortrupDevelopment.PropertyBook.model.TableContractNSN;

import java.util.ArrayList;

/**
 * NSNLoader loads NSNs from the database
 * Created by andy on 6/13/14.
 */
public class NSNLoader extends AsyncTaskLoader<ArrayList<StockNumber>> {

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
  private ArrayList<StockNumber> mData;
  private NSNChangeHandler mObserver;

  public NSNLoader(Context context) {
    super(context);
    mContext = context;
    mLINId = -1;
  }

  @Override
  public ArrayList<StockNumber> loadInBackground() {
    if(mLINId != -1) {
      return NSNFromId();
    }
    return null;
  }

  private ArrayList<StockNumber> NSNFromId() {
    ArrayList<StockNumber> result = new ArrayList<StockNumber>();

    String selectionString = TableContractNSN.linID + " = ?";
    String selectionArgs[] = { Long.toString(mLINId) };

    ContentResolver resolver = mContext.getContentResolver();
    Cursor data = resolver.query(PropertyBookContentProvider.CONTENT_URI_NSN,
        LIN_PROJECTION,
        selectionString,
        selectionArgs,
        SORT_ORDER);

    while(data.moveToNext()) {
      result.add(StockNumber.NSNFromCursor(data));
    }

    data.close();

    return result;
  }

  @Override
  public void deliverResult(ArrayList<StockNumber> data) {
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
      mObserver = new NSNChangeHandler(this);
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


  public void setLIN(long linId) {
    mLINId = linId;
  }

  public long getLIN() {
    return mLINId;
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
