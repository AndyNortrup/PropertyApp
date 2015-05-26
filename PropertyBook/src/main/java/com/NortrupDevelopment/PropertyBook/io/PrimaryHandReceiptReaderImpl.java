package com.NortrupDevelopment.PropertyBook.io;


import android.text.TextUtils;
import android.util.Log;

import com.NortrupDevelopment.PropertyBook.dao.LineNumber;
import com.NortrupDevelopment.PropertyBook.dao.PropertyBook;
import com.NortrupDevelopment.PropertyBook.dao.SerialNumber;
import com.NortrupDevelopment.PropertyBook.dao.StockNumber;
import com.NortrupDevelopment.PropertyBook.model.ModelFactory;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.Colour;
import jxl.read.biff.BiffException;

public class PrimaryHandReceiptReaderImpl implements PrimaryHandReceiptReader {

  //Row where description of the property book is found
  private static final int propertyBookDescriptionRow = 1;
  private static final int propertyBookDescriptionCell = 0;

  //All of the column numbers where information for a LIN can be found
  private static final int firstLINRowNumber = 8;
  private static final int colNumberLIN = 0;
  private static final int columnNumberLinSubLin = 1;
  private static final int columnNumberLinSri = 2;
  private static final int columnNumberLinSubErc = 3;
  private static final int columnNumberLinNomenclature = 4;
  private static final int columnNumberLinAuthDoc = 9;
  private static final int columnNumberLinRequired = 12;
  private static final int columnNumberLinAuthorized = 13;
  private static final int columnNumberLinDI = 16;

  //All of the column numbers where information can be found
  private static final int columnNumberNSN = 0;
  private static final int columnNumberNsnUi = 2;
  private static final int columnNumberNsnUp = 3;
  private static final int columnNumberNsnNomenclature = 4;
  private static final int columnNumberNsnLLC = 8;
  private static final int columnNumberNsnECS = 9;
  private static final int columnNumberNsnSRRC = 10;
  private static final int columnNumberNsnCiic = 12;
  private static final int columnNumberNsnDLA = 13;
  private static final int columnNumberNsnUiiManaged = 11;
  private static final int columnNumberNsnPubData = 14;
  private static final int columnNumberNsnOH = 16;

  //Serial numbers are listed in columns B, F, J & N
  private static final int[] columnNumberSerialNumbers = {1, 5, 9, 13};

  //REGEX to vet a LIN number 9, alphanumeric characters
  private static final String regexLIN = "[A-Z0-9]{6}";
  //REGEX to identify a NSN 13 alphanumeric characters
  private static final String regexNSN = "[A-Z0-9]{13}";

  ModelFactory mModelFactory;

  LineNumber currentLineNumber;
  StockNumber currentStockNumber;

  @Inject
  public PrimaryHandReceiptReaderImpl(ModelFactory modelFactory) {
    mModelFactory = modelFactory;
  }

  /**
   * Imports the property book PBIC sheet specified by the path and sheet
   * index into an ArrayList of LIN objects.
   *
   * @param inFile Reference to the file to be imported
   * @param sheets Array of the indexs of the sheets (PBIC) to be opened
   * @return ArrayList of LIN objects
   */
  @Override
  public List<PropertyBook> readHandReceipt(InputStream inFile,
                                            int[] sheets)
      throws BiffException,
      IOException {

    Workbook workbook;

    try {
      workbook = Workbook.getWorkbook(inFile);
    } catch (BiffException biff) {
      Log.e("HandReceiptReader", biff.getMessage());
      throw biff;
    } catch (IOException ioe) {
      Log.e("HandReceiptReader", ioe.getMessage());
      throw ioe;
    }

    ArrayList<PropertyBook> result = new ArrayList<>(sheets.length);

    for (int sheetIndex = 0; sheetIndex < sheets.length; sheetIndex++) {

      Sheet sheet = workbook.getSheet(sheets[sheetIndex]);

      boolean checkForSerialNumbers = false;

      PropertyBook pbic = getPropertyBook(sheet);
      result.add(pbic);
      //skip past the header data
      int currentRowNumber = firstLINRowNumber;

      while (currentRowNumber < sheet.getRows()) {

        //LIN's match the REGEX regexLIN
        if (isLineNumber(sheet.getCell(colNumberLIN, currentRowNumber))) {
          currentLineNumber = createLineNumberFrom(sheet,
              currentRowNumber,
              pbic);
        }
        //Sub LIN has a LIN number in the second column and has a grey
        // background like a LIN.
        else if (isLineNumber(
            sheet.getCell(columnNumberLinSubLin, currentRowNumber))) {
          currentLineNumber = createSubLineNumberFrom(sheet, currentRowNumber);
        }
        /**
         * NSNs lines are defined by having an NSN (13 digit alpha numeric
         * in the first column (cellZero)
         */
        else if (isStockNumber(
            sheet.getCell(colNumberLIN, currentRowNumber))) {
          currentStockNumber = createStockNumber(sheet, currentRowNumber);
          currentLineNumber.getStockNumbers().add(currentStockNumber);
          currentStockNumber.setParentLineNumber(currentLineNumber);

          checkForSerialNumbers = true;

          /**
           * If we have found an NSN on the prior row, we check for
           * serial numbers.
           */
        } else if (checkForSerialNumbers) {

          /**
           * Loop through our four cells that should have serial numbers.
           * Check if they have contents.  If they do, create an item,
           * and add it to the current NSN.
           *
           * System numbers are in the cell immediately to the left (-1)
           */
          for (int x = 0; x < columnNumberSerialNumbers.length; x++) {
            if (!TextUtils.isEmpty(
                sheet.getCell(columnNumberSerialNumbers[x],
                    currentRowNumber)
                    .getContents())) {

              SerialNumber sn = createSerialNumber(sheet,
                  currentRowNumber,
                  columnNumberSerialNumbers[x]);

              sn.setStockNumber(currentStockNumber);
              currentStockNumber.getSerialNumbers().add(sn);

            } else {
              /**
               * Serial numbers always populate full rows from
               * left to right, if there is a blank cell than
               * there are no more serial numbers.
               *
               * Therefore pop ourselves out of the loop
               */
              x = columnNumberSerialNumbers.length;
              checkForSerialNumbers = false;
            }
          }


        }

        //Advance to next row
        currentRowNumber++;
      }
    }

    workbook.close();
    return result;

  }

  private boolean isStockNumber(Cell cell) {
    return (cell.getContents().matches(regexNSN) &&
        cell.getCellFormat().getBorder(Border.TOP).getValue() == 1 &&
        cell.getCellFormat().getBorder(Border.BOTTOM).getValue() == 1);
  }

  private boolean isLineNumber(Cell cell) {
    return (cell.getContents().matches(regexLIN) &&
        cell.getCellFormat().getBackgroundColour() ==
            Colour.GREY_25_PERCENT);
  }

  private PropertyBook getPropertyBook(Sheet sheet) {

    String pbic = sheet.getName();
    String pbicDescrip = sheet.getCell(0, 0).getContents();

    //Pull property book description information
    Cell descriptionCell = sheet.getCell(
        propertyBookDescriptionCell,
        propertyBookDescriptionRow);

    String[] descriptionPieces = descriptionCell.getContents().split("/");

    String uic = descriptionPieces[3].trim();
    String unitName = descriptionPieces[4].trim();

    return createPropertyBook(pbic, pbicDescrip, uic, unitName);
  }

  private PropertyBook createPropertyBook(
      String pbic,
      String pbicDescription,
      String uic,
      String unitName) {
    return mModelFactory.createOrphanPropertyBook(pbic, pbicDescription, uic, unitName);
  }

  private LineNumber createLineNumberFrom(Sheet sheet,
                                          int currentRowNumber,
                                          PropertyBook pb) {

    //test if numeric conversions are valid for Required and Authorized
    int required = 0;
    int authorized = 0;
    int dueIn = 0;

    if (!sheet.getCell(columnNumberLinAuthorized,
        currentRowNumber).getContents().equals("")) {
      authorized = Integer.parseInt(
          sheet.getCell(columnNumberLinAuthorized,
              currentRowNumber)
              .getContents());
    }

    if (!sheet.getCell(columnNumberLinRequired,
        currentRowNumber).getContents().equals("")) {
      required = Integer.parseInt(
          sheet.getCell(columnNumberLinRequired,
              currentRowNumber)
              .getContents());
    }

    if (!sheet.getCell(columnNumberLinDI,
        currentRowNumber).getContents().equals("")) {
      dueIn = Integer.parseInt(
          sheet.getCell(columnNumberLinDI,
              currentRowNumber)
              .getContents());
    }

    LineNumber lineNumber = mModelFactory.createOrphanLineNumber(
        sheet.getCell(colNumberLIN, currentRowNumber).getContents(),
        sheet.getCell(columnNumberLinSubLin, currentRowNumber).getContents(),
        sheet.getCell(columnNumberLinSri, currentRowNumber).getContents(),
        sheet.getCell(columnNumberLinSubErc, currentRowNumber).getContents(),
        sheet.getCell(columnNumberLinNomenclature, currentRowNumber).getContents(),
        sheet.getCell(columnNumberLinAuthDoc, currentRowNumber).getContents(),
        authorized,
        required,
        dueIn);

    pb.getLineNumbers().add(lineNumber);
    lineNumber.setPropertyBook(pb);

    return lineNumber;
  }

  private LineNumber createSubLineNumberFrom(Sheet sheet,
                                             int currentRowNumber) {

    LineNumber lineNumber = mModelFactory.createOrphanLineNumber(currentLineNumber.getLin(),
        sheet.getCell(columnNumberLinSubLin, currentRowNumber).getContents(),
        currentLineNumber.getSri(),
        currentLineNumber.getErc(),
        currentLineNumber.getNomenclature(),
        currentLineNumber.getAuthDoc(),
        currentLineNumber.getAuthorized(),
        currentLineNumber.getRequired(),
        currentLineNumber.getDueIn());

    currentLineNumber.getPropertyBook().getLineNumbers().add(lineNumber);
    lineNumber.setPropertyBook(currentLineNumber.getPropertyBook());
    return lineNumber;
  }

  /**
   * Creates a stock number based on the information in the sheet and row
   * indicated.  The parent child relationship is set with the Line Number as
   * well.
   *
   * @param sheet            Excel sheet containing the data.
   * @param currentRowNumber Number of the row in the sheet containg the data
   * @return
   */
  private StockNumber createStockNumber(Sheet sheet,
                                        int currentRowNumber) {

    BigDecimal unitPrice = new BigDecimal(0);
    if (!TextUtils.isEmpty(
        sheet.getCell(columnNumberNsnUp, currentRowNumber).getContents())) {

      unitPrice = new BigDecimal(Double.parseDouble(
          sheet.getCell(columnNumberNsnUp, currentRowNumber)
              .getContents()));
    }

    int onHand = 0;
    String onHandString =
        sheet.getCell(columnNumberNsnOH, currentRowNumber)
            .getContents();

    if (!TextUtils.isEmpty(onHandString)) {
      onHand = Integer.parseInt(onHandString);
    }

    StockNumber stockNumber = mModelFactory.createOrphanStockNumber(
        sheet.getCell(columnNumberNSN, currentRowNumber).getContents(),
        sheet.getCell(columnNumberNsnUi, currentRowNumber).getContents(),
        unitPrice,
        sheet.getCell(
            columnNumberNsnNomenclature, currentRowNumber).getContents(),
        sheet.getCell(columnNumberNsnLLC, currentRowNumber).getContents(),
        sheet.getCell(columnNumberNsnECS, currentRowNumber).getContents(),
        sheet.getCell(columnNumberNsnSRRC, currentRowNumber).getContents(),
        sheet.getCell(columnNumberNsnUiiManaged, currentRowNumber).getContents(),
        sheet.getCell(columnNumberNsnCiic, currentRowNumber).getContents(),
        sheet.getCell(columnNumberNsnDLA, currentRowNumber).getContents(),
        sheet.getCell(columnNumberNsnPubData, currentRowNumber).getContents(),
        onHand);

    stockNumber.setParentLineNumber(currentLineNumber);
    return stockNumber;
  }

  private SerialNumber createSerialNumber(Sheet sheet,
                                          int currentRowNumber,
                                          int columnIndex) {
    SerialNumber sn = mModelFactory.createOrphanSerialNumber(
        sheet.getCell(columnIndex, currentRowNumber).getContents(),
        sheet.getCell(columnIndex - 1, currentRowNumber).getContents());

    sn.setStockNumber(currentStockNumber);
    return sn;
  }

  @Override
  public String[] getPBICList(InputStream fStream)
      throws IOException, BiffException {
    String[] pbics;

    try {
      Workbook workbook = Workbook.getWorkbook(fStream);
      pbics = workbook.getSheetNames();
    } catch (BiffException biff) {
      throw biff;
    } catch (IOException ioe) {
      throw ioe;
    }
    return pbics;
  }


}
