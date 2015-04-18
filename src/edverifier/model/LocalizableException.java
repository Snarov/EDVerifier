/*
 * BSUIR, Department of Electronics. 2015
 * Developed by Kiskin
 *
 */
package edverifier.model;

import static edverifier.EDVerifier.app;

/**
 * Exception subclass that overrides {@link #getLocalizedMessage()}
 * @author Kiskin
 */
public class LocalizableException extends Exception{
	LocalizableException(String msg){
		super(msg);
	}
	
	@Override
	public String getLocalizedMessage(){
		return app.getResources().getString(getMessage());
	}
}
