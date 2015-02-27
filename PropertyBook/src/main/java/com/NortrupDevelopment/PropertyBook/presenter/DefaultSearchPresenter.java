package com.NortrupDevelopment.PropertyBook.presenter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.adapters.DefaultSearchAdapter;
import com.NortrupDevelopment.PropertyBook.bus.DefaultAddSearchResultsViewEvent;
import com.NortrupDevelopment.PropertyBook.model.LineNumber;
import com.NortrupDevelopment.PropertyBook.model.ModelSearcher;
import com.NortrupDevelopment.PropertyBook.model.RealmDefinition;
import com.NortrupDevelopment.PropertyBook.model.RealmModelSearcher;
import com.NortrupDevelopment.PropertyBook.model.SerialNumber;
import com.NortrupDevelopment.PropertyBook.model.StockNumber;

import java.util.AbstractList;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import io.realm.RealmResults;

/**
 * Default Search presenter handles the the lookup of search results for the
 * SearchView
 * Created by andy on 2/8/15.
 */
public class DefaultSearchPresenter implements SearchPresenter {

  private Context mContext;

  private DefaultSearchPresenter(Context context) {
    mContext = context;
  }


  @Override
  public AbstractList<View> searchForTerm(String term) {
    ArrayList<View> result = new ArrayList<View>();

    ModelSearcher searcher = new RealmModelSearcher(
        RealmDefinition.getRealm(mContext, RealmDefinition.PRODUCTION_REALM));
    boolean foundSomething = false;

    View lineNumbers = searchForLineNumbers(searcher, term);
    if (lineNumbers != null) {
      EventBus.getDefault()
          .post(new DefaultAddSearchResultsViewEvent(lineNumbers));
      foundSomething = true;
    }

    View stockNumbers = searchForStockNumbers(searcher, term);
    if (stockNumbers != null) {
      EventBus.getDefault()
          .post(new DefaultAddSearchResultsViewEvent(stockNumbers));
      foundSomething = true;
    }

    View serialNumbers = searchForSerialNumbers(searcher, term);
    if (stockNumbers != null) {
      EventBus.getDefault()
          .post(new DefaultAddSearchResultsViewEvent(serialNumbers));
      foundSomething = true;
    }

    if (!foundSomething) {

      NoSearchResultsView view = new NoSearchResultsView(mContext);
      view.setSearchTerm(term);

      EventBus.getDefault()
          .post(new DefaultAddSearchResultsViewEvent(view));
    }
    return result;
  }

  /**
   * Search for any matches in the line numbers.
   *
   * @param searcher Search object to query for data
   */
  @Nullable
  private View searchForLineNumbers(ModelSearcher searcher, String term) {

    RealmResults<LineNumber> lineNumbers =
        (RealmResults<LineNumber>) searcher.searchLineNumber(term);

    if (lineNumbers.size() == 0) {
      return null;
    }

    SearchResultsList view = new DefaultSearchResultsView(mContext,
        mContext.getString(R.string.lin));

    RecyclerView.Adapter adapter =
        new DefaultSearchAdapter<>(lineNumbers);
    view.setAdapter(adapter);
    return view;
  }

  /**
   * Search for any matches in the NSN Nomenclature
   *
   * @param searcher ModelSearcher used to look for the data
   * @return View to display the data
   */
  @Nullable
  private View searchForStockNumbers(ModelSearcher searcher, String term) {
    AbstractList<StockNumber> stockNumbers = searcher.searchStockNumber(term);

    if (stockNumbers.size() == 0) {
      return null;
    }

    SearchResultsList view = new DefaultSearchResultsView(mContext,
        mContext.getString(R.string.nsn));

    RecyclerView.Adapter adapter =
        new DefaultSearchAdapter<>(stockNumbers);
    view.setAdapter(adapter);
    return view;
  }

  @Nullable
  private View searchForSerialNumbers(ModelSearcher searcher, String term) {

    AbstractList<SerialNumber> serialNumbers =
        searcher.searchSerialNumber(term);

    if (serialNumbers.size() == 0) {
      return null;
    }

    SearchResultsList view = new DefaultSearchResultsView(mContext,
        mContext.getString(R.string.serial_number));

    RecyclerView.Adapter adapter =
        new DefaultSearchAdapter<>(serialNumbers);
    view.setAdapter(adapter);

    return view;
  }
}
