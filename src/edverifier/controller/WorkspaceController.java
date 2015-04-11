/*
 * BSUIR, Department of Electronics. 2015
 * Developed by Kiskin
 *
 */
package edverifier.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.CheckBox;

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

}
