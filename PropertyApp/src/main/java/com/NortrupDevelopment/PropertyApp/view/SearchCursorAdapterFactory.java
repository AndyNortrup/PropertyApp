package com.NortrupDevelopment.PropertyApp.view;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

import com.NortrupDevelopment.PropertyApp.R;
import com.NortrupDevelopment.PropertyApp.model.ViewContractItemData;

import java.util.Arrays;

/**
 * Created by andy on 3/15/14.
 */
public class SearchCursorAdapterFactory {

    private static final String[] LIN_DATABASE_FIELDS = {
            ViewContractItemData.ALIAS_LIN,
            ViewContractItemData.ALIAS_LIN_NOMENCLATURE
    };

    private static final int[] LIN_DISPLAY_FIELDS = {
            R.id.lin_search_lin,
            R.id.lin_search_nomenclature
    };


    private static final String[] NSN_DATABASE_FIELDS = {
            ViewContractItemData.ALIAS_NSN,
            ViewContractItemData.ALIAS_LIN,
            ViewContractItemData.ALIAS_LIN_NOMENCLATURE,
            ViewContractItemData.ALIAS_NSN_NOMENCLATURE
    };

    private static final int[] NSN_DISPLAY_FIELDS = {
            R.id.nsn_search_nsn,
            R.id.lin_search_lin,
            R.id.lin_search_nomenclature,
            R.id.nsn_search_nomenclature
    };

    private static final String[] SN_DATABASE_FIELDS = {
            ViewContractItemData.ALIAS_SERIAL_NUMBER,
            ViewContractItemData.ALIAS_NSN,
            ViewContractItemData.ALIAS_LIN,
            ViewContractItemData.ALIAS_LIN_NOMENCLATURE,
            ViewContractItemData.ALIAS_NSN_NOMENCLATURE
    };


    private static final int[] SN_DISPLAY_FIELDS = {
            R.id.serial_number_search_sn,
            R.id.nsn_search_nsn,
            R.id.lin_search_lin,
            R.id.lin_search_nomenclature,
            R.id.nsn_search_nomenclature
    };

    /**
     * Returns a simple cursor adapter to match the type of data queried.
     * @param cursor Cursor with data
     * @param loaderId ID from SearchLoaderFactory
     * @param context activity context that contains the ListView
     * @return SimpleCursorAdapter for the dataset
     */
    public static SimpleCursorAdapter getCursorAdapter(
            Cursor cursor,
            int loaderId,
            Context context)
    {
        int layout;
        String[] databaseFields;
        int[] displayFields;


        switch (loaderId) {
            case SearchLoaderFactory.LOADER_LIN:
                databaseFields = LIN_DATABASE_FIELDS;
                displayFields = LIN_DISPLAY_FIELDS;
                layout = R.layout.search_result_lin;
                break;
            case SearchLoaderFactory.LOADER_NSN:
            case SearchLoaderFactory.LOADER_NOMENCLATURE:
                //Both the NSN and Nomenclature displays are the same
                databaseFields = NSN_DATABASE_FIELDS;
                displayFields = NSN_DISPLAY_FIELDS;
                layout = R.layout.search_result_nsn;
                break;
            case SearchLoaderFactory.LOADER_SN:
                databaseFields = SN_DATABASE_FIELDS;
                displayFields = SN_DISPLAY_FIELDS;
                layout = R.layout.search_result_searial_number;
                break;
            default:
                throw new RuntimeException("Unexpected Loader ID provided.");
        }

        SimpleCursorAdapter result = new SimpleCursorAdapter(
                context,
                layout,
                cursor,
                databaseFields,
                displayFields,
                0);

        if(Arrays.asList(databaseFields).contains(
                ViewContractItemData.ALIAS_NSN))
        {
            result.setViewBinder(new NSNViewBinder());
        }

        return result;
    }
}
