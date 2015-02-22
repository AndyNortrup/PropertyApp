package com.NortrupDevelopment.PropertyBook.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.NortrupDevelopment.PropertyBook.model.LineNumber;
import com.NortrupDevelopment.PropertyBook.model.SerialNumber;
import com.NortrupDevelopment.PropertyBook.model.StockNumber;

import java.util.AbstractList;

import io.realm.RealmObject;

/**
 * Created by andy on 2/8/15.
 */
public class DefaultSearchAdapter<T extends RealmObject>
    extends RecyclerView.Adapter<DefaultSearchViewHolder> {

  protected AbstractList<T> mItems;

  public DefaultSearchAdapter(AbstractList<T> data) {
    mItems = data;
  }


  @Override
  public DefaultSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if(mItems.size() > 0) {
      if(mItems.get(0) instanceof LineNumber) {
        return new DefaultSearchLineNumberViewHolder(parent);
      } else if(mItems.get(0) instanceof StockNumber) {
        return new DefaultSearchStockNumberViewHolder(parent);
      } else if (mItems.get(0) instanceof SerialNumber) {
        return new DefaultSearchSerialNumberViewHolder(parent);
      }
    }
    return null;
  }

  @Override
  public void onBindViewHolder(DefaultSearchViewHolder holder, int position) {
    holder.setData(mItems.get(position));
  }

  /**
   * Returns the total number of items in the data set hold by the adapter.
   *
   * @return The total number of items in this adapter.
   */
  @Override
  public int getItemCount() {
    return mItems.size();
  }
}

