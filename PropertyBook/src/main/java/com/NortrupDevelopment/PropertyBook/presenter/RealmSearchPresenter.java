package com.NortrupDevelopment.PropertyBook.presenter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.adapters.DefaultSearchAdapter;
import com.NortrupDevelopment.PropertyBook.adapters.DefaultSearchLineNumberAdapter;
import com.NortrupDevelopment.PropertyBook.adapters.DefaultSearchSerialNumberAdapter;
import com.NortrupDevelopment.PropertyBook.adapters.DefaultSearchStockNumberAdapter;
import com.NortrupDevelopment.PropertyBook.bus.DefaultAddSearchResultsViewEvent;
import com.NortrupDevelopment.PropertyBook.model.LineNumber;
import com.NortrupDevelopment.PropertyBook.model.ModelSearcher;
import com.NortrupDevelopment.PropertyBook.model.RealmModelSearcher;
import com.NortrupDevelopment.PropertyBook.model.SerialNumber;
import com.NortrupDevelopment.PropertyBook.model.StockNumber;
import com.NortrupDevelopment.PropertyBook.view.SearchResultsView;

import java.util.AbstractList;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import io.realm.RealmResults;

/**
 * Created by andy on 2/8/15.
 */
public class RealmSearchPresenter implements SearchPresenter {

  private Context mContext;

  private RealmSearchPresenter(Context context) {
    mContext = context;
  }


  @Override
  public AbstractList<View> searchForTerm(String term) {
    ArrayList<View> result = new ArrayList<View>();

    ModelSearcher searcher = new RealmModelSearcher(mContext);
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
    if(stockNumbers != null) {
      EventBus.getDefault()
          .post(new DefaultAddSearchResultsViewEvent(serialNumbers));
      foundSomething = true;
    }

    if(!foundSomething) {

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

    SearchResultsView view = new SearchResultsView(mContext);
    view.setTitle(mContext.getString(R.string.lin));

    RecyclerView.Adapter adapter =
        new DefaultSearchAdapter<LineNumber>(lineNumbers);
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
    RealmResults<StockNumber> stockNumbers =
        (RealmResults<StockNumber>) searcher.searchStockNumber(term);

    if (stockNumbers.size() == 0) {
      return null;
    }

    SearchResultsView view = new SearchResultsView(mContext);
    view.setTitle(mContext.getString(R.string.nsn));

    RecyclerView.Adapter adapter =
        new DefaultSearchAdapter<StockNumber>(stockNumbers);
    view.setAdapter(adapter);
    return view;
  }

  @Nullable
  private View searchForSerialNumbers(ModelSearcher searcher, String term) {

    RealmResults<SerialNumber> serialNumbers =
        (RealmResults<SerialNumber>) searcher.searchSerialNumber(term);

    if (serialNumbers.size() == 0) {
      return null;
    }

    SearchResultsView view = new SearchResultsView(mContext);
    view.setTitle(mContext.getString(R.string.serial_number));

    RecyclerView.Adapter adapter =
        new DefaultSearchAdapter<SerialNumber>(serialNumbers);
    view.setAdapter(adapter);

    return view;
  }
}
