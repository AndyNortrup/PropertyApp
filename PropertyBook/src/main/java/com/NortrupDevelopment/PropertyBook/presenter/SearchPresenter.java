package com.NortrupDevelopment.PropertyBook.presenter;

import android.view.View;

import java.util.AbstractList;

/**
 * The search presenter is used to control a search view.
 * Created by andy on 2/8/15.
 */
public interface SearchPresenter {

  /**
   * Search the database for results that match the term provided.
   * @param term Search term to look for.
   * @return A list of views containing the results of the search
   */
  AbstractList<View> searchForTerm(String term);
}
