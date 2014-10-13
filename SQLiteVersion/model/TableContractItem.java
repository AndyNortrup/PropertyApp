package com.NortrupDevelopment.PropertyBook.model;

import android.provider.BaseColumns;

public class TableContractItem implements BaseColumns {
	public static final String tableName = "Items";
	public static final String columnSerialNumber = "SN";
	public static final String columnSysNo = "SysNo";
	public static final String columnNsnId = "NsnId";

	public static final String createItemTable = "CREATE TABLE "+
			tableName + " (" +
			_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			columnSerialNumber + " TEXT, " + 
			columnSysNo + " TEXT, " + 
			columnNsnId + " INTEGER, " + 
			"FOREIGN KEY(" + columnNsnId + ") REFERENCES " +
			TableContractNSN.tableName + "(" +
			TableContractNSN._ID + "))";			
}
