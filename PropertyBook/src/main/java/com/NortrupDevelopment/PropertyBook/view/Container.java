package com.NortrupDevelopment.PropertyBook.view;

import com.NortrupDevelopment.PropertyBook.model.LineNumber;

/**
 * Created by andy on 1/7/15.
 */
public interface Container {
  void showLineNumber(LineNumber lineNumber);
  void showBrowser();
  boolean onBackPressed();
}
