package com.NortrupDevelopment.PropertyBook.view;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.bus.BusProvider;
import com.NortrupDevelopment.PropertyBook.bus.ImportCompleteEvent;
import com.NortrupDevelopment.PropertyBook.bus.ImportRequestedEvent;
import com.NortrupDevelopment.PropertyBook.bus.LINDetailRequestedEvent;
import com.NortrupDevelopment.PropertyBook.model.LineNumber;
import com.NortrupDevelopment.PropertyBook.presenter.MainActivityPresenter;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;

/**
 * Serves as the central Activity for the activity, all components are handled
 * in individual fragments
 * Created by andy on 12/15/14.
 */
public class MainActivity extends ActionBarActivity {

  private final static String currentFragmentTag = "DISPLAYED_FRAGMENT";
  private MainActivityPresenter mPresenter;
  private FragmentManager mFragmentManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.base_layout);

    getActionBar();

    mPresenter = new MainActivityPresenter();
    BusProvider.getBus().register(this);

    mFragmentManager = getSupportFragmentManager();
    FragmentTransaction ft = mFragmentManager.beginTransaction();

    LINBrowserFragment linBrowserFragment = new LINBrowserFragment();

    ft.add(R.id.fragment_container, linBrowserFragment, currentFragmentTag);

    ft.commit();

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
    replaceWithLINDetail(event.getRequestedLIN());
  }

  /**
   * Handles a request from the bus to display the import fragment
   * @param event
   */
  @Subscribe public void importRequested(ImportRequestedEvent event) {
    replaceWithImport();
  }

  @Subscribe public void importComplete(ImportCompleteEvent event) {
    replaceWithBrowser();
  }

  /**
   * Replace the existing fragment currentFragmentTag with the a LIN Detail
   * Fragment
   * @param requestedLIN Line Number to be displayed by the fragment.
   */
  public void replaceWithLINDetail(LineNumber requestedLIN) {
    TabbedLINDetailFragment linDetailFragment = new TabbedLINDetailFragment();
    linDetailFragment.setRetainInstance(true);

    FragmentTransaction fragmentTransaction =
        mFragmentManager.beginTransaction();

    fragmentTransaction.replace(R.id.fragment_container,
        linDetailFragment,
        currentFragmentTag)
        .addToBackStack("Browser");

    fragmentTransaction.commit();

    linDetailFragment.addLIN(requestedLIN);

  }

  /**
   * Replace the current fragment tagged: CurrentFragmentTag with the an
   * Import Fragment
   */
  public void replaceWithImport() {
    ImportFragment importFragment = new ImportFragment();

    FragmentTransaction fragmentTransaction =
        mFragmentManager.beginTransaction();

    fragmentTransaction.replace(
        mFragmentManager.findFragmentByTag(currentFragmentTag).getId(),
        importFragment,
        currentFragmentTag)
        .addToBackStack("import")
        .commit();
  }

  /**
   * Replace the current fragment with the LINBrowserFragment
   */
  public void replaceWithBrowser() {
    LINBrowserFragment linBrowserFragment = new LINBrowserFragment();

    FragmentTransaction fragmentTransaction =
        mFragmentManager.beginTransaction();

    fragmentTransaction.replace(
        mFragmentManager.findFragmentByTag(currentFragmentTag).getId(),
        linBrowserFragment,
        currentFragmentTag)
        .addToBackStack("browser")
        .commit();
  }
}
