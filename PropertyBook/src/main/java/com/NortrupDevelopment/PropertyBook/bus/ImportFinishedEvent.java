package com.NortrupDevelopment.PropertyBook.bus;

/**
 * Otto event used to notify the bus that the Import has completed.
 * Created by andy on 8/5/14.
 */
public class ImportFinishedEvent {
  private final int mStatus;

  /**
   * Create an ImportFinishedEvent
   * @param status Status code of the import. See: PropertyBookImporter for
   *               values.
   */
  public ImportFinishedEvent(int status) {
    mStatus = status;
  }

  public int getStatus() {
    return mStatus;
  }
}
