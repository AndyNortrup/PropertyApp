package com.NortrupDevelopment.PropertyBook.presenter;

import com.NortrupDevelopment.PropertyBook.dao.LineNumber;

/**
 * Defines an interface to be implmented by any Activity or Fragment that
 * will display LINDetail information under the control of the
 * LINDetailPresenter.
 * <p/>
 * Created by andy on 7/17/14.
 */
public interface LINDetail {


  /**
   * Directs the view to take the provided LIN object and add it to the view
   *
   * @param lineNumber LIN to be added to the view.
   */
  public void setLineNumber(LineNumber lineNumber);

}
