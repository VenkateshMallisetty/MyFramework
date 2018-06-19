package com.Gmail.Support;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.Cell;

import com.Gmail.accelerators.ActionEngine;
import com.Gmail.accelerators.TestEngine;

@SuppressWarnings("all")
public class ExcelReader extends ActionEngine
{
	public String path;
	public FileInputStream fis = null;
	public FileOutputStream fileOut = null;

	private HSSFWorkbook workbook = null;
	private HSSFSheet sheet = null;
	private HSSFRow row = null;
	private Column col = null;
	private HSSFCell cell = null;
	private String sheetName;

	public ExcelReader(String path,String sheetName) {

		this.path = path;
		try {
			fis = new FileInputStream(path);
			workbook = new HSSFWorkbook(fis);
			sheet = workbook.getSheetAt(0);
			this.sheetName = sheetName;
			fis.close();
		} catch (Exception e) {
			//e.printStackTrace();
		}

	}

	// returns the row count in a sheet

	/**
	 * 
	 * @param sheetName
	 * @return
	 */
	public int getRowCount(String sheetName) {
		int index = workbook.getSheetIndex(sheetName);// (arg0)getSheetIndex
		if (index == -1)
			return 0;
		else {
			sheet = workbook.getSheetAt(index);
			int number = sheet.getLastRowNum() + 1;
			return number;
		}

	}
	public String getData(String rowName,String colName) throws Throwable{
		int rowNum=-1;
		  try{
			 
			  int index = workbook.getSheetIndex(sheetName);
				int rowNumber = -1;
				int colNumber = -1;
				boolean flag = false;
				if (index == -1)
					return "";
				sheet = workbook.getSheetAt(index);
				
				
				for (int i = 2; i < sheet.getPhysicalNumberOfRows(); ) {
					try
					{
					row = sheet.getRow(i);
						
				
					if (row.getCell(0).toString().equalsIgnoreCase(rowName)) {
						rowNumber = i;
						
						break;
					}
					i=i+2;
					}
					catch(NullPointerException e)
					{
						continue;
					}
				}
				row = sheet.getRow(rowNumber-1);
				for (int j = 0; j <=row.getPhysicalNumberOfCells(); j++) {
					try
					{
					if (row.getCell(j).toString().equalsIgnoreCase(colName)) {
						colNumber = j;
						break;
					}
					}
					catch(NullPointerException e)
					{
						continue;
					}
				}
				if(colNumber==-1)
				{
					failureReport(driver,rowName,"Unable to find the column with name"+colName);
				}
				row = sheet.getRow(rowNumber);
				if (row == null)
					return "";
				cell = row.getCell(colNumber);
				if (cell == null)
					return "";

				if (cell.getCellType() == Cell.CELL_TYPE_STRING)
					return cell.getStringCellValue().trim();
				else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC
						|| cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

					String cellText = String.valueOf(cell.getNumericCellValue())
							.replaceFirst(".0", "");

					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						// format in form of M/D/YY
						double d = cell.getNumericCellValue();

						Calendar cal = Calendar.getInstance();
						cal.setTime(HSSFDateUtil.getJavaDate(d));
						cellText = (String.valueOf(cal.get(Calendar.YEAR)))
								.substring(2);
						cellText = cal.get(Calendar.MONTH) + 1 + "/"
								+ cal.get(Calendar.DAY_OF_MONTH) + "/" + cellText;

						// System.out.println(cellText);

					}

					return cellText;
				} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
					return "";
				else
					return String.valueOf(cell.getBooleanCellValue()).trim();

		  }
		  catch(Exception e){
		   
		failureReport(driver,"Excel Data Reading", "row "+rowName+" or column "+colName +" does not exist in xls");
			  TestEngine.logger.error(e.toString());
			  
		   return "";
		  }
		 }
	public boolean setData(String rowName,String colName,String data) throws Throwable{
		try {  
			fis = new FileInputStream(path);
			workbook = new HSSFWorkbook(fis);
			
			 int index = workbook.getSheetIndex(sheetName);
				int rowNum = -1;
				int colNum = -1;
				boolean flag = false;
				if (index == -1)
					return false;
				sheet = workbook.getSheetAt(index);
				
				
				for (int i = 0; i < sheet.getPhysicalNumberOfRows(); ) {
					try
					{
					row = sheet.getRow(i);
						
				
					if (row.getCell(0).toString().equalsIgnoreCase(rowName)) {
						rowNum = i;
						
						break;
					}
					i=i+2;
					}
					catch(NullPointerException e)
					{
						continue;
					}
				}
				
				row = sheet.getRow(rowNum-1);
				for (int j = 0; j <row.getPhysicalNumberOfCells(); j++) {
					try
					{
					if (row.getCell(j).toString().equalsIgnoreCase(colName)) {
						colNum = j;
						break;
					}
					}
					catch(NullPointerException e)
					{
						continue;
					}
				}
			if (rowNum <= 0)
				return false;

			
			if (index == -1)
				return false;

			sheet = workbook.getSheetAt(index);
		
			
			if (colNum == -1)
				return false;

			sheet.autoSizeColumn(colNum);
			row = sheet.getRow(rowNum);
			if (row == null)
				row = sheet.createRow(rowNum);

			cell = row.getCell(colNum);
			if (cell == null)
				cell = row.createCell(colNum);

			
			cell.setCellValue(data);

			fileOut = new FileOutputStream(path);

			workbook.write(fileOut);

			fileOut.close();

		} catch (Exception e) {
			failureReport(driver,"Excel Data Reading", "row "+rowName+" or column "+colName +" does not exist in xls");
			TestEngine.logger.error(e.toString());
			return false;
		}
		return true;
		 }
	public String getDataBySheet(String sheetName,String rowName,String colName) throws Throwable{
		int rowNum=-1;
		  try{
			 
			  int index = workbook.getSheetIndex(sheetName);
				int rowNumber = -1;
				int colNumber = -1;
				boolean flag = false;
				if (index == -1)
					return "";
				sheet = workbook.getSheetAt(index);
				
				
 				for (int i = 1; i < sheet.getPhysicalNumberOfRows(); ) {
					try
					{
					row = sheet.getRow(i);
						
				
					if (row.getCell(0).toString().equalsIgnoreCase(rowName)) {
						rowNumber = i;
						
						break;
					}
					i=i+1;
					}
					catch(NullPointerException e)
					{
						continue;
					}
				}
				row = sheet.getRow(0);
				for (int j = 0; j <row.getPhysicalNumberOfCells(); j++) {
					try
					{
					if (row.getCell(j).toString().equalsIgnoreCase(colName)) {
						colNumber = j;
						break;
					}
					}
					catch(NullPointerException e)
					{
						continue;
					}
				}
				if(colNumber==-1)
				{
					failureReport(driver,rowName,"Unable to find the column with name"+colName);
				}
				row = sheet.getRow(rowNumber);
				if (row == null)
					return "";
				cell = row.getCell(colNumber);
				if (cell == null)
					return "";

				if (cell.getCellType() == Cell.CELL_TYPE_STRING)
					return cell.getStringCellValue().trim();
				else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC
						|| cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

					String cellText = String.valueOf(cell.getNumericCellValue())
							.replaceFirst(".0", "");

					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						// format in form of M/D/YY
						double d = cell.getNumericCellValue();

						Calendar cal = Calendar.getInstance();
						cal.setTime(HSSFDateUtil.getJavaDate(d));
						cellText = (String.valueOf(cal.get(Calendar.YEAR)))
								.substring(2);
						cellText = cal.get(Calendar.MONTH) + 1 + "/"
								+ cal.get(Calendar.DAY_OF_MONTH) + "/" + cellText;

						// System.out.println(cellText);

					}

					return cellText;
				} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
					return "";
				else
					return String.valueOf(cell.getBooleanCellValue()).trim();

		  }
		  catch(Exception e){
		   
			  failureReport(driver,"Excel Data Reading", "row "+rowName+" or column "+colName +" does not exist in xls");
			  TestEngine.logger.error(e.toString());
		   return "";
		  }
		 }
	public String getDataByRowindex(String sheetName,int rowIndex,String colName) throws Throwable{
		
		String rowName=null;
		  try{
			 
			  int index = workbook.getSheetIndex(sheetName);
			
				int colNumber = -1;
				boolean flag = false;
				
				if (index == -1)
					return "";
				sheet = workbook.getSheetAt(index);
				
				
				row = sheet.getRow(0);
				for (int j = 0; j <row.getPhysicalNumberOfCells(); j++) {
					try
					{
					if (row.getCell(j).toString().equalsIgnoreCase(colName)) {
						colNumber = j;
						break;
					}
					}
					catch(NullPointerException e)
					{
						continue;
					}
				}
				if(colNumber==-1)
				{
					failureReport(driver,colName,"Unable to find the column with name"+colName);
				}
				row = sheet.getRow(rowIndex);
				if (row == null)
					return "";
				cell = row.getCell(colNumber);
				if (cell == null)
					return "";

				if (cell.getCellType() == Cell.CELL_TYPE_STRING)
					return cell.getStringCellValue().trim();
				else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC
						|| cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

					String cellText = String.valueOf(cell.getNumericCellValue())
							.replaceFirst(".0", "");

					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						// format in form of M/D/YY
						double d = cell.getNumericCellValue();

						Calendar cal = Calendar.getInstance();
						cal.setTime(HSSFDateUtil.getJavaDate(d));
						cellText = (String.valueOf(cal.get(Calendar.YEAR)))
								.substring(2);
						cellText = cal.get(Calendar.MONTH) + 1 + "/"
								+ cal.get(Calendar.DAY_OF_MONTH) + "/" + cellText;

						// System.out.println(cellText);

					}

					return cellText;
				} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
					return "";
				else
					return String.valueOf(cell.getBooleanCellValue()).trim();

		  }
		  catch(Exception e){
		   
			  failureReport(driver,"Excel Data Reading", "row index "+rowIndex+" or column "+colName +" does not exist in xls");
			  TestEngine.logger.error(e.toString());
		   return "";
		  }
		 }
	public String[][] getDataArrayBySheet(String sheetName) throws Throwable{
		int rows=-1;
		int columns=-1;
		String[][] data=null;
		  try{
			 
			  int index = workbook.getSheetIndex(sheetName);
				
				boolean flag = false;
				if (index == -1)
				{  
					failureReport(driver,"Excel Data Reading","Unable to find sheet with name  "+sheetName);
					return null;
				}
				sheet = workbook.getSheetAt(index);
				rows=sheet.getPhysicalNumberOfRows();
				columns=sheet.getRow(0).getLastCellNum();
				 data=new String[rows-1][columns];
 				for (int i = 1; i < rows; i++) {
					 
						 for(int j=0;j<columns;j++)
						 { 
							try
							{
					     row = sheet.getRow(i);
					     data[i-1][j]=row.getCell(j).toString();
							}
							catch(NullPointerException e)
							{
								break;
							}
				
					
					
					}
						 
 				}
 				return data;
		  }
		  
					catch(Exception e)
					{
						failureReport(driver,"Excel Data Reading","Unable to get the data from the sheet "+sheetName);
						TestEngine.logger.error(e.toString());
						return null;
					}
					
					
				}
	public String getData(String colName) throws Throwable{
		int rowNum=-1;
		  try{
			 
			  int index = workbook.getSheetIndex(sheetName);
				int rowNumber = -1;
				int colNumber = -1;
				boolean flag = false;
				if (index == -1)
					return "";
				sheet = workbook.getSheetAt(index);
				
				
				for (int i = 2; i < sheet.getPhysicalNumberOfRows(); ) {
					try
					{
					row = sheet.getRow(i);
						
				
					if (row.getCell(0).toString().equalsIgnoreCase(TestEngine.myClass)) {
						rowNumber = i;
						
						break;
					}
					i=i+2;
					}
					catch(NullPointerException e)
					{
						continue;
					}
				}
				row = sheet.getRow(rowNumber-1);
				for (int j = 0; j <=row.getPhysicalNumberOfCells(); j++) {
					try
					{
					if (row.getCell(j).toString().equalsIgnoreCase(colName)) {
						colNumber = j;
						break;
					}
					}
					catch(NullPointerException e)
					{
						continue;
					}
				}
				if(colNumber==-1)
				{
					failureReport(driver,myClass,"Unable to find the column with name"+colName);
				}
				row = sheet.getRow(rowNumber);
				if (row == null)
					return "";
				cell = row.getCell(colNumber);
				if (cell == null)
					return "";

				if (cell.getCellType() == Cell.CELL_TYPE_STRING)
					return cell.getStringCellValue().trim();
				else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC
						|| cell.getCellType() == Cell.CELL_TYPE_FORMULA) {

					String cellText = String.valueOf(cell.getNumericCellValue())
							.replaceFirst(".0", "");

					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						// format in form of M/D/YY
						double d = cell.getNumericCellValue();

						Calendar cal = Calendar.getInstance();
						cal.setTime(HSSFDateUtil.getJavaDate(d));
						cellText = (String.valueOf(cal.get(Calendar.YEAR)))
								.substring(2);
						cellText = cal.get(Calendar.MONTH) + 1 + "/"
								+ cal.get(Calendar.DAY_OF_MONTH) + "/" + cellText;

						// System.out.println(cellText);

					}

					return cellText;
				} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK)
					return "";
				else
					return String.valueOf(cell.getBooleanCellValue()).trim();

		  }
		  catch(Exception e){
		   
		failureReport(driver,"Excel Data Reading", "row "+myClass+" or column "+colName +" does not exist in xls");
			  TestEngine.logger.error(e.toString());
			  
		   return "";
		  }
		 }

}
