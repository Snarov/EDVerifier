/*
 * BSUIR, Department of Electronics. 2015
 * Developed by Kiskin
 *
 */
package edverifier.view;

import edverifier.model.Model;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Class that encapsulates application stage and provides methods for interacting with model
 *
 * @author Kiskin
 */
public class View {

	private final Stage appStage;

	//dynamic GUI elements which reflect changes in the model
	private final Label h11Value;
	private final Label h12Value;
	private final Label h21Value;
	private final Label h22Value;

	private final LineChart outCVC;
	private final LineChart inCVC;

	private final ComboBox amperageBox;
	private final ComboBox voltageBox;

	//view constraints
	private int hParamPrecision = 2;

	//cached values
	private double h11ValueCache;
	private double h12ValueCache;
	private double h21ValueCache;
	private double h22ValueCache;

	public View(Stage appStage) {
		this.appStage = appStage;

		//init dynamic GUI elements. Their IDs set in .fxml files
		Scene scene = appStage.getScene();

		h11Value = (Label) scene.lookup("#h11Value");
		h12Value = (Label) scene.lookup("#h12Value");
		h21Value = (Label) scene.lookup("#h21Value");
		h22Value = (Label) scene.lookup("#h22Value");

		outCVC = (LineChart) scene.lookup("#outCVC");
		inCVC = (LineChart) scene.lookup("#inCVC");

		amperageBox = (ComboBox) scene.lookup("#comboBoxI");
		voltageBox = (ComboBox) scene.lookup("#comboBoxU");

	}

	/**
	 * changes the view in accordance with the changed model
	 *
	 * @param model model containing data for render
	 * @param updateType bitmask that contains info about update
	 */
	public void onModelUpdated(Model model, int updateType) {

		if ((updateType & Model.WORK_POINTS_CHANGED) != 0) {
			amperageBox.getItems().clear();
			voltageBox.getItems().clear();

			amperageBox.getItems().addAll(model.getTableManager().getoTable().getConstValues());
			voltageBox.getItems().addAll(model.getTableManager().getiTable().getConstValues());
		}
		if ((updateType & Model.WORK_POINT_CHANGED) != 0) {
			h11ValueCache = model.getH11();
			h12ValueCache = model.getH12();
			h21ValueCache = model.getH21();
			h22ValueCache = model.getH22();
			
			//if workpoint has been set not by controller's (which is bound with that view) call
			amperageBox.setValue(model.getWorkPoint().getAmperage());
			voltageBox.setValue(model.getWorkPoint().getVoltage());
		}
		//TODO add if statement for TABLES_CHANGED
		
		refresh();
	}

	private void refresh() {
		//redraw h-params with current precision
		String formatString = String.format("%%.%df", hParamPrecision);
		
		h11Value.setText(String.format(formatString, h11ValueCache));
		h12Value.setText(String.format(formatString, h12ValueCache));
		h21Value.setText(String.format(formatString, h21ValueCache));
		h22Value.setText(String.format(formatString, h22ValueCache));
		
		//here may be something else
	}

	/**
	 * Simple method for set new value of h-parameters precision
	 *
	 * @param precision new precision for h-parameters
	 */
	public void changeHParamsPrecision(int precision) {
		hParamPrecision = precision;
		refresh();
	}

	/**
	 * @return the appStage
	 */
	public Stage getAppStage() {
		return appStage;
	}

}
