package com.NortrupDevelopment.PropertyBook.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.NortrupDevelopment.PropertyBook.model.LineNumber;
import com.NortrupDevelopment.PropertyBook.presenter.LINDetailPresenter;

/**
 * Created by andy on 12/14/14.
 */
public class TabbedLINBrowser extends Activity implements LINDetail {

  LINDetailPresenter mPresenter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
       mPresenter = new LINDetailPresenter(this);
  }

  /**
   * Get a copy of the view's Context
   *
   * @return The view's context
   */
  @Override
  public Context getContext() {
    return this;
  }

  /**
   * Directs the view to take the provided LIN object and add it to the view
   *
   * @param lineNumber LIN to be added to the view.
   */
  @Override
  public void addLIN(LineNumber lineNumber) {

  }
}
