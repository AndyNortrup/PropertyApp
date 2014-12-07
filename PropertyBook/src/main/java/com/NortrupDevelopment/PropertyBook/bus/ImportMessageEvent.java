package com.NortrupDevelopment.PropertyBook.bus;

/**
 * Otto event used to send an updated Message about the import progress.
 * Created by andy on 8/5/14.
 */
public class ImportMessageEvent {
  private final String mMessage;

  public ImportMessageEvent(String message) {
    mMessage = message;
  }

  public String getMessage() {
    return mMessage;
  }
}
