package com.NortrupDevelopment.PropertyBook.presenter;

import com.NortrupDevelopment.PropertyBook.dao.LineNumber;

/**
 * Created by andy on 2/7/15.
 */
public interface MainActivityPresenter {

  LineNumber getCurrentDetailLineNumber();

  void attach(MainActivity activity);

  void setCurrentDetailLineNumber(LineNumber lineNumber);

  void requestCurrentScreen();

  void setScreenDetail();

  void searchRequested(String searchTerm);

  void setImportView();
}
