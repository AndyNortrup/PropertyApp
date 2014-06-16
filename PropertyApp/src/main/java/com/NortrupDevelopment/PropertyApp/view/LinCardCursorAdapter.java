package com.NortrupDevelopment.PropertyApp.view;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.AlphabetIndexer;
import android.widget.SectionIndexer;

import com.NortrupDevelopment.PropertyApp.model.TableContractLIN;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardCursorAdapter;

/**
 * Created by andy on 2/15/14.
 */
public class LinCardCursorAdapter extends CardCursorAdapter
    implements SectionIndexer
{

    Context mContext;
    AlphabetIndexer mAlphabetIndexer;

    public LinCardCursorAdapter(Context c) {
        super(c);
        mContext = c;
    }

    @Override
    protected Card getCardFromCursor(Cursor cursor)
    {
      LinCard card = new LinCard(super.getContext());

      //Set the ID of the card
      card.setId(Integer.toString(
              cursor.getInt(
                      cursor.getColumnIndex(TableContractLIN._ID))));


      card.setOnClickListener(new Card.OnCardClickListener() {
          @Override
          public void onClick(Card card, View view) {
                  Intent nsnListIntent = new Intent(
                          mContext,
                          LINDetailActivity.class);
                  nsnListIntent.putExtra(LINDetailActivity.LIN_ID_KEY,
                      card.getId());
                  mContext.startActivity(nsnListIntent);
              }
          }
      );

      //Set the thumbnail
      card.setLin(cursor.getString(
          cursor.getColumnIndex(TableContractLIN.columnLIN)));

      //Set the nomenclature and sublin in the main body of the card
      card.setLin(cursor.getString(
          cursor.getColumnIndex(TableContractLIN.columnLIN)));

      card.setNomenclature(cursor.getString(
              cursor.getColumnIndex(TableContractLIN.columnNomenclature)));

      card.setSubLin(cursor.getString(
              cursor.getColumnIndex(TableContractLIN.columnSubLIN)));

      return card;
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
