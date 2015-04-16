/*
 * BSUIR, Department of Electronics. 2015
 * Developed by Kiskin
 *
 */
package edverifier.model;

/**
 *	simple class that describes a workPoint of BT with two fields: amperage and voltage
 * 
 * @author Kiskin
 */
public class WorkPoint {
	
	private Double amperage;
	private Double voltage;
	
	/**
	 * no-arg Constructor
	 */
	public WorkPoint() {}

	/**
	 * Constructor
	 * 
	 * @param amperage
	 * @param voltage 
	 */
	public WorkPoint(Double amperage, Double voltage) {
		this.amperage = amperage;
		this.voltage = voltage;
	}

	/**
	 * @return the amperage
	 */
	public Double getAmperage() {
		return amperage;
	}

	/**
	 * @param amperage the amperage to set
	 */
	public void setAmperage(Double amperage) {
		this.amperage = amperage;
	}

	/**
	 * @return the voltage
	 */
	public Double getVoltage() {
		return voltage;
	}

	/**
	 * @param voltage the voltage to set
	 */
	public void setVoltage(Double voltage) {
		this.voltage = voltage;
	}
			
	/**
	 * convenient method that is the same as call {@link #setAmperage()} and {@link #setVoltage()} with null argument
	 */
	public void clean(){
		amperage = voltage = null;
	}
	
}
