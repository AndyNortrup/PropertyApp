package com.NortrupDevelopment.PropertyBook.view;

import android.content.Context;
import android.widget.ScrollView;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.dao.StockNumber;
import com.NortrupDevelopment.PropertyBook.model.ModelSearcher;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.AbstractList;
import java.util.Locale;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardView;

/**
 * Statistics activity provides the user with a quick overview of key
 * statistics of their property book.
 * Created by andy on 4/6/14.
 */
public class PropertyBookStatisticsActivity extends ScrollView {

  private static final String LOG_TAG = "STATISTICS";
  ModelSearcher searcher;
  private AbstractList<StockNumber> stockNumbers;

  public PropertyBookStatisticsActivity(Context context,
                                        ModelSearcher searcher) {
    super(context);
    this.searcher = searcher;

    setupTotalValueCard();

    setupItemCountCard();

    setupLineNumberCountCard();
  }

  private void setupLineNumberCountCard() {
    Card mCardLINCount;
    mCardLINCount = new Card(getContext(), R.layout.card_statistics_layout);
    mCardLINCount.setTitle(String.valueOf(getTotalLineNumbers()));

    CardHeader linCountHeader = new CardHeader(getContext());
    linCountHeader.setTitle(
        getContext().getString(R.string.property_book_total_lins));
    mCardLINCount.addCardHeader(linCountHeader);

    CardView linCountCardView =
        (CardView) findViewById(R.id.property_book_lin_count);
    linCountCardView.setCard(mCardLINCount);
  }

  private void setupItemCountCard() {
    Card mCardItemCount;
    mCardItemCount = new Card(getContext(), R.layout.card_statistics_layout);
    mCardItemCount.setTitle(String.valueOf(getItemCount()));

    CardHeader itemCountHeader = new CardHeader(getContext());
    itemCountHeader.setTitle(
        getContext().getString(R.string.property_book_total_items));
    mCardItemCount.addCardHeader(itemCountHeader);

    CardView itemCountCardView =
        (CardView) findViewById(R.id.property_book_item_count);
    itemCountCardView.setCard(mCardItemCount);
  }

  private void setupTotalValueCard() {
    Card mCardValue;
    mCardValue = new Card(getContext(), R.layout.card_statistics_layout);
    mCardValue.setTitle(getPropertyBookValue());

    CardHeader valueHeader = new CardHeader(getContext());
    valueHeader.setTitle(
        getContext().getString(R.string.property_book_value));
    mCardValue.addCardHeader(valueHeader);

    CardView valueCardView = (CardView) findViewById(R.id.property_book_value);
    valueCardView.setCard(mCardValue);
  }

  /**
   * Calculate the value of the property book
   * SUM(StockNumber.onHand * StockNumber.unitPrice)
   *
   * @return String formatted as currency of the total value of the property
   * book
   */
  private String getPropertyBookValue() {

    if (stockNumbers == null) {
      stockNumbers = searcher.getAllStockNumbers();
    }
    BigDecimal totalValue = new BigDecimal(0);
    for (StockNumber stockNumber : stockNumbers) {

      BigDecimal lineValue = new BigDecimal(
          stockNumber.getOnHand() * stockNumber.getUnitPrice() / 100);

      totalValue.add(lineValue);
    }

    NumberFormat formatter =
        NumberFormat.getCurrencyInstance(Locale.getDefault());

    return formatter.format(totalValue);
  }

  /**
   * Get the total number of items in the property book SUM(StockNumber.onHand)
   *
   * @return Total number of items
   */
  private int getItemCount() {

    if (stockNumbers == null) {
      stockNumbers = searcher.getAllStockNumbers();
    }

    int result = 0;

    for (StockNumber stockNumber : stockNumbers) {
      result += stockNumber.getOnHand();
    }

    return result;
  }

  /**
   * Get the total count of LineNumbers
   *
   * @return Total number of LineNumbers
   */
  private long getTotalLineNumbers() {
    return searcher.getAllLineNumbers().size();
  }

}
