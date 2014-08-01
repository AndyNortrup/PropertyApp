package com.NortrupDevelopment.PropertyApp.presenter;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;

import com.NortrupDevelopment.PropertyApp.model.LIN;
import com.NortrupDevelopment.PropertyApp.model.LINLoader;

import java.util.ArrayList;

/**
 * Provides presentation layer processing for the LINBrowserActivity.
 * Created by andy on 7/12/14.
 */
public class LINBrowserPresenter implements LoaderManager.LoaderCallbacks<ArrayList<LIN>> {

  private LINBrowser mActivity;

  private int LIN_LOADER = 0;

  public LINBrowserPresenter(LINBrowser activity) {
    mActivity = activity;

    //Show the loading progress bar.
    mActivity.showLoadingProgressBar();

    //Start loading data from the database
    mActivity.getActivityLoaderManager().initLoader(LIN_LOADER, null, this);
  }

  /**
   * Called by the activity to notify the presenter that it has resumed.
   */
  public void activityResumed() {
    mActivity.getActivityLoaderManager().restartLoader(LIN_LOADER, null, this);
  }

  //region UI Click events

  public void listItemSelected(LIN selected) {
    mActivity.startLINDetailActivity(selected.getLinId());
  }

  /**
   * Called by the Activity when the user requests the import activity.
   */
  public void importRequested() {
    mActivity.startImportActivity();
  }


  /**
   * Called by the Activity when the user request the import activity.
   */
  public void statisticsRequested() {
    mActivity.startStatisticsActivity();
  }
  //endregion

  //region LoaderCallbacks
  @Override
  public Loader<ArrayList<LIN>> onCreateLoader(int id, Bundle args) {
    LINLoader loader = new LINLoader((Context)mActivity);
    loader.includeSubLINs(false);
    return loader;
  }

  @Override
  public void onLoadFinished(Loader<ArrayList<LIN>> loader, ArrayList<LIN> data) {
    if(data == null || data.size() == 0) {
      mActivity.startImportActivity();
    } else {
      mActivity.setCardList(data);
      mActivity.showList();
    }

  }

  @Override
  public void onLoaderReset(Loader<ArrayList<LIN>> loader) {
    mActivity.setCardList(null);
  }
  //endregion
}

