/*
 * BSUIR, Department of Electronics. 2015
 * Developed by Kiskin
 *
 */
package edverifier.model.IO;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import static edverifier.EDVerifier.resources;

/**
 * Class for reading and parse excel file (.xls and .xlsx) in the form suitable for creating CharacteristicTable
 * @author Kiskin
 */
public class SpreadSheetReader {
	
//	workbook representation in HSSF or XSSF format
	private Object workbook;
	
	/**
	 * 
	 * @param spreadSheetFilepath - relative path to .xls or .xlsx file
	 * @throws java.io.FileNotFoundException
	 * @throws java.io.IOException;
	 * 
	 */
	public SpreadSheetReader(String spreadSheetFilepath) throws FileNotFoundException, IOException{
		if (spreadSheetFilepath.matches("xls"))
			workbook = new HSSFWorkbook(new FileInputStream(spreadSheetFilepath));
		else if (spreadSheetFilepath.matches(".xlsx"))
			workbook = new XSSFWorkbook(new FileInputStream(spreadSheetFilepath));
		else
			throw new IOException(resources.getString("UnsupportedFormatExc"));
	}
	
	/**
	 * reads the next table and returns zero if it does not exist
	 * @return array that represents read table or null if no more tables left
	 */
	public String[][] readNextTable(){
		
	}
}
