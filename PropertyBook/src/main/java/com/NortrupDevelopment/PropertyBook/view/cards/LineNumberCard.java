package com.NortrupDevelopment.PropertyBook.view.cards;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.model.LineNumber;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Custom Card used to provide additional details about a LIN
 * Created by andy on 2/15/14.
 */
public class LineNumberCard extends Card {

  private LineNumber mLIN;

  public LineNumberCard(Context context) {
      super(context, R.layout.list_item_lin_browser);
  }

  @Override
  public void setupInnerViewElements(ViewGroup parent, View view) {

    if(!TextUtils.isEmpty(mLIN.getLin())) {
      TextView linTextView = (TextView)view.findViewById(R.id.lin);
      linTextView.setText(mLIN.getLin());
    }

    if(!TextUtils.isEmpty(mLIN.getNomenclature())) {
        TextView textView = (TextView)view.findViewById(
                R.id.lin_nomenclature);
        textView.setText(mLIN.getNomenclature());
    }

    TextView subLinView = (TextView)view.findViewById(R.id.sub_lin);
    if(!TextUtils.isEmpty(mLIN.getSubLin())) {
      subLinView.setText("Sub LIN: " + mLIN.getSubLin());
      subLinView.setVisibility(View.VISIBLE);
    } else {
      subLinView.setVisibility(View.GONE);
    }

  }

  public void setLIN(LineNumber lineNumber) {
    mLIN = lineNumber;
  }

  public LineNumber getLIN() {
    return mLIN;
  }

}
