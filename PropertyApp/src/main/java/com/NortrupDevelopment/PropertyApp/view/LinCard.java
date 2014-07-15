package com.NortrupDevelopment.PropertyApp.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.NortrupDevelopment.PropertyApp.R;
import com.NortrupDevelopment.PropertyApp.model.LIN;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Custom Card used to provide additional details about a LIN
 * Created by andy on 2/15/14.
 */
public class LinCard extends Card {

  private LIN mLIN;

  public LinCard(Context context) {
      super(context, R.layout.card_lin_browser);
  }

  @Override
  public void setupInnerViewElements(ViewGroup parent, View view) {

    if(!TextUtils.isEmpty(mLIN.getLin())) {
      TextView linTextView = (TextView)view.findViewById(R.id.lin);
      linTextView.setText(mLIN.getLin());
    }

    if(!TextUtils.isEmpty(mLIN.getNomencalture())) {
        TextView textView = (TextView)view.findViewById(
                R.id.lin_nomenclature);
        textView.setText(mLIN.getNomencalture());
    }

    TextView subLinView = (TextView)view.findViewById(R.id.sub_lin);
    if(!TextUtils.isEmpty(mLIN.getSubLin())) {
      subLinView.setText("Sub LIN: " + mLIN.getSubLin());
      subLinView.setVisibility(View.VISIBLE);
    } else {
      subLinView.setVisibility(View.GONE);
    }

  }

  public void setLIN(LIN lin) {
    mLIN = lin;
  }

  public LIN getLIN() {
    return mLIN;
  }

}
