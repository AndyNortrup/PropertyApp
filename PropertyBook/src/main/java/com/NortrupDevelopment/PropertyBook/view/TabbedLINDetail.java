package com.NortrupDevelopment.PropertyBook.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.model.LineNumber;
import com.NortrupDevelopment.PropertyBook.model.StockNumber;
import com.NortrupDevelopment.PropertyBook.presenter.LINDetailPresenter;
import com.NortrupDevelopment.PropertyBook.util.NSNFormatter;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by andy on 12/14/14.
 */
public class TabbedLINDetail extends LinearLayout implements LINDetail {

  public static final String KEY_LIN_UUID = "LIN_UUID";

  private LineNumber mLIN;
  private ViewPagerAdapter mPagerAdapter;
  private LINDetailPresenter mPresenter;

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

  public TabbedLINDetail(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected void onFinishInflate() {

    ButterKnife.inject(this);

    mViewPager = (ViewPager)findViewById(R.id.nsn_detail_pager);
    mPagerAdapter = new ViewPagerAdapter();

    mPresenter = new LINDetailPresenter(this);

  }

  /**
   * Directs the view to take the provided LIN object and add it to the view
   *
   * @param lineNumber LIN to be added to the view.
   */
  @Override
  public void addLineNumber(LineNumber lineNumber) {
    mLIN = lineNumber;
    setupViewFields();
  }

  /**
   * Retrieves the currently displayed LineNumber
   *
   * @return Currently displayed LineNumber
   */
  @Override
  public LineNumber getLineNumber() {
    return mLIN;
  }

  private void setupViewFields() {
    setupHeaderFields();
    mViewPager.setAdapter(mPagerAdapter);
    if(mLIN.getStockNumbers().size() > 0) {
      mPagerAdapter.notifyDataSetChanged();
    } else {
      mViewPager.setVisibility(View.GONE);
      mNoStockNumbersLabel.setVisibility(View.VISIBLE);
    }
  }

  /**
   * Draws information from mLIN and sets up the header fields on the view.
   */
  private void setupHeaderFields() {
    linTV.setText(mLIN.getLin());
    nomenclatureTV.setText(mLIN.getNomenclature());

    if (!TextUtils.isEmpty(mLIN.getSubLin())) {
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

    if (mLIN.getPropertyBook() != null) {
      pbicTV.setText(mLIN.getPropertyBook().getPbic().replace("pbicTV ", ""));
    }
  }

  private class ViewPagerAdapter extends PagerAdapter {

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
      return mLIN.getStockNumbers().size();
    }

    /**
     * This method may be called by the ViewPager to obtain a title string
     * to describe the specified page. This method may return null
     * indicating no title for this page. The default implementation returns
     * null.
     *
     * @param position The position of the title requested
     * @return A title for the requested page
     */
    @Override
    public CharSequence getPageTitle(int position) {
      return NSNFormatter.getFormattedNSN(
          mLIN.getStockNumbers().get(position).getNsn());
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {

      StockNumber stockNumber = mLIN.getStockNumbers().get(position);

      LayoutInflater inflater = LayoutInflater.from(getContext());
      NSNDetail view =
          (NSNDetail)inflater.inflate(R.layout.nsn_detail, null);
      view.setStockNumber(stockNumber);

      ((ViewPager)collection).addView(view);

      return view;
    }

    /**
     * Determines whether a page View is associated with a specific key object
     * as returned by {@link #instantiateItem(android.view.ViewGroup, int)}. This method is
     * required for a PagerAdapter to function properly.
     * @param view   Page View to check for association with <code>object</code>
     * @param object Object to check for association with <code>view</code>
     * @return true if <code>view</code> is associated with the key object <code>object</code>
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
      return view == object;
    }

    /**
     * Remove an object from the the collection
     * @param container Container with the item in it.
     * @param position Position of the view in the collection
     * @param view View to be removed
     */
    @Override public void destroyItem(ViewGroup container,
                                      int position,
                                      Object view)
    {
      ((ViewPager)container).removeView((View)view);
    }
  }
}
