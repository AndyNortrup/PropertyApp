package com.NortrupDevelopment.PropertyBook.view;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.NortrupDevelopment.PropertyBook.App;
import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.bus.DisplayBrowserEvent;
import com.NortrupDevelopment.PropertyBook.bus.DisplayLineNumberDetailEvent;
import com.NortrupDevelopment.PropertyBook.bus.FileSelectRequestedEvent;
import com.NortrupDevelopment.PropertyBook.bus.FileSelectedEvent;
import com.NortrupDevelopment.PropertyBook.bus.ImportCompleteEvent;
import com.NortrupDevelopment.PropertyBook.bus.ImportRequestedEvent;
import com.NortrupDevelopment.PropertyBook.bus.SearchRequestedEvent;
import com.NortrupDevelopment.PropertyBook.io.AuthorizedFileTypeIntent;
import com.NortrupDevelopment.PropertyBook.presenter.MainActivity;
import com.NortrupDevelopment.PropertyBook.presenter.MainActivityPresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import io.realm.Realm;

/**
 * Serves as the central Activity for the activity, all components are handled
 * in individual views
 * Created by andy on 12/15/14.
 */
public class DefaultMainActivity
    extends ActionBarActivity
    implements MainActivity
{

  @Inject Realm realm;
  @Inject MainActivityPresenter mPresenter;
  Container mContainer;

  private static final int OPEN_FILE_REQUEST = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ((App) getApplication()).component().inject(this);
    mPresenter.getCurrentDetailLineNumber();
    setContentView(R.layout.base_layout);

    mContainer = (Container) findViewById(R.id.container);
    mPresenter.requestCurrentScreen();

    getSupportActionBar();

  }

  @Override
  protected void onStart() {
    super.onStart();
    mPresenter.attach(this);
    EventBus.getDefault().register(this);
  }

  @Override
  protected void onStop() {
    super.onStop();
    EventBus.getDefault().unregister(this);
  }


  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    mPresenter.requestCurrentScreen();
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
      mContainer.showSearchResults();
      mPresenter.searchRequested(intent.getStringExtra(SearchManager.QUERY));
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    ButterKnife.reset(this);
  }

  /**
   * Handles a request from the bus to display the LIN Detail Fragment
   * @param event  Bus Event passed through EventBus
   */
  @Override
  public void onEvent(DisplayLineNumberDetailEvent event) {
    mPresenter.setCurrentDetailLineNumber(event.getRequestedLIN());
    mContainer.showLineNumber(event.getRequestedLIN());
  }

  @Override public void showImport() {
    mContainer.showImport();
  }

  @Override public void closeImport() {
    mContainer.showBrowser();
  }

  /**
   * Handles a request from the bus to display the import screen
   * @param event Bus Event passed through EventBus
   */
  @Override
  public void onEvent(ImportRequestedEvent event) {
    Log.i("Main Activity", "Received import request");
    mPresenter.setImportView();
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

  @Override
  public void onEvent(FileSelectRequestedEvent event) {
    Intent selectFile = new AuthorizedFileTypeIntent();
    if (getIntent().resolveActivity(getPackageManager()) != null) {
      startActivityForResult(selectFile, OPEN_FILE_REQUEST);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == RESULT_OK && requestCode == OPEN_FILE_REQUEST) {
      EventBus.getDefault().post(new FileSelectedEvent(data.getData()));
    }
  }

}
