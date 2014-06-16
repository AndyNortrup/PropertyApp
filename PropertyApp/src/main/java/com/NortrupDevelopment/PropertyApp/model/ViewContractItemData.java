package com.NortrupDevelopment.PropertyApp.model;

/**
 * Created by andy on 3/10/14.
 */
public class ViewContractItemData {

    public static final String VIEW_NAME = "item_data";
    public static final String ALIAS_LIN_ID = "_id";
    public static final String ALIAS_LIN_NOMENCLATURE = "lin_nomenclature";
    public static final String ALIAS_LIN = "lin";

    public static final String ALIAS_NSN_ID = "nsn_id";
    public static final String ALIAS_NSN_NOMENCLATURE = "nsn_nomenclature";
    public static final String ALIAS_NSN = "nsn";

    public static final String ALIAS_ITEM_ID = "item_id";
    public static final String ALIAS_SERIAL_NUMBER = "item_serial_number";

    public static final String CREATE_VIEW = "CREATE VIEW IF NOT EXISTS " + VIEW_NAME +
            " AS SELECT " +
            //Lin
            TableContractLIN.tableName + "." +
                TableContractLIN.columnLIN +
                " AS " + ALIAS_LIN + ", " +
            //LIN nomenclature
            TableContractLIN.tableName + "." +
                TableContractLIN.columnNomenclature +
                " AS " + ALIAS_LIN_NOMENCLATURE + ", " +
            //LIN _id
            TableContractLIN.tableName + "." + TableContractLIN._ID +
                " AS " + ALIAS_LIN_ID + ", " +

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

            //Item Information
            //Item ID
            TableContractItem.tableName + "." +
                TableContractItem._ID +
                " AS " + ALIAS_ITEM_ID + ", " +

            //Item Serial Number
            TableContractItem.tableName + "." +
                TableContractItem.columnSerialNumber +
                " AS " +  ALIAS_SERIAL_NUMBER + " " +

            //FROM
            "FROM " + TableContractLIN.tableName + ", " +
                TableContractNSN.tableName + ", " +
                TableContractItem.tableName +

            //WHERE
            " WHERE " + TableContractLIN.tableName + "." + TableContractLIN._ID + " = " +
                TableContractNSN.tableName + "." + TableContractNSN.linID +
            " AND " + TableContractNSN.tableName + "." + TableContractNSN._ID + " = " +
                TableContractItem.tableName + "." + TableContractItem.columnNsnId;
}
