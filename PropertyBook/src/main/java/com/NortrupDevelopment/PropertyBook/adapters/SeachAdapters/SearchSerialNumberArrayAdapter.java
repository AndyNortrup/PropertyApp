package com.NortrupDevelopment.PropertyBook.adapters.SeachAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.model.LineNumber;
import com.NortrupDevelopment.PropertyBook.model.SerialNumber;

import io.realm.RealmResults;

/**
 * Binds the results of a LIN Number query to a ListView
 * Created by andy on 10/14/14.
 */
public class SearchSerialNumberArrayAdapter
    extends SearchResultAdapter<SerialNumber>

{

  /**
   * Constructor
   *
   * @param context  The current context.
   * @param results data to be displayed
   */
  public SearchSerialNumberArrayAdapter(Context context,
                                        RealmResults<SerialNumber> results,
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
    SerialNumber result = (SerialNumber)super.getItem(position);
    if(convertView == null) {
      convertView = LayoutInflater.from(super.context)
          .inflate(R.layout.search_result_searial_number, parent, false);
    }

    TextView lineNumber =(TextView)convertView.findViewById(R.id.lin_search_lin);
    TextView nomenclature =
        (TextView)convertView.findViewById(R.id.lin_search_nomenclature);
    TextView stockNumberNomenclature =
        (TextView)convertView.findViewById(R.id.nsn_search_nomenclature);
    TextView stockNumber =
        (TextView)convertView.findViewById(R.id.nsn_search_nsn);
    TextView serialNumber =
        (TextView)convertView.findViewById(R.id.serial_number_search_sn);

    lineNumber.setText(highlightSearchTerms(
        result.getStockNumber().getParentLineNumber().getLin()));
    nomenclature.setText(highlightSearchTerms(
        result.getStockNumber()
        .getParentLineNumber().getNomenclature()));
    stockNumberNomenclature.setText(highlightSearchTerms(
        result.getStockNumber().getNomenclature()));
    stockNumber.setText(highlightSearchTerms(
        result.getStockNumber().getNsn()));
    serialNumber.setText(highlightSearchTerms(result.getSerialNumber()));

    return convertView;
  }

  @Override
  public LineNumber getLineNumberForIndex(int index) {
    return ((SerialNumber)super.getItem(index))
        .getStockNumber()
        .getParentLineNumber();
  }
}

