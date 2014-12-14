package com.NortrupDevelopment.PropertyBook.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.NortrupDevelopment.PropertyBook.R;

/**
 * This activity presents the user with a listing of all NSN's and Serial
 * numbers for a given LIN which is passed as an extra from the calling
 * activity.
 * Created by andy on 2/16/14.
 */
public class LINDetailActivity extends ActionBarActivity {

  public static final String LIN_ID_KEY = "LIN_ID";
  private static final String TAG_DISPLAY_FRAGMENT = "display_fragment";

  LINDetailFragment detailFragment;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_lin_detail);

    FragmentManager fm = getFragmentManager();
    detailFragment =
        (LINDetailFragment)fm.findFragmentByTag(TAG_DISPLAY_FRAGMENT);

    Intent intent = getIntent();

    if(detailFragment == null) {
      detailFragment = new LINDetailFragment();
      Bundle arguments = new Bundle();
      arguments.putString(LIN_ID_KEY, intent.getStringExtra(LIN_ID_KEY));
      detailFragment.setArguments(arguments);
    }

    FragmentTransaction ft = getFragmentManager().beginTransaction();
    ft.replace(R.id.lin_detail_activity_layout,
        detailFragment,
        TAG_DISPLAY_FRAGMENT);
    ft.commit();
  }
}