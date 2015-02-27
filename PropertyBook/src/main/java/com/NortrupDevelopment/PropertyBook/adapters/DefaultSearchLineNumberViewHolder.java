package com.NortrupDevelopment.PropertyBook.adapters;

import android.view.View;
import android.widget.TextView;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.model.LineNumber;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by andy on 2/8/15.
 */
public class DefaultSearchLineNumberViewHolder
    extends DefaultSearchViewHolder<LineNumber> {

  @InjectView(R.id.lin_search_lin)
  TextView lineNumber;
  @InjectView(R.id.lin_search_nomenclature)
  TextView nomenclature;

  public DefaultSearchLineNumberViewHolder(View itemView) {
    super(itemView);
    ButterKnife.inject(itemView);
  }

  @Override
  public void setData(LineNumber data) {
    lineNumber.setText(data.getLin());
    lineNumber.setText(data.getNomenclature());
  }


}
