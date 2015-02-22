package com.NortrupDevelopment.PropertyBook.bus;

import android.view.View;

/**
 * Created by andy on 2/8/15.
 */
public class DefaultAddSearchResultsViewEvent
    implements AddSearchResultsViewEvent {

  View mView;

  public DefaultAddSearchResultsViewEvent(View view) {
    mView = view;
  }

  public View getView() {
    return mView;
  }
}
