package com.NortrupDevelopment.PropertyBook.bus;

/**
 * Event object sent to MainActivity to direct it to display the LIN Browser
 * Created by andy on 2/7/15.
 */
public class DefaultDisplayBrowserEvent implements DisplayBrowserEvent {
  public DefaultDisplayBrowserEvent() {};

  @Override
  public int getScrollPosition() {
    return 0;
  }
}
