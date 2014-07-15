package com.NortrupDevelopment.PropertyApp.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.NortrupDevelopment.PropertyApp.R;
import com.NortrupDevelopment.PropertyApp.adapters.ItemArrayAdapter;
import com.NortrupDevelopment.PropertyApp.model.Item;
import com.NortrupDevelopment.PropertyApp.model.LIN;
import com.NortrupDevelopment.PropertyApp.model.NSN;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.prototypes.CardWithList;
import it.gmariotti.cardslib.library.prototypes.LinearListView;

/**
 * Card used to display a LIN with NSN details.
 * Created by andy on 6/7/14.
 */
public class LINCardWithNSNs extends CardWithList
{

  private LIN mLIN;
  private ArrayList<NSN> mNSNs;

  public LINCardWithNSNs(Context context, LIN lin) {
    super(context);
    mLIN = lin;
    mNSNs = new ArrayList<NSN>();
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

  /**
   * Convert our List of NSNs into a list of ListObjects
   * @return List of ListObjects with IDs corresponding to the NSN Id.
   */
  @Override
  protected List<ListObject> initChildren() {

    ArrayList<ListObject> nsnObjects =
        new ArrayList<ListObject>();

    if(mNSNs != null) {
      for (NSN nsn : mNSNs) {
        NSNListObject object = new NSNListObject(this, nsn);
        nsnObjects.add(object);

        object.setOnItemClickListener(new OnItemClickListener() {
          @Override
          public void onItemClick(LinearListView linearListView,
                                  View view,
                                  int i,
                                  ListObject listObject) {
            //Get our listView
            NonScrollingListView listView = (NonScrollingListView) view.findViewById(R.id.serial_number_list);


            //If the listView is Visible then we want to hide it
            if (listView.getVisibility() == View.VISIBLE) {
              listView.setVisibility(View.GONE);
            } else {

              //Find out if we need to create a new adapter for the grid
              if(listView.getAdapter() == null || listView.getCount() == 0) {

                //Get our list of Items
                ArrayList<Item> items =
                    ((NSNListObject) listObject).getNSN().getItemList();

                //Make sure that the list isn't empty.
                if (items != null && items.size() > 0) {
                  ItemArrayAdapter itemArrayAdapter =
                      new ItemArrayAdapter(getContext(),
                          R.layout.item_list_item,
                          items);

                  listView.setAdapter(itemArrayAdapter);
                }
              }
              listView.setVisibility(View.VISIBLE);
            }

          }
        });
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
    NSN nsn = mNSNs.get(i);

    //Set the NSN
    if(!TextUtils.isEmpty(nsn.getFormatedNSN())) {
      ((TextView)view.findViewById(R.id.nsn_list_item_nsn_value))
          .setText(nsn.getFormatedNSN());
    }

    //Set the nomenclature
    if(!TextUtils.isEmpty(nsn.getNomencalture())) {
      ((TextView)view.findViewById(R.id.nsn_list_item_nomenclature_value))
          .setText(nsn.getNomencalture());
    }

    //Set the Unit of Issue
    if(!TextUtils.isEmpty(nsn.getUi())) {
      ((TextView)view.findViewById(R.id.nsn_list_item_ui_value))
          .setText(nsn.getUi());
    }


    //Set the on hand quantity
    ((TextView)view.findViewById(R.id.nsn_list_item_on_hand_value))
        .setText(Integer.toString(nsn.getOnHand()));

    //Set the Unit Price
    ((TextView)view.findViewById(R.id.nsn_list_item_unit_price_value)).setText(
        NumberFormat.getCurrencyInstance().format(nsn.getUnitPrice()));

    return view;

  }

  @Override
  public int getChildLayoutId() {
    return R.layout.nsn_card;
  }

  public void setNSN(ArrayList<NSN> NSNs) {

    mNSNs = NSNs;
    getLinearListAdapter().addAll(initChildren());
  }


  /**
   * Created by andy on 6/8/14.
   */
  public class NSNListObject extends CardWithList.DefaultListObject {

    private NSN mNSN;

    public NSNListObject(Card parent, NSN nsn) {
      super(parent);
      mNSN = nsn;

    }

    @Override
    public String getObjectId() {
      return String.valueOf(mNSN.getNsnId());
    }

    public NSN getNSN() {
      return mNSN;
    }

  }
}
