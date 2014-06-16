package com.NortrupDevelopment.PropertyApp.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.NortrupDevelopment.PropertyApp.R;
import com.NortrupDevelopment.PropertyApp.model.NSN;

import java.text.NumberFormat;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * NSN Card is used to display NSN information.
 * Created by andy on 5/10/14.
 */
public class NSNCard extends Card {

  private NSN mNSN;

  public NSNCard(Context context) {
    super(context, R.layout.nsn_card);
  }

  /**
   * Set the internals of the view correctly.
   * @param parent
   * @param view
   */
  @Override
  public void setupInnerViewElements(ViewGroup parent, View view) {


    //Set the NSN
    if(!TextUtils.isEmpty(mNSN.getFormatedNSN())) {
      ((TextView)view.findViewById(R.id.nsn_list_item_nsn_value))
          .setText(mNSN.getFormatedNSN());
    }

    //Set the nomenclature
    if(!TextUtils.isEmpty(mNSN.getNomencalture())) {
      ((TextView)view.findViewById(R.id.nsn_list_item_nomenclature_value))
          .setText(mNSN.getNomencalture());
    }

    //Set the Unit of Issue
    if(!TextUtils.isEmpty(mNSN.getUi())) {
      ((TextView)view.findViewById(R.id.nsn_list_item_ui_value))
          .setText(mNSN.getUi());
    }


    //Set the on hand quantity
    ((TextView)view.findViewById(R.id.nsn_list_item_on_hand_value))
      .setText(Integer.toString(mNSN.getOnHand()));

    //Set the Unit Price
    ((TextView)view.findViewById(R.id.nsn_list_item_unit_price_value)).setText(
        NumberFormat.getCurrencyInstance().format(mNSN.getUnitPrice()));

  }

  public void setNSN(NSN nsn) {
    mNSN = nsn;
  }
}
