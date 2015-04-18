/*
 * BSUIR, Department of Electronics. 2015
 * Developed by Kiskin
 *
 */
package edverifier.model;


/**
 * Exception occurs when there are errors in the preparation of the table
 * @author Kiskin
 */
public class TableReadException extends LocalizableException{
	
	int rowNum;
	int colNum;
	
	TableReadException(String msg){
		super(msg);
	}

	public TableReadException(String msg, int rowNum, int colNum) {
		super(msg);
		
		this.rowNum = rowNum;
		this.colNum = colNum;
	}
	
	@Override
	public String getLocalizedMessage(){
		return String.format(super.getLocalizedMessage(), rowNum, colNum);
	}
}
