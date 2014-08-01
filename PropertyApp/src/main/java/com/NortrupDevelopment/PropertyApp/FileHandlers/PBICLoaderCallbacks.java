package com.NortrupDevelopment.PropertyApp.FileHandlers;

/**
 * Callback for the PBICLoader to return results back to the originator.
 * Created by andy on 7/31/14.
 */
public interface PBICLoaderCallbacks {

  /**
   * Sends an array of strings from the AsyncTask back to the callback.
   * @param result String array of PBIC sheet names.
   */
  public void receivePBICList(String[] result);
}
