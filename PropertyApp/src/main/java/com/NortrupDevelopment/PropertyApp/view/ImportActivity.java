package com.NortrupDevelopment.PropertyApp.view;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.NortrupDevelopment.PropertyApp.R;
import com.NortrupDevelopment.PropertyApp.presenter.ImportPresenter;
import com.NortrupDevelopment.PropertyApp.presenter.ImportView;

import java.util.ArrayList;

/**
 * Displays a wizard to manage the import of a property book into the database.
 * Created by andy on 7/23/14.
 */
public class ImportActivity extends Activity
    implements ImportView, View.OnClickListener{

  private static final String FILE_URI_KEY = "FILE_URI";
  private static final String PBIC_LIST_KEY = "PBIC_LIST_KEY";

  private static final int FILE_SELECT_CODE = 1;

  private Button mImportButton, mCancelButton, mFileSelectButton;
  private ListView mPBICSelectList;
  private ArrayAdapter<String> mPBICListAdapter;
  private ArrayList<String> mPBICs;

  private ImportPresenter mPresenter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mPresenter = new ImportPresenter(this);
    setContentView(R.layout.activity_import);

    mFileSelectButton = (Button)findViewById(R.id.import_file_select_button);
    mFileSelectButton.setOnClickListener(this);

    mCancelButton = (Button)findViewById(R.id.cancel_button);
    mCancelButton.setOnClickListener(this);

    mImportButton = (Button)findViewById(R.id.import_button);
    mImportButton.setOnClickListener(this);
    mImportButton.setEnabled(false);

    mPBICSelectList = (ListView)findViewById(R.id.pbic_select_list);

    //Restore instance state if it exists
    if(savedInstanceState != null) {

      if(savedInstanceState.containsKey(FILE_URI_KEY)) {
        if(savedInstanceState.containsKey(PBIC_LIST_KEY)) {
          mPresenter.restoreFileInformation(
              Uri.parse(savedInstanceState.getString(FILE_URI_KEY)),
              savedInstanceState.getStringArray(PBIC_LIST_KEY),
              null);
        } else {
          mPresenter.fileSelected(
              Uri.parse(savedInstanceState.getString(FILE_URI_KEY)));
        }
      }
    }
  }

  @Override
  /**
   * Store state for the activity when the activity is distoryed.
   */
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    if(mPresenter.getFileUri() != null) {
      outState.putString(FILE_URI_KEY, mPresenter.getFileUri().toString());
    }

    if(mPBICs != null) {
      outState.putStringArray(PBIC_LIST_KEY, mPBICs.toArray(new String[0]));
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    if(requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK) {
      mPresenter.fileSelected(data.getData());
    }
  }

  /**
   * Called when a view has been clicked.
   *
   * @param v The view that was clicked.
   */
  @Override
  public void onClick(View v) {
    if(v == mImportButton) {
      mPresenter.fileSelectRequested();
    } else if(v == mCancelButton) {
      mPresenter.cancelRequested();
    } else if(v == mFileSelectButton) {
      mPresenter.fileSelectRequested();
    }
  }


  public void closeView() {
    this.finish();
  }

  /**
   * Called when the interface must display an intent to allow the user to
   * select a file.
   */
  public void showFileSelectIntent() {

    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.addCategory(Intent.CATEGORY_OPENABLE);

    MimeTypeMap mime = MimeTypeMap.getSingleton();
    intent.setType(mime.getMimeTypeFromExtension("xls"));

    try {
      startActivityForResult(intent, FILE_SELECT_CODE);
    } catch(ActivityNotFoundException ex) {
      Toast.makeText(this,
          "Please install a file manager.",
          Toast.LENGTH_SHORT).show();
    }
  }

  public void showPBICSelect() {
    mPBICSelectList.setVisibility(View.VISIBLE);
    findViewById(R.id.import_welcome_textview).setVisibility(View.GONE);

    //Create list and list adapter
    mPBICs = new ArrayList<String>();
    mPBICListAdapter = new ArrayAdapter<String>(
        this, android.R.layout.simple_list_item_multiple_choice, mPBICs);
    mPBICSelectList.setAdapter(mPBICListAdapter);

    //TODO: Capture check and uncheck events.
  }

  /**
   * Displays the selected file in the file display TextView
   * @param value Value to set for the text
   * @param good Good if the file is acceptable, bad if the file is not.
   */
  public void setFileNameView(String value, boolean good) {

    TextView filePathDisplay = (TextView)findViewById(R.id.import_file_path);
    filePathDisplay.setText(value);

    if(!good) {
      filePathDisplay.setTextColor(
          getResources().getColor(android.R.color.holo_red_dark));
    } else {
      filePathDisplay.setTextColor(
          getResources().getColor(R.color.property_app_dark_green));
    }
  }

  /**
   * Directs the view to add a PBIC to the list
   *
   * @param index position in the list that the PBIC should be added
   * @param pbicName String name of he PBIC from the property book
   */
  @Override
  public void addPBIC(int index, String pbicName) {
    mPBICListAdapter.add(pbicName);
    mPBICListAdapter.notifyDataSetChanged();
  }

}
