package com.NortrupDevelopment.PropertyBook.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.model.LIN;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by andy on 8/4/14.
 */
public class LINArrayAdapter extends ArrayAdapter<LIN>
  implements SectionIndexer
{

  private String[] mSectionNames;
  private int[] mSectionStartPoints;

  /**
   * Constructor
   *
   * @param context  The current context.
   * @param resource The resource ID for a layout file containing a TextView to
   *                 use when
   */
  public LINArrayAdapter(Context context, int resource, ArrayList<LIN> lins) {
    super(context, resource, lins);

    //Mark where our mSectionMap are.
    HashMap<String, Integer> map = new HashMap<String, Integer>();
    for(int x=0; x<lins.size(); x++) {
      String sectionName = lins.get(x).getLin().substring(0,1);
      if(!map.containsKey(sectionName)) {
        map.put(sectionName, x);
      }
    }

    ArrayList<String> keySet = new ArrayList<String>(map.keySet());
    Collections.sort(keySet);

    mSectionNames = new String[keySet.size()];
    keySet.toArray(mSectionNames);

    mSectionStartPoints = new int[map.keySet().size()];
    int x = 0;
    for(String key : keySet) {
      mSectionStartPoints[x] = map.get(key);
      x++;
    }
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LIN lin = getItem(position);
    if(convertView == null) {
      convertView = LayoutInflater.from(getContext())
          .inflate(R.layout.card_lin_browser, parent, false);
    }

    TextView tvLIN = (TextView)convertView.findViewById(R.id.lin);
    TextView tvNomenclature = (TextView)convertView.findViewById(R.id.lin_nomenclature);
    TextView tvSubLin = (TextView)convertView.findViewById(R.id.sub_lin);

    tvLIN.setText(lin.getLin());
    tvNomenclature.setText(lin.getNomencalture());
    if(!TextUtils.isEmpty(lin.getSubLin())) {
      tvSubLin.setText(lin.getSubLin());
    } else {
      tvSubLin.setVisibility(View.GONE);
    }

    return convertView;
  }

  /**
   * Returns an array of objects representing mSectionMap of the list. The
   * returned array and its contents should be non-null.
   * <p/>
   * The list view will call toString() on the objects to get the preview text
   * to display while scrolling. For example, an adapter may return an array
   * of Strings representing letters of the alphabet. Or, it may return an
   * array of objects whose toString() methods return their section titles.
   *
   * @return the array of section objects
   */
  @Override
  public Object[] getSections() {
    return mSectionNames;
  }

  /**
   * Given the index of a section within the array of section objects, returns
   * the starting position of that section within the adapter.
   * <p/>
   * If the section's starting position is outside of the adapter bounds, the
   * position must be clipped to fall within the size of the adapter.
   *
   * @param sectionIndex the index of the section within the array of section
   *                     objects
   * @return the starting position of that section within the adapter,
   * constrained to fall within the adapter bounds
   */
  @Override
  public int getPositionForSection(int sectionIndex) {
    return mSectionStartPoints[sectionIndex];
  }

  /**
   * Given a position within the adapter, returns the index of the
   * corresponding section within the array of section objects.
   * <p/>
   * If the section index is outside of the section array bounds, the index
   * must be clipped to fall within the size of the section array.
   * <p/>
   * For example, consider an indexer where the section at array index 0
   * starts at adapter position 100. Calling this method with position 10,
   * which is before the first section, must return index 0.
   *
   * @param position the position within the adapter for which to return the
   *                 corresponding section index
   * @return the index of the corresponding section within the array of
   * section objects, constrained to fall within the array bounds
   */
  @Override
  public int getSectionForPosition(int position) {
    return 0;
  }
}