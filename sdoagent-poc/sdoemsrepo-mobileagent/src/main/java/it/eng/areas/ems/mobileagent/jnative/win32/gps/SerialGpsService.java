/**
 * 
 */
package it.eng.areas.ems.mobileagent.jnative.win32.gps;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Observer;
import java.util.Properties;
import java.util.TimeZone;

import org.pmw.tinylog.Logger;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;
import it.eng.areas.ems.mobileagent.device.IGps;
import it.eng.areas.ems.mobileagent.device.gps.GPSData;
import it.eng.areas.ems.mobileagent.device.gps.GpsConfiguration;
import it.eng.areas.ems.mobileagent.device.gps.GpsEvent;
import it.eng.areas.ems.mobileagent.device.gps.GpsObservable;


/**
 * @author Bifulco Luigi
 *
 */
public class SerialGpsService implements IGps {
	
	
	public short STATUS_CONNECTED = 2;
	
	public short STATUS_AVAILABLE = 3;

	public short STATUS_TEMP_UNAVAILABLE = 0;

	public short STATUS_OUT_OF_ORDER = -1;
	
	public static final String PATTERN_STANDARD_DDMMYYYYHHMMSS = "dd/MM/yyyy HH:mm:ss";

	public static final String PATTERN_STANDARD_DDMMYYYHHMM = "dd/MM/yyyy HH:mm";

	public static final String PATTERN_ISO8601 = "dd/MM/yyyy'T'HH:mm:ssZ";

	public static final String PATTERN_HHMMSSDDMMYYY = "HH.mm.ss dd/MM/yyyy";

	public static final String PATTERN_HHMMSSmmm = "HHmmss.SSS";

	public static final String PATTERN_YYMMDD = "yyddMM";

	public static final String PATTERN_DDMMYY = "ddMMyy";

	public static final String PATTERN_DDMMYYYY = "ddMMyyyy";

	public static final String PATTERN_STANDARD_DDMMYYYY = "dd/MM/yyyy";

	public static final String PATTERN_STANDARD_HHMMSS = "HH:mm:ss";

	public static final String PATTERN_STANDARD_HHMM = "HH:mm";
	

	private GpsObservable observable;

	private short status;

	private boolean running;

	private String name;

	private GpsInfo tmpGpsInfo;

	private GpsInfo lastGpsInfo;

	private NMEAParser parser;

	private GpsReader gpsReader;

	private GpsConnector gpsConnector;

	private short unreadedAge;

	private boolean auto;

	public SerialGpsService() {
		name = "ESEL_GPS";
		observable = new GpsObservable();
		parser = new NMEAParser();
		status = STATUS_TEMP_UNAVAILABLE;
		gpsReader = new GpsReader();
		gpsConnector = new GpsConnector();
		unreadedAge = 0;
	}

	private void changeStatus(final short status) {
		short oldStatus = this.status;
		this.status = status;

		if (oldStatus != status) {
			Thread thd = new Thread() {
				public void run() {
					GpsEvent evt = new GpsEvent(status);
					observable.setAction(status, true);
				}
			};
			thd.setPriority(Thread.MIN_PRIORITY);
			thd.setDaemon(true);
			thd.start();
		}
	}

	public String getName() {
		return name;
	}

	public GPSData getPosition() {
		return info2data(lastGpsInfo);
	}
	
	public void config(GpsConfiguration config) {
		this.auto = config.isAutoPortScan();
		
		
	}

//	public void config(Configuration configuration) {
//		String strPropsFile = configuration.getParameterValue(name, "EXTERNAL_PROPS", "");
//		auto = configuration.getParameterValue(name, "AUTO_DETECT", true);
//
//		File file = new File(strPropsFile);
//		if (!file.exists()) {
//			String separator = System.getProperty("file.separator");
//			file = new File("bin" + separator + strPropsFile);
//		}
//
//		if (file.exists()) {
//			Properties props = new Properties();
//			try {
//				props.load(new FileInputStream(file));
//
//				gpsConnector.setPort(props.getProperty("PORT", ""));
//				gpsConnector.setBaudRate(Integer.parseInt(props.getProperty("BAUD_RATE", "4800")));
//			} catch (Exception e) {
//				Logger.error("GPS MODULE PROPS FILE LOAD FAILURE", e);
//			}
//		} else {
//			Logger.error("MISSING PROPS FILE FOR GPS MODULE (" + strPropsFile + ")");
//		}
//	}

	public boolean isRunning() {
		return running;
	}

	public boolean startup() {
		running = true;

		gpsConnector.start();
		gpsReader.start();

		return running;
	}

	public boolean shutdown() {
		running = false;

		gpsConnector.stop();
		gpsReader.stop();
		return true;
	}

	public void restart() {
		shutdown();
		startup();
	}

	public void suspend() {
	}

	public void resume() {
	}

	

	public void addObserver(Observer observer) {
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

	public short getStatus() {
		return status;
	}

	private double DM2DD(double c) {
		double deg = Math.floor(c * 1e-2);
		double min = c - deg * 1e2;
		return deg + min / 60.0;
	}

	private class GpsConnector implements Runnable {

		private boolean halted;

		private Thread thread;

		private String port;

		private int baudRate;

		private InputStream input;

		private OutputStream output;

		private SerialPort serialPort;

		private Map tablePort = new HashMap();

		public GpsConnector() {
			thread = new Thread(this, "GpsConnectorthread");
			thread.setPriority(Thread.MIN_PRIORITY);
			thread.setDaemon(true);

		}

		public void start() {
			thread.start();
		}

		public void stop() {
			changeStatus(STATUS_OUT_OF_ORDER);
			halted = true;
			thread.interrupt();
		}

		private void connect() {
			if (auto) {
				Enumeration ports = CommPortIdentifier.getPortIdentifiers();
				int count = 0;
				while (ports.hasMoreElements()) {
					count++;
					CommPortIdentifier p = (CommPortIdentifier) ports.nextElement();
					if (tablePort.containsKey(p.getName())) {
						Boolean notvalid = (Boolean) tablePort.get(p.getName());
						if (notvalid != null && notvalid.booleanValue()) {
							port = null;
							continue;
						}
					} else if (port == null) {
						port = p.getName();
					}
				}
				if (count == tablePort.size()) {
					tablePort.clear();
					return;
				}
			}

			System.out.println("gps device connecting to ..." + port);
			try {
				serialPort = (SerialPort) CommPortIdentifier.getPortIdentifier(port).open("GPSDriver", 60);
				serialPort.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

				input = serialPort.getInputStream();
				output = serialPort.getOutputStream();

				try {
					// consume first reading					
					readLine();
				} catch (Exception e) {					
					Logger.error(e,"first reading failure");
				}

				changeStatus(STATUS_AVAILABLE);
				insertEntry(port, new Boolean(false));

				System.out.println("gps device connected.");
			} catch (NoSuchPortException n1) {
				Logger.error(n1,"gps port not found..");
				insertEntry(port,  new Boolean(true));
				changeStatus(STATUS_OUT_OF_ORDER);				
			} catch (Exception e) {
				Logger.error(e,"gps startup failure.");
				insertEntry(port,  new Boolean(true));
				changeStatus(STATUS_OUT_OF_ORDER);
			}
		}

		private void disconnect() {
			insertEntry(port,  new Boolean(true));
			System.out.println("gps device disconnecting ...");
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					Logger.error(e,"disconnect exception");
				}
			}
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					Logger.error(e,"close exception");
				}
			}
			if (serialPort != null) {
				serialPort.close();
			}
			input = null;
			output = null;
			serialPort = null;

			changeStatus(STATUS_TEMP_UNAVAILABLE);

			System.out.println("gps device disconnected");
		}

		public void run() {
			changeStatus(STATUS_TEMP_UNAVAILABLE);
			while (!halted) {
				// synchronized (SYNCH_STATUS) {
				while (!halted && STATUS_AVAILABLE == status) {
					System.out.println("gps::run available and sleeping");
					Thread.yield();
					try {
						Thread.sleep(5000);
						// wait(5000);
					} catch (Exception e) {
						Logger.error(e,"exceptin during sleep");
						
					}
				}
				// }

				if (!halted) {
					// System.out.println("gps::run connection not available");
					if (serialPort != null) {
						disconnect();
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						Logger.error(e,"InterruptedException during sleep");
					}
					connect();
				}
			}

			disconnect();
		}

		// public int getBaudRate() {
		// return baudRate;
		// }

		public void setBaudRate(int baudRate) {
			this.baudRate = baudRate;
		}

		// public String getPort() {
		// return port;
		// }

		public void setPort(String port) {
			// this.port = port;
		}

		public String readLine() throws IOException {
			StringBuffer line = new StringBuffer();

			byte buffer;
			while ((buffer = (byte) input.read()) != -1) {
				if ((buffer == 13) || (buffer == 10)) {
					break;
				} else {
					line.append((char) buffer);
				}
			}
			//System.out.println("readling returning "+line);
			return line.toString();
		}

		private void insertEntry(String key, Boolean value) {
			if (key != null && value != null) {
				tablePort.put(key, value);
			}

		}
	}

	private class GpsReader implements Runnable {

		private Thread thread;

		private boolean halted;

		public GpsReader() {
			thread = new Thread(this, "GpsReaderThread");
			thread.setPriority(Thread.MIN_PRIORITY);
			thread.setDaemon(true);
			halted = false;
		}

		public void start() {
			thread.start();
		}

		public void stop() {
			halted = true;
		}

		public void run() {
			tmpGpsInfo = new GpsInfo();
			lastGpsInfo = new GpsInfo();

			long t1 = System.currentTimeMillis();
			long t2 = t1;

			while (!halted) {
				if (STATUS_AVAILABLE == status) {
					try {
						String msg = gpsConnector.readLine();
						//Thread.sleep(100);
						t2 = System.currentTimeMillis();

						if (msg != null && msg.length() > 0) {
							//System.out.println("gps msg : " + msg.length());

							unreadedAge = 0;

							if (parser.parse(msg, tmpGpsInfo)) {
								tmpGpsInfo.latitude = DM2DD(tmpGpsInfo.latitude);
								tmpGpsInfo.longitude = DM2DD(tmpGpsInfo.longitude);

								lastGpsInfo = (GpsInfo) tmpGpsInfo.clone();

								//TODO: test only int offset = 2000;
								int offset = 500;
								if (t2 - t1 > offset) {								
									t1 = t2;									
									//System.out.println("offset: " + t1);
									GPSData d = info2data(lastGpsInfo);
									GpsEvent evt = new GpsEvent(d);
									//Date curr = new Date(evt.getGpsData().getTimestamp());
									//System.out.println(DateFormat.getDateTimeInstance().format(curr));
									observable.setAction(evt, true);
								}								
							}
						} else {
							unreadedAge++;

							if (unreadedAge > 100) {
								System.out.println("GPS INACTIVE!");
								changeStatus(STATUS_TEMP_UNAVAILABLE);
								unreadedAge = 0;
							}
						}
					} catch (Exception e) {						
						Logger.error(e,"Gps reading failure");
						changeStatus(STATUS_TEMP_UNAVAILABLE);
						
					}
				} else {
					try {
						Thread.sleep(500);
					} catch (Exception e) {
						Logger.error(e,"Exception during sleep");												
					}
				}

				Thread.yield();
			}
		}
	}

	private GPSData info2data(GpsInfo info) {
		// Location loc = new Location();
		// loc.setCourse(info.course);
		// loc.setSpeed(info.speed);
		// loc.setTimestamp(normalizeTimestamp(info));
		// loc.setValid(true);
		// loc.setQualifiedCoordinates(new QualifiedCoordinates(info.latitude,
		// info.longitude));
		GPSData gpsData = new GPSData();

		gpsData.setAltitude(info.height);
		gpsData.setCourse(info.course);
		gpsData.setHdop(info.hdop);
		gpsData.setLatitude(info.latitude);
		gpsData.setLongitude(info.longitude);
		gpsData.setSatellite(info.satellites);
		gpsData.setSpeed(info.speed);
		try{
			gpsData.setTimestamp(normalizeTimestamp(info));
		}catch(Exception e){
			Logger.error(e,"Exception during normalizeTimestamp");
			gpsData.setTimestamp(new Date().getTime());
		}
		gpsData.setVdop(info.vdop);
		gpsData.setValid(true);

		return gpsData;
	}

	public static long normalizeTimestamp(GpsInfo info) {
		String timestamp = "";
		String pattern = "";
		if (info.date != null && info.date.length() > 0) {
			timestamp += info.date;
			pattern += PATTERN_DDMMYY;
		} else {
			timestamp += format(getDate(), PATTERN_DDMMYY, TimeZone.getTimeZone("UTC"));
			pattern += PATTERN_DDMMYY;
		}
		if (info.time != null && info.time.length() > 0) {
			timestamp += info.time;
			pattern += PATTERN_HHMMSSmmm;
		} else {
			timestamp += format(getDate(), PATTERN_HHMMSSmmm, TimeZone.getTimeZone("UTC"));
			pattern += PATTERN_HHMMSSmmm;
		}

		long normtime = getUTCDate().getTime();
		if (timestamp.length() > 0) {
			normtime = parse(timestamp, pattern, TimeZone.getTimeZone("UTC")).getTime();
		}
		return normtime;
	}
	
	public static String format(Date date, String pattern, TimeZone timezone) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setTimeZone(timezone);

		String output = null;
		try {
			return sdf.format(date);
		} catch (Exception e) {
			Logger.error(e,"Exception during format");
			
		}
		return output;
	}
	
	public static Date getUTCDate() {
		return GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC")).getTime();
	}
	
	public static Date getDate() {
		return GregorianCalendar.getInstance(Locale.ITALY).getTime();
	}
	
	public static Date parse(String date, String pattern) {
		return parse(date, pattern, TimeZone.getDefault());
	}

	public static Date parse(String date, String[] patterns) {
		return parse(date, patterns, TimeZone.getDefault());
	}

	public static Date parse(String date, String pattern, TimeZone timezone) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setTimeZone(timezone);

		Date output = null;
		try {
			output = sdf.parse(date);
		} catch (Exception e) {
			//e.printStackTrace();
			//Logger.error(e,"Exception during parse");
			output = Calendar.getInstance().getTime();
		}
		return output;
	}

	public static Date parse(String date, String[] patterns, TimeZone timezone) {
		Date output = null;
		for (int idx = 0; idx < patterns.length; idx++) {
			SimpleDateFormat sdf = new SimpleDateFormat(patterns[idx]);
			sdf.setTimeZone(timezone);
			try {
				output = sdf.parse(date);
				break;
			} catch (Exception e) {
				//e.printStackTrace();
				//Logger.error(e,"Exception during parse");
				output = Calendar.getInstance().getTime();
			}
		}
		return output;
	}
}
