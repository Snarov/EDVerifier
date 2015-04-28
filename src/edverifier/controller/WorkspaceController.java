/*
 * BSUIR, Department of Electronics. 2015
 * Developed by Kiskin
 *
 */
package edverifier.controller;

import static edverifier.EDVerifier.app;
import edverifier.model.CalculateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;

/**
 * FXML Controller class
 *
 * @author Kiskin
 */
public class WorkspaceController {

	@FXML
	LineChart inCVC;
	@FXML
	LineChart outCVC;

	/**
	 * common for inCVC and outCVC. it calls on change of "selected" property of check box
	 */
	void cnangeChartVisibility(LineChart targetChart, Boolean newValue) {
		targetChart.setVisible(newValue);
		targetChart.setManaged(newValue);
	}

	/**
	 * handle choosing new value on work point amperage comboBox
	 */
	@FXML
	private void handleAmperageChange(ObservableValue<Double> observable, Double oldValue, Double newValue) {
		try {
			app.getModel().setWorkPoint(newValue, null);
		} catch (CalculateException ex) {
			showErrDialog(ex);
		}
	}

	/**
	 * handle choosing new value on work point voltage comboBox
	 */
	@FXML
	private void handleVoltageChange(ObservableValue<Double> observable, Double oldValue, Double newValue) {
		try {
			app.getModel().setWorkPoint(null, newValue);
		} catch (CalculateException ex) {
			showErrDialog(ex);
		}
	}

	private void showErrDialog(Exception ex) {
		Alert errorDialog = new Alert(Alert.AlertType.ERROR, ex.getLocalizedMessage());
		String localizedErrorWord = app.getResources().getString("error");
		errorDialog.setHeaderText(localizedErrorWord);
		errorDialog.setTitle(localizedErrorWord);
		errorDialog.getDialogPane().setMinHeight(Control.USE_PREF_SIZE);
		errorDialog.showAndWait();
	}

}
