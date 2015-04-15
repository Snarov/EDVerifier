/*
 * BSUIR, Department of Electronics. 2015
 * Developed by Kiskin
 *
 */
package edverifier.view;

import javafx.stage.Stage;

/**
 *	Class that encapsulates application stage and provides methods that used by model
 * @author Kiskin
 */
public class View {
	private final Stage appStage;
	
	public View(Stage appStage){
		this.appStage = appStage;
	}
	
	// TODO: add interface methods for interacting with a model

	/**
	 * @return the appStage
	 */
	public Stage getAppStage() {
		return appStage;
	}
}
