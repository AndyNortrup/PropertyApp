package com.NortrupDevelopment.PropertyBook.bus;

import com.NortrupDevelopment.PropertyBook.dao.LineNumber;

/**
 * Created by andy on 12/15/14.
 */
public class DefaultLineNumberDetailEvent
    implements DisplayLineNumberDetailEvent {

  LineNumber mRequestedLIN;

  public DefaultLineNumberDetailEvent(LineNumber requestedLIN) {
    mRequestedLIN = requestedLIN;
  }

  @Override
  public LineNumber getRequestedLIN() {
    return mRequestedLIN;
  }
}
