package com.NortrupDevelopment.PropertyBook.view.cards;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.model.LineNumber;
import com.NortrupDevelopment.PropertyBook.model.SerialNumber;
import com.NortrupDevelopment.PropertyBook.model.StockNumber;
import com.NortrupDevelopment.PropertyBook.util.NSNFormatter;
import com.afollestad.materialdialogs.MaterialDialog;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.prototypes.CardWithList;
import it.gmariotti.cardslib.library.prototypes.LinearListView;

/**
 * Card used to display a LIN with NSN details.
 * Created by andy on 6/7/14.
 */
public class LINCardWithNSNs extends CardWithList
  implements CardWithList.OnItemClickListener
{

  private LineNumber mLIN;

  public LINCardWithNSNs(Context context, LineNumber lineNumber) {
    super(context);
    mLIN = lineNumber;
    init();
  }

  @Override
  protected CardHeader initCardHeader() {
    return new LINDetailHeader(getContext(), mLIN);
  }

  @Override
  protected void initCard() {
    setSwipeable(false);
    setUseProgressBar(true);
    setUseEmptyView(true);
  }

  public void setLIN(LineNumber lineNumber) {
    mLIN = lineNumber;
  }

  /**
   * Convert our List of NSNs into a list of ListObjects
   * @return List of ListObjects with IDs corresponding to the NSN Id.
   */
  @Override
  protected List<ListObject> initChildren() {

    ArrayList<ListObject> nsnObjects =
        new ArrayList<ListObject>();

    if(mLIN.getStockNumbers().size() > 0) {
      for (int x=0; x<mLIN.getStockNumbers().size(); x++) {
        StockNumber stockNumber = mLIN.getStockNumbers().get(x);

        NSNListObject object = new NSNListObject(this, stockNumber);
        nsnObjects.add(object);

        object.setOnItemClickListener(this);
      }
    }

    return nsnObjects;
  }

  @Override
  public View setupChildView(int i,
                             ListObject listObject,
                             View view,
                             ViewGroup viewGroup)
  {
    StockNumber stockNumber = ((NSNListObject)listObject).getNSN();

    //Set the NSN
    if(!TextUtils.isEmpty(stockNumber.getNsn())) {
      ((TextView)view.findViewById(R.id.nsn_value))
          .setText(NSNFormatter.getFormattedNSN(stockNumber.getNsn()));
    }

    //Set the nomenclature
    if(!TextUtils.isEmpty(stockNumber.getNomenclature())) {
      ((TextView)view.findViewById(R.id.nomenclature_value))
          .setText(stockNumber.getNomenclature());
    }

    //Set the Unit of Issue
    if(!TextUtils.isEmpty(stockNumber.getUi())) {
      ((TextView)view.findViewById(R.id.unit_of_issue_value))
          .setText(stockNumber.getUi());
    }


    //Set the on hand quantity
    ((TextView)view.findViewById(R.id.on_hand_value))
        .setText(Integer.toString(stockNumber.getOnHand()));

    //Set the Unit Price
    ((TextView)view.findViewById(R.id.unit_price_value)).setText(
        NumberFormat.getCurrencyInstance()
            .format(stockNumber.getUnitPrice()/100));

    return view;

  }

  @Override
  public int getChildLayoutId() {
    return R.layout.nsn_card;
  }

  public void setNSN() {
    getLinearListAdapter().clear();
    getLinearListAdapter().addAll(initChildren());
    getLinearListAdapter().notifyDataSetChanged();
  }

  public LineNumber getLIN() {
    return mLIN;
  }

  @Override
  public void onItemClick(LinearListView linearListView,
                          View view,
                          int i,
                          ListObject listObject) {
    //Get our list of Items
    RealmList<SerialNumber> items =
        mLIN.getStockNumbers().get(i).getSerialNumbers();

    String[] serialNumbers = new String[items.size()];
    for(int x=0; x<items.size(); x++) {
      serialNumbers[x] = items.get(x).getSerialNumber();
    }

    if(items.size() > 0) {
      MaterialDialog.Builder builder =
          new MaterialDialog.Builder(getCardView().getContext());

      builder.title(mLIN.getStockNumbers().get(i).getNomenclature())
          .items(serialNumbers)
          .titleColorRes(R.color.primary_color)
          .show();
    } else {
      Toast.makeText(getContext(),
          R.string.no_serial_numbers,
          Toast.LENGTH_SHORT).show();
    }
  }


  /**
   * Created by andy on 6/8/14.
   */
  public class NSNListObject extends CardWithList.DefaultListObject {

    private StockNumber mNSN;

    public NSNListObject(Card parent, StockNumber stockNumber) {
      super(parent);
      mNSN = stockNumber;

    }

    @Override
    public String getObjectId() {
      return String.valueOf(mNSN.getNsn());
    }

    public StockNumber getNSN() {
      return mNSN;
    }

  }
}
