package com.NortrupDevelopment.PropertyBook.adapters;

import android.view.View;
import android.widget.TextView;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.model.SerialNumber;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by andy on 2/8/15.
 */
public class DefaultSearchSerialNumberViewHolder
    extends DefaultSearchViewHolder<SerialNumber> {

  @InjectView(R.id.lin_search_lin)
  TextView mLineNumberLIN;
  @InjectView(R.id.lin_search_nomenclature)
  TextView mLineNumberNomenclature;
  @InjectView(R.id.nsn_search_nomenclature)
  TextView mNSNNomenclature;
  @InjectView(R.id.nsn_search_nsn)
  TextView mNSN;
  @InjectView(R.id.serial_number_search_sn)
  TextView mSerialNumber;

  public DefaultSearchSerialNumberViewHolder(View itemView) {
    super(itemView);
    ButterKnife.inject(itemView);
  }

  @Override
  public void setData(SerialNumber data) {
    mLineNumberLIN.setText(
        data.getStockNumber().getParentLineNumber().getLin());
    mLineNumberNomenclature.setText(
        data.getStockNumber().getParentLineNumber().getNomenclature());
    mNSNNomenclature.setText(data.getStockNumber().getNomenclature());
    mNSN.setText(data.getStockNumber().getNsn());
    mSerialNumber.setText(data.getSerialNumber());

  }
}
