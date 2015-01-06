package com.NortrupDevelopment.PropertyBook.presenter;

import com.NortrupDevelopment.PropertyBook.bus.BusProvider;
import com.NortrupDevelopment.PropertyBook.bus.ImportRequestedEvent;
import com.NortrupDevelopment.PropertyBook.bus.LINDetailRequestedEvent;
import com.NortrupDevelopment.PropertyBook.bus.StatisticsRequestedEvent;
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

  private LINBrowser mInstance;

  public LINBrowserPresenter(LINBrowser activity) {
    mInstance = activity;
  }

  /**
   * Called by the activity to notify the presenter that it has resumed.
   */
  public void activityResumed() {
    loadListContents();
  }

  private void loadListContents() {
    //Show the loading progress bar.
    mInstance.showLoadingProgressBar();

    Realm realm = RealmDefinition.getRealm(mInstance.getContex(),
        RealmDefinition.PRODUCTION_REALM);
    RealmResults<LineNumber> lineNumbers =
        realm.where(LineNumber.class)
            .findAll();

    if(lineNumbers.size() > 0) {
      lineNumbers.sort("lin");

      mInstance.setList(lineNumbers);
      mInstance.showList();
    } else {
      BusProvider.getBus().post(new ImportRequestedEvent());
    }
  }

  //region UI Click events
  public void listItemSelected(LineNumber selected) {
    BusProvider.getBus().post(new LINDetailRequestedEvent(selected));
  }

  /**
   * Called by the Activity when the user requests the import activity.
   */
  public void importRequested() {
    BusProvider.getBus().post(new ImportRequestedEvent());
  }


  /**
   * Called by the Activity when the user request the import activity.
   */
  public void statisticsRequested() {
    BusProvider.getBus().post(new StatisticsRequestedEvent());
  }
  //endregion
}

