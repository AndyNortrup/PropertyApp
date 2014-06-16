package com.NortrupDevelopment.PropertyApp.view;

import android.app.ActionBar;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;

import com.NortrupDevelopment.PropertyApp.R;

import java.util.ArrayList;

/**abListener
 * Activity used to recieve and display search results
 * Created by andy on 3/14/14.
 */
public class SearchActivity extends FragmentActivity
    implements LoaderManager.LoaderCallbacks<Cursor>
{

    private static final String LOG_TAG = "SearchActivity";
    private static final String KEY_QUERY = "mSearchTerm";

    private String mSearchQuery;

    private ArrayList<Fragment> mFragmentArray;
    private ViewPager mViewPager;
    private SearchResultPageAdapter mPageAdapter;
    private ActionBar.TabListener mTabListener;
    private int mCursorsReturned = 0;
    private int mCursorswithResults = 0;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Restore the search query from our saved instance state
        //This will be over written if there is a new search from an intent

        if(savedInstanceState != null &&
                savedInstanceState.containsKey(KEY_QUERY)) {
            mSearchQuery = savedInstanceState.getString(KEY_QUERY);
        }

        setContentView(R.layout.activity_search);

        mFragmentArray = new ArrayList<Fragment>();

        //Enable Tabs
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mTabListener = new ActionBar.TabListener() {

            public void onTabSelected(ActionBar.Tab tab,
                                      android.app.FragmentTransaction fm) {
              mViewPager.setCurrentItem(tab.getPosition());

            }

            public  void onTabUnselected(ActionBar.Tab tab,
                                         android.app.FragmentTransaction fm) {

            }

            public void onTabReselected(ActionBar.Tab tab,
                                        android.app.FragmentTransaction fm) {
                //Ignore
            }
        };

        //setup our pager
        mPageAdapter = new SearchResultPageAdapter(getSupportFragmentManager());
        mPageAdapter.setFragments(mFragmentArray);

        mViewPager = (ViewPager)findViewById(R.id.search_result_pager);
        mViewPager.setAdapter(mPageAdapter);

        mViewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int pageNum) {
                getActionBar().setSelectedNavigationItem(pageNum);
            }
        });

        //Handle the search intent
        if(Intent.ACTION_SEARCH.equals((getIntent().getAction()))) {
            handleIntent(getIntent());
        } else if (!TextUtils.isEmpty(mSearchQuery)) {
            doSearch();
        }
    }

    /**
     * Saves the search query to the saved instance state so that we can
     * requery later if required.
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
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            mSearchQuery =
                    intent.getStringExtra(SearchManager.QUERY);
            doSearch();
        }
    }

    private void doSearch() {
        try {
            //Handle the search
            Log.i(LOG_TAG, "Starting to query PB");

            getLoaderManager().initLoader(
                    SearchLoaderFactory.LOADER_LIN, null, this);
            getLoaderManager().initLoader(
                    SearchLoaderFactory.LOADER_NSN, null, this);
            getLoaderManager().initLoader(
                    SearchLoaderFactory.LOADER_SN, null, this);
            getLoaderManager().initLoader(
                    SearchLoaderFactory.LOADER_NOMENCLATURE, null, this);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
        }
    }



    /*
     * Queries the database for the results of the search
     */
    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        Log.i(LOG_TAG,
                "Starting search for query:" + mSearchQuery
                        + " on tag: " + id);
        try {
            return SearchLoaderFactory.getLoader(id, mSearchQuery, this);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error generating Loader for search : " + mSearchQuery
             + " on tag: " + id);
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mCursorsReturned++;
        if(data.getCount() > 0) {
            SearchResultsFragment resultFragment = new SearchResultsFragment();
            mCursorswithResults++;

            switch (loader.getId()) {

                case SearchLoaderFactory.LOADER_LIN:
                    Log.i(LOG_TAG, "Search results returned for LIN");
                    resultFragment.init(
                            SearchResultsFragment.TYPE_LIN,
                            data,
                            this);
                    getActionBar().addTab(
                            getActionBar().newTab()
                                    .setText(R.string.lin)
                                    .setTabListener(mTabListener));
                    break;
                case SearchLoaderFactory.LOADER_NSN:
                    Log.i(LOG_TAG, "Search results returned for NSN");
                    resultFragment.init(
                            SearchResultsFragment.TYPE_NSN,
                            data,
                            this);
                    getActionBar().addTab(
                            getActionBar().newTab().setText(R.string.nsn)
                                .setTabListener(mTabListener));
                    break;
                case SearchLoaderFactory.LOADER_SN:
                    Log.i(LOG_TAG, "Search results returned for SN");
                    resultFragment.init(
                            SearchResultsFragment.TYPE_SN,
                            data,
                            this);
                    getActionBar().addTab(
                            getActionBar().newTab()
                                    .setText(R.string.serial_number)
                                    .setTabListener(mTabListener));
                    break;
                case SearchLoaderFactory.LOADER_NOMENCLATURE:
                    Log.i(LOG_TAG, "Search results returned for Nomenclature");
                    resultFragment.init(
                            SearchResultsFragment.TYPE_NOMENCLATURE,
                            data,
                            this);

                    getActionBar().addTab(
                            getActionBar().newTab()
                            .setText(R.string.nomenclature)
                            .setTabListener(mTabListener));
                    break;
                default:
                    throw new RuntimeException("Unexpected Loader ID provided.");
            }
            mFragmentArray.add(resultFragment);
            mPageAdapter.notifyDataSetChanged();

        }

        if(mCursorsReturned == 4 && mCursorswithResults == 0) {
            //replace the search results fragment with a no results found note
            NoSearchResultsFragment noResultsFragment =
                    NoSearchResultsFragment.getInstance(mSearchQuery);
            mFragmentArray.add(0, noResultsFragment);
            mPageAdapter.notifyDataSetChanged();

            //Turn off the tabs, nothing to show.
            getActionBar().setNavigationMode(
                    ActionBar.NAVIGATION_MODE_STANDARD);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        SearchResultsFragment fragment;
        switch (loader.getId()) {
            case SearchLoaderFactory.LOADER_LIN:
                break;
            case SearchLoaderFactory.LOADER_NSN:
                break;
            case SearchLoaderFactory.LOADER_SN:
                break;
            case SearchLoaderFactory.LOADER_NOMENCLATURE:
                break;
            default:
                throw new RuntimeException("Unexpected Loader ID provided.");
        }
    }
}

class SearchResultPageAdapter extends FragmentPagerAdapter {


    private ArrayList<android.support.v4.app.Fragment> mFragments;

    public SearchResultPageAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    public void setFragments(ArrayList<android.support.v4.app.Fragment> fragments) {
        mFragments = fragments;
    }

    public android.support.v4.app.Fragment getItem(int i) {
        return mFragments.get(i);
    }

    public int getCount() {
        return mFragments.size();
    }
}



