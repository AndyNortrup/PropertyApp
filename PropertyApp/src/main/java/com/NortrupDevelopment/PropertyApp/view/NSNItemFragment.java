package com.NortrupDevelopment.PropertyApp.view;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.NortrupDevelopment.PropertyApp.R;
import com.NortrupDevelopment.PropertyApp.model.NSN;
import com.NortrupDevelopment.PropertyApp.model.PropertyBookContentProvider;
import com.NortrupDevelopment.PropertyApp.model.TableContractItem;
import com.NortrupDevelopment.PropertyApp.model.TableContractNSN;

import it.gmariotti.cardslib.library.view.CardGridView;
import it.gmariotti.cardslib.library.view.CardView;

/**
 * This Fragment displays a Card with the NSN details then shows a
 * CardGridView of all of the serial numbers for that NSN.
 * Created by andy on 5/25/14.
 */
public class NSNItemFragment extends Fragment
  implements LoaderManager.LoaderCallbacks<Cursor>
{

  public static final String NSN_KEY = "NSN";
  private static final int NSN_LOADER = 1;
  private static final int ITEM_LOADER = 2;

  private ItemCardGridCursorAdapter mCardCursorAdapter;

  //Store the NSN for this fragment.
  private NSN mNSN;

  //<editor-fold desc="NSN Database Variables">
  private static final String[] NSN_PROJECTION = {
      TableContractNSN.columnNSN,
      TableContractNSN.columnNomenclature,
      TableContractNSN.columnUI,
      TableContractNSN.columnOnHand,
      TableContractNSN.columnUnitPrice,
      TableContractNSN.columnLLC,
      TableContractNSN.columnECS,
      TableContractNSN.columnDLA,
      TableContractNSN.columnSRRC,
      TableContractNSN.columnUIIManaged,
      TableContractNSN.columnCIIC,
      TableContractNSN.columnPubData,
      TableContractNSN.linID,
      TableContractNSN._ID
  };

  private static final String NSN_SELECTION =
      TableContractNSN._ID + " = ?";

  private static final String NSN_SORT_ORDER =
      TableContractNSN.columnNomenclature + " ASC";
  //</editor-fold>


  //<editor-fold desc="Item Database Variables">
  private static final String[] ITEM_PROJECTION = {
      TableContractItem.columnSerialNumber,
      TableContractItem.columnSysNo,
      TableContractItem._ID
  };

  private static final String ITEM_SELECTION =
      TableContractItem.columnNsnId + " = ?";

  private static final String ITEM_SORT_ORDER =
      TableContractItem.columnSerialNumber + " ASC";
  //</editor-fold>


  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle SavedInstanceState)
  {
    View result = inflater.inflate(R.layout.fragment_nsn_item, container, false);

    getLoaderManager().initLoader(NSN_LOADER, getArguments(), this);
    setRetainInstance(true);

    return result;
  }

  //<editor-fold desc="Loader Callbacks">

  /**
   * Loads data on either NSNs or Items from the content provider
   * @param id Either NSN_LOADER or ITEM_LOADER ids.
   * @param args Only one argument is passed stored in the LIN_ID_KEY.
   *             Contains the ID of the parent item LIN _id for the NSN
   *             or NSN _id for Items that is used as a selection argument
   *             for the query.
   * @return A cursor loader for NSN or Item data.
   */
  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {

    CursorLoader loader;

    String[] selectionArgs =
        new String[]{((Long) args.getLong(NSN_KEY)).toString()};

    if (id == NSN_LOADER) {
      loader = new CursorLoader(getActivity(),
          PropertyBookContentProvider.CONTENT_URI_NSN,
          NSN_PROJECTION,
          NSN_SELECTION,
          selectionArgs,
          NSN_SORT_ORDER);
    } else {
      loader = new CursorLoader(getActivity(),
          PropertyBookContentProvider.CONTENT_URI_ITEM,
          ITEM_PROJECTION,
          ITEM_SELECTION,
          selectionArgs,
          ITEM_SORT_ORDER);
    }

    return loader;
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    if(loader.getId() == NSN_LOADER) {

      //If we have returned an NSN, start looking for items and fill data
      // into the NSN Card
      if(data.getCount() > 0) {
        data.moveToFirst();
        getItemsForNSN(
            data.getLong(data.getColumnIndex(TableContractNSN._ID)));
        loadNSN(data);
      } else {
        //No NSNs found for this LIN
        noNSNFound();
      }

    } else if (loader.getId() == ITEM_LOADER) {
      loadItems(data);
    }
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {

    if(loader.getId() == ITEM_LOADER) {
      disconnectItemAdapter();
    }
  }
  //</editor-fold>

  //<editor-fold desc="NSN Data Operations">
  /**
   * Takes a cursor form the Loader and creates the NSN card at the top of the
   * fragment
   * @param data Cursor with the NSN data from the loader;
   */
  private void loadNSN(Cursor data) {
    if(data.getCount() > 0) {

      CardView cardView = (CardView) getView().findViewById(R.id.nsn_card);

      mNSN = NSN.NSNFromCursor(data);

      NSNCard nsnCard = new NSNCard(getActivity());
      nsnCard.setNSN(mNSN);

      cardView.setCard(nsnCard);
    } else {
      noNSNFound();
    }
  }

  /**
   * This method hides the NSN card and the Item CardGrid and shows a No NSNs
   * found Message.
   */
  private void noNSNFound() {
    CardView cardView = (CardView)getView().findViewById(R.id.nsn_card);
    cardView.setVisibility(View.GONE);

    CardGridView gridView = (CardGridView)getView().findViewById(R.id.item_grid);
    gridView.setVisibility(View.GONE);

    TextView empty = (TextView)getView().findViewById(android.R.id.empty);
    empty.setVisibility(View.VISIBLE);

  }
  //</editor-fold>

  //<editor-fold desc="Item Data Operations">
  /**
   * Starts a CursorLoader Search for items belonging to the NSN represented by
   * the given nsnId
   * @param nsnId _id of the NSN to which items belong.
   */
  private void getItemsForNSN(long nsnId) {
    Bundle args = new Bundle();
    args.putLong(NSN_KEY, nsnId);

    getLoaderManager().initLoader(ITEM_LOADER, args, this);
  }

  /**
   *
   * Creates an ItemCardGridCursorAdapter and connects it to the CardGridView
   * @param data Cursor with item data.
   */
  private void loadItems(Cursor data) {
    if(data.getCount() > 0) {
      mCardCursorAdapter = new ItemCardGridCursorAdapter(getActivity());
      mCardCursorAdapter.changeCursor(data);

      CardGridView gridView =
          (CardGridView) getView().findViewById(R.id.item_grid);

      if (gridView != null) {
        gridView.setAdapter(mCardCursorAdapter);
      }
    } else {
      noItemsFound();
    }
  }

  /**
   * Used to hide the CardGridView in the event that there are no serial numbers
   * to display for this NSN.
   */
  private void noItemsFound() {
    CardGridView gridView = (CardGridView)getView().findViewById(R.id.item_grid);
    gridView.setVisibility(View.GONE);

    TextView empty = (TextView)getView().findViewById(android.R.id.empty);
    empty.setText(R.string.no_serial_numbers);
    empty.setVisibility(View.VISIBLE);

  }

  /**
   * Disconects the CardGridView in the event that the loader is reset.
   */
  private void disconnectItemAdapter() {
    if(mCardCursorAdapter != null) {
      mCardCursorAdapter.changeCursor(null);
    }
  }
  //</editor-fold>
}
