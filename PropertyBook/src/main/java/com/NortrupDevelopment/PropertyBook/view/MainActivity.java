package com.NortrupDevelopment.PropertyBook.view;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.bus.BusProvider;
import com.NortrupDevelopment.PropertyBook.bus.ImportCompleteEvent;
import com.NortrupDevelopment.PropertyBook.bus.ImportRequestedEvent;
import com.NortrupDevelopment.PropertyBook.bus.LINDetailRequestedEvent;
import com.NortrupDevelopment.PropertyBook.model.CurrentView;
import com.NortrupDevelopment.PropertyBook.presenter.MainActivityPresenter;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;

/**
 * Serves as the central Activity for the activity, all components are handled
 * in individual views
 * Created by andy on 12/15/14.
 */
public class MainActivity extends ActionBarActivity {

  private MainActivityPresenter mPresenter;
  Container mContainer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.base_layout);

    getActionBar();

    mContainer = (Container) findViewById(R.id.container);

    mPresenter = MainActivityPresenter.getInstance(this);
    BusProvider.getBus().register(this);

    int currentScreen = CurrentView.getInstance().getCurrentScreen();
    if(currentScreen == CurrentView.SCREEN_BROWSE) {
      mContainer.showBrowser();
    } else if(currentScreen == CurrentView.SCREEN_DETAIL &&
        mPresenter.getCurrentDetailLineNumber() != null) {
      mContainer.showLineNumber(mPresenter.getCurrentDetailLineNumber());
    } else {
      mContainer.showBrowser();
    }

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
  protected void onDestroy() {
    super.onDestroy();

    ButterKnife.reset(this);

    BusProvider.getBus().unregister(this);
  }

  /**
   * Handles a request from the bus to display the LIN Detail Fragment
   * @param event
   */
  @Subscribe public void linDetailRequested(LINDetailRequestedEvent event) {
    mPresenter.setCurrentDetailLineNumber(event.getRequestedLIN());
    mContainer.showLineNumber(event.getRequestedLIN());
  }

  /**
   * Handles a request from the bus to display the import fragment
   * @param event
   */
  @Subscribe public void importRequested(ImportRequestedEvent event) {
    //TODO: Show the import view
  }

  @Subscribe public void importComplete(ImportCompleteEvent event) {
    //TODO: Show the browser after completing an import
  }

}
