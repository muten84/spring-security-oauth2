/**
 * 
 */
package it.eng.areas.ems.mobileagent.jnative.win32;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;

import org.pmw.tinylog.Logger;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.WinError;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.Winsvc;
import com.sun.jna.platform.win32.Winsvc.HandlerEx;
import com.sun.jna.platform.win32.Winsvc.SC_HANDLE;
import com.sun.jna.platform.win32.Winsvc.SERVICE_DESCRIPTION;
import com.sun.jna.platform.win32.Winsvc.SERVICE_MAIN_FUNCTION;
import com.sun.jna.platform.win32.Winsvc.SERVICE_STATUS;
import com.sun.jna.platform.win32.Winsvc.SERVICE_STATUS_HANDLE;
import com.sun.jna.platform.win32.Winsvc.SERVICE_TABLE_ENTRY;

/**
 * @author Bifulco Luigi
 *
 */
public class Win32Service {

	/**
	 * main.
	 *
	 * @param args
	 *            arguments
	 */
	public static void main(String[] args) {
		Win32Service service = new Win32Service();

		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("install")) {
				System.out.println(Win32Service.install());
			} else if (args[0].equalsIgnoreCase("uninstall")) {
				System.out.println(Win32Service.uninstall());
			} else {
				System.out.println("Arguments:");
				System.out.println("install   = install service");
				System.out.println("uninstall = uninstall service");
				System.out.println("<none>    = run service");
				System.exit(0);
			}
		} else {
			service.init();
		}
	}

	public static final String serviceName = "Win32Service";
	public static final String description = "TestService Description";
	private static final Set<String> SUFFIXES = new HashSet<String>();

	static {
		SUFFIXES.add("jna.jar");
		SUFFIXES.add("jna-test.jar");
		SUFFIXES.add("classes");
	}

	private final Object waitObject = new Object();
	private ServiceMain serviceMain;
	private ServiceControl serviceControl;
	private SERVICE_STATUS_HANDLE serviceStatusHandle;

	public Win32Service() {
	}

	/**
	 * Install the service.
	 *
	 * @return true on success
	 */
	public static boolean install() {
		boolean success = false;

		// It is assumed, that the ClassLoader loading Win32Service for the
		// unittest is
		// a) an URLClassLoader
		// b) holds all relevant dependencies
		String invocation;
		ClassLoader cl = Win32Service.class.getClassLoader();
		if (cl instanceof URLClassLoader) {
			StringBuilder sb = new StringBuilder();
			for (URL u : ((URLClassLoader) cl).getURLs()) {
				if ("file".equals(u.getProtocol())) {
					try {
						File f = new File(u.toURI());
						if (SUFFIXES.contains(f.getName())) {
							if (sb.length() != 0) {
								sb.append(";");
							}
							sb.append(f.getAbsolutePath());
						}
					} catch (URISyntaxException ex) {
						Logger.error(ex,"URISyntaxException during run");
					}
				}
			}
			invocation = String.format("java.exe -cp %s com.sun.jna.platform.win32.Win32Service", sb.toString());
			System.out.println("Invocation: " + invocation);
		} else {
			throw new IllegalStateException("Classloader loading Win32Service must be an URLClassLoader");
		}

		SERVICE_DESCRIPTION desc = new SERVICE_DESCRIPTION();
		desc.lpDescription = description;

		SC_HANDLE serviceManager = openServiceControlManager(null, Winsvc.SC_MANAGER_ALL_ACCESS);

		if (serviceManager != null) {
			SC_HANDLE service = Advapi32.INSTANCE.CreateService(serviceManager, serviceName, serviceName,
					Winsvc.SERVICE_ALL_ACCESS, WinNT.SERVICE_WIN32_OWN_PROCESS, WinNT.SERVICE_DEMAND_START,
					WinNT.SERVICE_ERROR_NORMAL, invocation, null, null, "\0", null, null);
			if (service != null) {
				success = Advapi32.INSTANCE.ChangeServiceConfig2(service, Winsvc.SERVICE_CONFIG_DESCRIPTION, desc);
				Advapi32.INSTANCE.CloseServiceHandle(service);
			} else {
				throw new IllegalStateException("Failed to install service ");
			}
			Advapi32.INSTANCE.CloseServiceHandle(serviceManager);
		}
		return success;
	}

	/**
	 * Uninstall the service.
	 *
	 * @return true on success
	 */
	public static boolean uninstall() {
		boolean success = false;

		SC_HANDLE serviceManager = openServiceControlManager(null, Winsvc.SC_MANAGER_ALL_ACCESS);

		if (serviceManager != null) {
			SC_HANDLE service = Advapi32.INSTANCE.OpenService(serviceManager, serviceName, Winsvc.SERVICE_ALL_ACCESS);

			if (service != null) {
				success = Advapi32.INSTANCE.DeleteService(service);
				Advapi32.INSTANCE.CloseServiceHandle(service);
			}
			Advapi32.INSTANCE.CloseServiceHandle(serviceManager);
		}
		return success;
	}

	/**
	 * Ask the ServiceControlManager to start the service.
	 *
	 * @return true on success
	 */
	public boolean start() {
		boolean success = false;

		SC_HANDLE serviceManager = openServiceControlManager(null, WinNT.GENERIC_EXECUTE);

		if (serviceManager != null) {
			SC_HANDLE service = Advapi32.INSTANCE.OpenService(serviceManager, serviceName, WinNT.GENERIC_EXECUTE);

			if (service != null) {
				success = Advapi32.INSTANCE.StartService(service, 0, null);
				Advapi32.INSTANCE.CloseServiceHandle(service);
			}
			Advapi32.INSTANCE.CloseServiceHandle(serviceManager);
		}

		return success;
	}

	/**
	 * Ask the ServiceControlManager to stop the service.
	 *
	 * @return true on success
	 */
	public boolean stop() {
		boolean success = false;

		SC_HANDLE serviceManager = openServiceControlManager(null, WinNT.GENERIC_EXECUTE);

		if (serviceManager != null) {
			SC_HANDLE service = Advapi32.INSTANCE.OpenService(serviceManager, serviceName, WinNT.GENERIC_EXECUTE);

			if (service != null) {
				SERVICE_STATUS serviceStatus = new SERVICE_STATUS();
				success = Advapi32.INSTANCE.ControlService(service, Winsvc.SERVICE_CONTROL_STOP, serviceStatus);
				Advapi32.INSTANCE.CloseServiceHandle(service);
			}
			Advapi32.INSTANCE.CloseServiceHandle(serviceManager);
		}

		return (success);
	}

	/**
	 * Initialize the service, connect to the ServiceControlManager.
	 */
	public void init() {
		serviceMain = new ServiceMain();
		SERVICE_TABLE_ENTRY entry = new SERVICE_TABLE_ENTRY();
		entry.lpServiceName = serviceName;
		entry.lpServiceProc = serviceMain;

		Advapi32.INSTANCE.StartServiceCtrlDispatcher((SERVICE_TABLE_ENTRY[]) entry.toArray(2));
	}

	/**
	 * Get a handle to the ServiceControlManager.
	 *
	 * @param machine
	 *            name of the machine or null for localhost
	 * @param access
	 *            access flags
	 * @return handle to ServiceControlManager or null when failed
	 */
	private static SC_HANDLE openServiceControlManager(String machine, int access) {
		return Advapi32.INSTANCE.OpenSCManager(machine, null, access);
	}

	/**
	 * Report service status to the ServiceControlManager.
	 *
	 * @param status
	 *            status
	 * @param win32ExitCode
	 *            exit code
	 * @param waitHint
	 *            time to wait
	 */
	private void reportStatus(int status, int win32ExitCode, int waitHint) {
		SERVICE_STATUS serviceStatus = new SERVICE_STATUS();
		serviceStatus.dwServiceType = WinNT.SERVICE_WIN32_OWN_PROCESS;
		serviceStatus.dwControlsAccepted = status == Winsvc.SERVICE_START_PENDING ? 0
				: (Winsvc.SERVICE_ACCEPT_STOP | Winsvc.SERVICE_ACCEPT_SHUTDOWN | Winsvc.SERVICE_CONTROL_PAUSE
						| Winsvc.SERVICE_CONTROL_CONTINUE);
		serviceStatus.dwWin32ExitCode = win32ExitCode;
		serviceStatus.dwWaitHint = waitHint;
		serviceStatus.dwCurrentState = status;

		Advapi32.INSTANCE.SetServiceStatus(serviceStatusHandle, serviceStatus);
	}

	/**
	 * Called when service is starting.
	 */
	public void onStart() {
		reportStatus(Winsvc.SERVICE_RUNNING, WinError.NO_ERROR, 0);
	}

	/*
	 * Called when service should stop.
	 */
	public void onStop() {
		reportStatus(Winsvc.SERVICE_STOP_PENDING, WinError.NO_ERROR, 25000);
	}

	/*
	 * Called when service should stop.
	 */
	public void onPause() {
		reportStatus(Winsvc.SERVICE_PAUSED, WinError.NO_ERROR, 0);
	}

	/*
	 * Called when service should stop.
	 */
	public void onContinue() {
		reportStatus(Winsvc.SERVICE_RUNNING, WinError.NO_ERROR, 0);
	}

	/**
	 * Implementation of the service main function.
	 */
	private class ServiceMain implements SERVICE_MAIN_FUNCTION {

		/**
		 * Called when the service is starting.
		 *
		 * @param dwArgc
		 *            number of arguments
		 * @param lpszArgv
		 *            pointer to arguments
		 */
		public void callback(int dwArgc, Pointer lpszArgv) {
			serviceControl = new ServiceControl();
			serviceStatusHandle = Advapi32.INSTANCE.RegisterServiceCtrlHandlerEx(serviceName, serviceControl, null);

			reportStatus(Winsvc.SERVICE_START_PENDING, WinError.NO_ERROR, 25000);

			onStart();

			try {
				synchronized (waitObject) {
					waitObject.wait();
				}
			} catch (InterruptedException ex) {
				Logger.error(ex,"InterruptedException during callback");
			}
			reportStatus(Winsvc.SERVICE_STOPPED, WinError.NO_ERROR, 0);

			// Avoid returning from ServiceMain, which will cause a crash
			// See http://support.microsoft.com/kb/201349, which recommends
			// having init() wait for this thread.
			// Waiting on this thread in init() won't fix the crash, though.
			// System.exit(0);
		}
	}

	/**
	 * Implementation of the service control function.
	 */
	private class ServiceControl implements HandlerEx {

		/**
		 * Called when the service get a control code.
		 *
		 * @param dwControl
		 * @param dwEventType
		 * @param lpEventData
		 * @param lpContext
		 */
		public int callback(int dwControl, int dwEventType, Pointer lpEventData, Pointer lpContext) {
			switch (dwControl) {
			case Winsvc.SERVICE_CONTROL_STOP:
			case Winsvc.SERVICE_CONTROL_SHUTDOWN:
				onStop();
				synchronized (waitObject) {
					waitObject.notifyAll();
				}
				break;
			case Winsvc.SERVICE_CONTROL_PAUSE:
				onPause();
				break;
			case Winsvc.SERVICE_CONTROL_CONTINUE:
				onContinue();
				break;
			}
			return WinError.NO_ERROR;
		}
	}
}
