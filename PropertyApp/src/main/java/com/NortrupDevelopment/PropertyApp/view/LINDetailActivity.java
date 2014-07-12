package com.NortrupDevelopment.PropertyApp.view;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.util.SparseArray;
import android.util.SparseBooleanArray;

import com.NortrupDevelopment.PropertyApp.R;
import com.NortrupDevelopment.PropertyApp.model.Item;
import com.NortrupDevelopment.PropertyApp.model.ItemLoader;
import com.NortrupDevelopment.PropertyApp.model.LIN;
import com.NortrupDevelopment.PropertyApp.model.LINLoader;
import com.NortrupDevelopment.PropertyApp.model.NSN;
import com.NortrupDevelopment.PropertyApp.model.NSNLoader;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.view.CardView;

/**
 * This activity presents the user with a listing of all NSN's and Serial
 * numbers for a given LIN which is passed as an extra from the calling
 * activity.
 * Created by andy on 2/16/14.
 */
public class LINDetailActivity extends Activity
{

  public static final String LIN_ID_KEY = "LIN_ID";
  public static final String NSN_ID_KEY = "NSN_ID";
  private static final int LIN_QUERY = 0;
  private static final int NSN_QUERY = 1;

  private long mLINid;
  private LINCardWithNSNs cardWithNSNs;
  private SparseArray<NSN> mNSNs;
  private SparseBooleanArray mItemQueryComplete;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_lin_detail);

    mLINid = Long.parseLong(getIntent().getExtras().getString(LIN_ID_KEY));

    //Start our search for the LIN
    getLoaderManager().initLoader(LIN_QUERY,
        getIntent().getExtras(),
        new LINLoaderCallback());
  }

  private void setLINCard(LIN lin) {
    cardWithNSNs = new LINCardWithNSNs(getBaseContext(), lin);
    //cardWithNSNs.initCard();

    CardView cardView = (CardView)findViewById(R.id.lin_card);
    cardView.setCard(cardWithNSNs);

    cardWithNSNs.updateProgressBar(false, false);

    //Start the search for the NSNs
    getLoaderManager().initLoader(NSN_QUERY,
        getIntent().getExtras(),
        new NSNLoaderCallback());
  }

  class LINLoaderCallback
      implements LoaderManager.LoaderCallbacks<ArrayList<LIN>>
  {
    @Override
    public Loader<ArrayList<LIN>> onCreateLoader(int id, Bundle args) {
      return new LINLoader(mLINid, getBaseContext());
    }

    @Override
    public void onLoadFinished(
        Loader<ArrayList<LIN>> loader,
        ArrayList<LIN> data) {

      //we got a result, display the card
      if(data.size() > 0) {
        setLINCard(data.get(0));
      }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<LIN>> loader) {
      //Nothing to do...no cursor;
    }
  }

  /**
   * Loads NSNs that match the LIN
   */
  class NSNLoaderCallback
   implements LoaderManager.LoaderCallbacks<ArrayList<NSN>>
  {

    @Override
    public Loader<ArrayList<NSN>> onCreateLoader(int id, Bundle args) {
      NSNLoader loader = new NSNLoader(getBaseContext());
      loader.setLIN(mLINid);
      return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<NSN>> loader, ArrayList<NSN> data) {
      if(data.size() > 0) {

        mNSNs = new SparseArray<NSN>();
        mItemQueryComplete = new SparseBooleanArray();

        //Add the NSN to mNSNs & Query for serial numbers under this NSN.
        for(NSN nsn : data) {
          mNSNs.put((int)nsn.getNsnId(), nsn);
          mItemQueryComplete.put((int)nsn.getNsnId(), false);

          Bundle args = new Bundle();
          args.putLong(NSN_ID_KEY, nsn.getNsnId());

          getLoaderManager().initLoader((int)nsn.getNsnId(),
              args,
              new ItemLoaderCallback());
        }
      }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<NSN>> loader) {
      //Nothing needed in this instance
    }
  }

  /**
   * Loader callbacks to handle loading serial numbers from the database.
   *
   */
  class ItemLoaderCallback
    implements LoaderManager.LoaderCallbacks<ArrayList<Item>> {

    @Override
    public Loader<ArrayList<Item>> onCreateLoader(int id, Bundle args) {
      ItemLoader loader = new ItemLoader(getBaseContext());
      loader.setNSNID(args.getLong(NSN_ID_KEY));
      return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Item>> loader, ArrayList<Item> data) {
      if(data.size() > 0) {
        //Add the item to the NSN

        NSN nsn = mNSNs.get(loader.getId());
        for(Item item : data) {
          nsn.addItem(item);
        }
      }

      mItemQueryComplete.put(loader.getId(), true);

      //Check if we have all of the items back yet.
      if(mItemQueryComplete.indexOfValue(false) < 0) {

        //Convert our SparseArrayList to an ArrayList and put it in the card.
        ArrayList<NSN> arrayList = new ArrayList<NSN>();
        for(int x = 0; x<mNSNs.size(); x++) {
          arrayList.add(mNSNs.valueAt(x));
        }
        cardWithNSNs.setNSN(arrayList);

        //Update the loading graphic.
        cardWithNSNs.updateProgressBar(true, true);
      }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Item>> loader) {
      //Nothing needed in this instance
    }
  }

}
