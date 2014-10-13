package com.NortrupDevelopment.PropertyBook.util;

/**
 * Adds dashes to a NSN in order to show a human readable NSN in the
 * format 1234-56-789-0123 rather than 1234567890123.
 *
 * Created by andy on 10/13/14.
 */
public class NSNFormatter {

  /**
   * Reformats the given string with dashes.
   * @param nsn
   * @return input nsn with dashes
   */
  public static String getFormattedNSN(String nsn) {
    final String DASH = "-";
    StringBuilder sb = new StringBuilder(nsn);
    sb.insert(4, DASH);
    sb.insert(7, DASH);
    sb.insert(11, DASH);
    return sb.toString();
  }
}
