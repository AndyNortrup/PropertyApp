package com.NortrupDevelopment.PropertyBook.presenter;

import android.content.Context;

/**
 * Search results list is a view that contains a RecyclerView for presenting
 * the results of a user search.  This is the default implementation.
 * Created by andy on 2/21/15.
 */
public class DefaultSearchResultsView extends SearchResultsList {

  private String title;

  public DefaultSearchResultsView(Context context, String title) {
    super(context);
    this.title = title;
  }


  @Override
  public CharSequence getTitle() {
    return title;
  }
}
