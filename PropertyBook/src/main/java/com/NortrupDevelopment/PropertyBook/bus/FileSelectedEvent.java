package com.NortrupDevelopment.PropertyBook.bus;

import android.net.Uri;

/**
 * Class used to pass the results of an ACTION_GET_CONTENT intent back to another
 * part of the app (most likely the import screen).
 * Created by andy on 5/14/15.
 */
public class FileSelectedEvent {
  private Uri mUri;

  public FileSelectedEvent(Uri data) {
    mUri = data;
  }

  public Uri getUri() {
    return mUri;
  }
}
