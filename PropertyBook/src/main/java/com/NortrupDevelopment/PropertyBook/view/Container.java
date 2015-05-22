package com.NortrupDevelopment.PropertyBook.view;

import com.NortrupDevelopment.PropertyBook.dao.LineNumber;

/**
 * Created by andy on 1/7/15.
 */
public interface Container {
  void showLineNumber(LineNumber lineNumber);

  void showBrowser();

  void showImport();

  boolean onBackPressed();
}
