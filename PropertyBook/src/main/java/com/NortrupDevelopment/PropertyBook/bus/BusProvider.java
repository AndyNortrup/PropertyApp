package com.NortrupDevelopment.PropertyBook.bus;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Singleton method for access to our otto Bus.
 * Created by andy on 8/5/14.
 */
public class BusProvider {

  private static Bus BUS;
  private BusProvider() {
    //Prevent instantiation
  }

  public static Bus getBus() {

    if(BUS == null) {
      BUS = new Bus(ThreadEnforcer.MAIN);
    }
    return BUS;
  }
}
