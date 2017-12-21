package upskills.com.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import jxl.SheetSettings;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import upskills.com.model.CompareRequest;

public class JExcelUtils {

	public static void addCell(WritableSheet sheet, int col, int row, String value, WritableCellFormat cellFormat) 
			throws RowsExceededException, WriteException{
		sheet.addCell(new Label(col, row, value, cellFormat));
	}
	
	public static void writeFile(List<String> resultData,  CompareRequest compareRequest) throws Exception{
		if (resultData != null && resultData.size() > 0){
			String fileName = compareRequest.getResultFileName().replace("csv", "xls"); // + "________EXCEL.xls";
			String dataJoinDelimiter = compareRequest.getDataJoinDelimiter();
			WorkbookSettings wbSettings=new WorkbookSettings();
			wbSettings.setLocale(new Locale("en","EN"));
			WritableWorkbook workbook=Workbook.createWorkbook(new File(fileName), wbSettings);
			workbook.createSheet("Column In Value Difference", 0);
			workbook.createSheet("Missing Key", 1);
			workbook.createSheet("Result 0", 2);
			workbook.createSheet("Result 1", 3);
			workbook.createSheet("Result 2", 4);
			workbook.createSheet("Result 3", 5);
			workbook.createSheet("Result 4", 6);
			workbook.createSheet("Result 5", 7);
			workbook.createSheet("Result 6", 8);
			workbook.createSheet("Result 7", 9);
			workbook.createSheet("Result 8", 10);
			workbook.createSheet("Result 9", 11);
			WritableSheet sheet2 = workbook.getSheet(0);
			WritableSheet sheet3 = workbook.getSheet(1);
			WritableSheet sheet1 = workbook.getSheet(2);
			WritableSheet sheet4 = workbook.getSheet(3);
			WritableSheet sheet5 = workbook.getSheet(4);
			WritableSheet sheet6 = workbook.getSheet(5);
			WritableSheet sheet7 = workbook.getSheet(6);
			WritableSheet sheet8 = workbook.getSheet(7);
			WritableSheet sheet9 = workbook.getSheet(8);
			WritableSheet sheet10 = workbook.getSheet(9);
			WritableSheet sheet11 = workbook.getSheet(10);
			WritableSheet sheet12 = workbook.getSheet(11);
			WritableSheet tempSheet = null;
			
			for (int i =1; i<= 11; i++){
				SheetSettings ss = workbook.getSheet(i).getSettings();
				ss.setHorizontalFreeze(0);
			}
			
			WritableFont font = new WritableFont(WritableFont.TIMES, 12);
			
			WritableCellFormat cellFormat = new WritableCellFormat (font);
			WritableCellFormat cellDifFormat = null;
			WritableCellFormat cellDifFormatMx2 = null;
			WritableCellFormat cellDifFormat1 = new WritableCellFormat (font);
			cellDifFormat1.setBackground(Colour.LIGHT_GREEN);
			WritableCellFormat cellDifFormat2 = new WritableCellFormat (font);
			cellDifFormat2.setBackground(Colour.YELLOW);
			WritableCellFormat cellDifFormat3 = new WritableCellFormat (font);
			cellDifFormat3.setBackground(Colour.GRAY_25);
			WritableCellFormat cellDifFormat4 = new WritableCellFormat (font);
			cellDifFormat4.setBackground(Colour.LIGHT_BLUE);
			int row = 0;
			int sheet4Row = 0;
			/**
			 * sheet 1
			 */
			String arr[] = resultData.get(0).split(dataJoinDelimiter);
			for (int i=0, size = arr.length; i< size; i++){
				JExcelUtils.addCell(sheet1, i, row, arr[i], cellFormat);
				JExcelUtils.addCell(sheet4, i, row, arr[i], cellFormat);
				JExcelUtils.addCell(sheet5, i, row, arr[i], cellFormat);
				JExcelUtils.addCell(sheet6, i, row, arr[i], cellFormat);
				JExcelUtils.addCell(sheet7, i, row, arr[i], cellFormat);
				JExcelUtils.addCell(sheet8, i, row, arr[i], cellFormat);
				JExcelUtils.addCell(sheet9, i, row, arr[i], cellFormat);
				JExcelUtils.addCell(sheet10, i, row, arr[i], cellFormat);
				JExcelUtils.addCell(sheet11, i, row, arr[i], cellFormat);
				JExcelUtils.addCell(sheet12, i, row, arr[i], cellFormat);
			}
			
			String arr1[] = null;
			String arr2[] = null;
			row++;
			Boolean flag = true;
			Set<Integer> listDifCol = new TreeSet<Integer>();
			int missingTradeIndex = -1;
			List<String> missingKeyMx2 = new ArrayList<String>();
			List<String> missingKeyMx3 = new ArrayList<String>();
			sheet4Row = row;
			Integer sheetFlag = 0;
			String sheetNumber = null;
			for (int i= 1, size = resultData.size(); i< size;){
				
				if (i > 65500*9){
					/*
					 * Nhay wa sheet 5
					 */
					tempSheet = sheet12;
					if (sheetFlag == 8){
						row = sheet4Row;
					}
					sheetFlag = 9;
				}else if (i > 65500*8){
					/*
					 * Nhay wa sheet 5
					 */
					tempSheet = sheet11;
					if (sheetFlag == 7){
						row = sheet4Row;
					}
					sheetFlag = 8;
				}else if (i > 65500*7){
					/*
					 * Nhay wa sheet 5
					 */
					tempSheet = sheet10;
					if (sheetFlag == 6){
						row = sheet4Row;
					}
					sheetFlag = 7;
				}else if (i > 65500*6){
					/*
					 * Nhay wa sheet 5
					 */
					tempSheet = sheet9;
					if (sheetFlag == 5){
						row = sheet4Row;
					}
					sheetFlag = 6;
				}else if (i > 65500*5){
					/*
					 * Nhay wa sheet 5
					 */
					tempSheet = sheet8;
					if (sheetFlag == 4){
						row = sheet4Row;
					}
					sheetFlag = 5;
				}else if (i > 65500*4){
					/*
					 * Nhay wa sheet 5
					 */
					tempSheet = sheet7;
					if (sheetFlag == 3){
						row = sheet4Row;
					}
					sheetFlag = 4;
				}else if (i > 65500*3){
					/*
					 * Nhay wa sheet 5
					 */
					tempSheet = sheet6;
					if (sheetFlag == 2){
						row = sheet4Row;
					}
					sheetFlag = 3;
				}else if (i > 65500*2){
					/*
					 * Nhay wa sheet 5
					 */
					tempSheet = sheet5;
					if (sheetFlag == 1){
						row = sheet4Row;
					}
					sheetFlag = 2;
				}else if (i > 65500){
					/*
					 * Nhay wa sheet 4
					 */
					tempSheet = sheet4;
					if (sheetFlag == 0){
						row = sheet4Row;
					}
					sheetFlag = 1;
				}else{
					/*
					 * Write o sheet 1
					 */
					tempSheet = sheet1;
				}
				arr1 = (resultData.get(i)).split(dataJoinDelimiter);
				arr2 = (resultData.get(i+1)).split(dataJoinDelimiter);
				
				if (flag){
					cellDifFormat = cellDifFormat1;
				}else{
					cellDifFormat = cellDifFormat2;
				}
				flag = !flag;
				
				if (arr1.length == arr2.length){
					for (int j=0; j< arr1.length; j++){
						if (arr1[j].trim().equals(arr2[j].trim())){
							JExcelUtils.addCell(tempSheet, j, row, arr1[j], cellFormat);
							JExcelUtils.addCell(tempSheet, j, row+1, arr2[j], cellFormat);
						}else{
							JExcelUtils.addCell(tempSheet, j, row, arr1[j], cellDifFormat);
							JExcelUtils.addCell(tempSheet, j, row+1, arr2[j], cellDifFormat);
							listDifCol.add(j);
						}
					}
				}else{
					if (missingTradeIndex < 0){
						missingTradeIndex = row + 1;
					}
					if (i > 65500*9){
						if (sheetNumber == null){
							sheetNumber = "Sheet 12";
						}
					}else if (i > 65500*8){
						if (sheetNumber == null){
							sheetNumber = "Sheet 11";
						}
					}else if (i > 65500*7){
						if (sheetNumber == null){
							sheetNumber = "Sheet 10";
						}
					}else if (i > 65500*6){
						if (sheetNumber == null){
							sheetNumber = "Sheet 9";
						}
					}else if (i > 65500*5){
						if (sheetNumber == null){
							sheetNumber = "Sheet 8";
						}
					}else if (i > 65500*4){
						if (sheetNumber == null){
							sheetNumber = "Sheet 7";
						}
					}else if (i > 65500*3){
						if (sheetNumber == null){
							sheetNumber = "Sheet 6";
						}
					}else if (i > 65500*2){
						if (sheetNumber == null){
							sheetNumber = "Sheet 5";
						}
					}else if (i > 65500){
						if (sheetNumber == null){
							sheetNumber = "Sheet 4";
						}
					}else{
						if (sheetNumber == null){
							sheetNumber = "Sheet 1";
						}
					}
					if (arr1.length > arr2.length){ // MX.3 missing Key
						cellDifFormatMx2 = cellDifFormat3;
						missingKeyMx3.add(arr2[2]);
					}else{// MX.2 missing Key
						cellDifFormatMx2 = cellDifFormat4;
						missingKeyMx2.add(arr1[2]);
					}
					for (int j=0, jSize = arr1.length; j< jSize; j++){
						JExcelUtils.addCell(tempSheet, j, row, arr1[j], cellDifFormatMx2);
					}
					for (int j=0, jSize = arr2.length; j< jSize; j++){
						JExcelUtils.addCell(tempSheet, j, row+1, arr2[j], cellDifFormatMx2);
					}
				}
				
				row = row + 2;
				i = i + 2;
				//System.out.println(i);
			}
			
			/**
			 * sheet 2
			 */
			int index = 0;
			Iterator<Integer> ite = listDifCol.iterator();
			for (int size = listDifCol.size(); index< size; ite.hasNext()) { 
				JExcelUtils.addCell(sheet2, 0, index, arr[ite.next()], cellFormat);
				index++;
			}
			if (missingTradeIndex > 0){
				JExcelUtils.addCell(sheet2, 0, index, "Start missing trade at line number " + missingTradeIndex + " at " + sheetNumber, cellDifFormat1);
			}
			/**
			 * Sheet 3
			 */
			for (int i=0; i< (missingKeyMx2.size() > missingKeyMx3.size()? missingKeyMx2.size(): missingKeyMx3.size() / 65000)+3; i = i+3){
				JExcelUtils.addCell(sheet3, i+0, 0, "Missing Key In Mx2", cellDifFormat1);
				JExcelUtils.addCell(sheet3, i+1, 0, "Missing Key In Mx3", cellDifFormat2);
			}
			
			int rowIndex = 1;
			int colIndex = 0;
			for(int i=0, size = missingKeyMx2.size(); i< size; i++){
				if (i % 65000 == 0 && i > 0){
					rowIndex = 1;
					colIndex = colIndex + 3;
				}
				JExcelUtils.addCell(sheet3, colIndex, rowIndex++, missingKeyMx2.get(i), cellDifFormat1);
			}
			
			rowIndex = 1;
			colIndex = 1;
			for(int i=0, size = missingKeyMx3.size(); i< size; i++){
				if (i % 65000 == 0 && i > 0){
					rowIndex = 1;
					colIndex = colIndex + 3;
				}
				JExcelUtils.addCell(sheet3, colIndex, rowIndex++, missingKeyMx3.get(i), cellDifFormat2);
			}
			/**
			 * end writing!
			 */
			workbook.write();
			workbook.close();
		}
	}
	
}



















