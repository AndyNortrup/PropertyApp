package com.NortrupDevelopment.PropertyApp.view;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.widget.LinearLayout;

import com.NortrupDevelopment.PropertyApp.R;
import com.NortrupDevelopment.PropertyApp.model.Item;
import com.NortrupDevelopment.PropertyApp.model.ItemLoader;
import com.NortrupDevelopment.PropertyApp.model.LIN;
import com.NortrupDevelopment.PropertyApp.model.LINLoader;
import com.NortrupDevelopment.PropertyApp.model.NSN;
import com.NortrupDevelopment.PropertyApp.model.NSNLoader;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardView;

/**
 * This activity presents the user with a listing of all NSN's and Serial
 * numbers for a given LIN which is passed as an extra from the calling
 * activity.
 * Created by andy on 2/16/14.
 */
public class LINDetailActivity extends Activity {

  public static final String LIN_ID_KEY = "LIN_ID";
  public static final String NSN_ID_KEY = "NSN_ID";
  private static final int LIN_QUERY = 0;

  private long mLINid;
  private SparseArray<LINCardWithNSNs> mLINCards;
  private SparseBooleanArray mItemQueryComplete;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_lin_detail);

    mLINid = getIntent().getExtras().getLong(LIN_ID_KEY);

    //Start our search for the LIN
    getLoaderManager().initLoader(LIN_QUERY,
        getIntent().getExtras(),
        new LINLoaderCallback());

  }

  private CardView createCardView(Card card) {
    //Create the card view
    CardView cardView = new CardView(this);
    cardView.setCard(card);

    //Set the margins
    LinearLayout.LayoutParams layoutParams =
        new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);


    //convert 12 dp to pixels
    int px12dp = (int) TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics());
    layoutParams.setMargins(px12dp, px12dp / 2, px12dp, px12dp / 2);

    cardView.setLayoutParams(layoutParams);
    return cardView;
  }

  private void setLINCard(LIN lin) {
    //Create our card model
    LINCardWithNSNs cardWithNSNs = new LINCardWithNSNs(this, lin);
    cardWithNSNs.updateProgressBar(false, false);
    mLINCards.put((int) lin.getLinId(), cardWithNSNs);


    //Get the Linear List View and add the card
    LinearLayout layout = (LinearLayout) findViewById(R.id.lin_detail_layout);
    layout.addView(createCardView(cardWithNSNs));

    //Start the search for the NSNs
    getLoaderManager().initLoader((int) lin.getLinId(),
        getIntent().getExtras(),
        new NSNLoaderCallback());
  }

  class LINLoaderCallback
      implements LoaderManager.LoaderCallbacks<ArrayList<LIN>> {
    @Override
    public Loader<ArrayList<LIN>> onCreateLoader(int id, Bundle args) {
      LINLoader loader = new LINLoader(getBaseContext());
      loader.setLinID(mLINid);
      loader.includeSubLINs(true);
      return loader;
    }

    @Override
    public void onLoadFinished(
        Loader<ArrayList<LIN>> loader,
        ArrayList<LIN> data) {


      //we got a result, display the card
      if (data.size() > 0) {
        mLINCards = new SparseArray<LINCardWithNSNs>();
        for (LIN lin : data) {
          setLINCard(lin);
        }
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
      implements LoaderManager.LoaderCallbacks<ArrayList<NSN>> {

    @Override
    public Loader<ArrayList<NSN>> onCreateLoader(int id, Bundle args) {
      NSNLoader loader = new NSNLoader(getBaseContext());
      loader.setLIN(id);
      return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<NSN>> loader, ArrayList<NSN> data) {
      if (data.size() > 0) {

        mItemQueryComplete = new SparseBooleanArray();

        //Add the NSN to mNSNs & Query for serial numbers under this NSN.
        for (NSN nsn : data) {

          //Add the NSN to the LIN
          mLINCards.get(loader.getId()).getLIN().addNSN(nsn);

          //Mark that we are waiting to find out if there are items for this
          //NSN.
          mItemQueryComplete.put((int) nsn.getNsnId(), false);

          Bundle args = new Bundle();
          args.putLong(NSN_ID_KEY, nsn.getNsnId());
          args.putLong(LIN_ID_KEY, loader.getId());

          getLoaderManager().initLoader((int) nsn.getNsnId(),
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
   */
  class ItemLoaderCallback
      implements LoaderManager.LoaderCallbacks<ArrayList<Item>> {

    @Override
    public Loader<ArrayList<Item>> onCreateLoader(int id, Bundle args) {
      ItemLoader loader = new ItemLoader(getBaseContext());
      loader.setNSN(args.getLong(NSN_ID_KEY));
      loader.setLIN(args.getLong(LIN_ID_KEY));
      return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Item>> loader, ArrayList<Item> data) {

      int linID = (int) ((ItemLoader) loader).getLIN();

      if (data.size() > 0) {
        //Find the NSN in the list of LINs.
        LIN lin = mLINCards.get(linID).getLIN();
        NSN nsn = lin.getNSNById(loader.getId());

        //Add the item to the NSN
        for (Item item : data) {
          nsn.addItem(item);
        }
      }

      mItemQueryComplete.put(loader.getId(), true);

      //True if all of the item look-ups have completed.
      if (mItemQueryComplete.indexOfValue(false) < 0) {

        for (int x = 0; x < mLINCards.size(); x++) {
          LINCardWithNSNs card = mLINCards.valueAt(x);

          card.setNSN();
          card.updateProgressBar(true, true);
        }
      }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Item>> loader) {
      //Nothing needed in this instance
    }
  }

}
