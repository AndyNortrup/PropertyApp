package com.NortrupDevelopment.PropertyBook.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.adapters.LineNumberArrayAdapter;
import com.NortrupDevelopment.PropertyBook.bus.AboutUsRequestedEvent;
import com.NortrupDevelopment.PropertyBook.bus.BusProvider;
import com.NortrupDevelopment.PropertyBook.bus.LINDetailRequestedEvent;
import com.NortrupDevelopment.PropertyBook.bus.SearchRequestedEvent;
import com.NortrupDevelopment.PropertyBook.model.LineNumber;
import com.NortrupDevelopment.PropertyBook.presenter.LINBrowserPresenter;

import io.realm.RealmResults;

public class LINBrowserFragment extends Fragment implements LINBrowser
{

  private LINBrowserPresenter mPresenter;
  private ListView mListView;
  private LinearLayout mLoadingLayout;

  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState)
  {
    View result = inflater.inflate(R.layout.lin_browser,
        container, false);

    //Grab our view elements
    mListView = (ListView) result.findViewById(R.id.lin_list);
    mLoadingLayout =
        (LinearLayout)result.findViewById(R.id.lin_loading_progress);

    return result;
  }

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //Connect our presenter
    mPresenter = new LINBrowserPresenter(this);

    setHasOptionsMenu(true);
  }

  @Override
  public void onResume() {
    super.onResume();
    mPresenter.activityResumed();
  }

  /**
   * Load up the Action Bar
   */
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.menu_lin_list, menu);
  }


  /**
   * Handles button clicks from the ActionBar
   *
   * @param item Button that was clicked.
   * @return True if the button has been handled
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
      case R.id.import_property_book:
        mPresenter.importRequested();
        break;
      case R.id.search_property_book:
        //Open search activity
        BusProvider.getBus().post(new SearchRequestedEvent());
        break;
      case R.id.property_book_statistics:
        //Open statistics activity
        mPresenter.statisticsRequested();
        break;
      case R.id.about_us:
        BusProvider.getBus().post(new AboutUsRequestedEvent());
        break;
    }

    return true;
  }

  /**
   * Shows the Loading progress bar and hides mCardList and mEmptyLayout.
   */
  @Override
  public void showLoadingProgressBar() {
    mListView.setVisibility(View.GONE);
    mLoadingLayout.setVisibility(View.VISIBLE);
  }

  /**
   * Shows the mCardList and hides the progress bar and empty list view.
   */
  @Override
  public void showList() {
    mLoadingLayout.setVisibility(View.GONE);
    mListView.setVisibility(View.VISIBLE);
  }

  /**
   * Takes a list of LINs, converts them into UI cards then creates and array
   * adapter for display in the CardList.
   *
   * @param lineNumbers List of LINs to be displayed.
   */
  public void setList(RealmResults<LineNumber> lineNumbers) {

    if (lineNumbers != null) {
      LineNumberArrayAdapter adapter = new LineNumberArrayAdapter(getActivity(),
          lineNumbers,
          true); //AutoUpdate

      mListView.setAdapter(adapter);
      mListView.setFastScrollEnabled(true);

      mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent,
                                View view,
                                int position,
                                long id) {

            LineNumber lin =  ((LineNumberArrayAdapter)mListView.getAdapter())
                .getItem(position);
            BusProvider.getBus().post(new LINDetailRequestedEvent(lin));
        }
      });
    } else {
      mPresenter.importRequested();
    }

  }

  @Override
  public Context getContex() {
    return getActivity();
  }
}
