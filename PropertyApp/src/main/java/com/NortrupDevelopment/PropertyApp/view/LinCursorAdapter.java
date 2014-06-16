package com.NortrupDevelopment.PropertyApp.view;

import android.content.Context;
import android.database.Cursor;
import android.widget.AlphabetIndexer;
import android.widget.SectionIndexer;
import android.widget.SimpleCursorAdapter;

import com.NortrupDevelopment.PropertyApp.model.TableContractLIN;

/**
 * Created by andy on 5/1/14.
 */
public class LinCursorAdapter extends SimpleCursorAdapter
  implements SectionIndexer
{

  AlphabetIndexer mAlphabetIndexer;

  public LinCursorAdapter(Context context,
                   int layout,
                   Cursor c,
                   String[] from,
                   int[] to,
                   int flags)
  {
    super(context, layout, c, from, to, flags);
  }

  @Override
  public Cursor swapCursor(Cursor newCursor) {
    if(newCursor != null && newCursor.getCount() > 0) {
      mAlphabetIndexer = new AlphabetIndexer(newCursor,
          newCursor.getColumnIndex(TableContractLIN.columnLIN),
          "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }
    return super.swapCursor(newCursor);
  }

  @Override
  public Object[] getSections() {
    return mAlphabetIndexer.getSections();
  }

  @Override
  public int getPositionForSection(int i) {
    return mAlphabetIndexer.getPositionForSection(i);
  }

  @Override
  public int getSectionForPosition(int i) {
    return mAlphabetIndexer.getSectionForPosition(i);
  }
}
