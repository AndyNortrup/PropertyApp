package com.NortrupDevelopment.PropertyApp.model;


import android.provider.BaseColumns;

public class TableContractLIN implements BaseColumns {
	public static final String TABLE_NAME = "LINs";
	public static final String LIN = "LIN";
	public static final String SUB_LIN = "SubLIN";
	public static final String SRI = "sri";
	public static final String ERC = "erc";
	public static final String NOMENCLATURE = "nomenclature";
	public static final String AUTH_DOC = "auth_doc";
	public static final String REQUIRED = "required";
	public static final String AUTHORIZED = "authorized";
	public static final String PROPERTY_BOOK_ID = "propertyBookId";
    public static final String DI = "di";

	
	public static final String createLinTable = "CREATE TABLE " +
      TABLE_NAME + "(" +
			_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
      LIN + " TEXT, " +
      SUB_LIN + " TEXT, " +
      SRI + " TEXT, " +
      ERC + " TEXT, " +
      NOMENCLATURE + " TEXT, " +
      AUTH_DOC + " TEXT, " +
      REQUIRED + " INTEGER, " +
      AUTHORIZED + " INTEGER, " +
      DI + " INTEGER, " +
      PROPERTY_BOOK_ID + " INTEGER, " +
			"FOREIGN KEY(" + PROPERTY_BOOK_ID + ") REFERENCES " +
			TableContractPropertyBook.TABLE_NAME + "(" +
			TableContractPropertyBook._ID + "))";
}
