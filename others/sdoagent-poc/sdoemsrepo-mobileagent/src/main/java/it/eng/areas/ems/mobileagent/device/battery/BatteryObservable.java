/**
 * 
 */
package it.eng.areas.ems.mobileagent.device.battery;

import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * @author Bifulco Luigi
 *
 */
public class BatteryObservable extends Observable {

	ExecutorService worker;

	public BatteryObservable() {
		if (worker == null) {
			ThreadFactory fact = 
					  new ThreadFactoryBuilder().setNameFormat("BatteryObservableWorker-thread-%d").build();
			worker = Executors.newFixedThreadPool(1, fact);
		}
	}

	public void setAction(final BatteryEvent event, boolean asynch) {
		super.setChanged();

		if (asynch) {
			worker.submit(new Runnable() {

				@Override
				public void run() {
					notifyObservers(event);
				}
			});
		} else {
			notifyObservers(event);
		}
	}

}
