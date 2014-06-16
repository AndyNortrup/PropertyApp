package com.NortrupDevelopment.PropertyApp.model;

import android.provider.BaseColumns;

public class TableContractNSN implements BaseColumns {

	public static final String tableName = "NSNs";
	public static final String columnNSN = "NSN";
	public static final String columnUI = "UI";
	public static final String columnUnitPrice = 
			"UNIT_PRICE";
	public static final String columnNomenclature = 
			"NOMENCLATURE";
	public static final String columnLLC = "LLC";
	public static final String columnECS = "ECS";
	public static final String columnSRRC = "SRRC";
	public static final String columnUIIManaged = 
			"UII_MANAGED";
	public static final String columnCIIC = "CIIC";
	public static final String columnDLA = "DLA";
	public static final String columnPubData = 
			"PUB_DATA";
	public static final String columnOnHand = 
			"ON_HAND";
	
	public static final String linID = "LinID";
	
	public static final String createNsnTable = 
			"CREATE TABLE " + tableName + "(" +
				_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				columnNSN + " TEXT, " +
				columnUI  + " TEXT, " +
				columnUnitPrice  + " TEXT, " +
				columnNomenclature + " TEXT, " +
				columnLLC  + " TEXT, " +
				columnECS  + " TEXT, " +
				columnSRRC + " TEXT, " +
				columnUIIManaged + " TEXT, " +
				columnDLA + " TEXT, " +
				columnPubData + " TEXT, " +
				columnOnHand + " INTEGER, " +
				columnCIIC + " TEXT, " +
				linID + " INTEGER, " +
				"FOREIGN KEY(" + linID + ") REFERENCES " + 
					TableContractLIN.tableName +"(" + 
					TableContractLIN._ID + ")" + 
			")";
}
