/**
 * 
 */
package it.eng.areas.ems.mobileagent.jnative.win32;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.pmw.tinylog.Logger;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;

import it.eng.areas.ems.mobileagent.device.idle.IdleListener;

/**
 * @author Bifulco Luigi
 *
 */
public class Win32IdleTime {

	final static Lock lock = new ReentrantLock();
	final static Condition stateChanged = lock.newCondition();

	static State currentState = State.UNKNOWN;

	public interface Kernel32 extends StdCallLibrary {
		Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("kernel32", Kernel32.class);

		/**
		 * Retrieves the number of milliseconds that have elapsed since the system was
		 * started.
		 * 
		 * @see http://msdn2.microsoft.com/en-us/library/ms724408.aspx
		 * @return number of milliseconds that have elapsed since the system was
		 *         started.
		 */
		public int GetTickCount();
	};

	public interface User32 extends StdCallLibrary {
		User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);

		/**
		 * Contains the time of the last input.
		 * 
		 * @see http://msdn.microsoft.com/library/default.asp?url=/library/en-us/winui/winui/windowsuserinterface/userinput/keyboardinput/keyboardinputreference/keyboardinputstructures/lastinputinfo.asp
		 */
		public static class LASTINPUTINFO extends Structure {
			public int cbSize = 8;
			/// Tick count of when the last input event was received.
			public int dwTime;

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.sun.jna.Structure#getFieldOrder()
			 */
			@Override
			protected List<String> getFieldOrder() {
				return Arrays.asList(new String[] { "cbSize", "dwTime" });
			}
		}

		/**
		 * Retrieves the time of the last input event.
		 * 
		 * @see http://msdn.microsoft.com/library/default.asp?url=/library/en-us/winui/winui/windowsuserinterface/userinput/keyboardinput/keyboardinputreference/keyboardinputfunctions/getlastinputinfo.asp
		 * @return time of the last input event, in milliseconds
		 */
		public boolean GetLastInputInfo(LASTINPUTINFO result);
	};

	/**
	 * Get the amount of milliseconds that have elapsed since the last input event
	 * (mouse or keyboard)
	 * 
	 * @return idle time in milliseconds
	 */
	public static int getIdleTimeMillisWin32() {
		User32.LASTINPUTINFO lastInputInfo = new User32.LASTINPUTINFO();
		User32.INSTANCE.GetLastInputInfo(lastInputInfo);
		return Kernel32.INSTANCE.GetTickCount() - lastInputInfo.dwTime;
	}

	enum State {
		UNKNOWN, ONLINE, IDLE, AWAY
	};

	public static State checkState(State oldState, int idleTimeInSeconds, int awayTimeInSeconds) {
		int idleSec = getIdleTimeMillisWin32() / 1000;
		State newState = idleSec < idleTimeInSeconds ? State.ONLINE
				: idleSec > awayTimeInSeconds ? State.AWAY : State.IDLE;
		return newState;

	}

	private static class IdleSignaler implements Runnable {

		private int idleTime;
		private int awayTime;

		public IdleSignaler(State initialState, int idleTime, int awayTime) {
			this.changeState(initialState);
			this.idleTime = idleTime;
			this.awayTime = awayTime;
		}

		public void changeState(State newState) {
			// System.out.println("new state detected: " + newState);
			try {
				lock.lock();
				// System.out.println("lock acquired");
				currentState = newState;
				switch (newState) {
				case IDLE:
					// System.out.println("signal idle");
					stateChanged.signal();
					break;
				case AWAY:
					// System.out.println("signal away");
					stateChanged.signal();
					break;
				case ONLINE:
					// System.out.println("signal online");
					stateChanged.signal();
					break;
				case UNKNOWN:
					// System.out.println("signal unknown");
					stateChanged.signal();
					break;
				default:
					break;
				}
			} finally {
				lock.unlock();
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			State newState = checkState(currentState, this.idleTime, this.awayTime);
			if (newState != currentState) {
				this.changeState(newState);
			}
		}

	}

	private static class IdleMonitor implements Runnable {

		private IdleListener callback;

		public IdleMonitor(IdleListener callback) {
			this.callback = callback;
		}

		/**
		 * @param callback
		 *            the callback to set
		 */
		public void setIdleListener(IdleListener callback) {
			this.callback = callback;
		}

		@Override
		public void run() {
			while (true) {
				try {

					lock.lock();
					if (currentState == State.ONLINE || currentState == State.UNKNOWN) {
						try {
							// System.out.println("current state: " + currentState + "waiting for idle
							// condition");
							callback.onOnline();
							stateChanged.await();
							// System.out.println("change from " + currentState + " state to IDLE");
						} catch (InterruptedException e) {
							Logger.error(e,"InterruptedException during run");
						}
					} else if (currentState == State.IDLE) {
						try {
							// System.out.println("current state: " + currentState + "waiting for ONLINE
							// condition");
							callback.onIdle();
							stateChanged.await();
							// System.out.println("chenge from " + currentState + " state to ONLINE");
						} catch (InterruptedException e) {
							Logger.error(e,"InterruptedException during run");
						}
					} else if (currentState == State.AWAY) {
						try {
							// System.out.println("current state: " + currentState + "waiting for idle
							// condition");
							callback.onAway();
							stateChanged.await();
							// System.out.println("chenge from " + currentState + " state to ONLINE");
						} catch (InterruptedException e) {
							Logger.error(e,"InterruptedException during run");
						}
					}
				} finally {
					lock.unlock();
				}
			}

		}

	}

	static IdleMonitor monitor = new IdleMonitor(new IdleListener() {

		@Override
		public void onOnline() {
			// System.out.println("on online");

		}

		@Override
		public void onIdle() {
			// System.out.println("on onIdle");

		}

		@Override
		public void onAway() {
			// System.out.println("on onAway");

		}
	});

	static ScheduledExecutorService poller = null;
	static Thread monitorThread =null;

	public static void start(int idleTime, int awayTime, long checkFreq, IdleListener listener) {
		ThreadFactory fact = new ThreadFactoryBuilder().setNameFormat("IdleTimePoller-thread-%d").build();
		poller = Executors.newSingleThreadScheduledExecutor(fact);
		poller.scheduleAtFixedRate(new IdleSignaler(State.UNKNOWN, idleTime, awayTime), 1000, checkFreq,
				TimeUnit.MILLISECONDS);
		monitor.setIdleListener(listener);
		monitorThread = new Thread(monitor);
		monitorThread.start();

	}

	public static void stop() {
		poller.shutdownNow();
		monitorThread.interrupt();
		monitorThread = null;
		poller = null;
	}

	public static void main(String[] args) throws InterruptedException {
		if (!System.getProperty("os.name").contains("Windows")) {
			System.err.println("ERROR: Only implemented on Windows");
			System.exit(1);
		}

		Win32IdleTime.start(5, 10, 100, new IdleListener() {

			@Override
			public void onOnline() {
				System.out.println("Win32IdleTime: onOnline");

			}

			@Override
			public void onIdle() {
				System.out.println("Win32IdleTime: onIdle");

			}

			@Override
			public void onAway() {
				System.out.println("Win32IdleTime: onAway");

			}
		});
		Thread.sleep(60000);
	}
}
