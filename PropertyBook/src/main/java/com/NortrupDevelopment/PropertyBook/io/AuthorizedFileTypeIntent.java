package com.NortrupDevelopment.PropertyBook.io;

import android.content.Intent;

/**
 * An intent that only allows the user to select files of the .xls type which
 * we can open to import to the property book.
 * Created by andy on 5/10/15.
 */
public class AuthorizedFileTypeIntent extends Intent {

  public final static String sType = "application/vnd.ms-excel";

  public AuthorizedFileTypeIntent() {
    this.setType(sType);
    this.setAction(Intent.ACTION_GET_CONTENT);
  }
}
