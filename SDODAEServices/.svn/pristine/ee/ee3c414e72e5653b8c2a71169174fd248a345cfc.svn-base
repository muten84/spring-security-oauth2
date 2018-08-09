/**
 *
 */
package it.eng.areas.ems;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Bifulco Luigi
 *
 */

public class Bower2POM {

		public static final int VERSION = 2;

	/*
	 * <dependency> <groupId>commons-io</groupId> <artifactId>commons-io</artifactId> <version>2.4</version>
	 * </dependency>
	 */
	public static void _main(String[] args) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		byte[] file = FileUtils.readFileToByteArray(new File("src/main/resources/bower.json"));
		BowerObject bower = mapper.readValue(file, BowerObject.class);
		Map<String, String> deps = bower.getDependencies();
		Set<String> keys = deps.keySet();
		for (String k : keys) {
			String version = deps.get(k).replace("^", "");
			StringBuffer s = new StringBuffer();
			s.append("<dependency>");
			s.append("\n");
			s.append("<groupId>org.webjars</groupId>");
			s.append("\n");
			s.append("<artifactId>" + k + "</artifactId>");
			s.append("\n");
			s.append("<version>" + version + "</version>");
			s.append("\n");
			s.append("</dependency>");
			s.append("\n");

			System.out.println(s.toString());

		}
		// System.out.println(bower.getDependencies().size());
	}

}
