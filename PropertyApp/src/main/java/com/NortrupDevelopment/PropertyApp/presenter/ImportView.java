package com.NortrupDevelopment.PropertyApp.presenter;

/**
 * Interface allowing for communcation between the ImportPresenter and
 * an implmenting view.
 * Created by andy on 7/23/14.
 */
public interface ImportView {

  public void closeView();

  /**
   * Called when the interface must display an intent to allow the user to
   * select a file.
   */
  public void showFileSelectIntent();

  /**
   * Displays the PBIC List.
   */
  public void showPBICSelect();

  /**
   * Direct the view to display the name of the selected file.
   */
  public void setFileNameView(String fileNameView, boolean goodFile);

  /**
   * Directs the view to add a PBIC to the list
   */
  public void addPBIC(int index, String pbicName);
}
