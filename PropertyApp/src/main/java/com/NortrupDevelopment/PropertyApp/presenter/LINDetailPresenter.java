package com.NortrupDevelopment.PropertyApp.presenter;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.util.SparseArray;

import com.NortrupDevelopment.PropertyApp.model.Item;
import com.NortrupDevelopment.PropertyApp.model.ItemLoader;
import com.NortrupDevelopment.PropertyApp.model.LIN;
import com.NortrupDevelopment.PropertyApp.model.LINLoader;
import com.NortrupDevelopment.PropertyApp.model.NSN;
import com.NortrupDevelopment.PropertyApp.model.NSNLoader;

import java.util.ArrayList;

/**
 * LINDetailPresenter handles the logic for the lookup and display of the
 * details of LINs for display.
 * Created by andy on 7/17/14.
 */
public class LINDetailPresenter {

  private static final String LIN_ID_KEY = "LIN_ID";
  private static final String NSN_ID_KEY = "NSN_ID";

  private static final int LIN_QUERY = 0;

  LINDetail mDetailView;
  long mLINID;
  SparseArray<LIN> mLINs;

  public LINDetailPresenter(LINDetail detailView) {
    mDetailView = detailView;
  }

  public void linSearchRequested(long linID) {
    mLINID = linID;

    //Start our search for the LIN
    mDetailView.getViewLoaderManager().initLoader(LIN_QUERY,
        null,
        new LINLoaderCallback());
  }

  /**
   * Called by the View in order to request that data be re-loaded into the view
   */
  public void restoreViewRequested() {
    for(int x = 0; x<mLINs.size(); x++) {
      mDetailView.addLIN(mLINs.valueAt(x));
      SparseArray<NSN> stockNumbers = mLINs.valueAt(x).getNSNs();
      for(int y = 0; y < stockNumbers.size(); y++) {
        mDetailView.addNSNtoLIN(stockNumbers.valueAt(y), mLINs.valueAt(x));
      }
    }
  }

  //region LIN Loader
  class LINLoaderCallback
      implements LoaderManager.LoaderCallbacks<ArrayList<LIN>> {
    @Override
    public Loader<ArrayList<LIN>> onCreateLoader(int id, Bundle args) {
      LINLoader loader = new LINLoader(mDetailView.getContext());
      loader.setLinID(mLINID);
      loader.includeSubLINs(true);
      return loader;
    }

    @Override
    public void onLoadFinished(
        Loader<ArrayList<LIN>> loader,
        ArrayList<LIN> data) {

      //we got a result, display the card
      if (data.size() > 0) {
        if(mLINs == null) {
          mLINs = new SparseArray<LIN>();
        }
        for (LIN lin : data) {
          if(mLINs.indexOfKey(lin.getLinId()) < 0) {
            mDetailView.addLIN(lin);
            mLINs.put(lin.getLinId(), lin);

            //Start the search for the NSNs
            mDetailView.getViewLoaderManager().initLoader(lin.getLinId(),
                null,
                new NSNLoaderCallback());
          }
        }
      }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<LIN>> loader) {
      //Nothing to do...no cursor;
    }
  }
  //endregion

  /**
   * Loads NSNs that match the LIN
   */
  class NSNLoaderCallback
      implements LoaderManager.LoaderCallbacks<ArrayList<NSN>> {

    @Override
    public Loader<ArrayList<NSN>> onCreateLoader(int id, Bundle args) {
      NSNLoader loader = new NSNLoader(mDetailView.getContext());
      loader.setLIN(id);
      return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<NSN>> loader, ArrayList<NSN> data) {
      if (data.size() > 0) {
        //Add the NSN to mNSNs & Query for serial numbers under this NSN.
        for (NSN nsn : data) {

          //Add the NSN to the LIN
          mLINs.get(loader.getId()).addNSN(nsn);

          Bundle args = new Bundle();
          args.putLong(NSN_ID_KEY, nsn.getNsnId());
          args.putLong(LIN_ID_KEY, loader.getId());

          mDetailView.getViewLoaderManager().initLoader((int) nsn.getNsnId(),
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
      ItemLoader loader = new ItemLoader(mDetailView.getContext());
      loader.setNSN(args.getLong(NSN_ID_KEY));
      loader.setLIN(args.getLong(LIN_ID_KEY));
      return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Item>> loader, ArrayList<Item> data) {

      LIN lin = mLINs.get((int)((ItemLoader)loader).getLIN());
      NSN nsn = lin.getNSNById(loader.getId());

      if (data.size() > 0) {
        //Find the NSN in the list of LINs.


        //Add the item to the NSN
        for (Item item : data) {
          if(!nsn.containsItem(item)) {
            nsn.addItem(item);
          }
        }

      }

      //add the NSN even if there are no serial numbers.
      mDetailView.addNSNtoLIN(nsn, lin);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Item>> loader) {
      //Nothing needed in this instance
    }
  }
}
