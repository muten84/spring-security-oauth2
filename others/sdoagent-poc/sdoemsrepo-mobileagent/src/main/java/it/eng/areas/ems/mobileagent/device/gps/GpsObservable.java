package it.eng.areas.ems.mobileagent.device.gps;

import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class GpsObservable extends Observable {

	protected short status;

	ExecutorService worker;

	public GpsObservable() {
		if (worker == null) {
			ThreadFactory fact = 
					  new ThreadFactoryBuilder().setNameFormat("GpsObservableWorker-thread-%d").build();
			worker = Executors.newFixedThreadPool(1,fact);
		}				
	}

	public void setAction(final GpsEvent event, boolean asynch) {
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
	
	public void setAction(short status, boolean asynch) {
		super.setChanged();
		GpsEvent event = new GpsEvent(status);
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
