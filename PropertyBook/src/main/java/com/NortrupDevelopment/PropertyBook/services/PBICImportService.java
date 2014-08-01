package com.NortrupDevelopment.PropertyBook.services;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.NortrupDevelopment.PropertyBook.model.PropertyBook;
import com.NortrupDevelopment.PropertyBook.model.PropertyBookContentProvider;
import com.NortrupDevelopment.PropertyBook.view.PrimaryHandReceiptReader;
import com.google.common.io.Closer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import jxl.read.biff.BiffException;

public class PBICImportService extends IntentService {

	public static final String INTENT_NAME = "PBICImportService";
	public static final String SOURCE_URI_KEY =
			"com.NortrupDevelopment.PropertyApp.EXTRA_IMPORT_URI";
	public static final String FILE_SHEET_INDEX_KEY = 
			"com.NortrupDevelopment.PropertyApp.EXTRA_SHEET_INDEX_KEY";
	public static final String RESULT_CODE =
			"com.NortrupDevelopment.PropertyApp.RESULT_CODE";
	public static final String RESULT_STRING = 
			"com.NortrupDevelopment.PropertyApp.RESULT_STRING";
	public static final String RECEIVER_KEY = 
			"com.NortrupDevelopment.PropertyApp.RECEIVER_KEY";
	public static final String EMPTY_DATABASE_KEY =
      "com.NortrupDevelopment.PropertyApp.EMPTY_DATABASE_KEY";

  public static final String BROADCAST_ACTION =
      "com.NortrupDevelopment.PropertyBook.BROADCAST";

    private static final String DEBUG_CODE = "PBIC_IMPORT_SERVICE";
	
	public static final int RESULT_ERROR = -1;
	public static final int RESULT_OK = 0;
	public static final int RESULT_PROCESSING = 1;
	
	private static final String PBIC_IMPORT_ERROR = "PBIC Import Error";

	private String results;
	
	private boolean isStopped = false;
	
	public PBICImportService()	{
		super("PBICImportService");
	}
	
	public PBICImportService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent arg0) {

		int[] sheetIndexes =
				arg0.getIntArrayExtra(FILE_SHEET_INDEX_KEY);
		int resultCode = RESULT_PROCESSING;
		
		InputStream inStream;
		Closer closer = Closer.create();
		
		
		try {
			try {
				inStream = getContentResolver().openInputStream(Uri.parse(arg0.getStringExtra(SOURCE_URI_KEY)));
				
				closer.register(inStream);
				
				ArrayList<ContentProviderOperation> writeActions =
          new ArrayList<ContentProviderOperation>();

        PropertyBookContentProvider provider =
          new PropertyBookContentProvider(getBaseContext());

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
				if(arg0.hasExtra(EMPTY_DATABASE_KEY) &&
            arg0.getBooleanExtra(EMPTY_DATABASE_KEY, false)) {

          sendUpdate("Deleting old data.", RESULT_PROCESSING);
					
					
					//Clear the contents of all tables
					writeActions.add(
							ContentProviderOperation.newDelete(
									PropertyBookContentProvider.CONTENT_URI_ITEM).build());


          writeActions.add(
							ContentProviderOperation.newDelete(
									PropertyBookContentProvider.CONTENT_URI_LIN).build());


          writeActions.add(
							ContentProviderOperation.newDelete(
									PropertyBookContentProvider.CONTENT_URI_NSN).build());
					
					writeActions.add(
							ContentProviderOperation.newDelete(
									PropertyBookContentProvider.CONTENT_URI_ITEM).build());

          Log.i(DEBUG_CODE, "Added removal old data removal commands.");
				}
				

        sendUpdate("Reading property book.", RESULT_PROCESSING);

        Log.i(DEBUG_CODE, "Starting to read property book.");
        ArrayList<PropertyBook> pbics = PrimaryHandReceiptReader
                .readHandReceipt(inStream,
                        sheetIndexes,
                        getBaseContext());


        for(PropertyBook pbic : pbics) {
            Log.i(DEBUG_CODE, "Transfering PBIC '" +
                    pbic.getDescription() + "' write actions");
            writeActions = pbic.getWriteAction(true, writeActions);
        }

        inStream.close();
				
				if(!isStopped) {
					
					sendUpdate("Writing data", RESULT_PROCESSING);

          try {
            ContentProviderResult[] insertResult =
              provider.applyBatch(writeActions);

            if(insertResult.length != writeActions.size()) {
              sendUpdate("Some items were not written to the database.",
                  RESULT_ERROR);
            }
          } catch (ArrayIndexOutOfBoundsException err) {
              Log.w("PBICImportService", "Error inserting into database");
          }
				}
				
				if(!isStopped) {
					sendUpdate("File import complete.", RESULT_OK);
				} else {
					sendUpdate("File import stopped", RESULT_ERROR);
				}
				
				inStream.close();
			} catch (FileNotFoundException e) {
				Log.e(PBIC_IMPORT_ERROR, e.getMessage(), e);
				results = "The file could not be found or accessed.";
				resultCode = RESULT_ERROR;
			} catch (IOException e) {
				Log.e(PBIC_IMPORT_ERROR, e.getMessage(), e);
				results = "There was an IO exception reading your file";
				resultCode = RESULT_ERROR;
			} catch (BiffException e) {
				Log.e(PBIC_IMPORT_ERROR, e.getMessage(), e);
				results = "There was a problem reading the excel file provided.";
				resultCode = RESULT_ERROR;
			} catch (OperationApplicationException e) {
				Log.e(PBIC_IMPORT_ERROR, e.getMessage(), e);
				results = "The application caused and exception during import.";
				resultCode = RESULT_ERROR;
			}  catch (Throwable t) {
				Log.e(PBIC_IMPORT_ERROR, t.getMessage(), t);
                throw closer.rethrow(t);
			} finally { 
				sendUpdate(results, resultCode);
				closer.close();
			}
		} catch (IOException ioe) {
			//TODO: so screwed
		}
		
	}

	
	/**
	 * Used to stop the service once an import has started
	 */
	@Override
	public void onDestroy() {
		isStopped = true;
		super.onDestroy();
	}

  private void sendUpdate(String message, int resultCode) {
    Intent intent = new Intent(BROADCAST_ACTION)
        .putExtra(RESULT_STRING, message)
        .putExtra(RESULT_CODE, resultCode);
    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
  }
}
