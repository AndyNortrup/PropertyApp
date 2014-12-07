package com.NortrupDevelopment.PropertyBook.view;

import android.content.Context;

import com.NortrupDevelopment.PropertyBook.model.LineNumber;

/**
 * Defines an interface to be implmented by any Activity or Fragment that
 * will display LINDetail information under the control of the
 * LINDetailPresenter.
 *
 * Created by andy on 7/17/14.
 */
public interface LINDetail {

  /**
   * Get a copy of the view's Context
   * @return The view's context
   */
  public Context getContext();

  /**
   * Directs the view to take the provided LIN object and add it to the view
   * @param lineNumber LIN to be added to the view.
   */
  public void addLIN(LineNumber lineNumber);

}
