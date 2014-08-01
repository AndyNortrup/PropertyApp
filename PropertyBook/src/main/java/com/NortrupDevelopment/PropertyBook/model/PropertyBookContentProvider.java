package com.NortrupDevelopment.PropertyBook.model;


import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.ArrayList;

public class PropertyBookContentProvider extends ContentProvider {

  private DatabaseOpenHelper mDatabaseHelper;
  private Context context;

  public static final String AUTHORITY =
      "com.NortrupDevelopment.PropertyApp.provider";

  private static UriMatcher uriMatcher;

  private static final int URI_CODE_ITEM = 0;
  private static final int URI_CODE_LIN = 1;
  private static final int URI_CODE_NSN = 2;
  private static final int URI_CODE_PROPERTY_BOOK = 3;
  private static final int URI_CODE_ITEM_DATA = 4;
  private static final int URI_CODE_NUM_LINS = 5;
  private static final int URI_CODE_NUM_ITEMS = 6;
  private static final int URI_CODE_TOTAL_VALUE = 7;

  private static final String PATH_ITEM = "item";
  private static final String PATH_LIN = "lin";
  private static final String PATH_NSN = "nsn";
  private static final String PATH_PROPERTY_BOOK = "property_book";
  private static final String PATH_ITEM_DATA = "item_data";
  private static final String PATH_NUM_LINS = "num_lins";
  private static final String PATH_NUM_ITEMS = "num_items";
  private static final String PATH_TOTAL_VALUE = "total_value";

  public static final Uri CONTENT_URI_ITEM = Uri.parse(
      "content://" + AUTHORITY + "/" + PATH_ITEM);
  public static final Uri CONTENT_URI_LIN = Uri.parse(
      "content://" + AUTHORITY + "/" + PATH_LIN);
  public static final Uri CONTENT_URI_NSN = Uri.parse(
      "content://" + AUTHORITY + "/" + PATH_NSN);
  public static final Uri CONTENT_URI_PROPERTY_BOOK = Uri.parse(
      "content://" + AUTHORITY + "/" + PATH_PROPERTY_BOOK);
  public static final Uri CONTENT_URI_ITEM_DATA_VIEW = Uri.parse(
      "content://" + AUTHORITY + "/" + PATH_ITEM_DATA);
  public static final Uri CONTENT_URI_ITEM_NUM_LINS = Uri.parse(
      "content://" + AUTHORITY + "/" + PATH_NUM_LINS);
  public static final Uri CONTENT_URI_ITEM_NUM_ITEMS = Uri.parse(
      "content://" + AUTHORITY + "/" + PATH_NUM_ITEMS);
  public static final Uri CONTENT_URI_ITEM_TOTAL_VALUE = Uri.parse(
      "content://" + AUTHORITY + "/" + PATH_TOTAL_VALUE);

  private boolean batchMode = false;
  private ArrayList<Uri> mBatchChangeNotifyList;


  /**
   * Alias for the column name for the total value of the property book
   */
  public static final String ALIAS_TOTAL_PRICE = "total_price";
  private static final String GET_TOTAL_VALUE =
      "SELECT SUM(" + TableContractNSN.columnUnitPrice +
          " * " +
          TableContractNSN.columnOnHand + ") AS " + ALIAS_TOTAL_PRICE +
          " FROM " + TableContractNSN.tableName;

  /**
   * Alias for column name for the total number of LINs
   */
  public static final String ALIAS_TOTAL_LINS = "total_lins";
  private static final String GET_TOTAL_LINS =
      "SELECT COUNT(*) as " + ALIAS_TOTAL_LINS + " FROM " +
          TableContractLIN.tableName;

  /**
   * Alias for the column name for the total number of Items on the PB
   */
  public static final String ALIAS_TOTAL_ITEMS = "total_items";
  private static final String GET_TOTAL_ITEMS =
      "SELECT SUM(" + TableContractNSN.columnOnHand + ")" +
          " as " + ALIAS_TOTAL_ITEMS +
          " FROM " + TableContractNSN.tableName;

  public PropertyBookContentProvider(Context context) {
    this.context = context;
    mDatabaseHelper = new DatabaseOpenHelper(context);
    mBatchChangeNotifyList = new ArrayList<Uri>();
  }


  //Empty constructor for installation of the Content Provider
  public PropertyBookContentProvider() {

  }

  static {
    uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    uriMatcher.addURI(AUTHORITY, PATH_ITEM, URI_CODE_ITEM);
    uriMatcher.addURI(AUTHORITY, PATH_LIN, URI_CODE_LIN);
    uriMatcher.addURI(AUTHORITY, PATH_NSN, URI_CODE_NSN);
    uriMatcher.addURI(
        AUTHORITY, PATH_PROPERTY_BOOK, URI_CODE_PROPERTY_BOOK);
    uriMatcher.addURI(AUTHORITY, PATH_ITEM_DATA, URI_CODE_ITEM_DATA);
    uriMatcher.addURI(AUTHORITY, PATH_NUM_LINS, URI_CODE_NUM_LINS);
    uriMatcher.addURI(AUTHORITY, PATH_NUM_ITEMS, URI_CODE_NUM_ITEMS);
    uriMatcher.addURI(AUTHORITY, PATH_TOTAL_VALUE, URI_CODE_TOTAL_VALUE);
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    int deletedRows = 0;

    switch (uriMatcher.match(uri)) {

      case URI_CODE_LIN:
        deletedRows = mDatabaseHelper.getWritableDatabase().delete(
            TableContractLIN.tableName, selection, selectionArgs);
        break;
      case URI_CODE_NSN:
        deletedRows = mDatabaseHelper.getWritableDatabase().delete(
            TableContractNSN.tableName, selection, selectionArgs);
        break;
      case URI_CODE_ITEM:
        deletedRows = mDatabaseHelper.getWritableDatabase().delete(
            TableContractItem.tableName, selection, selectionArgs);
        break;
      case URI_CODE_PROPERTY_BOOK:
        deletedRows = mDatabaseHelper.getWritableDatabase().delete(
            TableContractPropertyBook.TABLE_NAME,
            selection,
            selectionArgs);
        break;
    }

    if (deletedRows > 0 && !batchMode) {
      getContext().getContentResolver().notifyChange(uri, null);
    } else if (!mBatchChangeNotifyList.contains(uri)) {
      mBatchChangeNotifyList.add(uri);
    }

    return deletedRows;
  }

  @Override
  public String getType(Uri uri) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Uri insert(Uri uri, ContentValues values)
      throws NullPointerException {
    long id = -1;
    String path = "";

    switch (uriMatcher.match(uri)) {
      case URI_CODE_LIN:
        //must have a LIN number
        if (values.containsKey(TableContractLIN.columnLIN) &&
            !values.getAsString(TableContractLIN.columnLIN)
                .equals("")) {

          id = mDatabaseHelper.getWritableDatabase().insert(
              TableContractLIN.tableName, "", values);
          path = PATH_LIN;
        }
        break;
      case URI_CODE_NSN:
        //must have a NSN
        if (values.containsKey(TableContractNSN.columnNSN) &&
            !values.getAsString(TableContractNSN.columnNSN)
                .equals("")) {
          id = mDatabaseHelper.getWritableDatabase().insert(
              TableContractNSN.tableName, "", values);
          path = PATH_NSN;
        }
        break;
      case URI_CODE_ITEM:
        //must have a serial number
        if (values.containsKey(TableContractItem.columnSerialNumber) &&
            !values.getAsString(TableContractItem.columnSerialNumber)
                .equals("")) {

          id = mDatabaseHelper.getWritableDatabase().insert(
              TableContractItem.tableName, "", values);
          path = PATH_ITEM;
        }
      case URI_CODE_PROPERTY_BOOK:
        //must have a description
        if (values.containsKey(
            TableContractPropertyBook.DESCRIPTION) &&
            !values.getAsString(
                TableContractPropertyBook.DESCRIPTION)
                .equals("")) {
          id = mDatabaseHelper.getWritableDatabase().insert(
              TableContractPropertyBook.TABLE_NAME, "", values);
          path = PATH_ITEM;
        }
    }

    if (!batchMode) {
      context.getContentResolver().notifyChange(uri, null);
    } else if (!mBatchChangeNotifyList.contains(uri)) {
      mBatchChangeNotifyList.add(uri);
    }
    return Uri.parse(path + "/" + id);
  }

  @Override
  public boolean onCreate() {
    mDatabaseHelper = new DatabaseOpenHelper(getContext());
    return true;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection,
                      String[] selectionArgs, String sortOrder)
      throws NullPointerException {
    Cursor result = null;

    switch (uriMatcher.match(uri)) {
      case URI_CODE_LIN:
        result = mDatabaseHelper.getReadableDatabase().query(
            TableContractLIN.tableName,
            projection,
            selection,
            selectionArgs,
            "",
            "",
            sortOrder);
        break;
      case URI_CODE_NSN:
        result = mDatabaseHelper.getReadableDatabase().query(
            TableContractNSN.tableName,
            projection,
            selection,
            selectionArgs,
            "",
            "",
            sortOrder);
        break;
      case URI_CODE_ITEM:
        result = mDatabaseHelper.getReadableDatabase().query(
            TableContractItem.tableName,
            projection,
            selection,
            selectionArgs,
            "",
            "",
            sortOrder);
        break;
      case URI_CODE_PROPERTY_BOOK:
        result = mDatabaseHelper.getReadableDatabase().query(
            TableContractPropertyBook.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            "",
            "",
            sortOrder);
        break;
      case URI_CODE_ITEM_DATA:
        SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setDistinct(true);
        queryBuilder.setTables(ViewContractItemData.VIEW_NAME);

        result = queryBuilder.query(
            database,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder,
            null
        );
        break;
      case URI_CODE_NUM_LINS:
        result = getTotalLINs();
        break;
      case URI_CODE_NUM_ITEMS:
        result = getTotalItems();
        break;
      case URI_CODE_TOTAL_VALUE:
        result = getPropertyBookValue();
        break;
    }

    return result;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection,
                    String[] selectionArgs)
      throws NullPointerException {
    int result = 0;

    switch (uriMatcher.match(uri)) {
      case URI_CODE_NSN:
        result = mDatabaseHelper.getReadableDatabase().update(
            TableContractNSN.tableName,
            values,
            selection,
            selectionArgs);
        break;
      case URI_CODE_LIN:
        result = mDatabaseHelper.getReadableDatabase().update(
            TableContractLIN.tableName,
            values,
            selection,
            selectionArgs);
        break;
      case URI_CODE_ITEM:
        result = mDatabaseHelper.getReadableDatabase().update(
            TableContractItem.tableName,
            values,
            selection,
            selectionArgs);
        break;
      case URI_CODE_PROPERTY_BOOK:
        result = mDatabaseHelper.getReadableDatabase().update(
            TableContractPropertyBook.TABLE_NAME,
            values,
            selection,
            selectionArgs);
        break;
    }

    if (result > 0) {
      if (!batchMode) {
        context.getContentResolver().notifyChange(uri, null);
      } else if (!mBatchChangeNotifyList.contains(uri)) {
        mBatchChangeNotifyList.add(uri);
      }
    }
    return result;
  }


  @Override
  public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
      throws OperationApplicationException {
    batchMode = true;
    ContentProviderResult[] result;

    SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
    database.beginTransaction();

    try {
      result = super.applyBatch(operations);
      database.setTransactionSuccessful();

      for (Uri uri : mBatchChangeNotifyList) {
        context.getContentResolver().notifyChange(uri, null);
      }
      return result;
    } finally {
      batchMode = false;
      database.endTransaction();
    }
  }

  /**
   * Gets the total value of the database
   *
   * @return String representing the total value of the database formatted
   * as currency
   */
  public Cursor getPropertyBookValue() throws NullPointerException {
    SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

    return db.rawQuery(GET_TOTAL_VALUE, null);

  }

  /**
   * Returns the total number of LINs recorded in the property book
   *
   * @return Cursor containting the total number of LINs in the property book.
   * @throws NullPointerException
   */
  public Cursor getTotalLINs() throws NullPointerException {
    SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
    return db.rawQuery(GET_TOTAL_LINS, null);
  }

  public Cursor getTotalItems() {
    SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

    return db.rawQuery(GET_TOTAL_ITEMS, null);
  }

}
