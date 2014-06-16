package com.NortrupDevelopment.PropertyApp.view;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;

import com.NortrupDevelopment.PropertyApp.R;
import com.NortrupDevelopment.PropertyApp.model.LIN;
import com.NortrupDevelopment.PropertyApp.model.LINLoader;
import com.NortrupDevelopment.PropertyApp.model.NSN;

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
  private static final int LIN_QUERY = 0;
  private static final int NSN_QUERY = 1;

  private LIN mLIN;
  private long mLINid;
  private LINCardWithNSNs cardWithNSNs;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_lin_detail);

    mLINid = Long.parseLong(getIntent().getExtras().getString(LIN_ID_KEY));

    //Start our search for the LIN
    getLoaderManager().initLoader(LIN_QUERY,
        this.getIntent().getExtras(),
        new LINLoaderCallback());
  }

  private void setLINCard(LIN lin) {
    mLIN = lin;

    cardWithNSNs = new LINCardWithNSNs(getBaseContext(), lin);
    cardWithNSNs.updateProgressBar(false, false);
    cardWithNSNs.initCard();

    CardView cardView = (CardView)findViewById(R.id.lin_card);
    cardView.setCard(cardWithNSNs);
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
       //Start the search for the NSNs
        getLoaderManager().initLoader(NSN_QUERY,
            getIntent().getExtras(),
            new NSNLoaderCallback());
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
        cardWithNSNs.setNSN(data);
        cardWithNSNs.updateProgressBar(true, true);
      } else {

      }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<NSN>> loader) {
      //Nothing needed in this instance
    }
  }

}
