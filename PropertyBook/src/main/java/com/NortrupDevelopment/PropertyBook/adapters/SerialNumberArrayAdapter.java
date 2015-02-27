package com.NortrupDevelopment.PropertyBook.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.model.SerialNumber;

import java.util.AbstractList;

/**
 * Created by andy on 11/5/14.
 */
public class SerialNumberArrayAdapter
    extends ArrayAdapter<SerialNumber> {

  AbstractList<SerialNumber> mSerialNumbers;

  /**
   * Constructor
   *
   * @param context       The current context.
   * @param serialNumbers List of Serial Numbers to display
   */
  public SerialNumberArrayAdapter(Context context,
                                  AbstractList<SerialNumber> serialNumbers) {
    super(context, R.layout.item_list_item);
    mSerialNumbers = serialNumbers;
  }

  @Override
  public View getView(int position,
                      View convertView,
                      ViewGroup parent) {
    if (convertView == null) {
      convertView = LayoutInflater.from(getContext())
          .inflate(R.layout.item_list_item, parent, false);
    }

    SerialNumber serialNumber = mSerialNumbers.get(position);

    TextView serialNumberValue =
        (TextView) convertView.findViewById(R.id.serial_number_value);
    TextView lastFour =
        (TextView) convertView.findViewById(R.id.item_ends_in);
    TextView systemNumber =
        (TextView) convertView.findViewById(R.id.system_number_value);

    serialNumberValue.setText(serialNumber.getSerialNumber());

    lastFour.setText(serialNumber.getSerialNumber().trim().substring(
        serialNumber.getSerialNumber().length() - 4,
        serialNumber.getSerialNumber().length() - 1));

    systemNumber.setText(serialNumber.getSysNo());

    return convertView;
  }
}
