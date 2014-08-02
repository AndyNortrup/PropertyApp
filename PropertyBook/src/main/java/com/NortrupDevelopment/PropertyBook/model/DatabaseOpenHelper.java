package com.NortrupDevelopment.PropertyBook.model;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

	private static final int databaseVersion = 11;
	private static final String foreignKeyOn = "PRAGMA foreign_keys=ON;";
	
	public static final String DATABASE_NAME = "PropertyBook.db";
	
	public DatabaseOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, databaseVersion);	
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//turn on foreign key constraints 
		db.execSQL(foreignKeyOn);
		
		
		//create a table to hold property book listings
		db.execSQL(TableContractPropertyBook.createPropertyBookTable);
		
		//create our table to hold LIN listings
		db.execSQL(TableContractLIN.createLinTable);
		
		
		//create a table to hold NSN listings
		db.execSQL(TableContractNSN.createNsnTable);
		
		//create a table to hold item listings
		db.execSQL(TableContractItem.createItemTable);

        //create the item data view
        db.execSQL(ViewContractItemData.CREATE_VIEW);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.beginTransaction();
		
		try{
            if(oldVersion == 10) {
                db.execSQL(ViewContractItemData.CREATE_VIEW);
            }
			if(oldVersion < 8)	 {
				
				//drop tables
				db.execSQL("DROP TABLE " + TableContractItem.tableName);
				db.execSQL("DROP TABLE " + TableContractNSN.tableName);
				db.execSQL("DROP TABLE " + TableContractLIN.tableName);
				db.execSQL("DROP TABLE " + TableContractPropertyBook.TABLE_NAME);
				
				//create our table to hold LIN listings
				db.execSQL(TableContractLIN.createLinTable);
				
				//create a table to hold NSN listings
				db.execSQL(TableContractNSN.createNsnTable);
				
				//create a table to hold item listings
				db.execSQL(TableContractItem.createItemTable);
				
				//create a table to hold property book listings
				db.execSQL(TableContractPropertyBook.createPropertyBookTable);
				
				oldVersion += 1;
			}
			
			db.setTransactionSuccessful();
		} catch(SQLException e) {
			throw e;
		} finally {
			db.endTransaction();
		}
	}

}
