/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.persistence;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * @author Bifulco Luigi
 *
 */
public class DocumentTransactionalServiceTestHelper {

	public static byte[] readDocumentFromFile(String path) throws IOException {
		return FileUtils.readFileToByteArray(new File(path));
	}

	public static File saveDocument(String path, byte[] bytes) throws IOException {
		File f = new File(path);
		FileUtils.writeByteArrayToFile(f, bytes);
		return f;
	}

}
