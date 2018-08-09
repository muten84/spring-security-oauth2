/**
 * 
 */
package it.eng.areas.ems.mobileagent.jnative.win32;

import it.eng.areas.ems.mobileagent.device.IAudio;
import it.eng.areas.ems.mobileagent.device.IBattery;
import it.eng.areas.ems.mobileagent.device.IDisplay;
import it.eng.areas.ems.mobileagent.device.IGps;
import it.eng.areas.ems.mobileagent.device.audio.AudioServiceImpl;
import it.eng.areas.ems.mobileagent.device.battery.BatteryConfiguration;
import it.eng.areas.ems.mobileagent.device.battery.BatteryObserver;
import it.eng.areas.ems.mobileagent.device.gps.GPSData;
import it.eng.areas.ems.mobileagent.device.gps.GpsConfiguration;
import it.eng.areas.ems.mobileagent.device.gps.GpsObserver;
import it.eng.areas.ems.mobileagent.jnative.win32.gps.SerialGpsService;

/**
 * @author Bifulco Luigi
 *
 */
public class Win32Device {

	public static IBattery createBatteryService(BatteryConfiguration configuration, BatteryObserver observer) {
		Win32Battery bat = new Win32Battery();
		bat.start(configuration);
		bat.addObserver(observer);
		return bat;

	}

	public static IGps createGpsService(GpsConfiguration conf, GpsObserver o) {
		SerialGpsService service = new SerialGpsService();
		service.config(conf);
		service.addObserver(o);
		service.startup();
		return service;

	}

	public static IAudio createAudioService(String beepFileUrl) {
		AudioServiceImpl audio = new AudioServiceImpl();
		audio.config(beepFileUrl);
		audio.startup();
		return audio;
	}

	public static IDisplay createDisplay() {
		Win32Display d = new Win32Display();
		d.start();
		return d;
	}
	
	public static void main(String[] args) throws NumberFormatException, InterruptedException {
		long waitTime = 60000;
		if(args.length>0) {
			waitTime =Long.valueOf(args[0]);
		}
		GpsConfiguration conf = new GpsConfiguration();
		conf.setAutoPortScan(true);
		conf.setBaudRate(4800);
		conf.setPort("COM6");
		conf.setReadFrequencyInMillis(500);
		Win32Device.createGpsService(conf, new GpsObserver() {
			
			@Override
			public void onGpsStatus(short status) {
				System.out.println("onGpsStatus: "+status);
				
			}
			
			@Override
			public void onGpsData(GPSData data) {
				System.out.println("onGpsStatus: "+data.getLatitude()+" - "+data.getLongitude());
				
			}
		});
		Thread.sleep(waitTime);
	}

}
