package com.NortrupDevelopment.PropertyBook.presenter;

import android.content.Intent;
import android.support.annotation.Nullable;

import com.NortrupDevelopment.PropertyBook.model.LineNumber;

/**
 * Created by andy on 2/7/15.
 */
public interface MainActivityPresenter {
  @Nullable
  LineNumber getCurrentDetailLineNumber();

  void setCurrentDetailLineNumber(LineNumber lineNumber);

  void requestCurrentScreen();

  void setScreenDetail();

  void searchRequested(Intent intent);
}
