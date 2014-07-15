package com.NortrupDevelopment.PropertyApp.view;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.NortrupDevelopment.PropertyApp.R;
import com.NortrupDevelopment.PropertyApp.model.LIN;
import com.NortrupDevelopment.PropertyApp.presenter.LINBrowser;
import com.NortrupDevelopment.PropertyApp.presenter.LINBrowserPresenter;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

public class LINBrowserActivity extends Activity
    implements View.OnClickListener, LINBrowser, Card.OnCardClickListener {

  private LINBrowserPresenter mPresenter;
  private CardListView mCardList;
  private LinearLayout mEmptyLayout, mLoadingLayout;
  private Button mImportButton;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_lin_browser);
    getActionBar();

    //Grab our view elements
    mCardList = (CardListView)findViewById(R.id.lin_list);
    mEmptyLayout = (LinearLayout)findViewById(android.R.id.empty);
    mLoadingLayout = (LinearLayout)findViewById(R.id.lin_loading_progress);
    mImportButton = (Button)findViewById(R.id.btn_import_property_book);

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
   * @param v View sending the onClick event.
   */
  @Override
  public void onClick(View v) {
    if(v.getId() == R.id.btn_import_property_book) {
      startActivity(new Intent(this, ImportPropertyBookActivity.class));
    }
  }


  @Override
  public void showLoadingProgressBar() {
    mCardList.setVisibility(View.GONE);
    mEmptyLayout.setVisibility(View.GONE);
    mLoadingLayout.setVisibility(View.VISIBLE);
  }

  @Override
  public void showEmptyView() {
    mCardList.setVisibility(View.GONE);
    mLoadingLayout.setVisibility(View.GONE);
    mEmptyLayout.setVisibility(View.VISIBLE);
  }

  @Override
  public void showList() {
    mLoadingLayout.setVisibility(View.GONE);
    mEmptyLayout.setVisibility(View.GONE);
    mCardList.setVisibility(View.VISIBLE);
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
  public void startLINDetailActivity(long linId) {
    Intent intent = new Intent(this, LINDetailActivity.class);

    intent.putExtra(LINDetailActivity.LIN_ID_KEY, linId);

    startActivity(intent);
  }

  /**
   * Takes a list of LINs, converts them into UI cards then creates and array
   * adapter for display in the CardList.
   * @param lins List of LINs to be displayed.
   */
  public void setCardList(ArrayList<LIN> lins) {


    if(lins == null) {
      mCardList.setAdapter((CardArrayAdapter)null);
    } else {
      ArrayList<Card> cards = new ArrayList<Card>();
      for(LIN lin : lins) {
        LinCard card = new LinCard(this);
        card.setLIN(lin);
        cards.add(card);

        //Have this class listen for clicks on this card
        card.setOnClickListener(this);
      }

      mCardList.setAdapter(new CardArrayAdapter(this, cards));
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
