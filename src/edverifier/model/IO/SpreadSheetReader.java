/*
 * BSUIR, Department of Electronics. 2015
 * Developed by Kiskin
 *
 */
package edverifier.model.IO;

/**
 * Abstract class that is general for classes designed for reading from Microsoft Excel formats
 * @author Kiskin
 */
abstract public class SpreadSheetReader implements Reader{
	//	current "coordinates" in the workbook
	protected int curSheeetNum;
	protected int curRowNum;
	protected int curColNum;
	
	abstract protected boolean findNextTable();
}
