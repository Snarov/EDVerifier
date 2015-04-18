/*
 * BSUIR, Department of Electronics. 2015
 * Developed by Kiskin
 *
 */
package edverifier.model;

import edverifier.model.IO.Reader;
import java.io.File;
import java.io.IOException;
import edverifier.EDVerifier;
import java.util.ArrayList;

/**
 * class that encapsulates objects and provides methods related to the application business-logic layer this is restricted
 * implementation of classic MVC model so it can interact only with one view at the same time.
 *
 * @author Kiskin
 */
public class Model {

	private final CharacteristicTableManager tableManager = new CharacteristicTableManager();

	private Double h11;
	private Double h12;
	private Double h21;
	private Double h22;

	private WorkPoint workPoint;

	/**
	 * loadTable method uses {@link EDVerifier#readersMap} for determine a class used for load. Then this class is loaded by dint
	 * of reflection	mechanism. Method read file until it gets one input characteristic table and on output characteristic table.
	 *
	 * @param file file for load
	 * @throws TableReadException
	 * @throws java.io.IOException
	 * @throws edverifier.model.CalculateException
	 */
	public void loadTables(File file) throws TableReadException, IOException, CalculateException{
		if (file == null) {
			return;
		}
		//get the file extension
		String filepath = file.getPath();
		String fileExtension = filepath.substring(filepath.lastIndexOf('.'));

		if (!EDVerifier.readersMap.containsKey(fileExtension)) {
			throw new TableReadException("unsupportedFormatErr");
		}

		Reader reader = null;
		try {

			reader = (Reader) Class.forName(EDVerifier.readersMap.get(fileExtension)).newInstance();
		} catch (ClassNotFoundException ex) {
			System.err.printf("Reader class load error: %s\n", ex.getMessage());
		} catch (InstantiationException | IllegalAccessException ex) {
			System.err.printf("Reader class instantination error: %s\n", ex.getMessage());
		}

		//we need two tables with different types
		if (reader != null) {
			reader.open(filepath);
			String[][] rawTable = reader.readNextTable();
			CharacteristicTable characteristicTable = new CharacteristicTable(rawTable);

			tableManager.loadTable(characteristicTable);

			CharacteristicTable.TableType firstTableType = characteristicTable.getTableType();
			if (firstTableType != null) {
				do {	// searching for table with different type
					rawTable = reader.readNextTable();
					characteristicTable = new CharacteristicTable(rawTable);
				} while (characteristicTable.getTableType() == firstTableType);
			}

			tableManager.loadTable(characteristicTable);
		}

		recalculate();
		informView();
	}

	/**
	 * cleans all model data and informs view about it
	 */
	public void clean() {
		tableManager.clean();
		h11 = h12 = h21 = h22 = null;
		workPoint = null;
		
		informView();
	}

	/**
	 * recalculates all data in the model (e.g. h-parameters etc.). Informs the view that it should be refreshed in accordance
	 * with changes in the model
	 * 
	 * @throws CalculateException 
	 */
	private void recalculate() throws CalculateException {
		CharacteristicTable iTable = tableManager.getiTable();
		CharacteristicTable oTable = tableManager.getoTable();

		if (iTable == null || oTable == null) {
			throw new CalculateException("warning-table-not-loaded");
		}

		ArrayList<Double> amperages = iTable.getArguments();
		ArrayList<Double> voltages = oTable.getArguments();
		ArrayList<Double> constValuesI = oTable.getConstValues();
		ArrayList<Double> constValuesU = iTable.getConstValues();

		if (amperages.size() <= 1 || voltages.size() <= 1 || constValuesI.size() <= 1 || constValuesU.size() <= 1) {
			throw new CalculateException("warning-insufficient-info");
		}

		double dU1;	//delta U on input
		double dI1; //delta I on input
		double dU2; //delta U on output
		double dI2; //delta I on output
		//argNum is useful for determine numbers of arguments in the list of arguments that are used to calculate dI and dU
		int argNum;
//		//constValueNum is useful for determine numbers of constValues in the list of constValues
//		//that are used to calculate dI and dU
		int constValueNum;

		//h11
		argNum = amperages.indexOf(workPoint.getAmperage());
		if (argNum == -1) {
			throw new CalculateException("warning-invalid-tables-structure");
		}

		if (argNum == 0) {
			dI1 = amperages.get(1) - amperages.get(0);
			dU1 = iTable.getFuncValue(workPoint.getVoltage(), amperages.get(1))
					- iTable.getFuncValue(workPoint.getVoltage(), amperages.get(0));
		} else if (argNum == amperages.size() - 1) {
			dI1 = amperages.get(argNum) - amperages.get(argNum - 1);
			dU1 = iTable.getFuncValue(workPoint.getVoltage(), amperages.get(argNum))
					- iTable.getFuncValue(workPoint.getVoltage(), argNum - 1);
		} else {
			dI1 = amperages.get(argNum + 1) - amperages.get(argNum - 1);
			dU1 = iTable.getFuncValue(workPoint.getVoltage(), amperages.get(argNum + 1))
					- iTable.getFuncValue(workPoint.getVoltage(), argNum - 1);
		}

		h11 = dU1 / dI1;

		//h12
		constValueNum = constValuesU.indexOf(workPoint.getVoltage());
		if (constValueNum == 0){
			++constValueNum;
		}
		dU2 = constValuesU.get(constValueNum) - constValuesU.get(constValueNum - 1);
		dU1 = iTable.getFuncValue(constValuesU.get(constValueNum), workPoint.getAmperage())
				- iTable.getFuncValue(constValuesU.get(constValueNum - 1), workPoint.getAmperage());
		
		
		h12 = dU1 / dU2;
		
		
		//h21
		constValueNum = constValuesI.indexOf(workPoint.getAmperage());
		if (constValueNum == 0){
			++constValueNum;
		}
		dI1 = constValuesI.get(constValueNum) - constValuesI.get(constValueNum - 1);
		dI2 = oTable.getFuncValue(constValuesI.get(constValueNum), workPoint.getVoltage())
				- oTable.getFuncValue(constValuesI.get(constValueNum - 1), workPoint.getVoltage());
		h12 = dI2 / dI1;

		//h22
		argNum = voltages.indexOf(workPoint.getVoltage());
		if (argNum == -1) {
			throw new CalculateException("invalid-tables-structure");
		}

		if (argNum == 0) {
			dU2 = voltages.get(1) - voltages.get(0);
			dI2 = oTable.getFuncValue(workPoint.getAmperage(), voltages.get(1))
					- oTable.getFuncValue(workPoint.getAmperage(), voltages.get(0)); 
		} else if (argNum == amperages.size() - 1) {
			dU2 = voltages.get(argNum) - voltages.get(argNum - 1);
			dI2 = oTable.getFuncValue(workPoint.getAmperage(), voltages.get(argNum))
					- oTable.getFuncValue(workPoint.getAmperage(), voltages.get(argNum - 1)); 
		} else {
			dU2 = voltages.get(argNum + 1) - voltages.get(argNum - 1);
			dI2 = oTable.getFuncValue(workPoint.getAmperage(), voltages.get(argNum + 1))
					- oTable.getFuncValue(workPoint.getAmperage(), voltages.get(argNum - 1)); 
		}
		
		h22 = dI2 / dU2;
	}

	/**
	 * inform view about changes using predefined View method
	 */
	private void informView() {
		//TODO: add behaviour
	}
}
