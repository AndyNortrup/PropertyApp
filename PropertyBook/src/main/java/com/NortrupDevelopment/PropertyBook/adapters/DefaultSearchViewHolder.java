package com.NortrupDevelopment.PropertyBook.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by andy on 2/8/15.
 */
public abstract class DefaultSearchViewHolder<T> extends RecyclerView.ViewHolder{

  public DefaultSearchViewHolder(View itemView) {
    super(itemView);
  }

  public abstract void setData(T data);
}
