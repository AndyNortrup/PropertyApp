package com.NortrupDevelopment.PropertyBook.adapters;

import android.view.View;
import android.widget.TextView;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.model.StockNumber;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Relates a view to a set of StockNumber values
 * Created by andy on 2/8/15.
 */
public class DefaultSearchStockNumberViewHolder
    extends  DefaultSearchViewHolder<StockNumber> {

  @InjectView(R.id.nsn_search_nomenclature) TextView nomenclature;
  @InjectView(R.id.nsn_search_nsn) TextView stockNumber;

  public DefaultSearchStockNumberViewHolder(View view) {
    super(view);
    ButterKnife.inject(view);
  }

  @Override
  public void setData(StockNumber data) {
    nomenclature.setText(data.getNomenclature());
    stockNumber.setText(data.getNomenclature());
  }
}
