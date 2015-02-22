package com.NortrupDevelopment.PropertyBook.presenter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.view.TitledView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * This class is used to display a simple fragment when no search results are
 * found.
 * Created by andy on 4/25/14.
 */

public class NoSearchResultsView extends LinearLayout
    implements TitledView {

  @InjectView(R.id.no_search_result_found) TextView textView;

  public NoSearchResultsView(Context context) {
    super(context);
  }

  @Override
  public void onFinishInflate() {
    ButterKnife.inject(this);
  }

  public void setSearchTerm(String searchTerm) {
    Spannable spannable = createSpannable(searchTerm);
    textView.setText(spannable);
  }

  private Spannable createSpannable(String searchQuery) {
    //Create a spannable text field with the search term highlighted green
    Spannable spannable = new SpannableString(
        getContext().getString(R.string.no_search_result) + searchQuery);

    int textPrefixLength =
        getContext().getString(R.string.no_search_result).length();

    spannable.setSpan(
        new ForegroundColorSpan(
            getResources().getColor(R.color.primary_color)),
        textPrefixLength,
        textPrefixLength + searchQuery.length(),
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    );
    return spannable;
  }

  @Override
  public CharSequence getTitle() {
    return "No Search Results";
  }
}
