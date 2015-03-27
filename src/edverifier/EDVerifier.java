/*
 * EDVerifier
 * BSUIR, Department of Electronics. 2015
 * Developed by Kiskin
 *
 */
package edverifier;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Kiskin
 * @version 1.0
 */
public class EDVerifier extends Application {

	private final static String DEF_LANG = "ru";
	private final static String DEF_LOCATION = "RU";
	private static final String LOCALE_PATH = "bundles/locale";

	public static ResourceBundle resources = ResourceBundle.getBundle(LOCALE_PATH, new Locale(DEF_LANG, DEF_LOCATION));

	@Override
	public void start(Stage stage) {

		try {
			Parent root = FXMLLoader.load(getClass().getResource("EDVerifier.fxml"), resources);
			stage.setScene(new Scene(root));
			stage.show();
		} catch (IOException ex) {
			System.err.printf("Error during load resources: %s", ex.getMessage());
			System.exit(1);
		}
	}
	
}
	