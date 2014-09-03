package com.NortrupDevelopment.PropertyBook.presenter;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.util.SparseArray;

import com.NortrupDevelopment.PropertyBook.loaders.ItemLoader;
import com.NortrupDevelopment.PropertyBook.loaders.LINLoader;
import com.NortrupDevelopment.PropertyBook.loaders.NSNLoader;
import com.NortrupDevelopment.PropertyBook.loaders.PropertyBookLoader;
import com.NortrupDevelopment.PropertyBook.model.Item;
import com.NortrupDevelopment.PropertyBook.model.LIN;
import com.NortrupDevelopment.PropertyBook.model.NSN;
import com.NortrupDevelopment.PropertyBook.model.PropertyBook;
import com.NortrupDevelopment.PropertyBook.view.LINDetail;

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
  int mLINID;
  SparseArray<LIN> mLINs;

  public LINDetailPresenter(LINDetail detailView) {
    mDetailView = detailView;
  }

  public void linSearchRequested(int linID) {
    mLINID = linID;

    //Start our search for the LIN
    mDetailView.getViewLoaderManager().initLoader(LIN_QUERY,
        null,
        new LINLoaderCallback());
  }

  class PropertyBookLoaderCallback
      implements LoaderManager.LoaderCallbacks<ArrayList<PropertyBook>> {

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<ArrayList<PropertyBook>> onCreateLoader(int id, Bundle args) {
      PropertyBookLoader loader =
          new PropertyBookLoader(mDetailView.getContext());
      loader.setPropertyBookID(id);
      return loader;
    }

    /**
     * Called when a previously created loader has finished its load.  Note
     * that normally an application is <em>not</em> allowed to commit fragment
     * transactions while in this call, since it can happen after an
     * activity's state is saved.  See FragmentManager#beginTransaction()
     * FragmentManager.openTransaction()} for further discussion on this.
     * <p/>
     * <p>This function is guaranteed to be called prior to the release of
     * the last data that was supplied for this Loader.  At this point
     * you should remove all use of the old data (since it will be released
     * soon), but should not do your own release of the data since its Loader
     * owns it and will take care of that.  The Loader will take care of
     * management of its data so you don't have to.  In particular:
     * <p/>
     * <ul>
     * <li> <p>The Loader will monitor for changes to the data, and report
     * them to you through new calls here.  You should not monitor the
     * data yourself.  For example, if the data is a {@link android.database.Cursor}
     * and you place it in a {@link android.widget.CursorAdapter}, use
     * the {@link android.widget.CursorAdapter#CursorAdapter(android.content.Context,
     * android.database.Cursor, int)} constructor <em>without</em> passing
     * in either {@link android.widget.CursorAdapter#FLAG_AUTO_REQUERY}
     * or {@link android.widget.CursorAdapter#FLAG_REGISTER_CONTENT_OBSERVER}
     * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
     * from doing its own observing of the Cursor, which is not needed since
     * when a change happens you will get a new Cursor throw another call
     * here.
     * <li> The Loader will release the data once it knows the application
     * is no longer using it.  For example, if the data is
     * a {@link android.database.Cursor} from a {@link android.content.CursorLoader},
     * you should not call close() on it yourself.  If the Cursor is being placed in a
     * {@link android.widget.CursorAdapter}, you should use the
     * {@link android.widget.CursorAdapter#swapCursor(android.database.Cursor)}
     * method so that the old Cursor is not closed.
     * </ul>
     *
     * @param loader The Loader that has finished.
     * @param data   The data generated by the Loader.
     */
    @Override
    public void onLoadFinished(Loader<ArrayList<PropertyBook>> loader,
                               ArrayList<PropertyBook> data) {
      LIN requiredLIN = null;
      for(int x = 0; x<mLINs.size(); x++) {
        LIN lin = mLINs.get(mLINs.keyAt(x));
        if (lin.getPropertyBookID() == loader.getId()) {
          requiredLIN = lin;
          if(data.size() == 1) {
            requiredLIN.setPropertyBook(data.get(0));
          }
        }
      }

      if(requiredLIN == null) {
        return;
      }

      //Add the LIN to the view now that we have the PB information.
      mDetailView.addLIN(requiredLIN);

      //Start the search for the NSNs
      mDetailView.getViewLoaderManager().initLoader(
          requiredLIN.getLinId(),
          null,
          new NSNLoaderCallback());
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    @Override
    public void onLoaderReset(Loader<ArrayList<PropertyBook>> loader) {

    }
  }

  //region LIN Loader
  class LINLoaderCallback
      implements LoaderManager.LoaderCallbacks<ArrayList<LIN>>
  {
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

            mLINs.put(lin.getLinId(), lin);

            //Start the search for the Property Book Information
            mDetailView.getViewLoaderManager().initLoader(
                lin.getPropertyBookID(),
                null,
                new PropertyBookLoaderCallback());
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
          args.putInt(NSN_ID_KEY, nsn.getNsnId());
          args.putInt(LIN_ID_KEY, loader.getId());

          mDetailView.getViewLoaderManager().initLoader(nsn.getNsnId(),
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
      loader.setNSN(args.getInt(NSN_ID_KEY));
      loader.setLIN(args.getInt(LIN_ID_KEY));
      return loader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Item>> loader, ArrayList<Item> data) {

      //Find the NSN in the list of LINs.
      LIN lin = mLINs.get(((ItemLoader)loader).getLIN());
      NSN nsn = lin.getNSNById(loader.getId());

      if (data.size() > 0) {

        //Add the item to the NSN
        for (Item item : data) {
          if(!nsn.containsItem(item)) {
            nsn.addItem(item);
          }
        }
      }

      mDetailView.addNSNtoLIN(nsn, lin);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Item>> loader) {
      //Nothing needed in this instance
    }
  }
}
