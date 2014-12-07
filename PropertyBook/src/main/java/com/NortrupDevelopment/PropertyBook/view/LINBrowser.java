package com.NortrupDevelopment.PropertyBook.view;

import android.app.LoaderManager;

import com.NortrupDevelopment.PropertyBook.model.LineNumber;

import io.realm.RealmResults;

/**
 * This interface defines the methods required for the LINBrowserPresenter
 * to communicate with the LINBrowserActivity.
 * Created by andy on 7/12/14.
 */
public interface LINBrowser {

  /**
   * Change the view to show the progress bar while data is loading.
   */
  public void showLoadingProgressBar();


  /**
   * Change the view to show the list when there is data.
   */
  public void showList();


  /**
   * Retrieve a copy of the LoaderManager from the activity for use in the
   * presenter.
   * @return The Activity's LoaderManager provided by getLoaderManager().
   */
  public LoaderManager getActivityLoaderManager();

  /**
   * Implementing class should set the CardListView adapter to the
   * provided adapter.
   * @param LineNumbers Array of LINs to be used in the list.
   */
  public void setCardList(RealmResults<LineNumber> LineNumbers);

  /**
   * Implementing class should start the LinDetailActivity and pass that activity
   * the provided linId
   * @param lin LIN to be displayed on the LinDetailActivity
   */
  public void startLINDetailActivity(String lin);

  /**
   * Directs the activity to start the Import Activity.
   */
  public void startImportActivity();

  /**
   * Directs the activity to start the Property Book Statistics Activity
   */
  public void startStatisticsActivity();
}
