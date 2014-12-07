package com.NortrupDevelopment.PropertyBook.loaders;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.NortrupDevelopment.PropertyBook.R;

import java.io.IOException;

import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * PBICLoader opens the provided file and
 * Created by andy on 7/31/14.
 */
public class PBICLoader extends AsyncTask<Uri, Void, String[]> {

  PBICLoaderCallbacks mCallbacks;
  Uri mFile;
  Context mContext;

  String[] mResult;

  /**
   * Default constructor starts the background load.
   * @param file Uri of the user selected file.
   * @param callbacks Object to receive the results.
   */
  public PBICLoader(Uri file, PBICLoaderCallbacks callbacks, Context context) {
    mFile = file;
    mCallbacks = callbacks;
    mContext = context;

    execute(mFile);
  }

  /**
   * Override this method to perform a computation on a background thread. The
   * specified parameters are the parameters passed to {@link #execute}
   * by the caller of this task.
   * <p/>
   * This method can call {@link #publishProgress} to publish updates
   * on the UI thread.
   *
   * @param params The parameters of the task.
   * @return A result, defined by the subclass of this task.
   * @see #onPreExecute()
   * @see #onPostExecute
   * @see #publishProgress
   */
  @Override
  protected String[] doInBackground(Uri... params) {

    Workbook workbook;

    try {
      workbook = Workbook.getWorkbook(
      mContext.getContentResolver().openInputStream(params[0]));
      mResult = workbook.getSheetNames();

    } catch (IOException ex) {
      Toast.makeText(mContext,
          R.string.error_selecting_file,
          Toast.LENGTH_SHORT).show();
      return null;
    } catch (BiffException biff) {
      Toast.makeText(mContext,
          R.string.error_reading_excel_file,
          Toast.LENGTH_SHORT).show();
      return null;
    }

    return mResult;
  }

  @Override
  protected void onPostExecute(String[] result) {
    mCallbacks.receivePBICList(result);
  }
}
