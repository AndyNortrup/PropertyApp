package com.NortrupDevelopment.PropertyBook.view;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.model.LIN;
import com.NortrupDevelopment.PropertyBook.presenter.LINBrowserPresenter;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;

public class LINBrowserActivity extends Activity
    implements LINBrowser, Card.OnCardClickListener {

  private LINBrowserPresenter mPresenter;
  private ListView mListView;
  private LinearLayout mLoadingLayout;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_lin_browser);
    getActionBar();

    //Grab our view elements
    mListView = (ListView)findViewById(R.id.lin_list);
    mLoadingLayout = (LinearLayout)findViewById(R.id.lin_loading_progress);

    //Connect our presenter
    mPresenter = new LINBrowserPresenter(this);
	}

  @Override
  public void onResume() {
      super.onResume();
      mPresenter.activityResumed();
  }
	
	/***
	 * Load up the Action Bar
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_lin_list, menu);

		return super.onCreateOptionsMenu(menu);
	}

	
	/***
	 * Handles button clicks from the ActionBar
	 * @param item Button that was clicked.
	 * @return True if the button has been handled
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		    case R.id.import_property_book:
			    mPresenter.importRequested();
			    break;
        case R.id.search_property_book:
            //Open search activity
            onSearchRequested();
            break;
        case R.id.property_book_statistics:
          //Open statistics activity
            mPresenter.statisticsRequested();
            break;
      case R.id.about_us:
          startActivity(new Intent(this, AboutActivity.class));
        break;
    }
	
	  return true;
	}

  /**
   * Shows the Loading progress bar and hides mCardLista and mEmptyLayout.
   */
  @Override
  public void showLoadingProgressBar() {
    mListView.setVisibility(View.GONE);
    mLoadingLayout.setVisibility(View.VISIBLE);
  }


  /**
   * Shows the mCardList and hides the progress bar and empty list view.
   */
  @Override
  public void showList() {
    mLoadingLayout.setVisibility(View.GONE);
    mListView.setVisibility(View.VISIBLE);
  }

  @Override
  public LoaderManager getActivityLoaderManager() {
    return getLoaderManager();
  }

  /**
   * Starts the LINDetailActivity
   * @param linId Database _id number of the LIN to be displayed by the
   *              LINDetailActivity
   */
  public void startLINDetailActivity(int linId) {
    Intent intent = new Intent(this, LINDetailActivity.class);

    Bundle bundle = new Bundle();
    bundle.putInt(LINDetailActivity.LIN_ID_KEY, linId);

    intent.putExtras(bundle);

    startActivity(intent);
  }

  /**
   * Called when the user wants to start the import activity.
   */
  public void startImportActivity() {
    startActivity(new Intent(this, ImportActivity.class));
  }

  /**
   * Called when the user wants to start the property book statistics activity.
   */
  public void startStatisticsActivity() {
    startActivity(new Intent(this, PropertyBookStatisticsActivity.class));
  }

  /**
   * Takes a list of LINs, converts them into UI cards then creates and array
   * adapter for display in the CardList.
   * @param lins List of LINs to be displayed.
   */
  public void setCardList(ArrayList<LIN> lins) {

    if(lins != null) {
      LINArrayAdapter adapter = new LINArrayAdapter(this,
          R.layout.card_lin_browser,
          lins);

      mListView.setAdapter(adapter);
      mListView.setFastScrollEnabled(true);

      mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent,
                                View view,
                                int position,
                                long id) {

          mPresenter.listItemSelected(
              ((LINArrayAdapter)mListView.getAdapter()).getItem(position));
        }
      });
    } else {
      mPresenter.importRequested();
    }

  }

  /**
   * Implements onCardClickListener interface.  Captures card clicks from
   * mCardList.
   * @param card  Card Model associated with the view which generated the call.
   * @param view View that generated the call.
   */
  @Override
  public void onClick(Card card, View view) {
    LIN selected = ((LinCard)card).getLIN();
    mPresenter.listItemSelected(selected);
  }
}
