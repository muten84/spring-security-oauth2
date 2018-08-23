/**
 * 
 */
package it.eng.areas.ems.mobileagent.jnative.win32;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;

import it.eng.areas.ems.mobileagent.device.IBattery;
import it.eng.areas.ems.mobileagent.device.battery.BatteryConfiguration;
import it.eng.areas.ems.mobileagent.device.battery.BatteryData;
import it.eng.areas.ems.mobileagent.device.battery.BatteryEvent;
import it.eng.areas.ems.mobileagent.device.battery.BatteryObservable;
import it.eng.areas.ems.mobileagent.device.battery.BatteryObserver;

/**
 * @author Bifulco Luigi
 *
 */
public class Win32Battery implements IBattery {

	private BatteryObservable observable;

	private ScheduledExecutorService worker;

	private SYSTEM_POWER_STATUS batteryStatus;

	public Win32Battery() {
		batteryStatus = new SYSTEM_POWER_STATUS();
	}

	public void start(BatteryConfiguration configuration) {
		if (configuration.isInteralPoller()) {
			observable = new BatteryObservable();

			ThreadFactory fact = new ThreadFactoryBuilder().setNameFormat("BatteryPoller-thread-%d").build();

			worker = Executors.newSingleThreadScheduledExecutor(fact);
			worker.scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {
					BatteryData data = new BatteryData();
					data.setAcConnected(getBatteryAcConnected());
					data.setLifePercent(getBatteryLevelPercent());
					observable.setAction(new BatteryEvent(data), true);

				}
			}, 5000, configuration.getReadOffsetInSeconds(), TimeUnit.SECONDS);
		}

	}

	public void addObserver(BatteryObserver observer) {
		if (observer == null) {
			return;
		}
		observable.addObserver(observer);
	}

	public void remObserver(Observer observer) {
		if (observer == null) {
			return;
		}
		observable.deleteObserver(observer);
	}

	@Override
	public double getBatteryLevelPercent() {
		INSTANCE.GetSystemPowerStatus(batteryStatus);
		return batteryStatus.getBatteryLifePercent();
	}

	@Override
	public boolean getBatteryAcConnected() {
		INSTANCE.GetSystemPowerStatus(batteryStatus);
		return batteryStatus.getACLineStatusString().equals("Online");
	}

	public static IKernel32 INSTANCE = (IKernel32) Native.loadLibrary("Kernel32", IKernel32.class);

	public interface IKernel32 extends StdCallLibrary {

		public int GetSystemPowerStatus(SYSTEM_POWER_STATUS result);
	}

	public static class SYSTEM_POWER_STATUS extends Structure {
		public byte ACLineStatus;
		public byte BatteryFlag;
		public byte BatteryLifePercent;
		public byte Reserved1;
		public int BatteryLifeTime;
		public int BatteryFullLifeTime;

		protected List getFieldOrder() {
			ArrayList fields = new ArrayList();
			fields.add("ACLineStatus");
			fields.add("BatteryFlag");
			fields.add("BatteryLifePercent");
			fields.add("Reserved1");
			fields.add("BatteryLifeTime");
			fields.add("BatteryFullLifeTime");
			return fields;
		}

		/**
		 * The AC power status
		 */
		public String getACLineStatusString() {
			switch (ACLineStatus) {
			case (0):
				return "Offline";
			case (1):
				return "Online";
			default:
				return "Unknown";
			}
		}

		/**
		 * The battery charge status
		 */
		public String getBatteryFlagString() {
			switch (BatteryFlag) {
			case (1):
				return "High, more than 66 percent";
			case (2):
				return "Low, less than 33 percent";
			case (4):
				return "Critical, less than five percent";
			case (8):
				return "Charging";
			case ((byte) 128):
				return "No system battery";
			default:
				return "Unknown";
			}
		}

		/**
		 * The percentage of full battery charge remaining
		 */
		public int getBatteryLifePercent() {
			return (BatteryLifePercent == (byte) 255) ? -1 : BatteryLifePercent;
		}

		/**
		 * The number of seconds of battery life remaining
		 */
		public String getBatteryLifeTime() {
			return (BatteryLifeTime == -1) ? "Unknown" : BatteryLifeTime + " seconds";
		}

		/**
		 * The number of seconds of battery life when at full charge
		 */
		public String getBatteryFullLifeTime() {
			return (BatteryFullLifeTime == -1) ? "Unknown" : BatteryFullLifeTime + " seconds";
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("ACLineStatus: " + getACLineStatusString() + "\n");
			sb.append("Battery Flag: " + getBatteryFlagString() + "\n");
			sb.append("Battery Life: " + getBatteryLifePercent() + "% \n");
			sb.append("Battery Left: " + getBatteryLifeTime() + "\n");
			sb.append("Battery Full: " + getBatteryFullLifeTime() + "\n");
			return sb.toString();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.eng.areas.ems.mobileagent.device.IBattery#shutdown()
	 */
	@Override
	public boolean shutdown() {
		this.worker.shutdownNow();
		this.observable = null;
		this.batteryStatus = null;
		this.worker = null;
		return true;
	}

}
