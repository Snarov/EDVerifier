/*
 * BSUIR, Department of Electronics. 2015
 * Developed by Kiskin
 *
 */
package edverifier.model.IO;

import java.io.IOException;

/**
 * Readers for all supported formats implements this interface
 * @author Kiskin
 */
public interface Reader {
	String[][] readNextTable();
	void open(String filepath) throws IOException;
}
