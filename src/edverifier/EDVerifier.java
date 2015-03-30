/*
 * EDVerifier
 * BSUIR, Department of Electronics. 2015
 * Developed by Kiskin
 *
 */
package edverifier;

import edverifier.model.CharacteristicTable;
import edverifier.model.CharacteristicTableManager;
import edverifier.model.IO.Reader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class that represents entire application. This class contains data structures that are part of application. Methods realizes
 * logic of the application.
 *
 * @author Kiskin
 * @version 1.0
 */
public class EDVerifier extends Application {

	private final static String DEF_LANG = "ru";
	private final static String DEF_LOCATION = "RU";
	private static final String LOCALE_PATH = "bundles/locale";

	public static EDVerifier app;		//genius thing

	/**
	 * {@value map establishing the correspondence between file extension and reader class for it}
	 */
	private static final HashMap<String, String> readersMap = new HashMap<String, String>() {
		{
			put(".xls", "edverifier.model.IO.XLSSpreadSheetReader");
		}
	};

	private ResourceBundle resources = ResourceBundle.getBundle(LOCALE_PATH, new Locale(DEF_LANG, DEF_LOCATION));
	private final CharacteristicTableManager tableManager = new CharacteristicTableManager();

	@Override
	public void start(Stage stage) {
		app = this;

		try {
			Parent root = FXMLLoader.load(getClass().getResource("view/EDVerifier.fxml"), resources);
			stage.setScene(new Scene(root));
			stage.show();
		} catch (IOException ex) {
			System.err.printf("Error during load resources: %s\n", ex.getMessage());
			System.exit(1);
		}

		try {
			loadTables("/home/kiskin/Documents/Untitled 1.xls");
		} catch (LoadException ex) {
		} catch (IOException ex) {
		}
	}

	/**
	 * loadTable method uses {@link #readersMap} for determine a class used for load. Then this class is loaded by dint of
	 * reflection	mechanism. Method read file until it gets one input characteristic table and on output characteristic table.
	 *
	 * @param filepath - path to file for load
	 * @throws javafx.fxml.LoadException
	 */
	public void loadTables(String filepath) throws LoadException, IOException {
		//get the file extension
		String fileExtension = filepath.substring(filepath.lastIndexOf('.'));

		if (!readersMap.containsKey(fileExtension)) {
			throw new LoadException(resources.getString("unsupportedFormatExc"));
		}

		Reader reader = null;
		try {

			reader = (Reader) Class.forName(readersMap.get(fileExtension)).newInstance();
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
			
			app.getTableManager().loadTable(characteristicTable);
			
			CharacteristicTable.TableType firstTableType = characteristicTable.getTableType();
			do {	// searching for table with different type
				rawTable = reader.readNextTable();
				characteristicTable = new CharacteristicTable(rawTable);
			} while (characteristicTable.getTableType() == firstTableType);
			
			app.getTableManager().loadTable(characteristicTable);
		}

	}

	/**
	 * @return the resources
	 */
	public ResourceBundle getResources() {
		return resources;
	}

	/**
	 * @return the tableManager
	 */
	public CharacteristicTableManager getTableManager() {
		return tableManager;
	}

}
