package it.eng.areas.ems.mobileagent.db;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.garret.perst.Index;
import org.garret.perst.Storage;

import it.eng.area118.mdo.jme.data.Pathology;

public class PathologyLookupService extends IndexLookupService {

	public PathologyLookupService(Index index, Storage storage) {
		super(index, Pathology.class, storage);
	}

	public List lookup(String pattern) {
		Iterator iterator = index.iterator();
		ArrayList list = new ArrayList();
		while (iterator.hasNext()) {
			Pathology toBeFiltered = (Pathology) iterator.next();
			if (toBeFiltered.getPathologyClass().getName().indexOf(pattern) > -1) {
				list.add(toBeFiltered);
			}
		}
		return list;
	}

	public Comparator getComparator() {
		// TODO Auto-generated method stub
		return null;
	}

}
