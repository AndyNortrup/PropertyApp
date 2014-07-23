package com.NortrupDevelopment.PropertyApp.model;


import android.provider.BaseColumns;

public class TableContractLIN implements BaseColumns {
	public static final String tableName = "LINs";
	public static final String columnLIN = "LIN";
	public static final String columnSubLIN = "SubLIN";
	public static final String columnSRI = "sri";
	public static final String columnERC = "erc";
	public static final String columnNomenclature = "nomenclature";
	public static final String columnAuthDoc = "auth_doc";
	public static final String columnRequired = "required";
	public static final String columnAuthorized = "authorized";
	public static final String columnPropertyBookId = "propertyBookId";
    public static final String columnDI = "di";
	
	
	public static final String createLinTable = "CREATE TABLE " +
			tableName + "(" +
			_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			columnLIN + " TEXT, " +
			columnSubLIN + " TEXT, " +
			columnSRI + " TEXT, " +
			columnERC + " TEXT, " +
			columnNomenclature + " TEXT, " +
			columnAuthDoc + " TEXT, " +
			columnRequired + " INTEGER, " +
			columnAuthorized + " INTEGER, " +
            columnDI + " INTEGER, " +
			columnPropertyBookId + " INTEGER, " +
			"FOREIGN KEY(" + columnPropertyBookId + ") REFERENCES " +
			TableContractPropertyBook.tableName + "(" +
			TableContractPropertyBook._ID + "))";
}
