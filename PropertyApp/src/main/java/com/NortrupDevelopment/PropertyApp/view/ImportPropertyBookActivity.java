package com.NortrupDevelopment.PropertyApp.view;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.NortrupDevelopment.PropertyApp.ImportServiceReceiver;
import com.NortrupDevelopment.PropertyApp.ImportServiceReceiver.PBICReceiver;
import com.NortrupDevelopment.PropertyApp.R;
import com.NortrupDevelopment.PropertyApp.model.PBICImportService;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import jxl.Workbook;
import jxl.read.biff.BiffException;


public class ImportPropertyBookActivity extends Activity 
	implements PBICReceiver, OnClickListener
{
	
	private StartImportFragment sif;
	private ArrayList<Integer> importSheetIndexes;
    private Uri importUri;
	
	private ArrayAdapter<String> adapter;
	private ArrayList<String> progressUpdates;
	
	private Intent pbicImportIntent;
	private boolean importStarted;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Get the progress spinner for the activity
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		setContentView(R.layout.activity_import_property_book);
		
		importSheetIndexes = new ArrayList<Integer>();
		
		/*
		 * Set our listView to recieve updates from the service
		 */
		progressUpdates = new ArrayList<String>();
		adapter = 
				new ArrayAdapter<String>(this, 
						android.R.layout.simple_list_item_1,
						progressUpdates);
		
		
		ListView lv = (ListView)findViewById(R.id.import_status_log);
		lv.setAdapter(adapter);
		
		//Set ourselves up to catch the OK/Cancel button up
		Button btnOkCancel = (Button)this.findViewById(R.id.button_import_ok_cancel);
		btnOkCancel.setOnClickListener(this);
		
		FragmentManager fm = getFragmentManager();
		sif = new StartImportFragment();
		
		sif.show(fm, "SELECT_PROPERTY_BOOK");
		
	}

	/**
	 * Backward-compatible version of {@link ActionBar#getThemedContext()} that
	 * simply returns the {@link android.app.Activity} if
	 * <code>getThemedContext</code> is unavailable.
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private Context getActionBarThemedContextCompat() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return getActionBar().getThemedContext();
		} else {
			return this;
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {

	}
	
	public void onActivityResult(
			int requestCode, 
			int resultCode, 
			Intent data) 
	{
		if(requestCode==StartImportFragment.REQUEST_PROPERTY_BOOK_FILE) {
			if(resultCode == Activity.RESULT_OK) {
                onImportFileSelected(data.getData());
			}
		}
	}

	public void onImportFileSelected(Uri uri) {
		sif.dismiss();
        importUri = uri;
		
		//validate that the file exists
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select PBICs");
        builder.setPositiveButton(R.string.pbic_select_button,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startPBICImport();
                    }
                });

        builder.setMultiChoiceItems(getPBICSheets(uri), null,
                new DialogInterface.OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                            int which,
                            boolean isChecked) {
                        if(isChecked) {
                            importSheetIndexes.add(Integer.valueOf(which));
                        }
                        else if(
                            importSheetIndexes.contains(
                                    Integer.valueOf(which)))
                        {
                            importSheetIndexes.remove(
                                    Integer.valueOf(which));
                        }
                    }
                });

        builder.show();
	}
	
	private String[] getPBICSheets(Uri uri) {
		Workbook wb;
		String[] sheetNames = {"No sheets found"};
		try {
            InputStream inStream = getContentResolver().openInputStream(uri);
			wb = Workbook.getWorkbook(inStream);
   			sheetNames = wb.getSheetNames();
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sheetNames;
	}

	private void startPBICImport() {

        int[] importSheetsArray = new int[importSheetIndexes.size()];
        for(int x = 0; x<importSheetIndexes.size(); x++) {
            importSheetsArray[x] = (int)importSheetIndexes.get(x);
        }


		pbicImportIntent = new Intent(this, PBICImportService.class);
		pbicImportIntent.putExtra(PBICImportService.SOURCE_URI_KEY, importUri.toString());
		pbicImportIntent.putExtra(PBICImportService.FILE_SHEET_INDEX_KEY, importSheetsArray);
		pbicImportIntent.putExtra(PBICImportService.EMPTY_DATABASE_KEY, true);
		
		ImportServiceReceiver receiver = new ImportServiceReceiver(new Handler());
		receiver.setReceiver(this);
		
		pbicImportIntent.putExtra(PBICImportService.RECEIVER_KEY, receiver);
		
		setProgressBarIndeterminateVisibility(true);
		
		progressUpdates.add("Starting Import");
		adapter.notifyDataSetChanged();
		
		importStarted = true;
		Button okCancel = (Button)findViewById(R.id.button_import_ok_cancel);
		okCancel.setText(R.string.button_cancel);
		
		startService(pbicImportIntent);
	}
	
	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		if(resultCode == PBICImportService.RESULT_OK ||
				resultCode == PBICImportService.RESULT_ERROR) {
			setProgressBarIndeterminateVisibility(false);
			importStarted = false;
			Button okCancel = (Button)findViewById(R.id.button_import_ok_cancel);
			okCancel.setText(R.string.button_ok);
		}
		progressUpdates.add(resultData.getString(PBICImportService.RESULT_STRING));
		adapter.notifyDataSetChanged();
	}

	/**
	 * Receive and direct button click events from the activity
	 */
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.button_import_ok_cancel:
			if(importStarted) {
				cancelImport();
			} else {
				closeActivity();
			}
				
			break;
		}
		
	}
	
	/**
	 * Stop the PBICImportService while it is running.
	 */
	private void cancelImport() {
		if(pbicImportIntent != null) {
			stopService(pbicImportIntent);
			importStarted = false;
		}
	}
	
	private void closeActivity() {
		this.finish();
	}
	
	

}
