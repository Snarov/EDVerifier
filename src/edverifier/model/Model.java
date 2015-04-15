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

/**
 * class that encapsulates objects and provides methods related to the application business-logic layer
 * this is restricted implementation of classic MVC model so it can interact only with one view at the same time.
 * @author Kiskin
 */
public class Model {

	private final CharacteristicTableManager tableManager = new CharacteristicTableManager();

	/**
	 * loadTable method uses {@link #readersMap} for determine a class used for load. Then this class is loaded by dint of
	 * reflection	mechanism. Method read file until it gets one input characteristic table and on output characteristic table.
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
	public void clean(){
		//TODO: add behaviour
	}
	
	/**
	 * recalculates all data in the model (e.g. h-parameters, graphs etc.). Informs the view that it should be refreshed
	 * in accordance with changes in the model
	 */
	private void recalculate(){
		//TODO: add behaviour
	}
	
	/**
	 * inform view about changes using predefined View methods
	 */
	private void informView(){
		//TODO: add behaviour
	}
}
