package com.NortrupDevelopment.PropertyApp.view;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.NortrupDevelopment.PropertyApp.R;
import com.NortrupDevelopment.PropertyApp.model.PropertyBookContentProvider;
import com.NortrupDevelopment.PropertyApp.model.TableContractLIN;

import it.gmariotti.cardslib.library.view.CardListView;

public class LINBrowserActivity extends ListActivity
    implements LoaderManager.LoaderCallbacks<Cursor>,
    View.OnClickListener
{
	
  private static final int LOADER_ID_LINS = 0;

  private LinCardCursorAdapter mLinCardCursorAdapter;
  //private LinCursorAdapter mCursorAdapter;


  private static final String[] LIN_PROJECTION =
  {
      TableContractLIN._ID,
      TableContractLIN.columnLIN,
      TableContractLIN.columnNomenclature,
      TableContractLIN.columnSubLIN,
      TableContractLIN.columnRequired,
      TableContractLIN.columnAuthorized,
      TableContractLIN.columnAuthDoc,
      TableContractLIN.columnSRI,
      TableContractLIN.columnERC
  };

  private static final String[] LIN_DATABASE_FIELDS =
      {
          TableContractLIN.columnLIN,
          TableContractLIN.columnNomenclature
      };

  private static final int[] LIN_DISPLAY_FIELDS =
      {
        R.id.lin,
        R.id.lin_nomenclature
      };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lin_browser);

		getActionBar();

		fillLinList();

    getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent,
                              View view,
                              int position,
                              long id)
      {
        Intent intent = new Intent(parent.getContext(),
            LINDetailActivity.class);
        intent.putExtra(LINDetailActivity.LIN_ID_KEY, id);
        startActivity(intent);
      }
    });

	}

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(LOADER_ID_LINS, null, this);
    }
	
	/***
	 * Load up the Action Bar
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_lin_list, menu);

        //MenuItem searchViewItem = menu.findItem(R.id.search_property_book);
       // SearchView searchView = (SearchView)searchViewItem.getActionView();
       // searchView.setIconifiedByDefault(false);
		return super.onCreateOptionsMenu(menu);
	}

	
	/***
	 * Handles button clicks from the ActionBar
	 * @param item Button that was clicked.
	 * @return True if the button has been handled well
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		    case R.id.import_property_book:
			    startActivity(new Intent(this, ImportPropertyBookActivity.class));
			    break;
        case R.id.search_property_book:
            //Open search activity
            onSearchRequested();
            break;
        case R.id.property_book_statistics:
          //Open statistics activity
            startActivity(new Intent(this,
                PropertyBookStatisticsActivity.class));
            break;
    }
	
	return true;
	}


  /**
   * Respond to button clicks in the interface
   * @param v
   */
  @Override
  public void onClick(View v) {
    if(v.getId() == R.id.btn_import_property_book) {
      startActivity(new Intent(this, ImportPropertyBookActivity.class));
    }
  }
	

    /**
     * Fill the display with all items that meet the filter requirements
     */
	private void fillLinList() {


    mLinCardCursorAdapter = new LinCardCursorAdapter(this);
    CardListView mCardListView = (CardListView)getListView();
    if(mCardListView != null) {
        mCardListView.setAdapter(mLinCardCursorAdapter);
    }

    getListView().setAdapter(mLinCardCursorAdapter);
    getLoaderManager().initLoader(LOADER_ID_LINS, null, this);


	}

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        return new CursorLoader(
                this,
                PropertyBookContentProvider.CONTENT_URI_LIN,
                LIN_PROJECTION,
                null,
                null,
                TableContractLIN.columnLIN);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {


      //mLinCardCursorAdapter.swapCursor(cursor);
      mLinCardCursorAdapter.swapCursor(cursor);

      if(cursor.getCount() > 0) {
        getListView().setFastScrollEnabled(true);
          //cardListView.setFastScrollEnabled(true);
      }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        getListView().setFastScrollEnabled(false);

        mLinCardCursorAdapter.swapCursor(null);
    }

}
