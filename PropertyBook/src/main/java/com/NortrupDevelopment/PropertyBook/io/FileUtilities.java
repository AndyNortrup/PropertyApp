package com.NortrupDevelopment.PropertyBook.io;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.io.File;

import javax.inject.Inject;

/**
 * Created by andy on 5/14/15.
 */
public class FileUtilities {

  Context mContext;

  @Inject
  public FileUtilities(Context context) {
    mContext = context;
  }

  /**
   * Extracts the file name from the URI path for easy display
   *
   * @param fileUri URI to extract data from
   * @return Just the file name from the path in the URI
   */
  public String extractFileNameFrom(Uri fileUri) {

    if (fileUri.toString().contains("file:///")) {
      return fileNameFromPath(fileUri.getPath());
    } else {
      return fileNameFromContentUri(fileUri);
    }
  }

  private String fileNameFromContentUri(Uri fileUri) {
    Cursor fileInformation = mContext.getContentResolver()
        .query(fileUri, new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null);
    fileInformation.moveToFirst();
    return fileInformation.getString(0);
  }

  private String fileNameFromPath(String path) {
    File file = new File(path);
    return file.getName();
  }

  public boolean isValidImportFile(Uri uri) {
    return (mContext.getContentResolver()
        .getType(uri).compareTo(AuthorizedFileTypeIntent.sType) == 0);

  }

}
