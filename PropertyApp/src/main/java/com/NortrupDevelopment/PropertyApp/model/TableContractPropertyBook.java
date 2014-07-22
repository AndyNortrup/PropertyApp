package com.NortrupDevelopment.PropertyApp.model;

import android.provider.BaseColumns;

public class TableContractPropertyBook implements BaseColumns {
	public static final String TABLE_NAME = "PropertyBook";
	public static final String DESCRIPTION = "description";
	public static final String UIC = "uic";
	public static final String PBIC = "pbic";
	
	public static final String createPropertyBookTable = "CREATE TABLE " +
      TABLE_NAME + " (" +
			_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
      DESCRIPTION + " TEXT, " +
      UIC + " TEXT, " +
      PBIC + " TEXT)";
			
}
