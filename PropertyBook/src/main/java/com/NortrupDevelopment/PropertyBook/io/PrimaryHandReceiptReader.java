package com.NortrupDevelopment.PropertyBook.io;

import com.NortrupDevelopment.PropertyBook.dao.PropertyBook;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import jxl.read.biff.BiffException;

/**
 * Created by andy on 2/26/15.
 */
public interface PrimaryHandReceiptReader {
  List<PropertyBook> readHandReceipt(InputStream inFile,
                                     int[] sheets)
      throws BiffException,
      IOException;

  String[] getPBICList(InputStream fStream)
      throws IOException, BiffException;
}
