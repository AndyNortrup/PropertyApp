package com.NortrupDevelopment.PropertyApp.view;

import android.content.Context;
import android.database.Cursor;

import com.NortrupDevelopment.PropertyApp.model.TableContractItem;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridCursorAdapter;

/**
 * Created by andy on 5/25/14.
 */
public class ItemCardGridCursorAdapter extends CardGridCursorAdapter {

  Context mContext;

  public ItemCardGridCursorAdapter(Context context) {
    super(context);

    mContext = context;
  }

  /**
   * Return a card that displays the item's serial number from the cursor
   * @param cursor Cursor with data
   * @return A Card with a serial number set in the Title Field.
   */
  @Override
  protected Card getCardFromCursor(Cursor cursor) {

    Card itemCard = new Card(mContext);

    itemCard.setTitle(cursor.getString(
        cursor.getColumnIndex(TableContractItem.columnSerialNumber)));

    return itemCard;
  }
}
