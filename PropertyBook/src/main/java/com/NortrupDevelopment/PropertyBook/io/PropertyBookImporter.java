package com.NortrupDevelopment.PropertyBook.io;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.NortrupDevelopment.PropertyBook.dao.PropertyBook;
import com.NortrupDevelopment.PropertyBook.model.ModelUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;

import jxl.read.biff.BiffException;
import rx.Observable;

/**
 * Created by andy on 8/1/14.
 */
public class PropertyBookImporter {

  private static final String DEBUG_CODE = "PBIC_IMPORTER";

  Context mContext;
  ModelUtils mModelUtils;
  PrimaryHandReceiptReader mHandReceiptReader;

  @Inject
  public PropertyBookImporter(Context context,
                              ModelUtils modelUtils,
                              PrimaryHandReceiptReader reader) {

    mContext = context;
    mModelUtils = modelUtils;
    mHandReceiptReader = reader;
  }

  public Observable<String> importPropertyBook(Uri file, int[] sheets, boolean deleteOld) {
    try {

      return Observable.just(readInputFile(sheets, file, deleteOld));

    } catch (Exception e) {
      Log.e(DEBUG_CODE, e.getMessage(), e);
      return Observable.error(e);
    }
  }

  /**
   * Reads the contents of a file into model objects.
   *
   * @param sheetIndexes An array of integers indicating the indexes of sheets
   *                     to be read from the xls file.
   * @throws jxl.read.biff.BiffException Thrown if there are errors reading the XLS format.
   * @throws java.io.IOException         Thrown if there are errors reading the file.
   */
  private String readInputFile(int[] sheetIndexes, Uri uri, boolean deleteOld)
      throws BiffException, IOException {

    InputStream inStream =
        mContext.getContentResolver().openInputStream(uri);

    if (deleteOld) {
      deleteCurrentData();
    }

    Log.i(DEBUG_CODE, "Starting to read property book.");
    List<PropertyBook> propertyBooks = mHandReceiptReader.readHandReceipt(
        inStream,
        sheetIndexes);

    for (PropertyBook pb : propertyBooks) {
      mModelUtils.insertPropertyBook(pb);
    }

    inStream.close();
    return "All Property Books imported.";
  }

  private void deleteCurrentData() {
    //Clear the contents of all tables
    mModelUtils.deleteAllPropertyBooks();

    Log.i(DEBUG_CODE, "Added removal old data removal commands.");
  }
}
