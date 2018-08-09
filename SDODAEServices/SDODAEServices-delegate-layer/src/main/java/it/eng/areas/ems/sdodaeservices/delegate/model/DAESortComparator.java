package it.eng.areas.ems.sdodaeservices.delegate.model;

import java.util.Comparator;

public class DAESortComparator implements Comparator<Dae> {

	@Override
	public int compare(Dae o1, Dae o2) {
		if (o1.getDistance() == null) {
			return -1;
		}
		if (o2.getDistance() == null) {
			return 1;
		}
		if (o1.getDistance() == null && o2.getDistance() == null) {
			return 0;
		}
		return o1.getDistance().getDistance().compareTo(o2.getDistance().getDistance());
	}

}
