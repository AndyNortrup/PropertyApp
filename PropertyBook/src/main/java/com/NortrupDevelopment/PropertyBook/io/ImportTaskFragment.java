package com.NortrupDevelopment.PropertyBook.io;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import com.NortrupDevelopment.PropertyBook.bus.BusProvider;
import com.NortrupDevelopment.PropertyBook.bus.ImportCanceledEvent;
import com.NortrupDevelopment.PropertyBook.bus.ImportFinishedEvent;
import com.NortrupDevelopment.PropertyBook.bus.ImportMessageEvent;
import com.NortrupDevelopment.PropertyBook.control.ModelUtils;
import com.NortrupDevelopment.PropertyBook.model.RealmDefinition;
import com.google.common.io.Closer;
import com.squareup.otto.Bus;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import jxl.read.biff.BiffException;

/**
 * Fragment used to import data which can be retained across config changes.
 * Created by andy on 8/1/14.
 */
public class ImportTaskFragment extends Fragment {

  public static final String ARGUMENTS_KEY = "ARGUMENTS_KEY";

  private Bus bus;

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    bus = BusProvider.getBus();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setRetainInstance(true);

    PropertyBookImporter importer = new PropertyBookImporter(getActivity());
    ImportParameters parameters = getArguments().getParcelable(ARGUMENTS_KEY);
    importer.execute(parameters);

  }

  @Override
  public void onDetach() {
    super.onDetach();
  }


  /**
   * Created by andy on 8/1/14.
   */
  public class PropertyBookImporter extends AsyncTask<ImportParameters, String, Integer> {

    public static final int RESULT_ERROR = -1;
    public static final int RESULT_OK = 0;

    private static final String DEBUG_CODE = "PBIC_IMPORTER";

    Context mContext;

    public PropertyBookImporter(Context context) {
      mContext = context;
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
      Closer closer = Closer.create();
      String results = "";

      try {
        try {

          Looper.prepare();

          inStream =
              mContext.getContentResolver().openInputStream(params[0].getFile());

          closer.register(inStream);

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
          if(params[0].isEmptyDatabase()) {

            publishProgress("Deleting old data.");


            //Clear the contents of all tables
            ModelUtils.deleteAllPropertyBooks(mContext, RealmDefinition.PRODUCTION_REALM);

            Log.i(DEBUG_CODE, "Added removal old data removal commands.");
          }


          publishProgress("Reading property book");

          Log.i(DEBUG_CODE, "Starting to read property book.");
          PrimaryHandReceiptReader.readHandReceipt(inStream,
              sheetIndexes,
              mContext,
              RealmDefinition.PRODUCTION_REALM);


          publishProgress("Writing data");



          publishProgress("File import complete.");

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
          //Looper.loop();
          closer.close();
        }
      } catch (IOException ioe) {
        //Eat it.
        return RESULT_ERROR;
      }
      return RESULT_OK;
    }


    @Override
    protected void onProgressUpdate(String... message) {
      bus.post(new ImportMessageEvent(message[0]));
    }

    @Override
    protected void onCancelled() {
      bus.post(new ImportCanceledEvent());
    }

    @Override
    protected void onPostExecute(Integer statusCode) {
      bus.post(new ImportFinishedEvent(statusCode));
    }
  }


}
