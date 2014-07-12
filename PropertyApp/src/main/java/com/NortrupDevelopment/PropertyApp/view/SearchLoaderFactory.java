package com.NortrupDevelopment.PropertyApp.view;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;

import com.NortrupDevelopment.PropertyApp.model.PropertyBookContentProvider;
import com.NortrupDevelopment.PropertyApp.model.ViewContractItemData;

/**
 * The SearchLoaderFactory generates a different loader based on the type of
 * information that is being queried.
 * Created by andy on 3/15/14.
 */
public class SearchLoaderFactory {

    /**
     * Used to create a loaders searching LINs
     */
    public static final int LOADER_LIN = 0;

    /**
     * Used to create a loaders searching NSNs
     */
    public static final int LOADER_NSN = 1;

    /**
     * Used to create a loaders searching serial numbers
     */
    public static final int LOADER_SN = 2;

    /**
     * Used to create a loaders searching Nomenclature
     */
    public static final int LOADER_NOMENCLATURE = 3;


    private static final String[] LIN_PROJECTION = {
            ViewContractItemData.ALIAS_LIN_ID,
            ViewContractItemData.ALIAS_LIN,
            ViewContractItemData.ALIAS_LIN_NOMENCLATURE
    };

    private static final String LIN_SELECTION =
            ViewContractItemData.ALIAS_LIN + " LIKE ?";

    private static final String LIN_SORT_ORDER = ViewContractItemData.ALIAS_LIN
            + " ASC";

    private static final String[] NSN_PROJECTION = {
            ViewContractItemData.ALIAS_NSN,
            ViewContractItemData.ALIAS_LIN,
            ViewContractItemData.ALIAS_LIN_NOMENCLATURE,
            ViewContractItemData.ALIAS_NSN_NOMENCLATURE,
            ViewContractItemData.ALIAS_LIN_ID
    };

    private static final String NSN_SELECTION =
            ViewContractItemData.ALIAS_NSN + " LIKE ?";

    private static final String NSN_SORT_ORDER = ViewContractItemData.ALIAS_NSN
            + " ASC";

    private static final String[] SN_PROJECTION = {
            ViewContractItemData.ALIAS_NSN,
            ViewContractItemData.ALIAS_LIN,
            ViewContractItemData.ALIAS_LIN_NOMENCLATURE,
            ViewContractItemData.ALIAS_NSN_NOMENCLATURE,
            ViewContractItemData.ALIAS_LIN_ID,
            ViewContractItemData.ALIAS_SERIAL_NUMBER
    };

    private static final String SN_SELECTION =
            ViewContractItemData.ALIAS_SERIAL_NUMBER + " LIKE ?";

    private static final String SN_SORT_ORDER =
            ViewContractItemData.ALIAS_SERIAL_NUMBER + " ASC";


    private static final String[] NOMENCLATURE_PROJECTION = {
            ViewContractItemData.ALIAS_LIN_ID,
            ViewContractItemData.ALIAS_LIN_NOMENCLATURE,
            ViewContractItemData.ALIAS_NSN_NOMENCLATURE
    };

    private static final String NOMENCLATURE_SELECTION =
            ViewContractItemData.ALIAS_LIN_NOMENCLATURE + " LIKE ? OR " +
                    ViewContractItemData.ALIAS_NSN_NOMENCLATURE + " LIKE ?";

    private static final String NOMENCLATURE_SORT_ORDER =
            ViewContractItemData.ALIAS_LIN_NOMENCLATURE + " ASC, " +
                    ViewContractItemData.ALIAS_NSN_NOMENCLATURE + " ASC";

    public static Loader getLoader(int id, String query, Context context) {
        String[] projection;
        String selection;
        String[] selectionArgs = { "%" + query + "%"};
        String sortOrder;

        switch (id) {

            case LOADER_LIN:
                projection = LIN_PROJECTION;
                selection = LIN_SELECTION;
                sortOrder = LIN_SORT_ORDER;
                break;
            case LOADER_NSN:
                projection = NSN_PROJECTION;
                selection = NSN_SELECTION;
                sortOrder = NSN_SORT_ORDER;
                break;
            case LOADER_SN:
                projection = SN_PROJECTION;
                selection = SN_SELECTION;
                sortOrder = SN_SORT_ORDER;
                break;
            case LOADER_NOMENCLATURE:
                projection = NSN_PROJECTION;
                selection = NOMENCLATURE_SELECTION;
                selectionArgs = new String[]{
                        "%" + query + "%",
                        "%" + query + "%"};
                sortOrder = NOMENCLATURE_SORT_ORDER;
                break;
            default:
                throw new RuntimeException("Unexpected Loader ID provided.");
        }

        return new CursorLoader(context,
                PropertyBookContentProvider.CONTENT_URI_ITEM_DATA_VIEW,
                projection,
                selection,
                selectionArgs,
                sortOrder);

    }
}
