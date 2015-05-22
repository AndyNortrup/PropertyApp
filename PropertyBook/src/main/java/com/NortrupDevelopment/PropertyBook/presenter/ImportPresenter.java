package com.NortrupDevelopment.PropertyBook.presenter;

import android.net.Uri;
import android.util.SparseBooleanArray;

import com.NortrupDevelopment.PropertyBook.App;
import com.NortrupDevelopment.PropertyBook.bus.FileSelectRequestedEvent;
import com.NortrupDevelopment.PropertyBook.io.FileUtilities;
import com.NortrupDevelopment.PropertyBook.io.PBICNameReader;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Presentation class to control the import of a file into the property book.
 * Created by andy on 7/23/14.
 */
public class ImportPresenter {

  ImportView mView;
  String mFilePath;
  String[] mPBICSheetNames;
  SparseBooleanArray mSelectedPBICs;


  private static final String LOG_TAG = "Import Presenter";
  @Inject public PBICNameReader mPBICNameReader;
  @Inject public FileUtilities mFileUtilities;

  @Inject
  public ImportPresenter() {
  }

  public void attach(ImportView view) {
    mView = view;
    ((App) view.getContext().getApplicationContext()).component().inject(this);
  }

  /**
   * Called by the view when the user requests to start the import
   */
  public void importRequested() {
    mView.showImportProgress();

    //Remove all of the false values
    while (mSelectedPBICs.indexOfValue(false) >= 0) {
      mSelectedPBICs.delete(mSelectedPBICs.indexOfValue(false));
    }

    //Convert the SpareBooleanArray into an int array with a set of indexes to
    //import.
    int[] sheetsToImport = new int[mSelectedPBICs.size()];
    int index = 0;

    for (int x = 0; x < mSelectedPBICs.size(); x++) {
      if (mSelectedPBICs.valueAt(x)) {
        sheetsToImport[index] = x;
        index++;
      }
    }

    //TODO: Start the import process

  }


  /**
   * Called when the user requests to select a file to import from
   */
  public void fileSelectRequested() {
    EventBus.getDefault().post(new FileSelectRequestedEvent());
  }

  /**
   * Called by the view in order to notify the presenter that the list of
   * checked items has changed.
   *
   * @param checkedItems List of checked items in the PBIC list.
   */
  public void pbicSelectionChanged(SparseBooleanArray checkedItems) {
    mSelectedPBICs = checkedItems;
    if (checkedItems.indexOfValue(true) >= 0) {
      mView.setImportButtonEnabled(true);
    } else {
      mView.setImportButtonEnabled(false);
    }
  }

  /**
   * Called internally to restore the file name to the view and set the URI.
   * Using the lookup = true forces loolkup of sheet names from the file,
   * lookup = false is used when we are restoring from a configuration change
   * and already have the list of sheet names.
   *
   * @param fileName String which points to the desired file.
   */
  public void fileSelected(Uri fileName) {

    mFilePath = fileName.toString();

    //boolean properType = mFileUtilities.isValidImportFile(fileName);
    mView.setFileNameView(
        mFileUtilities.extractFileNameFrom(fileName),
        true);

    mView.showPBICSelect();
    mPBICNameReader.getSheetNamesFrom(fileName)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(s -> pbicListReceived(s));
  }


  public void pbicListReceived(String[] pbics) {
    mView.showPBICSelect();
    mView.addPBIC(pbics);
    mPBICSheetNames = pbics;
  }

  /**
   * Returns the uri of the currently selected file.
   *
   * @return File Uri
   */
  public String getFilePath() {
    return mFilePath;

  }


  public void importCancelRequested() {
    //Todo: handle canceling the import.
  }

  public void restoreViewState() {
    if (mFilePath != null && !mFilePath.isEmpty()) {
      mView.setFileNameView(
          mFileUtilities.extractFileNameFrom(Uri.parse(mFilePath)),
          true);
    }

    if (mPBICSheetNames != null) {
      pbicListReceived(mPBICSheetNames);
    }

    if (mSelectedPBICs != null) {
      mView.setSelectedPBICs(mSelectedPBICs);
      mView.setImportButtonEnabled(true);
    }
  }
}
