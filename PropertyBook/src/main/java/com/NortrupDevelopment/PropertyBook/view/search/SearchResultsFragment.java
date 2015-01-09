package com.NortrupDevelopment.PropertyBook.view.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.adapters.SeachAdapters.SearchLineNumberArrayAdapter;
import com.NortrupDevelopment.PropertyBook.adapters.SeachAdapters.SearchResultAdapter;
import com.NortrupDevelopment.PropertyBook.adapters.SeachAdapters.SearchSerialNumberArrayAdapter;
import com.NortrupDevelopment.PropertyBook.adapters.SeachAdapters.SearchStockNumberArrayAdapter;
import com.NortrupDevelopment.PropertyBook.model.LineNumber;
import com.NortrupDevelopment.PropertyBook.model.SerialNumber;
import com.NortrupDevelopment.PropertyBook.model.StockNumber;

import io.realm.RealmResults;

/**
 * Search results fragment recieves a fragment and a search type then displays
 * search results for that type by changin the list item layout to match the type
 * of search being conducted.
 * Created by andy on 3/8/14.
 */
public class SearchResultsFragment<T> extends ListFragment
  implements TitledFragment

{
  private SearchResultAdapter mArrayAdapter;

  String mTitle;

  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {

      setRetainInstance(true);
      return inflater.inflate(R.layout.fragment_search_results,
          container,
          false);

    }

    public void onStart() {
        super.onStart();


      if(mArrayAdapter != null) {
        getListView().setAdapter(mArrayAdapter);
      }

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id)
            {
          //TODO: Show LIN Detail view
            }
        });

    }

  public void initForLineNumber(RealmResults<LineNumber>data,
                   Context context,
                   String searchTerm)
  {
    mArrayAdapter = new SearchLineNumberArrayAdapter(context, data, searchTerm);
    mTitle="LIN";
  }

  /**
   * Initialize the fragment with a list adapter for Stock Numbers
   * @param data Data to be displayed
   * @param context
   */
  public void initForStockNumber(RealmResults<StockNumber>data,
                                 Context context,
                                 String searchTerm)
  {
    mArrayAdapter =
        new SearchStockNumberArrayAdapter(context, data, searchTerm);
    mTitle="NSN";
  }

  /**
   * Initialize the fragment with alist adapter full of Serial Numbers
   * @param data Data to be displayed
   * @param context Fragment context
   */
  public void initForSerialNumber(RealmResults<SerialNumber>data,
                                  Context context,
                                  String searchTerm)
  {
    mArrayAdapter =
        new SearchSerialNumberArrayAdapter(context, data, searchTerm);
    mTitle="SerialNumber";

  }

  public CharSequence getTitle() {
    return mTitle;
  }

}
