package com.NortrupDevelopment.PropertyBook.bus;

import android.view.View;

/**
 * Used to send a view with Search Results back to the
 * Created by andy on 2/8/15.
 */
public interface AddSearchResultsViewEvent {

  View getView();
}
