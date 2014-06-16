package com.NortrupDevelopment.PropertyApp.model;

import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

/**
 * Class used to create LIN objects from the content provider
 * Created by andy on 6/7/14.
 */
public class LINLoader extends AsyncTaskLoader<ArrayList<LIN>> {

  private static final String PROJECTION[] = {
    TableContractLIN._ID,
        TableContractLIN.columnNomenclature,
        TableContractLIN.columnSubLIN,
        TableContractLIN.columnLIN,
        TableContractLIN.columnAuthDoc,
        TableContractLIN.columnAuthorized,
        TableContractLIN.columnDI,
        TableContractLIN.columnERC,
        TableContractLIN.columnPropertyBookId,
        TableContractLIN.columnRequired,
        TableContractLIN.columnSRI
  };

  private static final String SORT_ORDER = TableContractLIN.columnLIN + " ASC";

  private long mLinID;
  private Context mContext;
  private ArrayList<LIN> mLoaderData;
  private LINContentObserver mObserver;


  public LINLoader(long linID, Context context) {
    super(context);

    mLinID = linID;
    mContext = context;
  }

  //<editor-fold desc="Async Task Methods">
  /**
   * Loads data in the background based on arguments in the constructor.
   * @return An ArrayList of LIN objects
   */
  @Override
  public ArrayList<LIN> loadInBackground() {
    return LINFromId(mLinID, mContext);
  }

  @Override
  public void deliverResult(ArrayList<LIN> data) {
    if(isReset()) {
      //Nothing to do now.
      return;
    }

    ArrayList<LIN> oldData = mLoaderData;
    mLoaderData = data;

    if(isStarted()) {
      super.deliverResult(mLoaderData);
    }

  }

  protected void onStartLoading() {
    if(mLoaderData != null) {
      deliverResult(mLoaderData);
    }

    if(mObserver == null) {

      mObserver = new LINContentObserver(new LINChangeHandler(this));

      mContext.getContentResolver().registerContentObserver(
        PropertyBookContentProvider.CONTENT_URI_LIN,
        true,
        mObserver);
    }

    if(takeContentChanged() || mLoaderData == null) {
      forceLoad();
    }

  }

  @Override
  protected  void onStopLoading() {
    cancelLoad();
  }

  @Override
  protected void onReset() {
    onStopLoading();

    //Clear the data
    if(mLoaderData != null) {
      mLoaderData = null;
    }

    //Unregister the content observer
    if(mObserver != null) {
      mContext.getContentResolver().unregisterContentObserver(mObserver);
      mObserver = null;
    }
  }

  @Override
  public void onCanceled(ArrayList<LIN> data) {
    super.onCanceled(data);
  }
  //</editor-fold>

  /**
   * Retrieve one or all LINs in the database.
   * @param linID ID of the LIN requested sending null returns all LINs in the
   *              database.
   * @return LIN object with all information from the database. Null if no LIN
   * is found for the ID.
   */
  private ArrayList<LIN> LINFromId(long linID, Context context) {

    String selectionString = TableContractLIN._ID + " = ?";
    String selectionArgs[] = { Long.toString(linID) };

    ContentResolver resolver = context.getContentResolver();
    Cursor data = resolver.query(PropertyBookContentProvider.CONTENT_URI_LIN,
        PROJECTION,
        selectionString,
        selectionArgs,
        SORT_ORDER);

    if(data.getCount() == 0) {
      return null;
    }

    ArrayList<LIN> result = new ArrayList<LIN>();

    while(data.moveToNext()) {
      result.add(createLINFromCursor(data));
    }
    return result;
  }


  private LIN createLINFromCursor(Cursor data) {
    return  new LIN(
        data.getLong(data.getColumnIndex(TableContractLIN._ID)),
        data.getString(data.getColumnIndex(TableContractLIN.columnLIN)),
        data.getString(data.getColumnIndex(TableContractLIN.columnSubLIN)),
        data.getString(data.getColumnIndex(TableContractLIN.columnSRI)),
        data.getString(data.getColumnIndex(TableContractLIN.columnERC)),
        data.getString(data.getColumnIndex(TableContractLIN.columnNomenclature)),
        data.getString(data.getColumnIndex(TableContractLIN.columnAuthDoc)),
        data.getInt(data.getColumnIndex(TableContractLIN.columnRequired)),
        data.getInt(data.getColumnIndex(TableContractLIN.columnAuthorized)),
        data.getInt(data.getColumnIndex(TableContractLIN.columnDI)));
  }
}

class LINContentObserver extends ContentObserver {

  public LINContentObserver(Handler handler) {
    super(handler);
  }

  @Override
  public void onChange(boolean selfChange) {
     this.onChange(selfChange, null);
  }

  @Override
  public void onChange(boolean selfChange, Uri uri) {

  }

}

class LINChangeHandler extends Handler {

  private AsyncTaskLoader receiver;

  public LINChangeHandler(AsyncTaskLoader receiver) {
    this.receiver = receiver;
  }

  @Override
  public void handleMessage(Message msg) {
    receiver.onContentChanged();
  }

}
