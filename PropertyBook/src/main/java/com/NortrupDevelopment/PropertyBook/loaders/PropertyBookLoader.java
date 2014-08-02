package com.NortrupDevelopment.PropertyBook.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.NortrupDevelopment.PropertyBook.model.DatabaseOpenHelper;
import com.NortrupDevelopment.PropertyBook.model.PropertyBook;
import com.NortrupDevelopment.PropertyBook.model.PropertyBookContentProvider;
import com.NortrupDevelopment.PropertyBook.model.TableContractPropertyBook;

import java.util.ArrayList;

/**
 * Class is used to load Property Book information from the database
 * asynchronously.
 * Created by andy on 7/22/14.
 */
public class PropertyBookLoader
    extends AsyncTaskLoader<ArrayList<PropertyBook>> {

  int mPropertyBookId;
  ArrayList<PropertyBook> mLoaderData;
  PropertyBookContentObserver mObserver;

  private static final String[] PROJECTION = new String[] {
    TableContractPropertyBook.DESCRIPTION,
    TableContractPropertyBook.PBIC,
    TableContractPropertyBook.UIC,
    TableContractPropertyBook._ID
  };

  private static final String WHERE_STRING_ID
      = TableContractPropertyBook._ID + "= ?";


  public PropertyBookLoader(Context context) {
    super(context);

  }

  public void setPropertyBookID(int propertyBookID) {
    mPropertyBookId = propertyBookID;
  }


  //<editor-fold desc="Async Task Methods">

  /**
   * Called on a worker thread to perform the actual load and to return
   * the result of the load operation.
   * <p/>
   * Implementations should not deliver the result directly, but should return them
   * from this method, which will eventually end up calling {@link #deliverResult} on
   * the UI thread.  If implementations need to process the results on the UI thread
   * they may override {@link #deliverResult} and do so there.
   * <p/>
   * To support cancellation, this method should periodically check the value of
   * {@link #isLoadInBackgroundCanceled} and terminate when it returns true.
   * Subclasses may also override {@link #cancelLoadInBackground} to interrupt the load
   * directly instead of polling {@link #isLoadInBackgroundCanceled}.
   * <p/>
   * When the load is canceled, this method may either return normally or throw
   * OperationCanceledException.  In either case, the Loader will
   * call {@link #onCanceled} to perform post-cancellation cleanup and to dispose of the
   * result object, if any.
   *
   * @return The result of the load operation.
   * @see #isLoadInBackgroundCanceled
   * @see #cancelLoadInBackground
   * @see #onCanceled
   */
  @Override
  public ArrayList<PropertyBook> loadInBackground() {
    return queryPropertyBook();
  }

  @Override
  public void deliverResult(ArrayList<PropertyBook> data) {
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

      mObserver = new PropertyBookContentObserver(new PropertyBookChangeHandler(this));

      getContext().getContentResolver().registerContentObserver(
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
      getContext().getContentResolver().unregisterContentObserver(mObserver);
      mObserver = null;
    }
  }

  @Override
  public void onCanceled(ArrayList<PropertyBook> data) {
    super.onCanceled(data);
  }
  //</editor-fold>

  /**
   * Query the database using information provided to the class
   * @return An ArrayList of PropertyBook objects resulting from the search.
   */
  public ArrayList<PropertyBook> queryPropertyBook() {
    ArrayList<PropertyBook> result = new ArrayList<PropertyBook>();

    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
    queryBuilder.setTables(TableContractPropertyBook.TABLE_NAME);
    ArrayList<String> queryArgs = new ArrayList<String>();
    StringBuilder queryString = new StringBuilder();
    String sortOrder = TableContractPropertyBook._ID + " ASC";

    if(mPropertyBookId > 0) {
      queryString.append(WHERE_STRING_ID);
      queryArgs.add(String.valueOf(mPropertyBookId));
    }

    DatabaseOpenHelper dbHelper = new DatabaseOpenHelper(getContext());

    Cursor cursor = queryBuilder.query(
        dbHelper.getReadableDatabase(),
        PROJECTION,
        queryString.toString(),
        queryArgs.toArray(new String[queryArgs.size()]),
        null,
        null,
        sortOrder
    );

    while(cursor.moveToNext()) {
      result.add(propertyBookFromCursor(cursor));
    }
    return result;
  }

  /**
   * Creates a property book object from the given cursor.  Method does not
   * advance the cursor.
   * @param data Cursor full of property book information.
   * @return A PropertyBook object with data from the cursor.
   */
  private PropertyBook propertyBookFromCursor(Cursor data) {
    return new PropertyBook(
        data.getInt(data.getColumnIndex(TableContractPropertyBook._ID)),
        data.getString(data.getColumnIndex(TableContractPropertyBook.DESCRIPTION)),
        data.getString(data.getColumnIndex(TableContractPropertyBook.UIC)),
        data.getString(data.getColumnIndex(TableContractPropertyBook.PBIC)));
  }
}


class PropertyBookContentObserver extends ContentObserver {

  public PropertyBookContentObserver(Handler handler) {
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

class PropertyBookChangeHandler extends Handler {

  private AsyncTaskLoader receiver;

  public PropertyBookChangeHandler(AsyncTaskLoader receiver) {
    this.receiver = receiver;
  }

  @Override
  public void handleMessage(Message msg) {
    receiver.onContentChanged();
  }

}
