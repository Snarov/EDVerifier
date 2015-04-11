/*
 * BSUIR, Department of Electronics. 2015
 * Developed by Kiskin
 *
 */
package edverifier.controller;

import static edverifier.EDVerifier.app;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Pair;

/**
 * FXML Controller class
 *
 * @author Kiskin
 */
public class EDVerifierController {

	@FXML
	private WorkspaceController workspaceController;

	@FXML
	private MenuItem fileSaveAsItem;
	@FXML
	private MenuItem fileSaveItem;
	@FXML
	private Button tbBtnSave;
	@FXML
	private Button tbBtnSaveAs;
	@FXML
	private CheckMenuItem menuICMI;
	@FXML
	private CheckMenuItem menuOCMI;
	@FXML
	private ToggleButton tbBtnShowInput;
	@FXML
	private ToggleButton tbBtnShowOutput;

	private boolean tableLoaded = false;

	/**
	 * {@value extensionPairs - pairs for extension filter in file chooser. First - description, second - file extensions}
	 */
	private final static ArrayList<Pair<String, String>> extensionPairs = new ArrayList<Pair<String, String>>() {
		{
			add(new Pair(app.getResources().getString("xls-description"), "*.xls"));
		}
	};

	/**
	 * handler runs file choosing dialog and runs model load method and may display error dialog message
	 *
	 * @param event event object
	 */
	@FXML
	private void handleFileOpen(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(app.getResources().getString("file-dialog-title"));

		for (Pair<String, String> extensionPair : extensionPairs) {
			fileChooser.getExtensionFilters().add(new ExtensionFilter(extensionPair.getKey(), extensionPair.getValue()));
		}

		File selectedFile = fileChooser.showOpenDialog(app.getAppStage());
		try {
			app.loadTables(selectedFile);
		} catch (IOException ex) {
			Alert errorDialog = new Alert(Alert.AlertType.ERROR, ex.getMessage());
			errorDialog.showAndWait();
		}

		if ((app.getTableManager().getiTable() != null || app.getTableManager().getoTable() != null) && !tableLoaded) {
			activateSaveItems();
			tableLoaded = true;
		}

	}

	@FXML
	private void initialize() {
		//binds together UI controls that activate/deactivate displaying of plots
		Bindings.bindBidirectional(tbBtnShowInput.selectedProperty(), menuICMI.selectedProperty());
		Bindings.bindBidirectional(tbBtnShowOutput.selectedProperty(), menuOCMI.selectedProperty());

		//		on change: property of corresponding line chart will be changed
		menuICMI.selectedProperty().addListener((ObservableValue<? extends Boolean> observable,
				Boolean oldValue,
				Boolean newValue) -> {
					workspaceController.cnangeChartVisibility(workspaceController.inCVC, newValue);
				});

		menuOCMI.selectedProperty().addListener((ObservableValue<? extends Boolean> observable,
				Boolean oldValue,
				Boolean newValue) -> {
					workspaceController.cnangeChartVisibility(workspaceController.outCVC, newValue);
				});
	}

	/**
	 * this handler call method clean() from table manager field in model to clean up fields that contains loaded tables
	 *
	 * @param event event object
	 */
	@FXML
	private void handleFileNew(ActionEvent event) {
		app.getTableManager().clean();
		if (!tableLoaded) {
			activateSaveItems();
		}
	}

	/**
	 * this handler terminate the application with saving its state
	 *
	 * @param event event object
	 */
	@FXML
	private void handleFileExit(ActionEvent event) {
		//TODO "save  app state"
		System.exit(0);
	}

	/**
	 * set property "disable" of save items in menu "file" in "false"
	 */
	private void activateSaveItems() {
		fileSaveAsItem.setDisable(false);
		fileSaveItem.setDisable(false);
		tbBtnSave.setDisable(false);
		tbBtnSaveAs.setDisable(false);
	}
}
