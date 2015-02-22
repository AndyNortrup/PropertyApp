package com.NortrupDevelopment.PropertyBook.presenter;

import com.NortrupDevelopment.PropertyBook.bus.DefaultImportRequestedEvent;
import com.NortrupDevelopment.PropertyBook.bus.DefaultLineNumberDetailEvent;
import com.NortrupDevelopment.PropertyBook.bus.StatisticsRequestedEvent;
import com.NortrupDevelopment.PropertyBook.model.LineNumber;
import com.NortrupDevelopment.PropertyBook.model.RealmDefinition;
import com.NortrupDevelopment.PropertyBook.view.LINBrowserView;

import de.greenrobot.event.EventBus;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Provides presentation layer processing for the LINBrowserActivity.
 * Created by andy on 7/12/14.
 */
public class LINBrowserPresenter{

  private LINBrowser mInstance;

  public LINBrowserPresenter(LINBrowserView activity) {
    mInstance = activity;
  }

  /**
   * Called by the activity to notify the presenter that it has resumed.
   */
  public void activityResumed() {
    loadListContents();
  }

  public void loadListContents() {
    //Show the loading progress bar.
    mInstance.showLoadingProgressBar();

    //TODO: Convert to use ModelSearcher
    Realm realm = RealmDefinition.getRealm(mInstance.getContext(),
        RealmDefinition.PRODUCTION_REALM);
    RealmResults<LineNumber> lineNumbers =
        realm.where(LineNumber.class)
            .findAll();

    if(lineNumbers.size() > 0) {
      lineNumbers.sort("lin");

      mInstance.setList(lineNumbers);
      mInstance.showList();
    } else {
      EventBus.getDefault().post(new DefaultImportRequestedEvent());
    }
  }

  //region UI Click events
  public void listItemSelected(LineNumber selected) {
    EventBus.getDefault().post(new DefaultLineNumberDetailEvent(selected));
  }

  /**
   * Called by the Activity when the user requests the import activity.
   */
  public void importRequested() {
    EventBus.getDefault().post(new DefaultImportRequestedEvent());
  }


  /**
   * Called by the Activity when the user request the import activity.
   */
  public void statisticsRequested() {
    EventBus.getDefault().post(new StatisticsRequestedEvent());
  }
  //endregion
}

