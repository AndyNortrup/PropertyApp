package com.NortrupDevelopment.PropertyApp.view;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.NortrupDevelopment.PropertyApp.R;
import com.NortrupDevelopment.PropertyApp.model.PropertyBookContentProvider;

import java.text.NumberFormat;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;

/**
 * Created by andy on 4/6/14.
 */
public class PropertyBookStatisticsActivity extends Activity
    implements LoaderManager.LoaderCallbacks<Cursor>
{

    private static final String LOG_TAG = "STATISTICS";
    private Card mCardValue;
    private Card mCardLINCount;
    private Card mCardItemCount;

    private static final int VALUE_ID = 1;
    private static final int LIN_ID = 2;
    private static final int ITEM_ID = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pb_statistics);

        mCardValue = new Card(this, R.layout.card_statistics_layout);
        CardHeader valueHeader = new CardHeader(this);
        valueHeader.setTitle(getString(R.string.property_book_value));
        mCardValue.addCardHeader(valueHeader);

        mCardItemCount = new Card(this, R.layout.card_statistics_layout);
        CardHeader itemCountHeader = new CardHeader(this);
        itemCountHeader.setTitle(getString(R.string.property_book_total_items));
        mCardItemCount.addCardHeader(itemCountHeader);

        mCardLINCount = new Card(this, R.layout.card_statistics_layout);
        CardHeader  linCountHeader = new CardHeader(this);
        linCountHeader.setTitle(getString(R.string.property_book_total_lins));
        mCardLINCount.addCardHeader(linCountHeader);

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(VALUE_ID, null, this);
        loaderManager.initLoader(LIN_ID, null, this);
        loaderManager.initLoader(ITEM_ID, null, this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        CursorLoader loader;
        switch (id) {
            case VALUE_ID:
                loader = new CursorLoader(this,
                        PropertyBookContentProvider.CONTENT_URI_ITEM_TOTAL_VALUE,
                        null,
                        null,
                        null,
                        "ASC");
                break;
            case LIN_ID:
                loader = new CursorLoader(this,
                        PropertyBookContentProvider.CONTENT_URI_ITEM_NUM_LINS,
                        null,
                        null,
                        null,
                        "ASC");
                break;
            case ITEM_ID:
                loader = new CursorLoader(this,
                        PropertyBookContentProvider.CONTENT_URI_ITEM_NUM_ITEMS,
                        null,
                        null,
                        null,
                        "ASC");
                break;
            default:
                loader = null;
                Log.e(LOG_TAG, "Unrecognized Loader ID passed in Statistics Activity");
                break;
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        data.moveToFirst();
        CardView cardView;
        switch(loader.getId()) {
            case VALUE_ID:
                mCardValue.setTitle(
                        NumberFormat.getCurrencyInstance().format(data.getLong(
                            data.getColumnIndex(
                                PropertyBookContentProvider.ALIAS_TOTAL_PRICE)
                        ))
                );
                cardView = (CardView)findViewById(R.id.property_book_value);
                cardView.setCard(mCardValue);
                break;
            case LIN_ID:
                mCardLINCount.setTitle(
                        Long.toString(data.getLong(data.getColumnIndex(
                            PropertyBookContentProvider.ALIAS_TOTAL_LINS)
                        )));
                cardView = (CardView)findViewById(R.id.property_book_lin_count);
                cardView.setCard(mCardLINCount);
                break;
            case ITEM_ID:
                mCardItemCount.setTitle(
                        Long.toString(data.getLong(data.getColumnIndex(
                                        PropertyBookContentProvider.ALIAS_TOTAL_ITEMS)
                        )));
                cardView = (CardView)findViewById(R.id.property_book_item_count);
                cardView.setCard(mCardItemCount);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        //Nothing required, no persistent cursor data.
    }
}
