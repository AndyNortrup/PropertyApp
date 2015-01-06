package com.NortrupDevelopment.PropertyBook.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.adapters.SeachAdapters.SerialNumberAdapter;
import com.NortrupDevelopment.PropertyBook.model.StockNumber;
import com.NortrupDevelopment.PropertyBook.util.NSNFormatter;
import com.NortrupDevelopment.PropertyBook.view.search.TitledFragment;

import java.text.NumberFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by andy on 12/9/14.
 */
public class NSNDetailFragment extends Fragment
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

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup parent,
    Bundle savedInstanceState)
  {
    View result = inflater.inflate(R.layout.fragment_nsn_detail, parent, false);

    ButterKnife.inject(this, result);

    //mRecyclerView.setHasFixedSize(true);
    mLayoutManager = new LinearLayoutManager(getActivity());
    mRecyclerView.setLayoutManager(mLayoutManager);
    DividerItemDecoration decoration = new DividerItemDecoration(getActivity(),
        DividerItemDecoration.VERTICAL_LIST);
    mRecyclerView.addItemDecoration(decoration);

    return result;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    if(mStockNumber != null) {
      setViewValues();
    }
  }

  public void setStockNumber(StockNumber stockNumber) {
    mStockNumber = stockNumber;

    if(getView() != null) {
      setViewValues();
    }
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
