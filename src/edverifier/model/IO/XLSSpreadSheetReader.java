/*
 * BSUIR, Department of Electronics. 2015
 * Developed by Kiskin
 *
 */
package edverifier.model.IO;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;

/**
 * Class for reading and parse excel file .xls in the form suitable for creating CharacteristicTable
 *
 * @author Kiskin
 */
public class XLSSpreadSheetReader extends SpreadSheetReader {

//	workbook representation in HSSF format
	private HSSFWorkbook workbook;
	private HSSFSheet sheet;

	public XLSSpreadSheetReader() {
	}

	/**
	 *
	 * @param spreadSheetFilepath - relative path to .xls file
	 * @throws java.io.FileNotFoundException
	 * @throws java.io.IOException;
	 *
	 */
	public XLSSpreadSheetReader(String spreadSheetFilepath) throws FileNotFoundException, IOException {
		workbook = new HSSFWorkbook(new FileInputStream(spreadSheetFilepath));
		sheet = workbook.getSheetAt(curSheeetNum);
	}

	/**
	 * reads the next table and returns {@code null} if it does not exist
	 *
	 * @return array that represents read table or null if no more tables left
	 */
	@Override
	public String[][] readNextTable() {
		int rows = 0, cols = 0;

		do {
//			skip tables	that are too small
			curRowNum += rows;
			curColNum += cols;

			if (!findNextTable()) {
				return null;
			}

			rows = findRowsQuant();
			cols = findQolsQuant();
		} while (rows < 2 && cols < 2);	// minimal table size is 2x2

		String[][] rawTable = new String[findRowsQuant()][findQolsQuant()];

		for (int i = 0;
				i < rawTable.length;
				i++) {
			for (int j = 0; j < rawTable[0].length; j++) {
				HSSFCell HSSFcell = sheet.getRow(curRowNum + i).getCell(curColNum + j);
				//determination of cell type
				if (HSSFcell != null) {
					switch (HSSFcell.getCellType()) {
						case Cell.CELL_TYPE_STRING:
							rawTable[i][j] = HSSFcell.getStringCellValue();
							break;
						case Cell.CELL_TYPE_NUMERIC:
							rawTable[i][j] = String.valueOf(HSSFcell.getNumericCellValue());
							break;
					}
				} else {
					rawTable[i][j] = "";
				}
			}
		}

		curRowNum += rawTable.length;
		curColNum += rawTable[0].length;

		return rawTable;
	}

	@Override
	public void open(String filepath) throws IOException {
		workbook = new HSSFWorkbook(new FileInputStream(filepath));
		sheet = workbook.getSheetAt(curSheeetNum);
	}

//	moves "coordinates" to the position of the next table
//	return false if no more tables exists
	@Override
	@SuppressWarnings("empty-statement")
	protected boolean findNextTable() {
		if (sheet.getRow(curRowNum) != null && sheet.getRow(curRowNum).getCell(curColNum) != null) //already in position
		{
			return true;
		}
//		if sheet is over - turn the page
		if (curRowNum >= sheet.getLastRowNum()) {
			do {
				if (curSheeetNum + 1 < workbook.getNumberOfSheets()) {
					sheet = workbook.getSheetAt(++curSheeetNum);
					if (sheet != null) {
						curRowNum = sheet.getFirstRowNum();
						
						if (sheet.getRow(curRowNum) != null) {
							curColNum = sheet.getRow(curRowNum).getFirstCellNum();
						} else {
							curColNum = 0;
							return false;
						}
					} else {
						return false;
					}
				}
				return true;
			} while (curRowNum >= sheet.getLastRowNum());
		} else {
//			API behaves unclear: for empty row result may be either null-reference or HSSFRow object whose method getFirstCellNum()
//			returns -1
			while ((sheet.getRow(++curRowNum) == null
					|| sheet.getRow(curRowNum) != null && sheet.getRow(curRowNum).getFirstCellNum() == -1)
					&& curRowNum < sheet.getLastRowNum());
			curColNum = sheet.getRow(curRowNum).getFirstCellNum();
		}
		return true;
	}

//	calculate quantity of rows in table whose top left corner lies on curRowNum, curColNum
	@SuppressWarnings("empty-statement")
	private int findRowsQuant() {
		int lastTableRow = curRowNum;

		while (sheet.getRow(++lastTableRow) != null
				&& sheet.getRow(lastTableRow).getCell(sheet.getRow(lastTableRow).getFirstCellNum()) != null);
		return lastTableRow - curRowNum;
	}

//	calculate quantity of rows in table whose top left corner lies on curRowNum, curColNum
	@SuppressWarnings("empty-statement")
	private int findQolsQuant() {
		int lastTableCol = curColNum;

		while (sheet.getRow(curRowNum).getCell(++lastTableCol) != null);

		return lastTableCol - curColNum;
	}
}
