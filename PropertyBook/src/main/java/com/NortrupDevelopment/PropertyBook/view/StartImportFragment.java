package com.NortrupDevelopment.PropertyBook.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.MimeTypeMap;

import com.NortrupDevelopment.PropertyBook.R;

//import com.ipaulpro.afilechooser.utils.FileUtils;


public class StartImportFragment extends DialogFragment 
	implements OnClickListener 
{

	public static final int REQUEST_PROPERTY_BOOK_FILE = 0;
		 
	 @Override
	 public Dialog onCreateDialog(Bundle savedInstanceData) {
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		 builder.setTitle("Import a property book")
		 	.setMessage(R.string.dialog_import_property_book)
		 	.setPositiveButton(R.string.start_import_fragment_button_text, this);
		 
		 return builder.create();
		 
	 }

	 /**
	  * Sends the user to the file picker to select a file.
	  */
	 public void pickFile() {
         Intent chooseFile;
         Intent intent;
         chooseFile = new Intent(Intent.ACTION_GET_CONTENT);

         MimeTypeMap mime = MimeTypeMap.getSingleton();
         chooseFile.setType(mime.getMimeTypeFromExtension("xls"));

         intent = Intent.createChooser(chooseFile, "Choose a file");
         getActivity().startActivityForResult(intent, REQUEST_PROPERTY_BOOK_FILE);

	 }

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		if(arg1 == DialogInterface.BUTTON_POSITIVE) {
			pickFile();
		}
	}
}
