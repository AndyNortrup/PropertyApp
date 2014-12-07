package com.NortrupDevelopment.PropertyBook.view;

import android.app.Activity;
import android.os.Bundle;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.model.LineNumber;
import com.NortrupDevelopment.PropertyBook.model.RealmDefinition;
import com.NortrupDevelopment.PropertyBook.model.StockNumber;

import java.text.NumberFormat;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;

/**
 * Statistics activity provides the user with a quick overview of key
 * statistics of their property book.
 * Created by andy on 4/6/14.
 */
public class PropertyBookStatisticsActivity extends Activity
{

  private static final String LOG_TAG = "STATISTICS";


  @Override
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

  Card mCardValue;
  Card mCardLINCount;
  Card mCardItemCount;

  setContentView(R.layout.activity_pb_statistics);

  Realm realm = RealmDefinition.getRealm(this,
      RealmDefinition.PRODUCTION_REALM);

    mCardValue = new Card(this, R.layout.card_statistics_layout);
    CardHeader valueHeader = new CardHeader(this);
    valueHeader.setTitle(getString(R.string.property_book_value));
    mCardValue.addCardHeader(valueHeader);
    mCardValue.setTitle(getPropertyBookValue(realm));

    CardView valueCardView = (CardView)findViewById(R.id.property_book_value);
    valueCardView.setCard(mCardValue);

    mCardItemCount = new Card(this, R.layout.card_statistics_layout);
    CardHeader itemCountHeader = new CardHeader(this);
    itemCountHeader.setTitle(getString(R.string.property_book_total_items));
    mCardItemCount.addCardHeader(itemCountHeader);
    mCardItemCount.setTitle(String.valueOf(getItemCount(realm)));

    CardView itemCountCardView =
        (CardView)findViewById(R.id.property_book_item_count);
    itemCountCardView.setCard(mCardItemCount);

    mCardLINCount = new Card(this, R.layout.card_statistics_layout);
    CardHeader  linCountHeader = new CardHeader(this);
    linCountHeader.setTitle(getString(R.string.property_book_total_lins));
    mCardLINCount.addCardHeader(linCountHeader);
    mCardLINCount.setTitle(String.valueOf(getTotalLineNumbers(realm)));

    CardView linCountCardView =
        (CardView)findViewById(R.id.property_book_lin_count);
    linCountCardView.setCard(mCardLINCount);

  }

  /**
   * Calculate the value of the property book
   * SUM(StockNumber.onHand * StockNumber.unitPrice)
   * @param realm Realm to get data from
   * @return String formatted as currency of the total value of the property
   * book
   */
  private String getPropertyBookValue(Realm realm) {
    RealmResults<StockNumber> stockNumbers = realm.where(StockNumber.class)
        .notEqualTo("onHand", 0)
        .findAll();

    long valueInCents = 0;
    for(StockNumber stockNumber : stockNumbers) {
      valueInCents += (stockNumber.getOnHand() * stockNumber.getUnitPrice());
    }

    NumberFormat formatter =
        NumberFormat.getCurrencyInstance(Locale.getDefault());

    return formatter.format((double)(valueInCents)/100);
  }

  /**
   * Get the total number of items in the property book SUM(StockNumber.onHand)
   * @param realm Realm to get data from
   * @return Total number of items
   */
  private int getItemCount(Realm realm) {
    RealmResults<StockNumber> stockNumbers = realm.where(StockNumber.class)
        .notEqualTo("onHand", 0)
        .findAll();

    int result = 0;

    for(StockNumber stockNumber : stockNumbers) {
      result += stockNumber.getOnHand();
    }

    return result;
  }

  /**
   * Get the total count of LineNumbers
   * @param realm Realm to get data from
   * @return Total number of LineNumbers
   */
  private long getTotalLineNumbers(Realm realm) {
    return realm.where(LineNumber.class).count();
  }

}
