package com.NortrupDevelopment.PropertyApp.view;


import android.content.Context;
import android.text.TextUtils;

import com.NortrupDevelopment.PropertyApp.model.Item;
import com.NortrupDevelopment.PropertyApp.model.LIN;
import com.NortrupDevelopment.PropertyApp.model.NSN;
import com.NortrupDevelopment.PropertyApp.model.PropertyBook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;

import jxl.Workbook;
import jxl.format.Border;
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
    private static final int columnNumberLIN = 0;
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
     * @param c Method needs a context in order to create a temporary file for reading format information
     * 	because JXL does not read any of the format information when in read-only mode.  
     * @return ArrayList of LIN objects
     */
    public static ArrayList<PropertyBook> readHandReceipt(InputStream inFile, int[] sheets, Context c)
        throws BiffException,
            IOException
    {
        Workbook workbook;
        WritableWorkbook copy;
        FileOutputStream tempFile;

        try{
        	workbook = Workbook.getWorkbook(inFile);
        	tempFile = c.openFileOutput(TEMPFILE, Context.MODE_PRIVATE);
        	copy = Workbook.createWorkbook(tempFile, workbook);
        } catch (BiffException biff) {
            throw biff;
        } catch (IOException ioe) {
            throw ioe;
        }

        ArrayList<PropertyBook> pbics = new ArrayList<PropertyBook>();

        for(int sheetIndex = 0; sheetIndex< sheets.length; sheetIndex++) {

            WritableSheet sheet = copy.getSheet(sheets[sheetIndex]);

            //create some markers
            LIN currentLIN = new LIN("","","","","","",0,0,0); //empty LIN until superseded by data
            NSN currentNSN = new NSN("", "", new BigDecimal(0), "", "","", "", "", "", "", "", 0, currentLIN);
            boolean checkForSerialNumbers = false;

            //Pull property book description information
            WritableCell descriptionCell = sheet.getWritableCell(
                    propertyBookDescriptionCell,
                    propertyBookDescriptionRow);

            String[] descriptionPieces = descriptionCell.getContents().split("/");

            PropertyBook result = new PropertyBook(
                    descriptionPieces[4].trim(),
                    descriptionPieces[3].trim(),
                    sheet.getName());

            //skip past the header data
            int currentRowNumber = firstLINRowNumber;

            while(currentRowNumber < sheet.getRows()) {

                //pull off the first and second cells in the row to figure out what the contents of the row are
                WritableCell columnZero = sheet.getWritableCell(columnNumberLIN, currentRowNumber);
                WritableCell columnOne = sheet.getWritableCell(columnNumberLinSubLin, currentRowNumber);
                //LIN's match the REGEX regexLIN and have a background that is grey.
                if(columnZero.getContents().matches(regexLIN)
                        &&
                        columnZero.getCellFormat().getBackgroundColour().
                            getDefaultRGB().getBlue() == GREY_QUANTITY
                   ) {

                    //we have found a LIN row turn off check for serial numbers
                    checkForSerialNumbers = false;

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

                    currentLIN = new LIN(
                            sheet.getCell(columnNumberLIN, currentRowNumber)
                                    .getContents(),
                            sheet.getCell(columnNumberLinSubLin, currentRowNumber)
                                    .getContents(),
                            sheet.getCell(columnNumberLinSri, currentRowNumber)
                                    .getContents(),
                            sheet.getCell(columnNumberLinSubErc, currentRowNumber)
                                    .getContents(),
                            sheet.getCell(columnNumberLinNomenclature, currentRowNumber)
                                    .getContents(),
                            sheet.getCell(columnNumberLinAuthDoc, currentRowNumber)
                                    .getContents(),
                            authorized,
                            required,
                            dueIn
                    );
                    result.addLIN(currentLIN);

                }
                //Sub LIN has a LIN number in the second column and has a grey background like a LIN.
                else if (columnOne.getContents().matches(regexLIN) &&
                        columnOne.getCellFormat().getBackgroundColour().
                            getDefaultRGB().getBlue() == GREY_QUANTITY) {
                    currentLIN = new LIN(
                            currentLIN.getLin(),
                            columnOne.getContents(),
                            currentLIN.getSri(),
                            currentLIN.getErc(),
                            currentLIN.getNomencalture(),
                            currentLIN.getAuthDoc(),
                            currentLIN.getAuthorized(),
                            currentLIN.getRequired(),
                            currentLIN.getDi());

                    result.addLIN(currentLIN);
                }
                /**
                 * NSNs lines are defined by having an NSN (13 digit alpha numeric
                 * in the first column (cellZero) and having a line above and below
                 * the cell.
                 */
                else if(columnZero.getContents().matches(regexNSN) &&
                        columnZero.getCellFormat().getBorder(Border.TOP)
                            .getValue() == 1 &&
                        columnZero.getCellFormat().getBorder(Border.BOTTOM)
                            .getValue() == 1) {

                    int onHand = 0;
                    if(sheet.getCell(columnNumberNsnOH, currentRowNumber)
                            .getContents().compareTo("") != 0) {
                        onHand = Integer.parseInt(
                                sheet.getCell(columnNumberNsnOH, currentRowNumber)
                                .getContents());
                    }

                    currentNSN = new NSN(
                            sheet.getCell(columnNumberNSN, currentRowNumber).getContents(),
                            sheet.getCell(columnNumberNsnUi, currentRowNumber).getContents(),
                            new BigDecimal(sheet.getCell(columnNumberNsnUp, currentRowNumber).getContents()),
                            sheet.getCell(columnNumberNsnNomenclature, currentRowNumber).getContents(),
                            sheet.getCell(columnNumberNsnLLC, currentRowNumber).getContents(),
                            sheet.getCell(columnNumberNsnECS, currentRowNumber).getContents(),
                            sheet.getCell(columnNumberNsnSRRC, currentRowNumber).getContents(),
                            sheet.getCell(columnNumberNsnUiiManaged, currentRowNumber).getContents(),
                            sheet.getCell(columnNumberNsnCiic, currentRowNumber).getContents(),
                            sheet.getCell(columnNumberNsnDLA, currentRowNumber).getContents(),
                            sheet.getCell(columnNumberNsnPubData, currentRowNumber).getContents(),
                            onHand,
                            currentLIN
                    );

                    currentLIN.addNSN(currentNSN);
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

                            Item sn = new Item(
                                    sheet.getCell(columnNumberSerialNumbers[x],
                                            currentRowNumber)
                                        .getContents(),
                                    sheet.getCell(
                                            columnNumberSerialNumbers[x] -1,
                                            currentRowNumber)
                                            .getContents(),
                                    currentNSN);
                            currentNSN.addItem(sn);
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

            pbics.add(result);
        }
        
        workbook.close();
        tempFile.flush();
        tempFile.close();
        c.deleteFile(TEMPFILE);

        return pbics;
    }
    
    public static String[] getPBICList(InputStream fStream) throws IOException, BiffException {
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
