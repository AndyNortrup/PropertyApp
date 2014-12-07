package com.NortrupDevelopment.PropertyBook.io;


import android.content.Context;
import android.text.TextUtils;

import com.NortrupDevelopment.PropertyBook.model.LineNumber;
import com.NortrupDevelopment.PropertyBook.model.PropertyBook;
import com.NortrupDevelopment.PropertyBook.model.RealmDefinition;
import com.NortrupDevelopment.PropertyBook.model.SerialNumber;
import com.NortrupDevelopment.PropertyBook.model.StockNumber;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.realm.Realm;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class PrimaryHandReceiptReader {

	//Row where description of the property book is found
	private static final int propertyBookDescriptionRow = 1;
	private static final int propertyBookDescriptionCell = 0;
	
	//All of the column numbers where information for a LIN can be found
    private static final int firstLINRowNumber = 8;
    private static final int colNumberLIN = 0;
    private static final int columnNumberLinSubLin= 1;
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
    //private static final int columnNumberSnB = 1;
    //private static final int columnNumberSnF = 5;
    //private static final int columnNumberSnJ = 9;
    //private static final int columnNumberSnN = 13;
    private static final int[] columnNumberSerialNumbers = {1, 5, 9, 13};

    //REGEX to vet a LIN number 9, alphanumeric characters
    private static final String regexLIN = "[A-Z0-9]{6}";
    //REGEX to identify a NSN 13 alphanumeric characters
    private static final String regexNSN = "[A-Z0-9]{13}";
  
    //used to determine if the cell is shaded 192,192,192 grey
    private static final int GREY_QUANTITY = 192;  
   
    
    private static final String TEMPFILE = "temp.xls";

    /**
     * Imports the property book PBIC sheet specified by the path and sheet
     * index into an ArrayList of LIN objects.
     * @param inFile Reference to the file to be imported
     * @param sheets Array of the indexs of the sheets (PBIC) to be opened
     * @param context Method needs a context in order to create a temporary file for reading format information
     * 	because JXL does not read any of the format information when in read-only mode.  
     * @return ArrayList of LIN objects
     */
    public static void readHandReceipt(InputStream inFile,
                                int[] sheets,
                                Context context,
                                String realmFile)
        throws BiffException,
            IOException
    {
        Workbook workbook;
        WritableWorkbook copy;
        FileOutputStream tempFile;

      Realm realm = RealmDefinition.getRealm(context, realmFile);
      realm.beginTransaction();

        try{
        	workbook = Workbook.getWorkbook(inFile);
        	tempFile = context.openFileOutput(TEMPFILE, Context.MODE_PRIVATE);
        	copy = Workbook.createWorkbook(tempFile, workbook);
        } catch (BiffException biff) {
            throw biff;
        } catch (IOException ioe) {
            throw ioe;
        }

        for(int sheetIndex = 0; sheetIndex< sheets.length; sheetIndex++) {

            WritableSheet sheet = copy.getSheet(sheets[sheetIndex]);

            //create some markers
            LineNumber currentLineNumber = new LineNumber();
            StockNumber currentStockNumber = new StockNumber();
            boolean checkForSerialNumbers = false;

            //Pull property book description information
            WritableCell descriptionCell = sheet.getWritableCell(
                    propertyBookDescriptionCell,
                    propertyBookDescriptionRow);

            String[] descriptionPieces = descriptionCell.getContents().split("/");

            PropertyBook pbic = createPropertyBook(
                descriptionPieces[3].trim(),
                descriptionPieces[4].trim(),
                realm);

            //skip past the header data
            int currentRowNumber = firstLINRowNumber;

            while(currentRowNumber < sheet.getRows()) {

                //pull off the first and second cells in the row to figure out what the contents of the row are
                WritableCell colZero =
                    sheet.getWritableCell(colNumberLIN, currentRowNumber);

                WritableCell colOne =
                    sheet.getWritableCell(columnNumberLinSubLin,
                        currentRowNumber);

                //LIN's match the REGEX regexLIN and have a background that is grey.
                if(colZero.getContents().matches(regexLIN)
                        &&
                    colZero.getCellFormat().getBackgroundColour().
                        getDefaultRGB().getBlue() == GREY_QUANTITY
                   )
                {

                    //we have found a LIN row turn off check for serial numbers
                  checkForSerialNumbers = false;

                  currentLineNumber = createLineNumber(sheet,
                      currentRowNumber,
                      realm);
                  pbic.getLineNumbers().add(currentLineNumber);
                  currentLineNumber.setPropertyBook(pbic);

                }
                //Sub LIN has a LIN number in the second column and has a grey
                // background like a LIN.
                else if (colOne.getContents().matches(regexLIN) &&
                        colOne.getCellFormat().getBackgroundColour().
                            getDefaultRGB().getBlue() == GREY_QUANTITY) {

                  currentLineNumber = createSubLineNumber(sheet,
                      currentRowNumber,
                      currentLineNumber,
                      realm);

                  pbic.getLineNumbers().add(currentLineNumber);

                }
                /**
                 * NSNs lines are defined by having an NSN (13 digit alpha numeric
                 * in the first column (cellZero) and having a line above and below
                 * the cell.
                 */
                else if(colZero.getContents().matches(regexNSN)) {

                  currentStockNumber = createStockNumber(sheet,
                      currentRowNumber,
                      realm);

                  currentLineNumber.getStockNumbers().add(currentStockNumber);
                  currentStockNumber.setParentLineNumber(currentLineNumber);

                  checkForSerialNumbers = true;


                /**
                 * If we have found an NSN on the prior row, we check for
                 * serial numbers.
                  */
                } else if(checkForSerialNumbers) {

                    /**
                     * Loop through our four cells that should have serial numbers.
                     * Check if they have contents.  If they do, create an item,
                     * and add it to the current NSN.
                     *
                     * System numbers are in the cell immediately to the left (-1)
                     */
                    for(int x = 0; x< columnNumberSerialNumbers.length; x++) {
                        if(!TextUtils.isEmpty(
                                sheet.getCell(columnNumberSerialNumbers[x],
                                currentRowNumber)
                                .getContents())) {

                          SerialNumber sn = createSerialNumber(sheet,
                              currentRowNumber,
                              columnNumberSerialNumbers[x],
                              realm);

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
                            x=columnNumberSerialNumbers.length;
                        }
                    }


                }

                //Advance to next row
                currentRowNumber++;
            }
        }
        
        workbook.close();
        tempFile.flush();
        tempFile.close();
        context.deleteFile(TEMPFILE);

        realm.commitTransaction();
    }

  private static PropertyBook createPropertyBook(
      String pbic,
      String uic,
      Realm realm)
  {
    PropertyBook propertyBook = realm.createObject(PropertyBook.class);
    propertyBook.setPbic(pbic);
    propertyBook.setUic(uic);

    return propertyBook;
  }

  private static LineNumber createLineNumber(Sheet sheet,
                                      int currentRowNumber,
                                      Realm realm)
  {
    LineNumber lineNumber = realm.createObject(LineNumber.class);

    //test if numeric conversions are valid for Required and Authorized
    int required = 0;
    int authorized = 0;
    int dueIn = 0;

    if(!sheet.getCell(columnNumberLinAuthorized,
        currentRowNumber).getContents().equals("")) {
      authorized = Integer.parseInt(
          sheet.getCell(columnNumberLinAuthorized,
              currentRowNumber)
              .getContents());
    }

    if(!sheet.getCell(columnNumberLinRequired,
        currentRowNumber).getContents().equals("")) {
      required = Integer.parseInt(
          sheet.getCell(columnNumberLinRequired,
              currentRowNumber)
              .getContents());
    }

    if(!sheet.getCell(columnNumberLinDI,
        currentRowNumber).getContents().equals("")) {
      dueIn = Integer.parseInt(
          sheet.getCell(columnNumberLinDI,
              currentRowNumber)
              .getContents());
    }

    lineNumber.setLin(sheet.getCell(colNumberLIN, currentRowNumber)
        .getContents());
    lineNumber.setSubLin(sheet.getCell(columnNumberLinSubLin, currentRowNumber)
        .getContents());
    lineNumber.setSri(sheet.getCell(columnNumberLinSri, currentRowNumber)
        .getContents());
    lineNumber.setErc(sheet.getCell(columnNumberLinSubErc, currentRowNumber)
        .getContents());
    lineNumber.setNomenclature(sheet.getCell(columnNumberLinNomenclature,
        currentRowNumber).getContents());
    lineNumber.setAuthDoc(sheet.getCell(columnNumberLinAuthDoc, currentRowNumber)
        .getContents());
    lineNumber.setAuthorized(authorized);
    lineNumber.setRequired(required);
    lineNumber.setDueIn(dueIn);

    return lineNumber;
  }

  private static LineNumber createSubLineNumber(Sheet sheet,
                                         int currentRowNumber,
                                         LineNumber primaryLIN,
                                         Realm realm) {
    LineNumber subLIN = realm.createObject(LineNumber.class);

    subLIN.setLin(primaryLIN.getLin());
    subLIN.setSri(primaryLIN.getSri());
    subLIN.setErc(primaryLIN.getErc());
    subLIN.setNomenclature(primaryLIN.getNomenclature());
    subLIN.setAuthDoc(primaryLIN.getAuthDoc());
    subLIN.setAuthorized(primaryLIN.getAuthorized());
    subLIN.setRequired(primaryLIN.getRequired());
    subLIN.setDueIn(primaryLIN.getDueIn());
    subLIN.setSubLin(sheet.getCell(
        columnNumberLinSubLin, currentRowNumber).getContents());

    return subLIN;
  }

  /**
   * Creates a stock number based on the information in the sheet and row
   * indicated.  The parent child relationship is set with the Line Number as
   * well.
   * @param sheet Excel sheet containing the data.
   * @param currentRowNumber Number of the row in the sheet containg the data
   * @param realm Realm to create the data in
   * @return
   */
  private static StockNumber createStockNumber(Sheet sheet,
                                        int currentRowNumber,
                                        Realm realm)
  {
    StockNumber stockNumber = realm.createObject(StockNumber.class);

    stockNumber.setNsn(
        sheet.getCell(columnNumberNSN, currentRowNumber).getContents());
    stockNumber.setUi(
        sheet.getCell(columnNumberNsnUi, currentRowNumber).getContents());

    if(!TextUtils.isEmpty(
        sheet.getCell(columnNumberNsnUp, currentRowNumber).getContents()))
    {
        stockNumber.setUnitPrice((long)
            Double.parseDouble(
                sheet.getCell(columnNumberNsnUp, currentRowNumber)
                    .getContents()) * 100);
    }
    stockNumber.setNomenclature(
        sheet.getCell(
            columnNumberNsnNomenclature, currentRowNumber).getContents());
    stockNumber.setLlc(
        sheet.getCell(columnNumberNsnLLC, currentRowNumber).getContents());
    stockNumber.setEcs(
        sheet.getCell(columnNumberNsnECS, currentRowNumber).getContents());
    stockNumber.setSrrc(
        sheet.getCell(columnNumberNsnSRRC, currentRowNumber).getContents());
    stockNumber.setUiiManaged(
        sheet.getCell(columnNumberNsnUiiManaged,
            currentRowNumber).getContents());
    stockNumber.setCiic(
        sheet.getCell(columnNumberNsnCiic, currentRowNumber).getContents());
    stockNumber.setDla(
        sheet.getCell(columnNumberNsnDLA, currentRowNumber).getContents());
    stockNumber.setPubData(
        sheet.getCell(columnNumberNsnPubData, currentRowNumber).getContents());

    String onHandString =
        sheet.getCell(columnNumberNsnOH, currentRowNumber)
            .getContents();

    if(!TextUtils.isEmpty(onHandString)) {
      stockNumber.setOnHand(Integer.parseInt(onHandString));
    }

    return stockNumber;
  }

  private static SerialNumber createSerialNumber(Sheet sheet,
                                          int currentRowNumber,
                                          int columnIndex,
                                          Realm realm)
  {
    SerialNumber serialNumber = realm.createObject(SerialNumber.class);

    serialNumber.setSerialNumber(
        sheet.getCell(columnIndex, currentRowNumber).getContents());

    //System number is in the cell one to the left of the serial number
    serialNumber.setSysNo(
        sheet.getCell(columnIndex - 1, currentRowNumber).getContents());

    return serialNumber;
  }

  public static String[] getPBICList(InputStream fStream)
        throws IOException, BiffException {
    	String[] pbics;
    	
    	try{ 
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
