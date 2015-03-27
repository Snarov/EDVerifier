/*
 * BSUIR, Department of Electronics. 2015
 * Developed by Kiskin
 *
 */
package edverifier.model;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.fxml.LoadException;
import static edverifier.EDVerifier.resources;

/**
 * Class that contains information about measurement performed in the circuit. Characteristic table is the table of values ​​of
 * functions in the form Io = f(Uo) |Ii = const (output characteristic) and Ui = f(Ii)|Uo = const (input characteristics). First
 * column in the table must be always the column of function arguments. Each other column contains function values for some fixed
 * constant Ii or Uo (depends on the table type)
 *
 * @author Kiskin
 */
public class CharacteristicTable {

	public enum TableType {

		OUTPUT, INPUT
	};

//	Next two fields represent patterns for input and output characteristics respectively
	private final static HashMap<String, String> iPatterns = new HashMap<String, String>(2) {
		{
			put("ArgColHead", "^\\s*[Ii][а-я]{0,2}$");
			put("ValColHead", "\\s*[Uu][а-я|a-z]{0,2}\\s*\\(\\s*[Uu][а-я|a-z]{0,2}\\s*=\\s*\\d*[\\.,]\\d+\\s*\\)$");
		}
	};

	private final static HashMap<String, String> oPatterns = new HashMap<String, String>(2) {
		{
			put("ArgColHead", "^\\s*[Uu][а-я]{0,2}$");
			put("ValColHead", "\\s*[Ii][а-я|a-z]{0,2}\\s*\\(\\s*[Ii][а-я|a-z]{0,2}\\s*=\\s*\\d*[\\.,]\\d+\\s*\\)$");
		}
	};

//	pattern for real decimal numbers
	private static final String REAL_DEC_PATTERN = "^\\s*\\d*[\\.,]\\d+\\s*$";
//	everything	but decimals and delimiters: . and , . Is used for isolate real number from string. 
	private static final String NOT_NUM_SYMB_PATTERN = "[^\\d\\.,]";

	private final TableType tableType;
//	arguments contents the fucntion arguments. arguments used as a keys in maps which are values in fTables.
	private final ArrayList<Double> arguments = new ArrayList<>();
//	fTables contents the fucntion tables: one for each different constant value. Each constant value is the key for this HashMap
	private final HashMap<Double, HashMap<Double, Double>> fTables = new HashMap<>();

	/**
	 * Constructs characteristic table from raw table
	 *
	 * @param rawTable - 2-dimensional array that represents a table read from file
	 * @throws LoadException if error occurred while constructing
	 */
	public CharacteristicTable(String[][] rawTable) throws LoadException {
		tableType = determineType(rawTable[0]);

//		construct arguments
		for (int rowNum = 1; rowNum < rawTable.length; rowNum++) {
			if (rawTable[rowNum][0].matches(REAL_DEC_PATTERN)) {
				arguments.add(Double.valueOf(rawTable[rowNum][0]));
			} else {
				throw new LoadException(resources.getString("wrongArgValueExc") + rowNum);
			}
		}
//		construct fTables
		for (int colNum = 1; colNum < rawTable[0].length; colNum++) {
			HashMap<Double, Double> fTable = new HashMap<>();
			
			for (int rowNum = 1; rowNum < rawTable.length; rowNum++) {
				if (rawTable[rowNum][colNum].matches(REAL_DEC_PATTERN)) {
					fTable.put(arguments.get(rowNum - 1), Double.valueOf(rawTable[rowNum][colNum]));
				} else {
					throw new LoadException(String.format(resources.getString("wrongFuncValueExc"), rowNum, colNum + 1));
				}
			}
			
			Double constVal = new Double(rawTable[0][colNum].replaceAll(NOT_NUM_SYMB_PATTERN , ""));
			fTables.put(constVal, fTable);
		}
	}

	/**
	 * Determine type of the table
	 *
	 * @param header - header of the table, the type of which it is necessary to determine
	 * @throws LoadException if the header is incorrect
	 */
	private TableType determineType(String[] header) throws LoadException {
		if (header[0].matches(iPatterns.get("ArgColHead"))) {
			for (int colNum = 1; colNum < header.length; colNum++) {
				if (!header[colNum].matches(iPatterns.get("ValColHead"))) {
					throw new LoadException(resources.getString("wrongTableHeaderExc"));
				}
			}
			return TableType.INPUT;
		}

		if (header[0].matches(oPatterns.get("ArgColHead"))) {
			for (int colNum = 1; colNum < header.length; colNum++) {
				if (!header[colNum].matches(oPatterns.get("ValColHead"))) {
					throw new LoadException(resources.getString("wrongTableHeaderExc"));
				}
			}
			return TableType.OUTPUT;
		}

		throw new LoadException(resources.getString("wrongTableHeaderExc"));
	}
	
		/**
	 * @return the tableType
	 */
	public TableType getTableType() {
		return tableType;
	}

	/**
	 * @return the arguments
	 */
	public ArrayList<Double> getArguments() {
		return arguments;
	}
	
	/**
	 * 
	 * @param constValue - constant value that determine function table
	 * @param arg - function argument
	 * @return the corresponding value of the function
	 */
	public double getFuncValue(double constValue, double arg){
		return fTables.get(constValue).get(arg);
	}
}
