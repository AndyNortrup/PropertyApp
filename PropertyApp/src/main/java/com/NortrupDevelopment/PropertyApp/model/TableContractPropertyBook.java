package com.NortrupDevelopment.PropertyApp.model;

import android.provider.BaseColumns;

public class TableContractPropertyBook implements BaseColumns {
	public static final String tableName = "PropertyBook";
	public static final String columnDescription = "description";
	public static final String columnUIC = "uic";
	public static final String columnPBIC = "pbic";
	
	public static final String createPropertyBookTable = "CREATE TABLE " +
			tableName + " (" +
			_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			columnDescription + " TEXT, " + 
			columnUIC + " TEXT, " +
			columnPBIC + " TEXT)";				
			
}
