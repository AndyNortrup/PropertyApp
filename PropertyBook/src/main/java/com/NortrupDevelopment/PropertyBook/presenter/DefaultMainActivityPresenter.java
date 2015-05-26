package com.NortrupDevelopment.PropertyBook.presenter;

import com.NortrupDevelopment.PropertyBook.bus.DefaultDisplayBrowserEvent;
import com.NortrupDevelopment.PropertyBook.bus.DefaultLineNumberDetailEvent;
import com.NortrupDevelopment.PropertyBook.dao.LineNumber;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import io.realm.Realm;

/**
 * Created by andy on 12/15/14.
 */
public class DefaultMainActivityPresenter implements MainActivityPresenter {

  private static final int SCREEN_BROWSER = 0;
  private static final int SCREEN_DETAIL = 1;
  private static final int SCREEN_IMPORT = 2;
  private static final int SCREEN_SEARCH = 3;

  LineNumber mCurrentDetailLineNumber;
  int mCurrentScreen;
  MainActivity activity;
  Realm realm;

  @Inject
  public DefaultMainActivityPresenter(Realm realm) {
    this.realm = realm;
  }

  @Override
  public LineNumber getCurrentDetailLineNumber() {
    return mCurrentDetailLineNumber;
  }

  @Override
  public void attach(MainActivity activity) {
    this.activity = activity;
  }

  @Override
  public void setCurrentDetailLineNumber(LineNumber lineNumber) {
    mCurrentDetailLineNumber = lineNumber;
  }

  @Override
  public void requestCurrentScreen() {
    switch (mCurrentScreen) {
      case SCREEN_DETAIL:
        setViewToLineDetail();
        break;
      case SCREEN_BROWSER:
        setViewToBrowser();
        break;
      case SCREEN_IMPORT:
        setImportView();
        break;
      case SCREEN_SEARCH:
        setViewSearch();
        break;
      default:
        setViewToBrowser();
        break;

    }
  }

  /**
   * Sends a message over the EventBus that the MainActivity should display the
   * Line Number Detail Screen
   */
  private void setViewToLineDetail() {
    if (mCurrentDetailLineNumber != null) {
      EventBus.getDefault().post(
          new DefaultLineNumberDetailEvent(mCurrentDetailLineNumber));
    } else {
      mCurrentScreen = SCREEN_BROWSER;
      setViewToBrowser();
    }
  }

  /**
   * Sends a message over the EventBus that the MainActivity should display the
   * Line Number Browser `Screen
   */
  private void setViewToBrowser() {
    EventBus.getDefault().post(new DefaultDisplayBrowserEvent());
  }

  /**
   * Sends a message over the EventBus that the MainActivity should display the
   * Line Number Browser Screen
   */
  @Override
  public void setScreenDetail() {
    mCurrentScreen = SCREEN_DETAIL;
  }

  /**
   * Searches the model for information based on a search term
   *
   * @param searchTerm - The term that is being searched for by the user
   */
  @Override
  public void searchRequested(String searchTerm) {

  }

  private void setViewSearch() {
  }

  @Override public void setImportView() {
    mCurrentScreen = SCREEN_IMPORT;
    activity.showImport();
  }

}
