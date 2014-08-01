package com.NortrupDevelopment.PropertyApp.presenter;

import android.content.Context;
import android.net.Uri;
import android.util.SparseBooleanArray;

import com.NortrupDevelopment.PropertyApp.FileHandlers.PBICLoader;
import com.NortrupDevelopment.PropertyApp.FileHandlers.PBICLoaderCallbacks;
import com.ipaulpro.afilechooser.utils.FileUtils;

import java.util.ArrayList;

/**
 * Presentation class to control the import of a file into the property book.
 * Created by andy on 7/23/14.
 */
public class ImportPresenter implements PBICLoaderCallbacks {

  ImportView mView;
  Uri mFileUri;
  ArrayList<String> mPBICSheetNames;

  private static final String REQUIRED_FILE_TYPE =
      "application/vnd.ms-excel";

  public ImportPresenter(ImportView importView) {
    mView = importView;
  }

  /**
   * Called when user requests to cancel the import process.
   */
  public void cancelRequested() {
    mView.closeView();
  }

  /**
   * Called when the user requests to select a file to import from
   */
  public void fileSelectRequested() {
    mView.showFileSelectIntent();
  }

  /**
   * Called when the user has selected a file through the ACTION_GET_CONTENT
   * picker
   * @param fileUri Uri which points to the desired file.
   */
  public void fileSelected(Uri fileUri) {
    fileSelected(fileUri, true);
  }

  /**
   * Called internally to restore the file name to the view and set the URI.
   * Using the lookup = true forces loolkup of sheet names from the file,
   * lookup = false is used when we are restoring from a configuration change
   * and already have the list of sheet names.
   *
   * @param fileUri Uri which points to the desired file.
   * @param lookup True to lookup the PBIC Sheet names from file.
   */
  public void fileSelected(Uri fileUri, boolean lookup) {

    if (fileUri != null) {

      boolean fileAccepted = false;

      //Check if the file is of the right data type
      if (FileUtils.getMimeType((Context) mView, fileUri)
          .equals(REQUIRED_FILE_TYPE)) {
        mFileUri = fileUri;

        mView.showPBICSelect();

        if(!lookup) {
          //Get list of PBICs
          new PBICLoader(mFileUri, this, (Context) mView);
        }

        fileAccepted = true;
      }

      mView.setFileNameView(
          FileUtils.getFile((Context) mView, fileUri).getName(), fileAccepted);
    }
  }

  /**
   * Used by the view when it wants to restore the fileUri after a configuration
   * change without forcing a rebuild of the list.
   * @param fileUri Uri of the selected file.
   *                @param pbics List of the Array
   */
  public void restoreFileInformation(Uri fileUri,
                                     String[] pbics,
                                     SparseBooleanArray selectedPBICs) {
    mFileUri = fileUri;

    if(pbics == null) {
      fileSelected(mFileUri, true);
    } else {
      fileSelected(mFileUri, false);
      receivePBICList(pbics);
    }


  }

  /**
   * Returns the uri of the currently selected file.
   * @return File Uri
   */
  public Uri getFileUri() {
    return mFileUri;
  }

  /**
   * Sends an array of strings from the AsyncTask back to the callback.
   *
   * @param result List of PBIC Sheet Names
   */
  @Override
  public void receivePBICList(String[] result) {
    mPBICSheetNames = new ArrayList<String>();
    for(int x=0; x<result.length; x++) {
      mPBICSheetNames.add(result[x]);
      mView.addPBIC(x, result[x]);
    }
  }
}
