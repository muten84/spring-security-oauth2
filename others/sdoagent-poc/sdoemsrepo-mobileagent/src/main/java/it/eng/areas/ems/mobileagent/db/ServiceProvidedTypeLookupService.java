package it.eng.areas.ems.mobileagent.db;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.garret.perst.Index;
import org.garret.perst.Storage;

import it.eng.area118.mdo.jme.data.Item;
import it.eng.area118.mdo.jme.data.ServiceProvided;

public class ServiceProvidedTypeLookupService extends IndexLookupService {

	protected String privilegedService = "VISITA GENERALE";
	private ServiceProvided privilegedServiceObj = null;

	public ServiceProvidedTypeLookupService(Index index, Storage storage) {
		super(index, ServiceProvided.class, storage);
		privilegedServiceObj = new ServiceProvided();
		privilegedServiceObj.setName(privilegedService);
	}

	public List getAll() {
		int cnt = 0;
		Iterator iterator = index.iterator();
		SortedSet list = null;
		if (getComparator() != null) {
			list = new TreeSet(getComparator());
		} else {
			list = new TreeSet(new Comparator() {
				public int compare(Object o1, Object o2) {
					Item i1 = (Item) o1;
					Item i2 = (Item) o2;
					return i1.getName().compareTo(i2.getName());
				}
			});
		}
		while (iterator.hasNext()) {
			ServiceProvided toBeFiltered = (ServiceProvided) iterator.next();
			// System.out.println(">>>>" + toBeFiltered.getName());
			if (toBeFiltered.getName().equals(privilegedServiceObj.getName())) {
				cnt++;
			}

			if (cnt > 1 && toBeFiltered.getName().equals(privilegedServiceObj.getName())) {
				continue;
			}
			list.add(toBeFiltered);

		}

		return new ArrayList(list);
	}

	public Comparator getComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				String firstName = ((ServiceProvided) o1).getName().trim();
				String secondName = ((ServiceProvided) o2).getName().trim();

				if (firstName.equals(privilegedService)) {
					return -1;
				}
				if (secondName.equals(privilegedService)) {
					return 1;
				}

				return firstName.compareTo(secondName);
			}
		};
	}

}
