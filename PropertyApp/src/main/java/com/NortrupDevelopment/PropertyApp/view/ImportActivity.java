package com.NortrupDevelopment.PropertyApp.view;

import android.app.Activity;
import android.os.Bundle;

import com.NortrupDevelopment.PropertyApp.presenter.ImportPresenter;
import com.NortrupDevelopment.PropertyApp.presenter.ImportView;

/**
 * Created by andy on 7/23/14.
 */
public class ImportActivity extends Activity implements ImportView {

  public ImportPresenter mPresenter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mPresenter = new ImportPresenter(this);
  }
}
