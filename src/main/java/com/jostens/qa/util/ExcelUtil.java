package com.jostens.qa.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

@SuppressWarnings("deprecation")
public class ExcelUtil {
	
	//Initialize Variable(s)
	public FileInputStream fis = null;
	public FileOutputStream fileOut = null;
	
	private static XSSFWorkbook ExcelWBook;
	private static XSSFSheet ExcelWSheet;
	private static XSSFCell Cell;
	private static XSSFRow Row;
	
	//Location of the Excel File
	private static String path = System.getProperty("user.dir") + "\\src\\main\\java\\com\\jostens\\qa\\testdata\\newJostens2.xlsx";
	public static String sheetName = "";
	
	public void setDataTablePath(String dataTablePath) {
		path = System.getProperty("user.dir") + dataTablePath;
	}
	
	public void setSheetName(String desiredSheetName) {
		sheetName = desiredSheetName;
	}
	
	//Method returns the current time when called
	public String getCurrentDateTime() {
		//Get the current time - will be used to indicate when the test finished for the Excel File output
		DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyy/MM/dd HH:mm:ss");
		LocalDateTime currentDateTime = LocalDateTime.now();
		String currentDateTimeStr = dateTimeFormat.format(currentDateTime);
		
		return currentDateTimeStr;
	}
	
	//Retrieve a single row of a DataSheet - each row of a DataSheet is iterated through by another method in this java class before sending off the DataSheet to the active script
	public static String[] retrieveRow(XSSFRow row, int numCols) {
		//Initialize Variable(s)
		String[] importRow = new String[numCols];
		XSSFCell cell;
		
		for (int i = 0; i < row.getLastCellNum(); i++) {
			//Set the values for Variable(s)
			cell = row.getCell(i);
			
			//If a cell has no value, create a cell & specify it as blank, then add that cell
			if(cell != null) {
				importRow[i] = cell.toString();
			} else {
				row.createCell(i);
				importRow[i] = row.getCell(i).toString();
			}
		}
		
		importRow[numCols-1] = "0";
		
		return importRow;
	}
	
	//Retrieve an Excel DataSheet for automation testing
	@DataProvider(name="inputs")
	public String[][] getDataTable() {
		
		//Initialise Variable(s)
		String[][] importDataTable = null;
		System.out.println(path + " - " + sheetName);
		try {
			//Open the Excel File
			FileInputStream ExcelFile = new FileInputStream(path);
			ExcelWBook = new XSSFWorkbook(ExcelFile);
			ExcelWSheet = ExcelWBook.getSheet(sheetName);
			
			//System.out.println(path + " - " + sheetName + " - " + ExcelWSheet.getPhysicalNumberOfRows());
			
			int numRows = ExcelWSheet.getPhysicalNumberOfRows() - 1;
			int numCols = ExcelWSheet.getRow(0).getPhysicalNumberOfCells() + 1;
			
			importDataTable = new String[numRows][numCols];
			
			//Setup an 2D ArrayList to use as Parameters
			for (int i = 1; i < ExcelWSheet.getPhysicalNumberOfRows(); i++) {
				XSSFRow row = ExcelWSheet.getRow(i);
				importDataTable[i-1] = retrieveRow(row, numCols);
			}
			
			for (int i = 0; i < importDataTable.length; i++) {
				importDataTable[i][numCols-1] = String.valueOf(i+1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return importDataTable;
	}

	public String getDataTableCell(int rowNum, int colNum) {
		//Initialize Variable(s)
		String cellValue = "";
		
		try {
			//Open the Excel file
			FileInputStream ExcelFile = new FileInputStream(path);
			
			//Access the Excel DataSheet
			ExcelWBook = new XSSFWorkbook(ExcelFile);
			ExcelWSheet = ExcelWBook.getSheet(sheetName);
			
			Row = ExcelWSheet.getRow(rowNum);
			Cell = Row.getCell(colNum);
			
			if (Cell == null) {
				cellValue = "";
			} else {
				cellValue = Cell.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return cellValue;
	}

	//Send output to a specific DataSheet cell
	public void setDataTableCell(String result, int rowNum, int colNum) {
		//Initialize Variable(s)
		colNum = colNum - 1; //Reduce the colNum by 1, since the ExcelSheet starts with 0. (no need to do this with rowNum, since the rows have row #0 dedicated to column titles)
		
		try {
			//Open the Excel file
			FileInputStream ExcelFile = new FileInputStream(path);
			
			//Access the Excel DataSheet
			ExcelWBook = new XSSFWorkbook(ExcelFile);
			ExcelWSheet = ExcelWBook.getSheet(sheetName);
			
			Row = ExcelWSheet.getRow(rowNum);
			Cell = Row.getCell(colNum);
			
			if (Cell == null) {
				Cell = Row.createCell(colNum);
				Cell.setCellValue(result);
			} else {
				Cell.setCellValue(result);
			}
			
			//Open the file to write the results
			FileOutputStream outputFile = new FileOutputStream(path);
			
			ExcelWBook.write(outputFile);
			outputFile.flush();
			outputFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//Send output to a specific DataSheet cell
	public void setDataTableCell(Double result, int rowNum, int colNum) throws Exception {
		try {
			//Ouput to the Excel File
			Row = ExcelWSheet.getRow(rowNum);
			Cell = Row.getCell(colNum);
			
			if (Cell == null) {
				Cell = Row.createCell(colNum);
				Cell.setCellValue(result);
			} else {
				Cell.setCellValue(result);
			}
			
			//Open the file to write the results
			FileOutputStream outputFile = new FileOutputStream(path);
			
			ExcelWBook.write(outputFile);
			outputFile.flush();
			outputFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//~~~~~~ ~~~~~~ Additional Methods ~~~~~~ ~~~~~~//
	
	public int getRowCount(String specifySheetName) {
		
		//Initialize Variable(s)
		int index = ExcelWBook.getSheetIndex(specifySheetName);
		
		//Return the row count of the specified sheet name (if the sheet name does not exist, return zero)
		if (index == -1) {
			return 0;
		} else {
			ExcelWSheet = ExcelWBook.getSheetAt(index);
			int number = ExcelWSheet.getLastRowNum() + 1;
			return number;
		}
	}
	
	@SuppressWarnings({ "static-access" })
	public String getCellData(String specifySheetName, String colName, int rowNum) {
		
		try {
			//If the row is 0 or below, return an empty string, since the row value is invalid
			if (rowNum <= 0) {
				return "";
			}
			
			//Initialize Variable(s)
			int index = ExcelWBook.getSheetIndex(specifySheetName);
			int colNum = -1;
			
			//If the index is pointing to an invalid sheet reference return an empty string
			if (index == -1) {
				return "";
			}
			
			//Open the relevant WorkSheet & read the first row (to find the relevant Header/Column name)
			ExcelWSheet = ExcelWBook.getSheetAt(index);
			Row = ExcelWSheet.getRow(0);
			
			//Iterate through the first row & locate the relevant Header/Column name
			for (int i = 0; i < Row.getLastCellNum(); i++) {
				if (Row.getCell(i).getStringCellValue().trim().equals(colName.trim())) {
					colNum = i;
				}
			}
			
			//If the colNum is invalid, return an empty string
			if (colNum == 1) {
				return "";
			}
			
			//Retrieve the specified row
			ExcelWSheet = ExcelWBook.getSheetAt(index);
			Row = ExcelWSheet.getRow(rowNum - 1);
			
			//If the row has no valid value(s), return an empty string
			if (Row == null) {
				return "";
			}
			
			//Retrieve the specified cell
			Cell = Row.getCell(colNum);
			
			//If the cell has no valid value(s), return an empty string
			if (Cell == null) {
				return "";
			}
			
			//Return the value of the cell, if it is a string
			if (Cell.getCellType().name().equals("STRING")) {
				return Cell.getStringCellValue();
			} else if (Cell.getCellType().name().equals("NUMERIC") || Cell.getCellType().name().equals("FORMULA")) {
				String cellText = String.valueOf(Cell.getNumericCellValue());
				if (HSSFDateUtil.isCellDateFormatted(Cell)) {
					//format in form of M/D/YY
					double d = Cell.getNumericCellValue();
					
					Calendar calender = Calendar.getInstance();
					calender.setTime(HSSFDateUtil.getJavaDate(d));
					cellText = (String.valueOf(calender.get(Calendar.YEAR))).substring(2);
					cellText = calender.get(Calendar.DAY_OF_MONTH) + "/" + calender.get(Calendar.MONTH) + 1 + "/" + cellText;
				}
				
				//Return the numeric or date value of the cell data
				return cellText;
			} else if (Cell.getCellType().BLANK != null) {
				return "";
			} else {
				return String.valueOf(Cell.getBooleanCellValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "row '" + rowNum + "' or column '" + colName + "' does not exist in the Excel File";
		}
	}
	
	@SuppressWarnings({ "static-access" })
	public String getCellData(String sheetName, int colNum, int rowNum) {
		try {
			if (rowNum <= 0)
				return "";

			int index = ExcelWBook.getSheetIndex(sheetName);

			if (index == -1)
				return "";

			ExcelWSheet = ExcelWBook.getSheetAt(index);
			Row = ExcelWSheet.getRow(rowNum - 1);
			if (Row == null)
				return "";
			Cell = Row.getCell(colNum);
			if (Cell == null)
				return "";

			//
			if (Cell.getCellType().name().equals("STRING"))
				return Cell.getStringCellValue();

			//
			// if (cell.getCellType().STRING != null)
			// return cell.getStringCellValue();
			else if ((Cell.getCellType().name().equals("NUMERIC")) || (Cell.getCellType().name().equals("FORMULA"))) {

				String cellText = String.valueOf(Cell.getNumericCellValue());
				if (HSSFDateUtil.isCellDateFormatted(Cell)) {
					// format in form of M/D/YY
					double d = Cell.getNumericCellValue();

					Calendar cal = Calendar.getInstance();
					cal.setTime(HSSFDateUtil.getJavaDate(d));
					cellText = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
					cellText = cal.get(Calendar.MONTH) + 1 + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cellText;

					// System.out.println(cellText);

				}

				return cellText;
			} else if (Cell.getCellType().BLANK != null)
				return "";
			else
				return String.valueOf(Cell.getBooleanCellValue());
		} catch (Exception e) {

			e.printStackTrace();
			return "row " + rowNum + " or column " + colNum + " does not exist  in xls";
		}
	}
	
	// returns true if data is set successfully else false
	public boolean setCellData(String sheetName, String colName, int rowNum, String data) {
		try {
			fis = new FileInputStream(path);
			ExcelWBook = new XSSFWorkbook(fis);

			if (rowNum <= 0)
				return false;

			int index = ExcelWBook.getSheetIndex(sheetName);
			int colNum = -1;
			if (index == -1)
				return false;

			ExcelWSheet = ExcelWBook.getSheetAt(index);

			Row = ExcelWSheet.getRow(0);
			for (int i = 0; i < Row.getLastCellNum(); i++) {
				// System.out.println(row.getCell(i).getStringCellValue().trim());
				if (Row.getCell(i).getStringCellValue().trim().equals(colName))
					colNum = i;
			}
			if (colNum == -1)
				return false;

			ExcelWSheet.autoSizeColumn(colNum);
			Row = ExcelWSheet.getRow(rowNum - 1);
			if (Row == null)
				Row = ExcelWSheet.createRow(rowNum - 1);

			Cell = Row.getCell(colNum);
			if (Cell == null)
				Cell = Row.createCell(colNum);

			// cell style
			// CellStyle cs = workbook.createCellStyle();
			// cs.setWrapText(true);
			// cell.setCellStyle(cs);
			Cell.setCellValue(data);

			fileOut = new FileOutputStream(path);

			ExcelWBook.write(fileOut);

			fileOut.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	// returns true if sheet is created successfully else false
	public boolean addSheet(String sheetname) {

		FileOutputStream fileOut;
		try {
			ExcelWBook.createSheet(sheetname);
			fileOut = new FileOutputStream(path);
			ExcelWBook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	// returns true if sheet is removed successfully else false if sheet does not exist
	public boolean removeSheet(String sheetName) {
		int index = ExcelWBook.getSheetIndex(sheetName);
		if (index == -1)
			return false;

		FileOutputStream fileOut;
		try {
			ExcelWBook.removeSheetAt(index);
			fileOut = new FileOutputStream(path);
			ExcelWBook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	// returns true if column is created successfully
	public boolean addColumn(String sheetName, String colName) {
		// System.out.println("**************addColumn*********************");

		try {
			fis = new FileInputStream(path);
			ExcelWBook = new XSSFWorkbook(fis);
			int index = ExcelWBook.getSheetIndex(sheetName);
			if (index == -1)
				return false;

			XSSFCellStyle style = ExcelWBook.createCellStyle();
			// style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
			// style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			ExcelWSheet = ExcelWBook.getSheetAt(index);

			Row = ExcelWSheet.getRow(0);
			if (Row == null)
				Row = ExcelWSheet.createRow(0);

			// cell = row.getCell();
			// if (cell == null)
			// System.out.println(row.getLastCellNum());
			if (Row.getLastCellNum() == -1)
				Cell = Row.createCell(0);
			else
				Cell = Row.createCell(Row.getLastCellNum());

			Cell.setCellValue(colName);
			Cell.setCellStyle(style);

			fileOut = new FileOutputStream(path);
			ExcelWBook.write(fileOut);
			fileOut.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	// removes a column and all the contents
	@SuppressWarnings("unused")
	public boolean removeColumn(String sheetName, int colNum) {
		try {
			if (!isSheetExist(sheetName))
				return false;
			fis = new FileInputStream(path);
			ExcelWBook = new XSSFWorkbook(fis);
			ExcelWSheet = ExcelWBook.getSheet(sheetName);
			XSSFCellStyle style = ExcelWBook.createCellStyle();
			// style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
			XSSFCreationHelper createHelper = ExcelWBook.getCreationHelper();
			// style.setFillPattern(XSSFCellStyle.NO_FILL);
			for (int i = 0; i < getRowCount(sheetName); i++) {
				Row = ExcelWSheet.getRow(i);
				if (Row != null) {
					Cell = Row.getCell(colNum);
					if (Cell != null) {
						Cell.setCellStyle(style);
						Row.removeCell(Cell);
					}
				}
			}
			fileOut = new FileOutputStream(path);
			ExcelWBook.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}
	
	// find whether sheets exists
	public boolean isSheetExist(String sheetName) {
		int index = ExcelWBook.getSheetIndex(sheetName);
		if (index == -1) {
			index = ExcelWBook.getSheetIndex(sheetName.toUpperCase());
			if (index == -1)
				return false;
			else
				return true;
		} else
			return true;
	}
	
	// returns number of columns in a sheet
	public int getColumnCount(String sheetName) {
		// check if sheet exists
		if (!isSheetExist(sheetName))
			return -1;

		ExcelWSheet = ExcelWBook.getSheet(sheetName);
		Row = ExcelWSheet.getRow(0);

		if (Row == null)
			return -1;

		return Row.getLastCellNum();
	}
	
	public int getCellRowNum(String sheetName, String colName, String cellValue) {
		for (int i = 2; i <= getRowCount(sheetName); i++) {
			if (getCellData(sheetName, colName, i).equalsIgnoreCase(cellValue)) {
				return i;
			}
		}
		
		return -1;
	}
}