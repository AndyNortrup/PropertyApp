package com.NortrupDevelopment.PropertyBook.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.NortrupDevelopment.PropertyBook.App;
import com.NortrupDevelopment.PropertyBook.R;
import com.NortrupDevelopment.PropertyBook.bus.DefaultImportCompleteEvent;
import com.NortrupDevelopment.PropertyBook.bus.FileSelectedEvent;
import com.NortrupDevelopment.PropertyBook.bus.ImportCanceledEvent;
import com.NortrupDevelopment.PropertyBook.bus.ImportFinishedEvent;
import com.NortrupDevelopment.PropertyBook.bus.ImportMessageEvent;
import com.NortrupDevelopment.PropertyBook.io.FileUtilities;
import com.NortrupDevelopment.PropertyBook.io.PropertyBookImporter;
import com.NortrupDevelopment.PropertyBook.presenter.ImportPresenter;
import com.NortrupDevelopment.PropertyBook.presenter.ImportView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Displays a wizard to manage the import of a property book into the database.
 * Created by andy on 7/23/14.
 */
public class ImportViewImpl extends LinearLayout
    implements ImportView,
    ListView.OnItemClickListener,
    DialogInterface.OnCancelListener,
    DialogInterface.OnClickListener {
  private static final String FILE_URI_KEY = "FILE_URI";
  private static final String PBIC_LIST_KEY = "PBIC_LIST_KEY";
  private static final String SELECTED_ITEMS_KEY = "SELECTED_ITEMS_KEY";
  private static final String PROGRESS_DIALOG_KEY = "PROGRESS_DIALOG_KEY";
  private final String SUPER_STATE_KEY = "instanceState";

  @InjectView(R.id.import_file_select_button) Button mFileSelectButton;
  @InjectView(R.id.import_button) Button mImportButton;
  @InjectView(R.id.pbic_select_list) ListView mPBICSelectList;
  @InjectView(R.id.import_file_path) TextView filePathDisplay;
  @InjectView(R.id.import_file_status) ImageView mImageFileStatus;

  private ArrayAdapter<String> mPBICListAdapter;
  private ArrayList<String> mPBICs;
  private ProgressDialog mImportProgressDialog;

  private String mImportMessage;
  @Inject ImportPresenter mPresenter;

  @Inject FileUtilities mFileUtilities;

  public ImportViewImpl(Context context, AttributeSet attrs) {
    super(context, attrs);
    ((App) context.getApplicationContext()).component().inject(this);
    mPresenter.attach(this);
    setId(generateViewId());
  }

  /**
   * Inflate the fragment view.
   *
   * @return An inflated view for display
   */
  @Override
  public void onFinishInflate() {

    ButterKnife.inject(this, getRootView());
    mPresenter.restoreViewState();
  }


  @Override public void onAttachedToWindow() {
    super.onAttachedToWindow();
    EventBus.getDefault().register(this);
  }

  @Override public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    EventBus.getDefault().unregister(this);
  }

  /**
   * Called when the interface must display an intent to allow the user to
   * select a file.
   */
  public void showFileSelectIntent() {

    //TODO: Pass a request to show the file manager up to the activity
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
    mPBICSelectList.setVisibility(VISIBLE);
  }

  /**
   * Displays the selected file in the file display TextView
   *
   * @param value Value to set for the text
   * @param good  Good if the file is acceptable, bad if the file is not.
   */
  public void setFileNameView(String value, boolean good) {

    filePathDisplay.setText(value);

    if (!good) {
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
   * @param pbics array of PBICs to add to the list
   */
  @Override
  public void addPBIC(String[] pbics) {
    for (int x = 0; x < pbics.length; x++) {
      mPBICListAdapter.add(pbics[x]);
    }
    mPBICListAdapter.notifyDataSetChanged();
  }


  /**
   * Called by the presenter to enable or disable the import button
   *
   * @param state True if the import button should be enabled.
   */
  @Override
  public void setImportButtonEnabled(boolean state) {
    mImportButton.setEnabled(state);
    if (state) {
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
    mImportMessage = getContext().getString(R.string.import_starting);
    mImportProgressDialog = new ProgressDialog(getContext())
        .show(getContext(),
            getContext().getString(R.string.import_progress_title),
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
        getContext().getString(R.string.import_complete),
        Toast.LENGTH_LONG).show();

    EventBus.getDefault().post(new DefaultImportCompleteEvent());
  }

  /**
   * Directs the view to inform the user that the import has failed.
   */
  public void importFailed() {

    mImportProgressDialog.dismiss();
    Toast.makeText(getContext(),
        getContext().getString(R.string.import_failed),
        Toast.LENGTH_LONG).show();
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

  @Override public void setSelectedPBICs(SparseBooleanArray selectedPBICs) {
    for (int x = 0; x < selectedPBICs.size(); x++) {
      mPBICSelectList.setItemChecked(x, selectedPBICs.get(x));
    }
  }

  /**
   * Callback method to be invoked when an item in this AdapterView has
   * been clicked.
   * <p>
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
                          long id) {
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
    if (which == DialogInterface.BUTTON_NEGATIVE) {
      mPresenter.importCancelRequested();
    }
  }

  public void onEvent(ImportMessageEvent event) {
    updateImportProgress(event.getMessage());
  }

  public void onEvent(ImportCanceledEvent event) {
    importFailed();
  }

  public void onEvent(ImportFinishedEvent event) {
    if (event.getStatus() == PropertyBookImporter.RESULT_OK) {
      importComplete();
    } else {
      importFailed();
    }
  }

  /**
   * Method is called when the event bus emits a FileSelectedEvent
   *
   * @param event FileSelectedEvent containing the file Uri of the selected file
   */
  public void onEvent(FileSelectedEvent event) {
    mPresenter.fileSelected(event.getUri());
  }

  private void fileReceived(Uri uri) {
    //Put the URI in the
  }


  @OnClick(R.id.import_button) public void importRequested() {
    mPresenter.importRequested();
  }

  @OnClick(R.id.import_file_select_button) public void fileSelectButton() {
    mPresenter.fileSelectRequested();
  }

}
