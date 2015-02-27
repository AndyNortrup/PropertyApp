package com.NortrupDevelopment.PropertyBook.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.test.InstrumentationTestCase;

import com.NortrupDevelopment.PropertyBook.model.LineNumber;
import com.NortrupDevelopment.PropertyBook.model.ModelSearcher;
import com.NortrupDevelopment.PropertyBook.model.RealmDefinition;
import com.NortrupDevelopment.PropertyBook.model.RealmModelSearcher;
import com.NortrupDevelopment.PropertyBook.view.DefaultMainActivity;

import java.util.AbstractList;

public class DefaultSearchAdapterTest extends InstrumentationTestCase {

  Activity mContext;

  @Override
  public void setUp() throws Exception {
    mContext = launchActivity("com.NortrupDevelopment.PropertyBook.view",
        DefaultMainActivity.class,
        new Bundle());
  }

  public void testOnCreateViewHolder() throws Exception {
    RecyclerView view = new RecyclerView(mContext);

    ModelSearcher searcher = new RealmModelSearcher(
        RealmDefinition.getRealm(mContext,
            RealmDefinition.TEASTING_REALM));

    AbstractList<LineNumber> contents = searcher.searchLineNumber("");

    DefaultSearchAdapter<LineNumber> adapter =
        new DefaultSearchAdapter<LineNumber>(contents);

    view.setAdapter(adapter);
    assertTrue(adapter.onCreateViewHolder(view, 0)
        instanceof DefaultSearchLineNumberViewHolder);
  }
}