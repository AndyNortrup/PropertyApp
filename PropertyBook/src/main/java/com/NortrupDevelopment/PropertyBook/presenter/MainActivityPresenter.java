package com.NortrupDevelopment.PropertyBook.presenter;

import android.support.annotation.Nullable;

import com.NortrupDevelopment.PropertyBook.model.LineNumber;
import com.NortrupDevelopment.PropertyBook.view.MainActivity;

/**
 * Created by andy on 12/15/14.
 */
public class MainActivityPresenter {

  private static final int SCREEN_DETAIL = 1;

  private static MainActivityPresenter instance;

  MainActivity mActivity;

  LineNumber mCurrentDetailLineNumber;
  int mCurrentScreen;

  public static MainActivityPresenter getInstance(MainActivity activity) {
    if(instance == null) {
      instance = new MainActivityPresenter(activity);
    } else {
      instance.setActivity(activity);
    }
    return instance;
  }

  private MainActivityPresenter(MainActivity activity) {
    mActivity = activity;


  }

  @Nullable
  public LineNumber getCurrentDetailLineNumber() {
    return mCurrentDetailLineNumber;
  }

  public void setActivity(MainActivity activity) {
    mActivity = activity;
  }

  public void setCurrentDetailLineNumber(LineNumber lineNumber) {
    mCurrentDetailLineNumber = lineNumber;
  }

  public int getCurrentScreen() {
    return mCurrentScreen;
  }

  public void setScreenDetail() {
    mCurrentScreen = SCREEN_DETAIL;
  }
}
