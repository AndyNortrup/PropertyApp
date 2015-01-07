package com.NortrupDevelopment.PropertyBook.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.bus.BusProvider;
import com.NortrupDevelopment.PropertyBook.bus.ImportCanceledEvent;
import com.NortrupDevelopment.PropertyBook.bus.ImportCompleteEvent;
import com.NortrupDevelopment.PropertyBook.bus.ImportFinishedEvent;
import com.NortrupDevelopment.PropertyBook.bus.ImportMessageEvent;
import com.NortrupDevelopment.PropertyBook.io.ImportParameters;
import com.NortrupDevelopment.PropertyBook.io.ImportTaskFragment;
import com.NortrupDevelopment.PropertyBook.presenter.ImportPresenter;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Displays a wizard to manage the import of a property book into the database.
 * Created by andy on 7/23/14.
 */
public class ImportFragment extends Fragment
    implements ImportView,
      View.OnClickListener,
      ListView.OnItemClickListener,
      DialogInterface.OnCancelListener,
      DialogInterface.OnClickListener
{
  private static final String FILE_URI_KEY = "FILE_URI";
  private static final String PBIC_LIST_KEY = "PBIC_LIST_KEY";
  private static final String SELECTED_ITEMS_KEY = "SELECTED_ITEMS_KEY";
  private static final String PROGRESS_DIALOG_KEY = "PROGRESS_DIALOG_KEY";

  private static final String TAG_IMPORT_FRAGMENT = "TAG_IMPORT_FRAGMENT";

  private static final int FILE_SELECT_CODE = 1;

  @InjectView(R.id.import_file_select_button) ImageButton mFileSelectButton;
  @InjectView(R.id.import_button) ImageButton mImportButton;
  @InjectView(R.id.pbic_select_list) ListView mPBICSelectList;
  @InjectView(R.id.import_file_path) TextView filePathDisplay;
  @InjectView(R.id.import_file_status) ImageView mImageFileStatus;


  private ArrayAdapter<String> mPBICListAdapter;
  private ArrayList<String> mPBICs;
  private ProgressDialog mImportProgressDialog;

  private String mImportMessage;
  private ImportPresenter mPresenter;

  private ImportTaskFragment mImportFragment;

  /**
   * Inflate the fragment view.
   * @param inflater Inflater service to do the inflating
   * @param container Container that the fragment will exist in.
   * @param savedInstanceState Previous state information.
   * @return An inflated view for display
   */
  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState)
  {
    View result = inflater.inflate(R.layout.activity_import, container, false);

    ButterKnife.inject(this, result);

    mFileSelectButton.setOnClickListener(this);
    mImportButton.setOnClickListener(this);
    mImportButton.setEnabled(false);

    mPresenter = new ImportPresenter(this);

    if(savedInstanceState != null) {
      restoreInstanceState(savedInstanceState);
    }

    mImportFragment = (ImportTaskFragment)getFragmentManager()
        .findFragmentByTag(TAG_IMPORT_FRAGMENT);

    return result;
  }

  private void restoreInstanceState(Bundle savedInstanceState) {

    if(savedInstanceState.containsKey(FILE_URI_KEY)) {
      if(savedInstanceState.containsKey(PBIC_LIST_KEY)) {
        mPresenter.restoreFileInformation(
            Uri.parse(savedInstanceState.getString(FILE_URI_KEY)),
            savedInstanceState.getStringArray(PBIC_LIST_KEY));

        //Restore checked items
        if(savedInstanceState.containsKey(SELECTED_ITEMS_KEY)) {
          ArrayList<Integer> checkedItems =
              savedInstanceState.getIntegerArrayList(SELECTED_ITEMS_KEY);

          for(Integer index : checkedItems) {
            mPBICSelectList.setItemChecked(index, true);
          }
          mPresenter.pbicSelectionChanged(
              mPBICSelectList.getCheckedItemPositions());
        }
      } else {
        mPresenter.fileSelected(
            Uri.parse(savedInstanceState.getString(FILE_URI_KEY)));
      }

      if(savedInstanceState.containsKey(PROGRESS_DIALOG_KEY)) {
        mImportMessage = savedInstanceState.getString(PROGRESS_DIALOG_KEY);
        showImportProgress();
        mImportProgressDialog.setMessage(mImportMessage);
      }
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    BusProvider.getBus().register(this);
  }


  @Override
  public void onStop() {
    super.onStop();
    BusProvider.getBus().unregister(this);
  }


  @Override
  /**
   * Store state for the activity when the activity is destroyed.
   */
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);

    if(mPresenter.getFileUri() != null) {
      outState.putString(FILE_URI_KEY, mPresenter.getFileUri().toString());
    }

    if(mPBICs != null) {
      outState.putStringArray(PBIC_LIST_KEY, mPBICs.toArray(new String[0]));

      //Store which items are checked in case of screen rotation
      ArrayList<Integer> selectedIndexes = new ArrayList<Integer>();
      SparseBooleanArray items = mPBICSelectList.getCheckedItemPositions();
      for(int x=0; x<items.size(); x++) {
        if(items.get(x)) {
          selectedIndexes.add(x);
        }
      }
      outState.putIntegerArrayList(SELECTED_ITEMS_KEY, selectedIndexes);
    }

    if(mImportProgressDialog != null) {
      outState.putString(PROGRESS_DIALOG_KEY, mImportMessage);
    }
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
      if(getActivity() != null) {
        Toast.makeText(getActivity(),
            "Please install a file manager.",
            Toast.LENGTH_SHORT).show();
      }
    }
  }

  /**
   * Displays the pbicTV List.
   */
  public void showPBICSelect() {

    //Create list and list adapter
    mPBICs = new ArrayList<String>();
    mPBICListAdapter = new ArrayAdapter<String>(
        getContext(), android.R.layout.simple_list_item_multiple_choice, mPBICs);
    mPBICSelectList.setAdapter(mPBICListAdapter);

    mPBICSelectList.setOnItemClickListener(this);
  }

  /**
   * Displays the selected file in the file display TextView
   * @param value Value to set for the text
   * @param good Good if the file is acceptable, bad if the file is not.
   */
  public void setFileNameView(String value, boolean good) {

    filePathDisplay.setText(value);

    if(!good) {
      filePathDisplay.setTextColor(
          getResources().getColor(android.R.color.holo_red_dark));
      mImageFileStatus.setImageDrawable(
          getResources().getDrawable(R.drawable.ic_action_navigation_cancel));
    } else {
      filePathDisplay.setTextColor(
          getResources().getColor(R.color.primary_color));
      mImageFileStatus.setImageDrawable(
          getResources().getDrawable(R.drawable.ic_action_navigation_accept));
    }
  }

  /**
   * Directs the view to add a pbicTV to the list
   *
   * @param index position in the list that the pbicTV should be added
   * @param pbicName String name of he pbicTV from the property book
   */
  @Override
  public void addPBIC(int index, String pbicName) {
    mPBICListAdapter.add(pbicName);
    mPBICListAdapter.notifyDataSetChanged();
  }


  /**
   * Called by the presenter to enable or disable the import button
   * @param state True if the import button should be enabled.
   */
  @Override
  public void setImportButtonEnabled(boolean state) {
    mImportButton.setEnabled(state);
    if(state) {
      mImportButton.setVisibility(View.VISIBLE);
    } else {
      mImportButton.setVisibility(View.GONE);
    }
  }

  /**
   * Called by the presenter to have the view show the user that the import
   * has started.
   */
  @Override
  public void showImportProgress() {
    mImportMessage = getString(R.string.import_starting);
    mImportProgressDialog = new ProgressDialog(getContext())
    .show(getContext(),
        getString(R.string.import_progress_title),
        mImportMessage,
        true,
        true,
        this);
  }

  /**
   * Directs the view to update the progress of the import with the provided
   * message.
   *
   * @param progressUpdate Message to be displayed.
   */
  public void updateImportProgress(String progressUpdate) {
    mImportMessage = progressUpdate;
    mImportProgressDialog.setMessage(mImportMessage);
  }

  /**
   * Directs the view to stop showing the import progress reports and report to
   * the user that the import is complete.
   */
  public void importComplete() {

    mImportProgressDialog.dismiss();
    Toast.makeText(getContext(),
        getString(R.string.import_complete),
        Toast.LENGTH_LONG).show();

    getFragmentManager().beginTransaction().remove(mImportFragment).commit();
    mImportFragment = null;
    BusProvider.getBus().post(new ImportCompleteEvent());
  }

  /**
   * Directs the view to inform the user that the import has failed.
   */
  public void importFailed() {

    mImportProgressDialog.dismiss();
    Toast.makeText(getContext(),
        getString(R.string.import_failed),
        Toast.LENGTH_LONG).show();

    getFragmentManager().beginTransaction().remove(mImportFragment).commit();
    mImportFragment = null;
  }

  /**
   * Provide the presenter with a current copy of the context.
   */
  @Override
  public Context getContext() {
    return getActivity();
  }

  /**
   * Return a copy of the Activity's presenter
   *
   * @return The presenter for this activity.
   */
  @Override
  public ImportPresenter getPresenter() {
    return mPresenter;
  }

  /**
   * Instructs the view to start an ImportTaskFragment
   */
  public void startImportFragment(Uri file, int[] sheets, boolean emptyDatabase)
  {

    if(mImportFragment == null) {
      FragmentManager fm = getActivity().getSupportFragmentManager();
      mImportFragment = new ImportTaskFragment();

      Bundle arguments = new Bundle();
      arguments.putParcelable(ImportTaskFragment.ARGUMENTS_KEY,
          new ImportParameters(file, sheets, emptyDatabase));

      mImportFragment.setArguments(arguments);
      fm.beginTransaction()
          .add(mImportFragment, TAG_IMPORT_FRAGMENT)
          .commit();
    }

  }


  /**
   * Callback method to be invoked when an item in this AdapterView has
   * been clicked.
   * <p/>
   * Implementers can call getItemAtPosition(position) if they need
   * to access the data associated with the selected item.
   *
   * @param parent   The AdapterView where the click happened.
   * @param view     The view within the AdapterView that was clicked (this
   *                 will be a view provided by the adapter)
   * @param position The position of the view in the adapter.
   * @param id       The row id of the item that was clicked.
   */
  @Override
  public void onItemClick(AdapterView<?> parent,
                          View view,
                          int position,
                          long id)
  {
    mPresenter.pbicSelectionChanged(mPBICSelectList.getCheckedItemPositions());
  }

  /**
   * This method will be invoked when the dialog is canceled.
   *
   * @param dialog The dialog that was canceled will be passed into the
   *               method.
   */
  @Override
  public void onCancel(DialogInterface dialog) {
    mPresenter.importCancelRequested();
  }

  /**
   * This method will be invoked when a button in the dialog is clicked.
   *
   * @param dialog The dialog that received the click.
   * @param which  The button that was clicked (e.g.
   *               {@link android.content.DialogInterface#BUTTON1}) or the position
   */
  @Override
  public void onClick(DialogInterface dialog, int which) {
    if(which == DialogInterface.BUTTON_NEGATIVE) {
      mPresenter.importCancelRequested();
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    if(requestCode == FILE_SELECT_CODE && resultCode ==
       Activity.RESULT_OK) {
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
      mPresenter.importRequested();
    } else if(v == mFileSelectButton) {
      mPresenter.fileSelectRequested();
    }
  }

  @Subscribe
  public void onImportMessage(ImportMessageEvent event) {
    updateImportProgress(event.getMessage());
  }

  @Subscribe public void onImportCanceled(ImportCanceledEvent event) {
    importFailed();
  }

  @Subscribe public void onImportFinished(ImportFinishedEvent event) {
    if(event.getStatus() == ImportTaskFragment.PropertyBookImporter.RESULT_OK) {
      importComplete();
    } else {
      importFailed();
    }
  }
}
