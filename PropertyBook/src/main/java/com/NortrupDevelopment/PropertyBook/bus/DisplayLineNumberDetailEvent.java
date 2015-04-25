package com.NortrupDevelopment.PropertyBook.bus;

import com.NortrupDevelopment.PropertyBook.dao.LineNumber;

/**
 * Created by andy on 2/7/15.
 */
public interface DisplayLineNumberDetailEvent {
  LineNumber getRequestedLIN();
}
