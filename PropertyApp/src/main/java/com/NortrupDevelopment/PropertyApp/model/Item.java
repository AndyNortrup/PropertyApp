package com.NortrupDevelopment.PropertyApp.model;


import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentValues;

/**
 * Created by andy on 5/16/13.
 */
public class Item {

    private long itemId;
    private String serialNumber;
    private String sysNo;
    private NSN nsn;

    private String queryString = TableContractItem._ID + " = ?";
    
    //Default ID value
    public static final int DEFAULT_ID = -1;
    
    private Item(
            String serialNumber,
            String sysNo,
            int itemId,
            NSN nsn
    ) {
        this.serialNumber = serialNumber;
        this.sysNo = sysNo;
        this.itemId = itemId;
        this.nsn = nsn;
    }
    
    public Item(String serialNumber, 
    		String sysNo, 
    		NSN nsn) {
    	this(serialNumber, sysNo, DEFAULT_ID, nsn);
    }

    public Item(String serialNumber, 
    		String sysNo, 
    		String detectSN, 
    		String lot) {
        this(serialNumber, sysNo, DEFAULT_ID, null);
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSysNo() {
        return sysNo;
    }

    public void setSysNo(String sysNo) {
        this.sysNo = sysNo;
    }

    public NSN getNsn() {
        return nsn;
    }

    public void setNsn(NSN nsn) {
        this.nsn = nsn;
    }
        
    /**
     * Generates either an Insert or an Update action based on if the item has 
     * an id already.
     * @return
     */
    public ContentProviderOperation getWriteAction(int backReference) {
    	
    	Builder updateAction;
    	if(itemId == -1) {
    		updateAction = ContentProviderOperation.newInsert(
        			PropertyBookContentProvider.CONTENT_URI_ITEM);
    	} else {
    		updateAction = ContentProviderOperation.newUpdate(
        			ContentUris.withAppendedId(
        					PropertyBookContentProvider.CONTENT_URI_ITEM, 
        					itemId));

            String queryValues[] = {String.valueOf(itemId)};
            updateAction.withSelection(queryString, queryValues);
    	}
    	
    	
    	ContentValues values = new ContentValues();
		updateAction.withValue(TableContractItem.columnSerialNumber, serialNumber)
		    .withValue(TableContractItem.columnSysNo, sysNo)
    	    .withValueBackReference(TableContractItem.columnNsnId,
                backReference);
    	
    	return updateAction.build();
    }
    
    public ContentProviderOperation getDeleteAction() {
    	Builder deleteAction = ContentProviderOperation.newDelete(
    			PropertyBookContentProvider.CONTENT_URI_ITEM);
    	    	
    	String queryValues[] = {String.valueOf(itemId)};
    	
    	deleteAction.withSelection(queryString, queryValues);
    	   	   	
    	return deleteAction.build();
    }
}
