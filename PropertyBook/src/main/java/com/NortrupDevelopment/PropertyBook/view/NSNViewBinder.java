package com.NortrupDevelopment.PropertyBook.view;

import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.NortrupDevelopment.PropertyBook.model.ViewContractItemData;

/**
 * Used to Bind an NSN in a cursor so that it appears with dashes.
 *
 * Created by andy on 4/20/14.
 */
public class NSNViewBinder implements SimpleCursorAdapter.ViewBinder {

    private static final String DASH = "-";

    @Override
    public boolean setViewValue(View view,
            Cursor cursor,
            int columnIndex) {
        if(columnIndex ==
                cursor.getColumnIndex(ViewContractItemData.ALIAS_NSN))
        {
            StringBuilder sb = new StringBuilder(
                    cursor.getString(cursor.getColumnIndex(
                            ViewContractItemData.ALIAS_NSN)));
            sb.insert(4, DASH);
            sb.insert(7, DASH);
            sb.insert(11, DASH);
            ((TextView)view).setText(sb.toString());
            return true;
        }
        return false;
    }
}
