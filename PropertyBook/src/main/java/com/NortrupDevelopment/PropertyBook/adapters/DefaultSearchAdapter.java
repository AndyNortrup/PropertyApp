package com.NortrupDevelopment.PropertyBook.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.NortrupDevelopment.PropertyBook.dao.LineNumber;
import com.NortrupDevelopment.PropertyBook.dao.SerialNumber;
import com.NortrupDevelopment.PropertyBook.dao.StockNumber;

import java.util.AbstractList;

/**
 * Created by andy on 2/8/15.
 */
public class DefaultSearchAdapter<T>
    extends RecyclerView.Adapter<DefaultSearchViewHolder> {

  protected AbstractList<T> mItems;
  //private SparseArray<RecyclerView.ViewHolder> viewHolders;

  public DefaultSearchAdapter(AbstractList<T> data) {
    mItems = data;
    //viewHolders = new SparseArray<RecyclerView.ViewHolder>(data.size());
  }


  @Override
  public DefaultSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    if (mItems.size() > 0) {
      if (mItems.get(0) instanceof LineNumber) {
        return new DefaultSearchLineNumberViewHolder(parent);
      } else if (mItems.get(0) instanceof StockNumber) {
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

