package com.NortrupDevelopment.PropertyBook.view;

import android.content.Context;

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
   * Implementing class should set the CardListView adapter to the
   * provided adapter.
   * @param LineNumbers Array of LINs to be used in the list.
   */
  public void setList(RealmResults<LineNumber> LineNumbers);

  /**
   * Return a copy of the instances context
   * @return Context for the instance
   */
  public Context getContex();
}
