package com.NortrupDevelopment.PropertyApp.model;

import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentUris;
import android.content.ContentValues;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

public class LIN {

	private long linId;
	private String lin;
	private String subLin;
	private String sri;
	private String erc;
	private String nomenclature;
	private String authDoc;
	private int required;
	private int authorized;
	private int di;
	
	private PropertyBook propertyBook;
    private Hashtable<String, NSN> nsnList;
    
    private static String queryString = TableContractLIN._ID + " = ?";
    
    //Default ID value
    public static final int DEFAULT_ID = -1;
	
	
	/**
	 * Creates a LIN object representing information for an individual LIN Number
	 * @param linId Unique identifier for the LIN in the database
	 * @param lin Input LIN
	 * @param subLin Input SubLIN
	 * @param sri Input SRI Code
	 * @param erc Input ERC Code
	 * @param nomenclature Input Nomenclature
	 * @param authDoc Input authorizing document
	 * @param required Input quantity required
	 * @param authorized Input quantity authorized
	 */
	public LIN(
			long linId,
			String lin,
			String subLin,
			String sri,
			String erc,
			String nomenclature,
			String authDoc,
			int required,
			int authorized,
			int di,
			PropertyBook propertyBook,
            Hashtable<String, NSN> nsnList) {

        this.linId = linId;
		this.lin = lin;
        this.subLin = subLin;
        this.sri = sri;
        this.erc = erc;
        this.nomenclature = nomenclature;
        this.authDoc = authDoc;
        this.required = required;
        this.authorized = authorized;
        this.di = di;
        this.propertyBook = propertyBook;
        this.nsnList = nsnList;

	}

  /**
   * Creates a LIN object representing information for an individual LIN
   * without having a property book object to nest it in.
   * @param lin Input LIN
   * @param subLin Input SubLIN
   * @param sri Input SRI Code
   * @param erc Input ERC Code
   * @param nomenclature Input Nomenclature
   * @param authDoc Input authorizing document
   * @param required Input quantity required
   * @param authorized Input quantity authorized
   */
  public LIN(
      Long _id,
      String lin,
      String subLin,
      String sri,
      String erc,
      String nomenclature,
      String authDoc,
      int required,
      int authorized,
      int di) {

    this(_id,
        lin,
        subLin,
        sri,
        erc,
        nomenclature,
        authDoc,
        required,
        authorized,
        di,
        null,
        new Hashtable<String, NSN>());

  }
    /**
     * Creates a LIN object representing information for an individual LIN
     * Number without having a linID from the database.
     * @param lin Input LIN
     * @param subLin Input SubLIN
     * @param sri Input SRI Code
     * @param erc Input ERC Code
     * @param nomenclature Input Nomenclature
     * @param authDoc Input authorizing document
     * @param required Input quantity required
     * @param authorized Input quantity authorized
     */
    public LIN(
            String lin,
            String subLin,
            String sri,
            String erc,
            String nomenclature,
            String authDoc,
            int required,
            int authorized,
            int di) {
		
		this(DEFAULT_ID,
				lin,
				subLin,
				sri,
				erc,
                nomenclature,
				authDoc,
				required,
				authorized,
				di,
				null,
				new Hashtable<String, NSN>());
		
	}

	/**
	 * @return the linID
	 */
	public long getLinId() {
		return linId;
	}


	/**
	 * @param linID the linID to set
	 */
	public void setLinID(long linID) {
		this.linId = linID;
	}


	/**
	 * @return the lin
	 */
	public String getLin() {
		return lin;
	}


	/**
	 * @param lin the lin to set
	 */
	public void setLin(String lin) {
		this.lin = lin;
	}


	/**
	 * @return the subLin
	 */
	public String getSubLin() {
		return subLin;
	}


	/**
	 * @param subLin the subLin to set
	 */
	public void setSubLin(String subLin) {
		this.subLin = subLin;
	}


	/**
	 * @return the sri
	 */
	public String getSri() {
		return sri;
	}


	/**
	 * @param sri the sri to set
	 */
	public void setSri(String sri) {
		this.sri = sri;
	}


	/**
	 * @return the erc
	 */
	public String getErc() {
		return erc;
	}


	/**
	 * @param erc the erc to set
	 */
	public void setErc(String erc) {
		this.erc = erc;
	}


	/**
	 * @return the nomenclature
	 */
	public String getNomencalture() {
		return nomenclature;
	}


	/**
	 * @param nomencalture set the nomenclature
	 */
	public void setNomencalture(String nomencalture) {
		this.nomenclature = nomencalture;
	}


	/**
	 * @return the authDoc
	 */
	public String getAuthDoc() {
		return authDoc;
	}


	/**
	 * @param authDoc the authDoc to set
	 */
	public void setAuthDoc(String authDoc) {
		this.authDoc = authDoc;
	}


	/**
	 * @return the required
	 */
	public int getRequired() {
		return required;
	}


	/**
	 * @param required the required to set
	 */
	public void setRequired(int required) {
		this.required = required;
	}


	/**
	 * @return the authorized
	 */
	public int getAuthorized() {
		return authorized;
	}


	/**
	 * @param authorized the authorized to set
	 */
	public void setAuthorized(int authorized) {
		this.authorized = authorized;
	}

    /**
	 * @return the di
	 */
	public int getDi() {
		return di;
	}

	/**
	 * @param di the di to set
	 */
	public void setDi(int di) {
		this.di = di;
	}
	
	public PropertyBook getPropertyBook() {
		return propertyBook;
	}
	
	public void setPropertyBook(PropertyBook propertyBook) {
		this.propertyBook = propertyBook;
	}

	public Hashtable<String, NSN> getNsnList() {
        return nsnList;
    }

    public void addNSN(NSN newNSN) {
        nsnList.put(newNSN.getNsn(), newNSN);
    }
    
    public void deleteNSN(NSN nsn) {
    	nsnList.remove(nsn.getNsnId());
    }    
         
    public ArrayList<ContentProviderOperation> getWriteAction(boolean includeSubElements,
              ArrayList<ContentProviderOperation> result,
              int propertyBookBackReference) {

    	ContentValues values = new ContentValues();
    	
    	values.put(TableContractLIN.columnLIN, lin);
    	values.put(TableContractLIN.columnSubLIN, subLin);
    	values.put(TableContractLIN.columnAuthDoc, authDoc);
    	values.put(TableContractLIN.columnAuthorized, authorized);
    	values.put(TableContractLIN.columnERC, erc);
    	values.put(TableContractLIN.columnNomenclature, nomenclature);
    	values.put(TableContractLIN.columnRequired, required);
        values.put(TableContractLIN.columnDI, di);
    	values.put(TableContractLIN.columnSRI, sri);

    	
    	if(propertyBook != null) {
    		values.put(TableContractLIN.columnPropertyBookId, propertyBook.getPropertyBookId());
    	}
    	
    	Builder builder;
    	
    	if(linId > 0) {
    		values.put(TableContractLIN._ID, linId);
    		builder = ContentProviderOperation.newUpdate(
    				PropertyBookContentProvider.CONTENT_URI_LIN);
    		
    		String[] selectionArgs = {String.valueOf(linId)};
    		builder.withSelection(queryString, selectionArgs);
    	} else {
    		builder = ContentProviderOperation.newInsert(
    				PropertyBookContentProvider.CONTENT_URI_LIN);
    	}

        builder.withValueBackReference(TableContractLIN.columnPropertyBookId,
                propertyBookBackReference);

    	builder.withValues(values);
    	
    	result.add(builder.build());
    	
    	if(includeSubElements) {
            int linBackReference = result.size() - 1;
    		Iterator<NSN> i = nsnList.values().iterator();
    		while(i.hasNext()) {
    			result = i.next().getWriteAction(includeSubElements, result, linBackReference);
    		}
    	}
    	
    	return result;
    }
    
    public ArrayList<ContentProviderOperation> getDeleteAction() {
    	ArrayList<ContentProviderOperation> result = 
    			new ArrayList<ContentProviderOperation>();
    	
    	result.add(ContentProviderOperation.newDelete(
			ContentUris.withAppendedId(
					PropertyBookContentProvider.CONTENT_URI_LIN, linId)).build());
    	    	
		Iterator<NSN> i = nsnList.values().iterator();
		while(i.hasNext()) {
			result.addAll(i.next().getDeleteAction());
		}

    	return result;
    }
    
    @Override
    public String toString() {
    	return getLin() + " " + getNomencalture();
    }

}
