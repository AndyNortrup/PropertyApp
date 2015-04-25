package com.NortrupDevelopment.PropertyBook.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.NortrupDevelopment.PropertyBook.App;
import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.bus.DisplayBrowserEvent;
import com.NortrupDevelopment.PropertyBook.bus.DisplayLineNumberDetailEvent;
import com.NortrupDevelopment.PropertyBook.bus.ImportCompleteEvent;
import com.NortrupDevelopment.PropertyBook.bus.ImportRequestedEvent;
import com.NortrupDevelopment.PropertyBook.bus.SearchRequestedEvent;
import com.NortrupDevelopment.PropertyBook.presenter.MainActivity;
import com.NortrupDevelopment.PropertyBook.presenter.MainActivityPresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Serves as the central Activity for the activity, all components are handled
 * in individual views
 * Created by andy on 12/15/14.
 */
public class DefaultMainActivity
    extends ActionBarActivity
    implements MainActivity
{

  @Inject
  MainActivityPresenter mPresenter;
  Container mContainer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ((App) getApplication()).inject(this);

    setContentView(R.layout.base_layout);

    getActionBar();

    mContainer = (Container) findViewById(R.id.container);
    mPresenter.requestCurrentScreen();

  }

  @Override
  protected void onStart() {
    super.onStart();
    EventBus.getDefault().register(this);
  }

  @Override
  protected void onStop() {
    super.onStop();
    EventBus.getDefault().register(this);
  }

  /**
   * Retrieve a copy of the current container
   * @return
   */
  public Container getContainer() {
    return mContainer;
  }

  /**
   * Handles the user pressing the back button locally.
   */
  @Override public void onBackPressed() {
    boolean handled = mContainer.onBackPressed();
    if(!handled) {
      finish();
    }
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    Log.i("Main Activity", "Received New Intent");
    if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
      mPresenter.searchRequested(intent);
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    ButterKnife.reset(this);
  }

  /**
   * Handles a request from the bus to display the LIN Detail Fragment
   * @param event
   */
  @Override
  public void onEvent(DisplayLineNumberDetailEvent event) {
    mPresenter.setCurrentDetailLineNumber(event.getRequestedLIN());
    mContainer.showLineNumber(event.getRequestedLIN());
  }

  /**
   * Handles a request from the bus to display the import fragment
   * @param event
   */
  @Override
  public void onEvent(ImportRequestedEvent event) {
    //TODO: Show the import view
    Log.i("Main Activity", "Received import request");
  }

  @Override
  public void onEvent(ImportCompleteEvent event) {
    mContainer.showBrowser();
  }

  @Override
  public void onEvent(SearchRequestedEvent event) {
    Log.i("Main Activity", "Received search request");
    onSearchRequested();
  }

  @Override
  public void onEvent(DisplayBrowserEvent event) {
    mContainer.showBrowser();
  }

}
