package com.NortrupDevelopment.PropertyBook.presenter;

import android.content.Context;

import com.NortrupDevelopment.PropertyBook.model.LineNumber;
import com.NortrupDevelopment.PropertyBook.model.RealmDefinition;
import com.NortrupDevelopment.PropertyBook.view.LINBrowser;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Provides presentation layer processing for the LINBrowserActivity.
 * Created by andy on 7/12/14.
 */
public class LINBrowserPresenter{

  private LINBrowser mActivity;

  public LINBrowserPresenter(LINBrowser activity) {
    mActivity = activity;
  }

  /**
   * Called by the activity to notify the presenter that it has resumed.
   */
  public void activityResumed() {
    loadListContents();
  }

  private void loadListContents() {
    //Show the loading progress bar.
    mActivity.showLoadingProgressBar();

    Realm realm = RealmDefinition.getRealm((Context)mActivity,
        RealmDefinition.PRODUCTION_REALM);
    RealmResults<LineNumber> lineNumbers =
        realm.where(LineNumber.class)
            .findAll();

    if(lineNumbers.size() > 0) {
      lineNumbers.sort("lin");

      mActivity.setCardList(lineNumbers);
      mActivity.showList();
    } else {
      mActivity.startImportActivity();
    }
  }

  //region UI Click events
  public void listItemSelected(LineNumber selected) {
    mActivity.startLINDetailActivity(selected.getLin());
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
}

