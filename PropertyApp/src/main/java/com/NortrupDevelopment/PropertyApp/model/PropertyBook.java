package com.NortrupDevelopment.PropertyApp.model;

import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentUris;
import android.content.ContentValues;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class PropertyBook {
	private long propertyBookId;
	private String description;
	private String uic;
	private String pbic;
	private Multimap<String, LIN> linList;
	
    //Default ID value
    public static final int DEFAULT_ID = -1;
		
	public PropertyBook(long propertyBookId, 
			String description,
			String uic,
			String pbic) {
		this.propertyBookId = propertyBookId;
		this.description = description;
		this.uic = uic;
		this.pbic = pbic;
		
		linList = ArrayListMultimap.create();
	}
	
	public PropertyBook(String description,
			String uic,
			String pbic) {
		this(DEFAULT_ID, description, uic, pbic);
	}

	public long getPropertyBookId() {
		return propertyBookId;
	}
	
	public void setPropertyBookId(long propertyBookId) {
		this.propertyBookId = propertyBookId;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getUIC() {
		return uic;
	}
	
	public void setUIC(String uic) {
		this.uic = uic;
	}
	
	public String getPBIC() {
		return pbic;
	}
	
	public void setPBIC(String pbic) {
		this.pbic = pbic;
	}
	
	public void addLIN(LIN newLIN) {
		linList.put(newLIN.getLin(), newLIN);
	}
	
	public void deleteLIN(LIN lin) {
        linList.remove(lin.getLin(), lin);
    }
	
	public ArrayList<LIN> getLIN(String lin) {
		return (ArrayList<LIN>)linList.get(lin);
	}
	
	public Collection<LIN> getAllLINs() {
		return linList.values();
	}
	
	public ArrayList<ContentProviderOperation> getWriteAction(boolean recurse,
                 ArrayList<ContentProviderOperation> result) {

		Builder updateAction;
		if(propertyBookId < 0) {
			updateAction = ContentProviderOperation.newInsert(
					PropertyBookContentProvider.CONTENT_URI_PROPERTY_BOOK);
		} else {
			updateAction = ContentProviderOperation.newUpdate(
					ContentUris.withAppendedId(
						PropertyBookContentProvider.CONTENT_URI_PROPERTY_BOOK,
						propertyBookId));
		}
		
		ContentValues values = new ContentValues();
		values.put(TableContractPropertyBook.DESCRIPTION, description);
		values.put(TableContractPropertyBook.UIC, uic);
        values.put(TableContractPropertyBook.PBIC, pbic);
		updateAction.withValues(values);
		
		result.add(updateAction.build());
		if(recurse) {
            int propertyBookBackReference = result.size() - 1;
			Iterator<LIN> i = linList.values().iterator();
			while(i.hasNext()) {
				LIN lin = i.next();
				result = lin.getWriteAction(recurse, result, propertyBookBackReference);
			}
		}
		
		return result;
	}
	
	public ArrayList<ContentProviderOperation> getDeleteAction()
	{
		ArrayList<ContentProviderOperation> result = 
				new ArrayList<ContentProviderOperation>();
		
		result.add(ContentProviderOperation.newDelete(
			ContentUris.withAppendedId(
				PropertyBookContentProvider.CONTENT_URI_PROPERTY_BOOK, 
				propertyBookId)).build());
		
		Iterator<LIN> i = linList.values().iterator();
		while(i.hasNext()) {
			result.addAll(i.next().getDeleteAction());
		}
		
		return result;
	}
		
}
