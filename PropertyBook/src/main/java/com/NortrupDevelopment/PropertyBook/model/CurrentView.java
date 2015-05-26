package com.NortrupDevelopment.PropertyBook.model;

/**
 * Singleton to store which view is currently displayed
 * Created by andy on 1/9/15.
 */
public class CurrentView {

  public static final int SCREEN_BROWSE = 1;
  public static final int SCREEN_DETAIL = 2;
  public static final int SCREEN_IMPORT = 3;
  public static final int SCREEN_SEARCH = 4;

  private int currentScreen;

  private static CurrentView instance;

  /**
   * Private constructor
   */
  private CurrentView(int startingScreen) {
    currentScreen = startingScreen;
  }

  public static CurrentView getInstance() {
    if(instance == null) {
      instance = new CurrentView(SCREEN_BROWSE);
    }
    return instance;
  }

  public int getCurrentScreen() {
    return currentScreen;
  }

  public void setCurrentScreen(int currentScreen) {
    this.currentScreen = currentScreen;
  }
}
