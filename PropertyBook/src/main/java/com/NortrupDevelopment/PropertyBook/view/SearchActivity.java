package com.NortrupDevelopment.PropertyBook.view;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.model.LineNumber;
import com.NortrupDevelopment.PropertyBook.model.RealmDefinition;
import com.NortrupDevelopment.PropertyBook.model.SearchSuggestionProvider;
import com.NortrupDevelopment.PropertyBook.model.SerialNumber;
import com.NortrupDevelopment.PropertyBook.model.StockNumber;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * abListener
 * Activity used to recieve and display search results
 * Created by andy on 3/14/14.
 */
public class SearchActivity extends ActionBarActivity {

  private static final String LOG_TAG = "SearchActivity";
  private static final String KEY_QUERY = "mSearchTerm";

  private String mSearchQuery;

  private ArrayList<Fragment> mFragmentArray;
  private ViewPager mViewPager;
  private SearchResultPageAdapter mPageAdapter;

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //Restore the search query from our saved instance state
    //This will be over written if there is a new search from an intent

    if (savedInstanceState != null &&
        savedInstanceState.containsKey(KEY_QUERY)) {
      mSearchQuery = savedInstanceState.getString(KEY_QUERY);
    }

    setContentView(R.layout.activity_search);

    mFragmentArray = new ArrayList<Fragment>();

    //setup our pager
    mPageAdapter = new SearchResultPageAdapter(getSupportFragmentManager());
    mPageAdapter.setFragments(mFragmentArray);

    mViewPager = (ViewPager) findViewById(R.id.search_result_pager);
    mViewPager.setAdapter(mPageAdapter);

    handleIntent(getIntent());
  }

  /**
   * Saves the search query to the saved instance state so that we can
   * requery later if required.
   *
   * @param savedInstanceState State saved when the activity was previously
   *                           destroyed.
   */
  public void onSaveInstanceState(Bundle savedInstanceState) {
    savedInstanceState.putString(KEY_QUERY, mSearchQuery);

    super.onSaveInstanceState(savedInstanceState);
  }


  public void onNewIntent(Intent intent) {
    setIntent(intent);
    handleIntent(intent);
  }

  public void handleIntent(Intent intent) {
    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
      mSearchQuery =
          intent.getStringExtra(SearchManager.QUERY).toUpperCase();
      doSearch();

      //Add the search term to our search suggestions
      SearchRecentSuggestions suggestions = new SearchRecentSuggestions(
          this,
          SearchSuggestionProvider.AUTHORITY,
          SearchSuggestionProvider.MODE);
      suggestions.saveRecentQuery(mSearchQuery, null);
    }


  }

  private void doSearch() {

    Realm realm =
        RealmDefinition.getRealm(this, RealmDefinition.PRODUCTION_REALM);

    Fragment lineNumberFragment = makeLineNumberFragment(realm);
    if(lineNumberFragment != null) {
      mFragmentArray.add(lineNumberFragment);
      //getActionBar().addTab(makeTab(R.string.lin));
      mPageAdapter.notifyDataSetChanged();
    }

    Fragment nsnFragment =
        makeNSNSearchFragment(realm);
    if(nsnFragment != null) {
      mFragmentArray.add(nsnFragment);
      mPageAdapter.notifyDataSetChanged();
    }

    Fragment serialNumberFragment =
        makeSerialNumberFragment(realm);
    if(serialNumberFragment != null) {
      mFragmentArray.add(serialNumberFragment);
      mPageAdapter.notifyDataSetChanged();
    }

    if(mFragmentArray.size() == 0) {
      mFragmentArray.add(NoSearchResultsFragment.getInstance(mSearchQuery));
      mPageAdapter.notifyDataSetChanged();
    }

  }
  /**
   * Makes an action bar and adds it to the listener
   *
   * @param resource Reference to the R.string resource for the tab text
   * @return
   */
/*
  private ActionBar.Tab makeTab(int resource) {
    return getActionBar().newTab()
        .setText(getResources().getString(resource))
        .setTabListener(mTabListener);
  }
*/
  /**
   * Search for any matches in the line numbers.
   *
   * @param realm Realm where data exists
   */
  private Fragment makeLineNumberFragment(Realm realm) {
    RealmResults<LineNumber> lineNumbers = realm.where(LineNumber.class)
        .contains("lin", mSearchQuery)
        .or()
        .contains("subLin", mSearchQuery)
        .or()
        .contains("nomenclature", mSearchQuery)
        .findAll();
    if (lineNumbers.size() > 0) {
      SearchResultsFragment<LineNumber> fragment =
          new SearchResultsFragment<LineNumber>();

      fragment.initForLineNumber(lineNumbers,
          this, mSearchQuery);

      return fragment;
    }
    return null;
  }

  /**
   * Search for any matches in the NSN Nomenclature
   * @param realm Realm where the data exists
   * @return Fragment to display the data
   */
  private Fragment makeNSNSearchFragment(Realm realm) {
    RealmResults<StockNumber> stockNumbers = realm.where(StockNumber.class)
        .contains("nomenclature", mSearchQuery)
        .or()
        .contains("nsn", mSearchQuery)
        .findAll();

    if(stockNumbers.size() > 0) {
      SearchResultsFragment<StockNumber> fragment =
          new SearchResultsFragment<StockNumber>();

      fragment.initForStockNumber(stockNumbers,
          this, mSearchQuery);
      return fragment;
    }
    return null;
  }

  private Fragment makeSerialNumberFragment(Realm realm) {
    RealmResults<SerialNumber> serialNumbers = realm.where(SerialNumber.class)
        .contains("serialNumber", mSearchQuery)
        .or()
        .contains("sysNo", mSearchQuery)
        .findAll();
    if(serialNumbers.size() > 0) {
      SearchResultsFragment<SerialNumber> fragment =
          new SearchResultsFragment<SerialNumber>();

      fragment.initForSerialNumber(serialNumbers, this, mSearchQuery);
      return fragment;
    }
    return null;
  }
}

class SearchResultPageAdapter extends FragmentPagerAdapter {


  private ArrayList<android.support.v4.app.Fragment> mFragments;

  public SearchResultPageAdapter(FragmentManager fm) {
    super(fm);
  }

  public void setFragments(ArrayList<android.support.v4.app.Fragment> fragments) {
    mFragments = fragments;
  }

  public android.support.v4.app.Fragment getItem(int i) {
    return mFragments.get(i);
  }

  @Override
  public CharSequence getPageTitle(int index) {
    return ((TitledFragment) mFragments.get(index)).getTitle();
  }

  public int getCount() {
    return mFragments.size();
  }
}



