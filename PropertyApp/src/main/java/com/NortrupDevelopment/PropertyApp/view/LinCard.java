package com.NortrupDevelopment.PropertyApp.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.NortrupDevelopment.PropertyApp.R;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Custom Card used to provide additional details about a LIN
 * Created by andy on 2/15/14.
 */
public class LinCard extends Card {

  private String mLin;
  private long mLinId;
  private String mSubLin;
  private String mNomenclature;


  public LinCard(Context context) {
      super(context, R.layout.card_lin_browser);
  }

  @Override
  public void setupInnerViewElements(ViewGroup parent, View view) {

    if(!TextUtils.isEmpty(mLin)) {
      TextView linTextView = (TextView)view.findViewById(R.id.lin);
      linTextView.setText(mLin);
    }

    if(!TextUtils.isEmpty(mNomenclature)) {
        TextView textView = (TextView)view.findViewById(
                R.id.lin_nomenclature);
        textView.setText(mNomenclature);
    }

    TextView subLinView = (TextView)view.findViewById(R.id.sub_lin);
    if(!TextUtils.isEmpty(mSubLin)) {
      subLinView.setText("Sub LIN: " + mSubLin);
      subLinView.setVisibility(View.VISIBLE);
    } else {
      subLinView.setVisibility(View.GONE);
    }

  }

  public long getLinId() {
    return mLinId;
  }

  public void setLinId(long linId) {
    this.mLinId = linId;
  }

  public String getSubLin() {
    return mSubLin;
  }

  public void setSubLin(String subLin) {
    this.mSubLin = subLin;
  }


  public String getNomenclature() {
      return mNomenclature;
  }

    public void setNomenclature(String nomenclature) {
        this.mNomenclature = nomenclature;
    }

  public void setLin(String lin) {
    mLin = lin;
  }

  public String getLin() {
    return mLin;
  }


}
