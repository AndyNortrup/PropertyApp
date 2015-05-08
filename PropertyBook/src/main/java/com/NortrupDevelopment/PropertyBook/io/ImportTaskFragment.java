package com.NortrupDevelopment.PropertyBook.io;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.NortrupDevelopment.PropertyBook.App;
import com.NortrupDevelopment.PropertyBook.R;

import javax.inject.Inject;

/**
 * Fragment used to import data which can be retained across config changes.
 * Created by andy on 8/1/14.
 */
public class ImportTaskFragment extends LinearLayout {

  public static final String ARGUMENTS_KEY = "ARGUMENTS_KEY";
  @Inject PropertyBookImporter importer;

  public ImportTaskFragment(Context context) {
    super(context);
    ((App) this.getContext().getApplicationContext()).component().inject(this);

    LayoutInflater inflater = LayoutInflater.from(context);
    inflater.inflate(R.layout.activity_import, this, true);
  }

  public void onCreate(Bundle savedInstanceState) {
    ImportParameters parameters = getArguments().getParcelable(ARGUMENTS_KEY);
    importer.execute(parameters);
  }

}
