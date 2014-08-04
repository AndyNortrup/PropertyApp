package com.NortrupDevelopment.PropertyBook.view;

import android.app.LoaderManager;
import android.content.Context;

import com.NortrupDevelopment.PropertyBook.model.LIN;
import com.NortrupDevelopment.PropertyBook.model.NSN;

/**
 * Defines an interface to be implmented by any Activity or Fragment that
 * will display LINDetail information under the control of the
 * LINDetailPresenter.
 *
 * Created by andy on 7/17/14.
 */
public interface LINDetail {
  /**
   * Requests a copy of the LoaderManager from the view
   * @return The view's LoaderManager
   */
  public LoaderManager getViewLoaderManager();

  /**
   * Get a copy of the view's Context
   * @return The view's context
   */
  public Context getContext();

  /**
   * Directs the view to take the provided LIN object and add it to the view
   * @param lin LIN to be added to the view.
   */
  public void addLIN(LIN lin);

  /**
   * Directs the view to add the give NSN to the given LIN.
   * @param nsn NSN to add.
   * @param lin LIN which it belongs to,
   */
  public void addNSNtoLIN(NSN nsn, LIN lin);
}
