package com.NortrupDevelopment.PropertyBook.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.adapters.SerialNumberAdapter;
import com.NortrupDevelopment.PropertyBook.model.StockNumber;
import com.NortrupDevelopment.PropertyBook.util.NSNFormatter;

import java.text.NumberFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * View extends a LinearLayout for the display of information pertaining to a
 * NSN
 * Created by andy on 12/9/14.
 */
public class NSNDetail extends LinearLayout
  implements TitledFragment
{

  @InjectView(R.id.serial_number_list) RecyclerView mRecyclerView;
  @InjectView(R.id.nomenclature_value) TextView mNomenclature;
  @InjectView(R.id.nsn_value) TextView mStockNumberLabel;
  @InjectView(R.id.on_hand_value) TextView mOnHand;
  @InjectView(R.id.unit_of_issue_value) TextView mUnitOfIssue;
  @InjectView(R.id.unit_price_value) TextView mUnitPrice;

  private RecyclerView.LayoutManager mLayoutManager;
  private StockNumber mStockNumber;

  public NSNDetail(Context context, AttributeSet attr) {
    super(context, attr);
  }

  @Override protected void onFinishInflate() {
    ButterKnife.inject(this);

    mLayoutManager = new LinearLayoutManager(getContext());
    mRecyclerView.setLayoutManager(mLayoutManager);
    DividerItemDecoration decoration = new DividerItemDecoration(getContext(),
        DividerItemDecoration.VERTICAL_LIST);
    mRecyclerView.addItemDecoration(decoration);
  }

  public void setStockNumber(StockNumber stockNumber) {
    mStockNumber = stockNumber;
    setViewValues();
  }

  public String getTitle() {
    if(mStockNumber == null) {
      return "";
    }
    return NSNFormatter.getFormattedNSN(mStockNumber.getNsn());
  }

  private void setViewValues() {
    mNomenclature.setText(mStockNumber.getNomenclature());
    mStockNumberLabel.setText(
        NSNFormatter.getFormattedNSN(mStockNumber.getNsn()));
    mOnHand.setText(String.valueOf(mStockNumber.getOnHand()));
    mUnitOfIssue.setText(mStockNumber.getUi());
    mUnitPrice.setText(
        NumberFormat.getCurrencyInstance().format(
            (long)(mStockNumber.getUnitPrice()/100)));

    mRecyclerView.setAdapter(
        new SerialNumberAdapter(mStockNumber.getSerialNumbers()));
  }
}
