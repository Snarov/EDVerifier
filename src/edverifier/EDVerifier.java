/*
 * EDVerifier
 * BSUIR, Department of Electronics. 2015
 * Developed by Kiskin
 *
 */
package edverifier;

import edverifier.controller.EDVerifierController;
import edverifier.model.Model;
import edverifier.view.View;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
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

	public static EDVerifier app;		//genius thing. It's my own invention so it is a pretty shitty... may be

	private final Model model = new Model();
	private View view;
	private EDVerifierController controller;

	private ResourceBundle resources = ResourceBundle.getBundle(LOCALE_PATH, new Locale(DEF_LANG, DEF_LOCATION));

	/**
	 * {@value map establishing the correspondence between file extension and reader class for it}
	 */
	public static final HashMap<String, String> readersMap = new HashMap<String, String>() {
		{
			put(".xls", "edverifier.model.IO.XLSSpreadSheetReader");
		}
	};

	@Override
	public void start(Stage stage) {
		app = this;
		Stage appStage = stage;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("view/EDVerifier.fxml"), resources);
		try {
			Parent root = loader.load();
			stage.setScene(new Scene(root));
			stage.show();
		} catch (IOException ex) {
			System.err.printf("Error during load resources: %s\n", ex.getMessage());
			System.exit(1);
		}

		view = new View(appStage);
		controller = loader.getController();
	}

	/**
	 * @return the resources
	 */
	public ResourceBundle getResources() {
		return resources;
	}

	/**
	 * @return the appStage
	 */
	public View getView() {
		return view;
	}

	/**
	 * @return the model
	 */
	public Model getModel() {
		return model;
	}

}
