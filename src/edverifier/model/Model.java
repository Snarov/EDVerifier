/*
 * BSUIR, Department of Electronics. 2015
 * Developed by Kiskin
 *
 */
package edverifier.model;

import static edverifier.EDVerifier.app;
import edverifier.model.IO.Reader;
import java.io.File;
import java.io.IOException;
import javafx.fxml.LoadException;
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
	 * @throws javafx.fxml.LoadException
	 */
	public void loadTables(File file) throws LoadException, IOException {
		if (file == null) {
			return;
		}
		//get the file extension
		String filepath = file.getPath();
		String fileExtension = filepath.substring(filepath.lastIndexOf('.'));

		if (!EDVerifier.readersMap.containsKey(fileExtension)) {
			throw new LoadException(app.getResources().getString("unsupportedFormatExc"));
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
	}

	/**
	 * cleans all model data and informs view about it
	 */
	public void clean() {
		tableManager.clean();
		h11 = h12 = h21 = h22 = null;
		workPoint = null;
	}

	/**
	 * recalculates all data in the model (e.g. h-parameters etc.). Informs the view that it should be refreshed in accordance
	 * with changes in the model
	 */
	private void recalculate() {
		CharacteristicTable iTable = tableManager.getiTable();
		CharacteristicTable oTable = tableManager.getoTable();

		if (iTable == null || oTable == null) {
			//TODO handle it
		}

		ArrayList<Double> amperages = iTable.getArguments();
		ArrayList<Double> voltages = oTable.getArguments();

		if (amperages.size() <= 1 || voltages.size() <= 1) {
			//TODO handle it
		}

		double dU1;	//delta U on input
		double dI1; //delata I on input
		//argNum is useful for determine numbers of arguments in the list of amperages that are used to calculate dI and dU
		int argNum;
		//h11
		//argNum is useful for determine numbers of arguments in the list of amperages that are used to calculate dI and dU
		argNum = amperages.indexOf(workPoint.getAmperage());
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
	}

	/**
	 * inform view about changes using predefined View method
	 */
	private void informView() {
		//TODO: add behaviour
	}
}
