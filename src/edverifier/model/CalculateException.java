/*
 * BSUIR, Department of Electronics. 2015
 * Developed by Kiskin
 *
 */
package edverifier.model;

/**
 * Exception occurs when there is unable to calculate some part of model data (e.g. h-parameters)
 *
 * @author Kiskin
 */
public class CalculateException extends LocalizableException{
	CalculateException(String msg){
		super(msg);
	}
}
