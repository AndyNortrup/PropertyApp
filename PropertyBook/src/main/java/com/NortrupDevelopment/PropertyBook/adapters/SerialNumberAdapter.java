package com.NortrupDevelopment.PropertyBook.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.dao.SerialNumber;

import java.util.AbstractList;

/**
 * Created by andy on 12/9/14.
 */
public class SerialNumberAdapter
    extends RecyclerView.Adapter<SerialNumberViewHolder> {

  private AbstractList<SerialNumber> mItems;

  public SerialNumberAdapter(AbstractList<SerialNumber> data) {
    mItems = data;
  }

  @Override
  public SerialNumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    View itemView = LayoutInflater.from(
        viewGroup.getContext())
        .inflate(R.layout.serial_number_item, viewGroup, false);

    return new SerialNumberViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(SerialNumberViewHolder serialNumberViewHolder,
                               int i) {
    SerialNumber item = mItems.get(i);
    serialNumberViewHolder.serialNumber.setText(item.getSerialNumber());
    serialNumberViewHolder.systemNumber.setText(item.getSystemNumber());
  }

  @Override
  public int getItemCount() {
    return mItems.size();
  }
}
