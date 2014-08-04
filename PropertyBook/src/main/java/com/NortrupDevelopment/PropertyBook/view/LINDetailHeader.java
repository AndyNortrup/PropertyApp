package com.NortrupDevelopment.PropertyBook.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.model.LIN;

import it.gmariotti.cardslib.library.internal.CardHeader;

/**
 * LINDetailHeader is a card header that provides all of the detailed
 * information about a LIN to the user.
 * Created by andy on 6/8/14.
 */
public class LINDetailHeader extends CardHeader {

  LIN mLIN;

  /**
   * Create an instance of the LINDetailHeader
   * @param context Context that the card will exist in
   */
  public LINDetailHeader(Context context, LIN lin) {
    super(context, R.layout.card_lin_detail);
    mLIN = lin;
  }

  @Override
  public void setupInnerViewElements(ViewGroup parent, View view) {

    TextView linTextView = (TextView)view.findViewById(R.id.lin);
    linTextView.setText(mLIN.getLin());

    TextView textView = (TextView)view.findViewById(
        R.id.lin_nomenclature);
    textView.setText(mLIN.getNomencalture());

    TextView subLinView = (TextView)view.findViewById(R.id.sub_lin);
    if(!TextUtils.isEmpty(mLIN.getSubLin())) {
      subLinView.setText("Sub LIN: " + mLIN.getSubLin());
      subLinView.setVisibility(View.VISIBLE);
    } else {
      subLinView.setVisibility(View.GONE);
    }

    TextView authorizedTV = (TextView)view.findViewById(
        R.id.lin_card_authorized_value);
    authorizedTV.setText(String.valueOf(mLIN.getAuthorized()));

    TextView requiredTV = (TextView)view.findViewById(
        R.id.lin_card_required_value);
    requiredTV.setText(String.valueOf(mLIN.getRequired()));

    TextView sriTV = (TextView)view.findViewById(
        R.id.lin_card_sri_value);
    sriTV.setText(mLIN.getSri());

    TextView mDueInTV = (TextView)view.findViewById(
        R.id.lin_card_due_in_value);
    mDueInTV.setText(Integer.toString(mLIN.getDi()));

    TextView ercTV = (TextView)view.findViewById(
        R.id.lin_card_erc_value);
    ercTV.setText(mLIN.getErc());

    TextView authDocTV = (TextView)view.findViewById(
        R.id.lin_card_auth_doc_value);
    authDocTV.setText(mLIN.getAuthDoc());

    if(mLIN.getPropertyBook() != null) {
      TextView PBIC = (TextView)view.findViewById(R.id.lin_card_pbic_value);
      PBIC.setText(mLIN.getPropertyBook().getPBIC().replace("PBIC ", ""));
    }
  }
}
