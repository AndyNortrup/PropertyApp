package com.NortrupDevelopment.PropertyBook.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.NortrupDevelopment.PropertyBook.view.TitledView;

/**
 * Created by andy on 2/21/15.
 */
public abstract class SearchResultsList extends RecyclerView
    implements TitledView {
  public SearchResultsList(Context context) {
    super(context);
  }
}
