package com.NortrupDevelopment.PropertyBook.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.NortrupDevelopment.PropertyBook.model.DatabaseOpenHelper;
import com.NortrupDevelopment.PropertyBook.model.LineNumber;
import com.NortrupDevelopment.PropertyBook.model.PropertyBookContentProvider;
import com.NortrupDevelopment.PropertyBook.model.TableContractLIN;

import java.util.ArrayList;

/**
 * Class used to create LIN objects from the content provider
 * Created by andy on 6/7/14.
 */
public class LINLoader extends AsyncTaskLoader<ArrayList<LineNumber>> {

  private static final String PROJECTION[] = {
      TableContractLIN._ID,
      TableContractLIN.NOMENCLATURE,
      TableContractLIN.SUB_LIN,
      TableContractLIN.LIN,
      TableContractLIN.AUTH_DOC,
      TableContractLIN.AUTHORIZED,
      TableContractLIN.DI,
      TableContractLIN.ERC,
      TableContractLIN.PROPERTY_BOOK_ID,
      TableContractLIN.REQUIRED,
      TableContractLIN.SRI
  };

  private static final String SORT_ORDER = TableContractLIN.LIN + " ASC";

  private boolean mIncludeSubLINs = false;
  private boolean mGroupByLIN = false;
  private int mLinID = -1;
  private int mPBICId = -1;
  private Context mContext;
  private ArrayList<LineNumber> mLoaderData;
  private LINContentObserver mObserver;

  public LINLoader(Context context) {
    super(context);

    mContext = context;
  }

  public void setLinID(int linID) {
    mLinID = linID;
  }

  public void setPBICId(int pbicId) {
    mPBICId = pbicId;
  }

  public void includeSubLINs(boolean includeSubLINs) {
    mIncludeSubLINs = includeSubLINs;
  }

  public void setGroupByLIN(boolean groupByLIN) {
    mGroupByLIN = groupByLIN;
  }

  //<editor-fold desc="Async Task Methods">
  /**
   * Loads data in the background based on arguments in the constructor.
   * @return An ArrayList of LIN objects
   */
  @Override
  public ArrayList<LineNumber> loadInBackground() {
    return queryLINs();
  }

  @Override
  public void deliverResult(ArrayList<LineNumber> data) {
    if(isReset()) {
      //Nothing to do now.
      return;
    }

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
  public void onCanceled(ArrayList<LineNumber> data) {
    super.onCanceled(data);
  }
  //</editor-fold>

  /**
   * Retrieve one or all LINs in the database.
   *
   * <p>
   *   LINs are selected by looking at a combination of two values,
   *   mIncludeSubLINs and if mLinID has been set.  This produces four options
   *   <ol>
   *     <li>Select all LINs that share a base LIN.  This gets all of a LIN and
   *      it's SubLINs.</li>
   *     <li>Select just one LIN, excluding any SubLINs that may exist</li>
   *     <li>Select all LINs but exculde SUB LINs</li>
   *     <li>Select everything in the table. (includeSubLINs = true &
   *      no _ID specified)</li>
   *   </ol>
   * </p>
   * @return LIN object with all information from the database. Null if no LIN
   * is found for the ID.
   */
  private ArrayList<LineNumber> queryLINs() {

    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
    queryBuilder.setTables(TableContractLIN.TABLE_NAME);

    StringBuilder selectionBuilder = new StringBuilder();
    String selectionString = null;
    String[] selectionArgs = null;
    String groupBy = "";

    if(mGroupByLIN) {
      groupBy = TableContractLIN.LIN;
    }

    if(mIncludeSubLINs && mLinID != -1) {
      //Use a subquery to select all of the LINs that share a Line Number
      //with the LIN with the given _ID (mLinID).
      selectionBuilder.append(TableContractLIN.LIN)
          .append(" IN (SELECT ")
          .append(TableContractLIN.LIN)
          .append(" FROM ")
          .append(TableContractLIN.TABLE_NAME)
          .append(" WHERE ")
          .append(TableContractLIN._ID)
          .append(" = ?)");

      selectionArgs = new String[]{String.valueOf(mLinID)};

    } else if(!mIncludeSubLINs && mLinID != -1) {
      //Query for just the specified LIN, don't grab any SubLINs that share the
      //same Line Number (A12345)
      selectionBuilder.append(TableContractLIN._ID + " = ?");

      selectionArgs = new String[]{String.valueOf(mLinID)};

    } else if(!mIncludeSubLINs) {
      //Select all primary LINs in the property book.  Any LIN with a value in
      //SubLIN field is excluded.
      selectionBuilder.append(TableContractLIN.SUB_LIN + " = ''");
    }
    //The fourth case in this set is to select everything from the database
    //this requires no else because the query has no where clause and
    //selectionString remains equal to null

    if(mPBICId >= 0) {
      selectionBuilder.append(" AND " + TableContractLIN.PROPERTY_BOOK_ID +
          " = ?");
    }


    DatabaseOpenHelper dbHelp = new DatabaseOpenHelper(getContext());
    SQLiteDatabase database = dbHelp.getReadableDatabase();

    selectionString  = selectionBuilder.toString();

    Cursor data = queryBuilder.query(
        database,
        PROJECTION,
        selectionString,
        selectionArgs,
        groupBy,
        "",
        SORT_ORDER);

    if(data.getCount() == 0) {
      return null;
    }

    ArrayList<LineNumber> result = new ArrayList<LineNumber>();

    while(data.moveToNext()) {
      result.add(createLINFromCursor(data));
    }
    data.close();
    database.close();
    dbHelp.close();

    return result;
  }


  /**
   * Converts one row of a cursor into a LIN object. The cursor is not advanced
   * or modified by calls to this method.
   * @param data Cursor with data to be converted.
   * @return a LIN object filled with data from the current cursor row.
   */
  private LineNumber createLINFromCursor(Cursor data) {
    return  new LineNumber(
        data.getInt(data.getColumnIndex(TableContractLIN._ID)),
        data.getString(data.getColumnIndex(TableContractLIN.LIN)),
        data.getString(data.getColumnIndex(TableContractLIN.SUB_LIN)),
        data.getString(data.getColumnIndex(TableContractLIN.SRI)),
        data.getString(data.getColumnIndex(TableContractLIN.ERC)),
        data.getString(data.getColumnIndex(TableContractLIN.NOMENCLATURE)),
        data.getString(data.getColumnIndex(TableContractLIN.AUTH_DOC)),
        data.getInt(data.getColumnIndex(TableContractLIN.REQUIRED)),
        data.getInt(data.getColumnIndex(TableContractLIN.AUTHORIZED)),
        data.getInt(data.getColumnIndex(TableContractLIN.DI)),
        data.getInt(data.getColumnIndex(TableContractLIN.PROPERTY_BOOK_ID)));
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
