package com.NortrupDevelopment.PropertyBook.presenter;

import com.NortrupDevelopment.PropertyBook.bus.DisplayBrowserEvent;
import com.NortrupDevelopment.PropertyBook.bus.DisplayLineNumberDetailEvent;
import com.NortrupDevelopment.PropertyBook.bus.FileSelectRequestedEvent;
import com.NortrupDevelopment.PropertyBook.bus.ImportCompleteEvent;
import com.NortrupDevelopment.PropertyBook.bus.ImportRequestedEvent;
import com.NortrupDevelopment.PropertyBook.bus.SearchRequestedEvent;

/**
 * Created by andy on 2/7/15.
 */
public interface MainActivity
{
  /**
   * Handle Requests to view the details of a LIN
   * @param event
   */
  void onEvent(DisplayLineNumberDetailEvent event);

  /**
   * Request to import a new property book
   * @param event
   */
  void onEvent(ImportRequestedEvent event);

  /**
   * Notification that an import is complete
   * @param event
   */
  void onEvent(ImportCompleteEvent event);

  /**
   * Handle a request to search for the
   * @param event
   */
  void onEvent(SearchRequestedEvent event);

  /**
   * Handle an event directing the activity to display the LIN Browser
   * @param event
   */
  void onEvent(DisplayBrowserEvent event);

  void onEvent(FileSelectRequestedEvent event);

  void showImport();
}
