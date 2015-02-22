package com.NortrupDevelopment.PropertyBook.test;

import android.support.v7.widget.RecyclerView;
import android.test.AndroidTestCase;

import com.NortrupDevelopment.PropertyBook.adapters.DefaultSearchAdapter;
import com.NortrupDevelopment.PropertyBook.adapters.DefaultSearchLineNumberViewHolder;
import com.NortrupDevelopment.PropertyBook.model.LineNumber;
import com.NortrupDevelopment.PropertyBook.model.ModelSearcher;
import com.NortrupDevelopment.PropertyBook.model.RealmModelSearcher;

import java.util.AbstractList;

/**
 * Created by andy on 2/14/15.
 */
public class ViewHolderTest  extends AndroidTestCase {
  public void testCreateViewHolder() {

    RecyclerView view = new RecyclerView(getContext());

    ModelSearcher searcher = new RealmModelSearcher(getContext());
    AbstractList<LineNumber> contents = searcher.searchLineNumber("");

    DefaultSearchAdapter<LineNumber> adapter =
        new DefaultSearchAdapter<LineNumber>(contents);

    view.setAdapter(adapter);
    assertTrue(adapter.onCreateViewHolder(view, 0)
        instanceof DefaultSearchLineNumberViewHolder);
  }

}
