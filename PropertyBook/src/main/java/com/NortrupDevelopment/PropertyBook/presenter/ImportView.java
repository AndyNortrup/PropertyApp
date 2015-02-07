package com.NortrupDevelopment.PropertyBook.presenter;

import android.content.Context;
import android.net.Uri;

import com.NortrupDevelopment.PropertyBook.presenter.ImportPresenter;

/**
 * Interface allowing for communcation between the ImportPresenter and
 * an implmenting view.
 * Created by andy on 7/23/14.
 */
public interface ImportView {

  /**
   * Called when the interface must display an intent to allow the user to
   * select a file.
   */
  public void showFileSelectIntent();

  /**
   * Displays the pbicTV List.
   */
  public void showPBICSelect();

  /**
   * Direct the view to display the name of the selected file.
   */
  public void setFileNameView(String fileNameView, boolean goodFile);

  /**
   * Directs the view to add a pbicTV to the list
   */
  public void addPBIC(int index, String pbicName);

  /**
   * Used by the presenter to enable or disable the Import Button.
   * @param state True if the import button should be enabled.
   */
  public void setImportButtonEnabled(boolean state);

  /**
   * Directs the view to display the ImportProgress Task.
   */
  public void showImportProgress();

  /**
   * Provide the presenter with a current copy of the context.
   */
  public Context getContext();

  /**
   * Return a copy of the Activity's presenter
   * @return The presenter for this activity.
   */
  public ImportPresenter getPresenter();

  /**
   * Instructs the view to start an ImportTaskFragment
   */
  public void startImportFragment(Uri file,
                                  int[] sheets,
                                  boolean emptyDatabase);

}
