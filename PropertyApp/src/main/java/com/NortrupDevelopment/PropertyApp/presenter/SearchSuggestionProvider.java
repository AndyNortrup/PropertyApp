package com.NortrupDevelopment.PropertyApp.presenter;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Provide search suggestions to the search provider.
 *
 * Reference: http://developer.android.com/guide/topics/search/adding-recent-query-suggestions.html#RecentQuerySearchableConfiguration
 *
 * Created by andy on 7/12/14.
 */
public class SearchSuggestionProvider extends SearchRecentSuggestionsProvider {
  public final static String AUTHORITY =
      "com.NortrupDevelopment.PropertyApp.SuggestionProvider";
  public final static int MODE = DATABASE_MODE_QUERIES;

  public SearchSuggestionProvider() {
    setupSuggestions(AUTHORITY, MODE);
  }
}
