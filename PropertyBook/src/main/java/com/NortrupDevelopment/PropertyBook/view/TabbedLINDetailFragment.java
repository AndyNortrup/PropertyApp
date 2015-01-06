package com.NortrupDevelopment.PropertyBook.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.model.LineNumber;
import com.NortrupDevelopment.PropertyBook.model.StockNumber;
import com.NortrupDevelopment.PropertyBook.view.search.TitledFragment;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by andy on 12/14/14.
 */
public class TabbedLINDetailFragment extends Fragment  implements LINDetail {

  private LineNumber mLIN;
  private NSNPagerAdapter mPagerAdapter;
  private ArrayList<NSNDetailFragment> mNSNFragments;

  @InjectView(R.id.nsn_detail_pager) ViewPager mViewPager;
  @InjectView(R.id.lin) TextView linTV;
  @InjectView(R.id.nomenclature) TextView nomenclatureTV;
  @InjectView(R.id.sub_lin) TextView subLinTV;
  @InjectView(R.id.authorized_value) TextView authorizedTV;
  @InjectView(R.id.required_value) TextView requiredTV;
  @InjectView(R.id.sri_value) TextView sriTV;
  @InjectView(R.id.due_in_value) TextView mDueInTV;
  @InjectView(R.id.erc_value) TextView ercTV;
  @InjectView(R.id.auth_doc_value) TextView authDocTV;
  @InjectView(R.id.pbic_value) TextView pbicTV;
  @InjectView(R.id.no_nsn_label) TextView mNoStockNumbersLabel;

  @Override
  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    View result = inflater.inflate(R.layout.tabbed_lin_detail,
        container,
        false);

    ButterKnife.inject(this, result);

    mNSNFragments = new ArrayList<NSNDetailFragment>();
    mViewPager = (ViewPager)result.findViewById(R.id.nsn_detail_pager);
    mPagerAdapter = new NSNPagerAdapter(
        getActivity().getSupportFragmentManager());
    mViewPager.setAdapter(mPagerAdapter);

    return result;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    if(mLIN != null) {
      setupViewFields();
    }
  }

  /**
   * Get a copy of the view's Context
   *
   * @return The view's context
   */
  @Override
  public Context getContext() {
    return getActivity();
  }

  /**
   * Directs the view to take the provided LIN object and add it to the view
   *
   * @param lineNumber LIN to be added to the view.
   */
  @Override
  public void addLIN(LineNumber lineNumber) {
    mLIN = lineNumber;
    setupViewFields();
  }

  private void setupViewFields() {
    setupHeaderFields();
    if(mLIN.getStockNumbers().size() > 0) {
      for (StockNumber stockNumber : mLIN.getStockNumbers()) {
        addStockNumber(stockNumber);
      }
    } else {
      mViewPager.setVisibility(View.GONE);
      mNoStockNumbersLabel.setVisibility(View.VISIBLE);
    }
  }

  private void addStockNumber(StockNumber stockNumber) {
    NSNDetailFragment detailFragment = new NSNDetailFragment();
    detailFragment.setStockNumber(stockNumber);
    mNSNFragments.add(detailFragment);
    mPagerAdapter.notifyDataSetChanged();
  }

  /**
   * Draws information from mLIN and sets up the header fields on the view.
   */
  private void setupHeaderFields() {
    linTV.setText(mLIN.getLin());
    nomenclatureTV.setText(mLIN.getNomenclature());

    if(!TextUtils.isEmpty(mLIN.getSubLin())) {
      subLinTV.setText("Sub LIN: " + mLIN.getSubLin());
      subLinTV.setVisibility(View.VISIBLE);
    } else {
      subLinTV.setVisibility(View.GONE);
    }

    authorizedTV.setText(String.valueOf(mLIN.getAuthorized()));
    requiredTV.setText(String.valueOf(mLIN.getRequired()));
    sriTV.setText(mLIN.getSri());
    mDueInTV.setText(Integer.toString(mLIN.getDueIn()));
    ercTV.setText(mLIN.getErc());
    authDocTV.setText(mLIN.getAuthDoc());

    if(mLIN.getPropertyBook() != null) {
      pbicTV.setText(mLIN.getPropertyBook().getPbic().replace("pbicTV ", ""));
    }
  }

  /**
   * Pager adapter to display all of our NSN detail fragments
   */
  private class NSNPagerAdapter extends FragmentStatePagerAdapter {

    public NSNPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
      return mNSNFragments.get(position);
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
      return mNSNFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int index) {
      return ((TitledFragment) mNSNFragments.get(index)).getTitle();
    }
  }
}
