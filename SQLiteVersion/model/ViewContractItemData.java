package com.NortrupDevelopment.PropertyBook.model;

/**
 * Creates a contract for a database view which pulls aggregates information
 * from all of the tables into one view.
 * Created by andy on 3/10/14.
 */
public class ViewContractItemData {

  public static final String VIEW_NAME = "item_data";
  public static final String ALIAS_LIN_ID = "_id";
  public static final String ALIAS_LIN_NOMENCLATURE = "lin_nomenclature";
  public static final String ALIAS_LIN = "lin";
  public static final String ALIAS_LIN_AUTHORIZED = "lin_authorized";
  public static final String ALIAS_LIN_REQUIRED = "lin_required";
  public static final String ALIAS_LIN_DUE_IN = "lin_due_in";
  public static final String ALIAS_LIN_AUTH_DOC = "lin_auth_doc";
  public static final String ALIAS_LIN_SRI = "lin_sri";
  public static final String ALIAS_LIN_ERC = "lin_erc";


  public static final String ALIAS_NSN_ID = "nsn_id";
  public static final String ALIAS_NSN_NOMENCLATURE = "nsn_nomenclature";
  public static final String ALIAS_NSN = "nsn";
  public static final String ALIAS_NSN_UNIT_PRICE = "nsn_price";
  public static final String ALIAS_NSN_ON_HAND = "nsn_on_hand";

  public static final String ALIAS_ITEM_ID = "item_id";
  public static final String ALIAS_SERIAL_NUMBER = "item_serial_number";
  public static final String ALIAS_ITEM_SYS_NO = "item_system_number";

  public static final String CREATE_VIEW = "CREATE VIEW IF NOT EXISTS " +
      VIEW_NAME + " AS SELECT " +
      //Lin
      TableContractLIN.TABLE_NAME + "" +
      TableContractLIN.LIN +
      " AS " + ALIAS_LIN + ", " +
      //LIN nomenclature
      TableContractLIN.TABLE_NAME + "." +
      TableContractLIN.NOMENCLATURE +
      " AS " + ALIAS_LIN_NOMENCLATURE + ", " +
      //LIN _id
      TableContractLIN.TABLE_NAME + "." + TableContractLIN._ID +
      " AS " + ALIAS_LIN_ID + ", " +
      //LIN authorized
      TableContractLIN.TABLE_NAME + "." + TableContractLIN.AUTHORIZED +
      " AS " + ALIAS_LIN_AUTHORIZED + ", " +
      //LIN _required
      TableContractLIN.TABLE_NAME + "." + TableContractLIN.REQUIRED +
      " AS " + ALIAS_LIN_REQUIRED + ", " +
      //LIN due in
      TableContractLIN.TABLE_NAME + "." + TableContractLIN.DI +
      " AS " + ALIAS_LIN_DUE_IN + ", " +
      //LIN authorizing document
      TableContractLIN.TABLE_NAME + "." + TableContractLIN.AUTH_DOC +
      " AS " + ALIAS_LIN_AUTH_DOC + ", " +
      //LIN authorizing document
      TableContractLIN.TABLE_NAME + "." + TableContractLIN.SRI +
      " AS " + ALIAS_LIN_SRI + ", " +
      //LIN authorizing document
      TableContractLIN.TABLE_NAME + "." + TableContractLIN.ERC +
      " AS " + ALIAS_LIN_ERC + ", " +

      //NSN information
      //NSN ID
      TableContractNSN.tableName + "." + TableContractNSN._ID +
      " AS " + ALIAS_NSN_ID + ", " +

      //Nomenclature
      TableContractNSN.tableName + "." +
      TableContractNSN.columnNomenclature +
      " AS " + ALIAS_NSN_NOMENCLATURE + ", " +

      //NSN
      TableContractNSN.tableName + "." +
      TableContractNSN.columnNSN +
      " AS " + ALIAS_NSN + ", " +

      //NSN - Unit Price
      TableContractNSN.tableName + "." +
      TableContractNSN.columnUnitPrice +
      " AS " + ALIAS_NSN_UNIT_PRICE + ", " +

      //NSN - On Hand quantity
      TableContractNSN.tableName + "." +
      TableContractNSN.columnOnHand +
      " AS " + ALIAS_NSN_ON_HAND + ", " +

      //Item Information
      //Item ID
      TableContractItem.tableName + "." +
      TableContractItem._ID +
      " AS " + ALIAS_ITEM_ID + ", " +

      //Item Serial Number
      TableContractItem.tableName + "." +
      TableContractItem.columnSerialNumber +
      " AS " +  ALIAS_SERIAL_NUMBER + " " +

      //Item Serial Number
      TableContractItem.tableName + "." +
      TableContractItem.columnSysNo +
      " AS " + ALIAS_ITEM_SYS_NO + " " +

      //FROM
      "FROM " + TableContractLIN.TABLE_NAME + ", " +
      TableContractNSN.tableName + ", " +
      TableContractItem.tableName +

      //WHERE
      " WHERE " + TableContractLIN.TABLE_NAME + "." + TableContractLIN._ID + " = " +
      TableContractNSN.tableName + "." + TableContractNSN.linID +
      " AND " + TableContractNSN.tableName + "." + TableContractNSN._ID + " = " +
      TableContractItem.tableName + "." + TableContractItem.columnNsnId;
}
