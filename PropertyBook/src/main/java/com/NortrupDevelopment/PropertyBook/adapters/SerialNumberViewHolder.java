package com.NortrupDevelopment.PropertyBook.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.NortrupDevelopment.PropertyBook.R;

/**
 * Created by andy on 12/14/14.
 */
public class SerialNumberViewHolder extends RecyclerView.ViewHolder {

  public TextView serialNumber;
  public TextView systemNumber;

  public SerialNumberViewHolder(View itemView) {
    super(itemView);
    serialNumber = (TextView)itemView.findViewById(R.id.serial_number);
    systemNumber = (TextView)itemView.findViewById(R.id.system_number);
  }
}
