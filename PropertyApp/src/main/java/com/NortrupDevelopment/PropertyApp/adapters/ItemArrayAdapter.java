package com.NortrupDevelopment.PropertyApp.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.NortrupDevelopment.PropertyApp.R;
import com.NortrupDevelopment.PropertyApp.model.Item;

import java.util.ArrayList;

/**
 * Used to display the contents of an array of Item objects in a ListView.
 * Created by andy on 6/22/14.
 */
public class ItemArrayAdapter extends ArrayAdapter<Item> {

  ArrayList<Item> mItems;
  Context mContext;

  public ItemArrayAdapter(Context context,
                          int resource,
                          ArrayList<Item> items) {
    super(context, resource);
    mContext = context;
    mItems = items;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View v = convertView;
    if(v == null) {
      v = LayoutInflater.from(mContext)
          .inflate(R.layout.item_list_item, null);
    }

    Item item = mItems.get(position);

    //Item Serial Number
    TextView snView =
        (TextView)v.findViewById(R.id.serial_number_value);
    snView.setText(item.getSerialNumber());

    //Item System Number
    TextView systemView =
        (TextView)v.findViewById(R.id.system_number_value);
    if(!TextUtils.isEmpty(item.getSysNo())) {
      systemView.setText(mItems.get(position).getSysNo());
      systemView.setVisibility(View.VISIBLE);
    } else {
      systemView.setVisibility(View.GONE);
    }

    //The ends in text
    //Sometimes the last character in a serial number is a non-significant
    //digit due to duplicates in the PBUSE system. Detect if this is something
    //other than a letter or a number.  If it is, move everything back one space
    String endsIn;
    if(!Character.isLetterOrDigit(
        item.getSerialNumber().charAt(item.getSerialNumber().trim().length() - 1)))
    {
      endsIn = item.getSerialNumber().trim().substring(
          item.getSerialNumber().length()-6,
          item.getSerialNumber().length()-2);
    } else {
      endsIn = item.getSerialNumber().trim().substring(
          item.getSerialNumber().length() - 5,
          item.getSerialNumber().length() - 1);
    }

    TextView endsInView = (TextView)v.findViewById(R.id.item_ends_in);
    endsInView.setText(endsIn);

    return v;
  }

  @Override
  public int getCount() {
    return mItems.size();
  }
}
