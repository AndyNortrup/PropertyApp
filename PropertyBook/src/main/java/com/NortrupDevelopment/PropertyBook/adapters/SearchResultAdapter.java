package com.NortrupDevelopment.PropertyBook.adapters;

import android.content.Context;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.model.LineNumber;

import io.realm.RealmBaseAdapter;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by andy on 10/26/14.
 */
public abstract class SearchResultAdapter<T extends RealmObject>
    extends RealmBaseAdapter {

  String mSearchTerm;

  /**
   * Constructor
   *
   * @param context  The current context.
   * @param result  RealmResult to be displayed by the adapter
   */
  public SearchResultAdapter(Context context,
                             RealmResults<T> result,
                             String searchTerm) {
    super(context, result, true);
    mSearchTerm = searchTerm;
  }

  public abstract LineNumber getLineNumberForIndex(int index);

  /**
   * Creates a Spannable object with the search term (mSearchTerm) highlighted
   * in the fieldText.
   * Pre-condition: fieldText and searchText are not null
   * Post-condition: None
   * @param fieldText Text to be added to the TextField

   * @return
   */
  protected Spannable highlightSearchTerms(String fieldText) {

    Spannable result = Spannable.Factory.getInstance().newSpannable(fieldText);


    int start = fieldText.indexOf(mSearchTerm);
    int end = start + mSearchTerm.length();
    if(start > 0) {
      result.setSpan(
          new ForegroundColorSpan(
              context.getResources().getColor(R.color.primary_color)),
          start,
          end,
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    return result;
  }
}
