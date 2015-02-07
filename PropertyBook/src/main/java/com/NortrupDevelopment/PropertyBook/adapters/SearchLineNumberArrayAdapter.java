package com.NortrupDevelopment.PropertyBook.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.model.LineNumber;

import io.realm.RealmResults;

/**
 * Binds the results of a LIN Number query to a ListView
 * Created by andy on 10/14/14.
 */
public class SearchLineNumberArrayAdapter
    extends SearchResultAdapter<LineNumber>

{
  /**
   * Constructor
   *
   * @param context The current context.
   * @param results data to be displayed
   */
  public SearchLineNumberArrayAdapter(Context context,
                                      RealmResults<LineNumber> results,
                                      String searchTerms) {
    super(context, results, searchTerms);
  }

  /**
   * Inflates the view if required and fills the TextViews with values from the
   * result
   *
   * @param position    Position in the result set
   * @param convertView The view if it already exists
   * @param parent      View that contains convertView
   * @return the view, inflated with information
   */
  @Override
  public View getView(int position,
                      View convertView,
                      ViewGroup parent) {
    LineNumber result = (LineNumber)super.getItem(position);
    if (convertView == null) {
      convertView = LayoutInflater.from(super.context)
          .inflate(R.layout.search_result_lin, parent, false);
    }

    TextView lineNumber =
        (TextView) convertView.findViewById(R.id.lin_search_lin);
    TextView nomenclature =
        (TextView) convertView.findViewById(R.id.lin_search_nomenclature);

    lineNumber.setText(highlightSearchTerms(result.getLin()));
    nomenclature.setText(highlightSearchTerms(result.getNomenclature()));

    return convertView;
  }

  @Override
  public LineNumber getLineNumberForIndex(int index) {
    return (LineNumber)super.getItem(index);
  }

}