package com.NortrupDevelopment.PropertyBook.io;

import android.content.Context;
import android.net.Uri;

import java.io.IOException;

import javax.inject.Inject;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import rx.Observable;

/**
 * PBIC Name Extractor is used to extract the names of PBICs that can be imported
 * from a given file.
 * Created by andy on 5/18/15.
 */
public class PBICNameReader {

  private final Context mContext;

  @Inject
  public PBICNameReader(Context context) {
    mContext = context;
  }

  /**
   * Read the list of PBICs in the given spreadsheet and return a list of their
   * names.
   *
   * @param uriPath A URI string pointing to the file to be read
   * @return Array of strings listing the names of the sheets in the workbook
   */
  public Observable<String[]> getSheetNamesFrom(Uri uriPath) {
    try {
      return Observable.just(getSheetNames(uriPath));
    } catch (Exception e) {
      return Observable.error(e);
    }
  }

  private String[] getSheetNames(Uri uri) throws IOException, BiffException {

    Workbook workbook;
    String[] result;

    workbook = Workbook.getWorkbook(
        mContext.getContentResolver().openInputStream(uri));
    result = workbook.getSheetNames();
    return result;
  }
}
