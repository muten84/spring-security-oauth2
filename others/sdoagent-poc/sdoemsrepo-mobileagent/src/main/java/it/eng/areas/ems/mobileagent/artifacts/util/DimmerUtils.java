/**
 * 
 */
package it.eng.areas.ems.mobileagent.artifacts.util;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.pmw.tinylog.Logger;

import it.eng.areas.ems.mobileagent.artifacts.ExecutionContext;
import it.eng.areas.ems.mobileagent.artifacts.JsonMapper;

/**
 * @author Bifulco Luigi
 *
 */
public class DimmerUtils {

	// private ExecutionContext ctx;

	private JsonMapper mapper;

	@Inject
	public DimmerUtils(JsonMapper mapper) {
		// this.ctx = ctx;
		this.mapper = mapper;
	}

	public void dimmerOn() throws IOException, InterruptedException {
		Runtime.getRuntime().exec("cmd.exe /c dimmer.exe", null, new File(System.getProperty("agent.workingDir")+"/bin"));
		// Process p = Runtime.getRuntime().exec("");
		// p.waitFor();
	}

	public void dimmerOff() throws IOException {
		Runtime.getRuntime().exec("taskkill /f /im dimmer.exe");
	}

	public void setBrightness(double opacity) throws IOException {
		// Logger.info();
		File f = new File(System.getenv("APPDATA") + "\\dimmer\\config.json");
		if (f.exists()) {
			try {
				String config = FileUtils.readFileToString(f);
				DimmerData data = this.mapper.parse(config, DimmerData.class);
				data.monitors.get("\\\\.\\DISPLAY1-0").setOpacity(opacity);
				FileUtils.writeStringToFile(f, this.mapper.stringify(data));
			} catch (Exception e) {
				Logger.error(e,"Exception in setBrightness");
			}
		}
	}

	private static class DimmerData {

		private DimmerConfig general;

		private Map<String, Monitor> monitors;

		/**
		 * @return the monitors
		 */
		public Map<String, Monitor> getMonitors() {
			return monitors;
		}

		/**
		 * @return the general
		 */
		public DimmerConfig getGeneral() {
			return general;
		}

		/**
		 * @param general
		 *            the general to set
		 */
		public void setGeneral(DimmerConfig general) {
			this.general = general;
		}

		/**
		 * @param monitors
		 *            the monitors to set
		 */
		public void setMonitors(Map<String, Monitor> monitors) {
			this.monitors = monitors;
		}

	}

	private static class DimmerConfig {
		private boolean globalEnabled;
		private boolean pollingEnabled;

		/**
		 * @return the globalEnabled
		 */
		public boolean isGlobalEnabled() {
			return globalEnabled;
		}

		/**
		 * @param globalEnabled
		 *            the globalEnabled to set
		 */
		public void setGlobalEnabled(boolean globalEnabled) {
			this.globalEnabled = globalEnabled;
		}

		/**
		 * @return the pollingEnabled
		 */
		public boolean isPollingEnabled() {
			return pollingEnabled;
		}

		/**
		 * @param pollingEnabled
		 *            the pollingEnabled to set
		 */
		public void setPollingEnabled(boolean pollingEnabled) {
			this.pollingEnabled = pollingEnabled;
		}

	}

	private static class Monitor {
		private double opacity;
		private long temperature;

		public Monitor() {
		}

		/**
		 * @return the opacity
		 */
		public double getOpacity() {
			return opacity;
		}

		/**
		 * @param opacity
		 *            the opacity to set
		 */
		public void setOpacity(double opacity) {
			this.opacity = opacity;
		}

		/**
		 * @return the temperature
		 */
		public long getTemperature() {
			return temperature;
		}

		/**
		 * @param temperature
		 *            the temperature to set
		 */
		public void setTemperature(long temperature) {
			this.temperature = temperature;
		}

	}
}
