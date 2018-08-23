/**
 * 
 */
package it.eng.areas.ems.mobileagent.test;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.profesorfalken.jpowershell.PowerShellNotAvailableException;

import it.eng.areas.ems.mobileagent.artifacts.util.InstallUtils;
import it.eng.areas.ems.mobileagent.http.SparkUtils;
import it.eng.areas.ems.sdoemsrepo.delegate.model.ArtifactInfo;
import it.eng.areas.ems.sdoemsrepo.delegate.model.InstallArtifactRequest;

import static spark.Spark.*;

import java.io.IOException;

/**
 * @author Bifulco Luigi
 *
 */

@RunWith(JUnit4.class)
public class UtilTest {

	@Test
	public final void testSpark() {
		Logger logger = Logger.getLogger(UtilTest.class);
		SparkUtils.createServerWithRequestLog(logger);

		get("/hello", (request, response) -> "world");
	}

	@Test
	public final void extractAndCopyTest() throws IOException {
		InstallUtils.extractAndCopy("/esel/terminal/download/NACI/terminal-1.3.0.zip", "/esel/terminal");
	}

	@Test
	public final void serializeInstallRequest() throws JsonProcessingException {
		InstallArtifactRequest request = new InstallArtifactRequest();
		ArtifactInfo info = new ArtifactInfo();
		info.setArtifactId("terminal");
		info.setArtifactType("zip");
		info.setGroupId("NACI");
		info.setVersion("1.3.0");

		request.setDestinationPath("/esel/terminal");
		request.setArtifact(info);
		ObjectMapper mapper = new ObjectMapper();
		//Logger.info(mapper.writeValueAsString(request));
	}

}
