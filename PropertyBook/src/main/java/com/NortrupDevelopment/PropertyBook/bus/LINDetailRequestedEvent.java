package com.NortrupDevelopment.PropertyBook.bus;

import com.NortrupDevelopment.PropertyBook.model.LineNumber;

/**
 * Created by andy on 12/15/14.
 */
public class LINDetailRequestedEvent {

  LineNumber mRequestedLIN;
  public LINDetailRequestedEvent(LineNumber requestedLIN) {
    mRequestedLIN = requestedLIN;
  }

  public LineNumber getRequestedLIN() {
    return mRequestedLIN;
  }
}
