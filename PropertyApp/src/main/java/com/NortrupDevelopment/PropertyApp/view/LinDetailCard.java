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
public class LinDetailCard extends Card {

  private String mLin;
  private long mLinId;
  private String mSubLin;
  private String mNomenclature;
  private int mAuthorized;
  private int mRequired;
  private String mAuthDoc;
  private int mDueIn;
  private String mSRI;
  private String mERC;

  public LinDetailCard(Context context) {
      super(context, R.layout.card_lin_detail);
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

    TextView authorizedTV = (TextView)view.findViewById(
        R.id.lin_card_authorized_value);
    authorizedTV.setText(String.valueOf(mAuthorized));

    TextView requiredTV = (TextView)view.findViewById(
        R.id.lin_card_required_value);
    requiredTV.setText(String.valueOf(mRequired));

    TextView sriTV = (TextView)view.findViewById(
        R.id.lin_card_sri_value);
    sriTV.setText(mSRI);

    TextView mDueInTV = (TextView)view.findViewById(
        R.id.lin_card_due_in_value);
    mDueInTV.setText(String.valueOf(mDueIn));

    TextView ercTV = (TextView)view.findViewById(
        R.id.lin_card_erc_value);
    ercTV.setText(mERC);

    TextView authDocTV = (TextView)view.findViewById(
        R.id.lin_card_auth_doc_value);
    authDocTV.setText(mAuthDoc);

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

  public int getAuthorized() {
    return mAuthorized;
  }

  public void setAuthorized(int mAuthorized) {
    this.mAuthorized = mAuthorized;
  }

  public int getRequired() {
    return mRequired;
  }

  public void setRequired(int mRequired) {
    this.mRequired = mRequired;
  }

  public String getAuthDoc() {
    return mAuthDoc;
  }

  public void setAuthDoc(String mAuthDoc) {
    this.mAuthDoc = mAuthDoc;
  }

  public int getDueIn() {
    return mDueIn;
  }

  public void setDueIn(int mDueIn) {
    this.mDueIn = mDueIn;
  }

  public String getSRI() {
    return mSRI;
  }

  public void setSRI(String mSRI) {
    this.mSRI = mSRI;
  }

  public String getERC() {
    return mERC;
  }

  public void setERC(String mERC) {
    this.mERC = mERC;
  }
}
