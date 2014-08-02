package com.NortrupDevelopment.PropertyBook.io;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Provides the task with all of the required parameters to execute.
 */
public class ImportParameters implements Parcelable,
    Parcelable.Creator<ImportParameters> {

  private Uri mFile;
  private int[] mSheets;
  private boolean mEmptyDatabase;

  public ImportParameters(Uri filePath, int[] sheets, boolean emptyDatabase) {
    mFile = filePath;
    mSheets = sheets;
    mEmptyDatabase = emptyDatabase;
  }

  public ImportParameters(Parcel dest) {
    mFile = Uri.parse(dest.readString());
    dest.readIntArray(mSheets);
    mEmptyDatabase = dest.readByte() !=0;
  }

  public Uri getFile() {
    return mFile;
  }

  public int[] getSheets() {
    return mSheets;
  }

  public boolean isEmptyDatabase() {
    return mEmptyDatabase;
  }

  /**
   * Describe the kinds of special objects contained in this Parcelable's
   * marshalled representation.
   *
   * @return a bitmask indicating the set of special object types marshalled
   * by the Parcelable.
   */
  @Override
  public int describeContents() {
    return 0;
  }

  /**
   * Flatten this object in to a Parcel.
   *
   * @param dest  The Parcel in which the object should be written.
   * @param flags Additional flags about how the object should be written.
   *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
   */
  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(mFile.toString());
    dest.writeIntArray(mSheets);
    dest.writeByte((byte)(mEmptyDatabase ? 1 : 0));
  }

  /**
   * Create a new instance of the Parcelable class, instantiating it
   * from the given Parcel whose data had previously been written by
   * {@link android.os.Parcelable#writeToParcel Parcelable.writeToParcel()}.
   *
   * @param source The Parcel to read the object's data from.
   * @return Returns a new instance of the Parcelable class.
   */
  @Override
  public ImportParameters createFromParcel(Parcel source) {
    return new ImportParameters(source);
  }

  /**
   * Create a new array of the Parcelable class.
   *
   * @param size Size of the array.
   * @return Returns an array of the Parcelable class, with every entry
   * initialized to null.
   */
  @Override
  public ImportParameters[] newArray(int size) {
    return new ImportParameters[0];
  }
}