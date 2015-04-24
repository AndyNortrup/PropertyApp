package com.NortrupDevelopment.PropertyBook.model.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Opens, creates and updates the database.
 * Created by andy on 2/28/15.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "propertybook.db";
  private static final int version = 0;

  public DatabaseOpenHelper(Context context) {
    super(context, DATABASE_NAME, null, version);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.beginTransaction();
    db.execSQL(TableContractPropertyBook.createPropertyBookTable);
    db.execSQL(TableContractLIN.createLinTable);
    db.execSQL(TableContractNSN.createNsnTable);
    db.execSQL(TableContractItem.createItemTable);
    db.endTransaction();
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }
}
