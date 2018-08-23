package it.eng.areas.ems.sdodaeservices.delegate.model;

import java.util.Comparator;

public class FRSortComparator implements Comparator<FirstResponder> {

	@Override
	public int compare(FirstResponder o1, FirstResponder o2) {
		if (o1.getCognome() == null) {
			return -1;
		}
		if (o2.getCognome() == null) {
			return 1;
		}
		if (o1.getCognome() == null && o2.getCognome() == null) {
			return 0;
		}
		return o1.getCognome().compareTo(o2.getCognome());
	}

}
