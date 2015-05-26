package com.NortrupDevelopment.PropertyBook.presenter;

import com.NortrupDevelopment.PropertyBook.bus.DefaultImportRequestedEvent;
import com.NortrupDevelopment.PropertyBook.bus.DefaultLineNumberDetailEvent;
import com.NortrupDevelopment.PropertyBook.bus.StatisticsRequestedEvent;
import com.NortrupDevelopment.PropertyBook.dao.LineNumber;
import com.NortrupDevelopment.PropertyBook.model.ModelSearcher;

import java.util.AbstractList;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

/**
 * Provides presentation layer processing for the LINBrowserActivity.
 * Created by andy on 7/12/14.
 */
public class LINBrowserPresenter {

  private LINBrowser mInstance;
  private ModelSearcher searcher;

  @Inject
  public LINBrowserPresenter(ModelSearcher searcher) {
    this.searcher = searcher;
  }

  public void attach(LINBrowser activity) {
    mInstance = activity;
  }


  public void loadListContents() {
    //Show the loading progress bar.
    mInstance.showLoadingProgressBar();

    AbstractList<LineNumber> lineNumbers = searcher.getAllLineNumbers();

    if (lineNumbers.size() > 0) {
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

