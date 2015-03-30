/*
 * BSUIR, Department of Electronics. 2015
 * Developed by Kiskin
 *
 */
package edverifier.model;

/**
 * Class for perform load of characteristic tables into application context
 * @author kiskin
 */
public class CharacteristicTableManager {
	
	private CharacteristicTable iTable;
	private CharacteristicTable oTable;
	
	
	/**
	 * method for load characteristic table into application context. Loaded table overrides previous stored table and becomes
	 * active. Method is general for all  types of tables, it overrides table with the corresponding type.
	 * 
	 * @param newTable table that will be load
	 */
	public void loadTable(CharacteristicTable newTable){
		if (newTable.getTableType() == null){ //do not load empty table
			return;
		}
		
		if(newTable.getTableType() == CharacteristicTable.TableType.INPUT){
			iTable = newTable;
		}else{
			oTable = newTable;
		}		
	}

	/**
	 * @return the iTable
	 */
	public CharacteristicTable getiTable() {
		return iTable;
	}

	/**
	 * @return the oTable
	 */
	public CharacteristicTable getoTable() {
		return oTable;
	}
	
	
}
