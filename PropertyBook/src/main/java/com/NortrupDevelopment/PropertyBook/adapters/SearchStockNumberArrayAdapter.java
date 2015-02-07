package com.NortrupDevelopment.PropertyBook.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.model.LineNumber;
import com.NortrupDevelopment.PropertyBook.model.StockNumber;
import com.NortrupDevelopment.PropertyBook.util.NSNFormatter;

import io.realm.RealmResults;

/**
 * Binds the results of a LIN Number query to a ListView
 * Created by andy on 10/14/14.
 */
public class SearchStockNumberArrayAdapter
    extends SearchResultAdapter<StockNumber>

{

  /**
   * Constructor
   *
   * @param context  The current context.
   * @param results data to be displayed
   */
  public SearchStockNumberArrayAdapter(Context context,
                                       RealmResults<StockNumber> results,
                                       String searchTerm) {
    super(context, results, searchTerm);
  }

  /**
   * Inflates the view if required and fills the TextViews with values from the
   * result
   * @param position Position in the result set
   * @param convertView The view if it already exists
   * @param parent View that contains convertView
   * @return the view, inflated with information
   */
  @Override public View getView(int position,
                                View convertView,
                                ViewGroup parent)
  {
    StockNumber result = (StockNumber)super.getItem(position);
    if(convertView == null) {
      convertView = LayoutInflater.from(super.context)
          .inflate(R.layout.search_result_nsn, parent, false);
    }

    //TextView lineNumber =(TextView)convertView.findViewById(R.id.lin_search_lin);
    //TextView nomenclature =
    //    (TextView)convertView.findViewById(R.id.lin_search_nomenclature);
    TextView stockNumberNomenclature =
        (TextView)convertView.findViewById(R.id.nsn_search_nomenclature);
    TextView stockNumber =
        (TextView)convertView.findViewById(R.id.nsn_search_nsn);

    //lineNumber.setText(highlightSearchTerms(
    //    result.getParentLineNumber().getLin()));
    //nomenclature.setText(highlightSearchTerms(
    //    result.getParentLineNumber().getNomenclature()));
    stockNumberNomenclature.setText(highlightSearchTerms(
        result.getNomenclature()));
    stockNumber.setText(highlightSearchTerms(
        NSNFormatter.getFormattedNSN(result.getNsn())));

    return convertView;
  }

  @Override
  public LineNumber getLineNumberForIndex(int index) {
    return ((StockNumber)super.getItem(index)).getParentLineNumber();
  }
}

