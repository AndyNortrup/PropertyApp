package com.NortrupDevelopment.PropertyBook.io;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import com.NortrupDevelopment.PropertyBook.bus.ImportCanceledEvent;
import com.NortrupDevelopment.PropertyBook.bus.ImportFinishedEvent;
import com.NortrupDevelopment.PropertyBook.bus.ImportMessageEvent;
import com.NortrupDevelopment.PropertyBook.model.ModelFactoryImpl;
import com.NortrupDevelopment.PropertyBook.model.ModelUtilsImpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import jxl.read.biff.BiffException;

/**
 * Created by andy on 8/1/14.
 */
public class PropertyBookImporter
    extends AsyncTask<ImportParameters, String, Integer> {

  public static final int RESULT_ERROR = -1;
  public static final int RESULT_OK = 0;

  private static final String DEBUG_CODE = "PBIC_IMPORTER";

  @Inject Context mContext;

  public PropertyBookImporter() {
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
  protected Integer doInBackground(ImportParameters... params) {
    int[] sheetIndexes =
        params[0].getSheets();

    InputStream inStream;
    String results = "";

    try {

      Looper.prepare();

      inStream =
          mContext.getContentResolver().openInputStream(params[0].getFile());

      /*
       * If we have set the EMPTY_DATABASE_KEY in the extras then
       * we want to delete all of the contents of the database before
       * importing the new property book.
       *
       * We create a ContentProviderOperation for each table and
       * specify no WHERE criteria, which should clear everything.
       *
       * Use of an if will eventually support a decision to merge or
       * replace the data.
       */
      if (params[0].isEmptyDatabase()) {
        deleteCurrentData();
      }

      readInputFile(sheetIndexes, inStream);

      inStream.close();

    } catch (FileNotFoundException e) {
      Log.e(DEBUG_CODE, e.getMessage(), e);
      results = "The file could not be found or accessed.";
    } catch (IOException e) {
      Log.e(DEBUG_CODE, e.getMessage(), e);
      results = "There was an IO exception reading your file";
    } catch (BiffException e) {
      Log.e(DEBUG_CODE, e.getMessage(), e);
      results = "There was a problem reading the excel file provided.";
    } catch (Throwable t) {
      Log.e(DEBUG_CODE, t.getMessage(), t);
      return RESULT_ERROR;
    } finally {
      publishProgress(results);
    }
    return RESULT_OK;
  }

  /**
   * Reads the contents of a file into model objects.
   *
   * @param sheetIndexes An array of integers indicating the indexes of sheets
   *                     to be read from the xls file.
   * @param inStream     A file stream for an XLS file.
   * @throws jxl.read.biff.BiffException Thrown if there are errors reading the XLS format.
   * @throws java.io.IOException         Thrown if there are errors reading the file.
   */
  private void readInputFile(int[] sheetIndexes, InputStream inStream)
      throws BiffException, IOException {
    publishProgress("Reading property book");

    Log.i(DEBUG_CODE, "Starting to read property book.");
    PrimaryHandReceiptReader reader = new PrimaryHandReceiptReaderImpl(
        new ModelFactoryImpl());
    reader.readHandReceipt(inStream, sheetIndexes);
  }

  private void deleteCurrentData() {
    publishProgress("Deleting old data.");


    //Clear the contents of all tables
    new ModelUtilsImpl().deleteAllPropertyBooks();

    Log.i(DEBUG_CODE, "Added removal old data removal commands.");
  }


  @Override
  protected void onProgressUpdate(String... message) {
    EventBus.getDefault().post(new ImportMessageEvent(message[0]));
  }

  @Override
  protected void onCancelled() {
    EventBus.getDefault().post(new ImportCanceledEvent());
  }

  @Override
  protected void onPostExecute(Integer statusCode) {
    EventBus.getDefault().post(new ImportFinishedEvent(statusCode));
  }
}
