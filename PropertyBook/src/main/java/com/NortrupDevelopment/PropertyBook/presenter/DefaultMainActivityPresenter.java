package com.NortrupDevelopment.PropertyBook.presenter;

import android.app.SearchManager;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.NortrupDevelopment.PropertyBook.bus.DefaultDisplayBrowserEvent;
import com.NortrupDevelopment.PropertyBook.bus.DefaultLineNumberDetailEvent;
import com.NortrupDevelopment.PropertyBook.dao.LineNumber;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

/**
 * Created by andy on 12/15/14.
 */
public class DefaultMainActivityPresenter implements MainActivityPresenter {

  private static final int SCREEN_BROWSER = 0;
  private static final int SCREEN_DETAIL = 1;

  LineNumber mCurrentDetailLineNumber;
  int mCurrentScreen;

  @Inject
  public DefaultMainActivityPresenter() {
  }

  @Override
  @Nullable
  public LineNumber getCurrentDetailLineNumber() {
    return mCurrentDetailLineNumber;
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
    if(mCurrentDetailLineNumber != null) {
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
   * Line Number Browser `Screen
   */
  @Override
  public void setScreenDetail() {
    mCurrentScreen = SCREEN_DETAIL;
  }

  /**
   *
   * @param intent
   */
  @Override
  public void searchRequested(Intent intent) {
    if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
      String query = intent.getStringExtra(SearchManager.QUERY);
    }

  }
}
