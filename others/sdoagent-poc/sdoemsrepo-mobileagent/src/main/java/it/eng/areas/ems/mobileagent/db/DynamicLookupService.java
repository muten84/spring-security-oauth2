package it.eng.areas.ems.mobileagent.db;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.garret.perst.Index;
import org.garret.perst.Storage;

import it.eng.area118.mdo.jme.data.Dynamic;

public class DynamicLookupService extends IndexLookupService {

	public DynamicLookupService(Index index, Storage storage) {
		super(index, Dynamic.class, storage);
	}

	public List lookup(String pattern) {
		Iterator iterator = index.iterator();
		Set list = new HashSet();

		while (iterator.hasNext()) {
			Dynamic dynamic = (Dynamic) iterator.next();

			if (dynamic.getName().equals("ALTRA DINAMICA") || dynamic.getName().equals("NON DEFINITA")) {
				list.add(dynamic);
			}
			if (pattern.equals(Dynamic.ACCIDENT) && dynamic.isAccident()) {
				list.add(dynamic);
			} else if (pattern.equals(Dynamic.OTHER) && !dynamic.isAccident()) {
				list.add(dynamic);
			}
		}
		return new ArrayList(list);
	}

	public Comparator getComparator() {
		// TODO Auto-generated method stub
		return null;
	}

}
