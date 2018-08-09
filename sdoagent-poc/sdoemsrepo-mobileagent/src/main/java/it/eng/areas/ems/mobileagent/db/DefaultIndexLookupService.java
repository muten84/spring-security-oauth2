package it.eng.areas.ems.mobileagent.db;

import java.util.Comparator;

import org.garret.perst.Index;
import org.garret.perst.Storage;

public class DefaultIndexLookupService extends IndexLookupService {

	public DefaultIndexLookupService(Index index, Class entityClass, Storage storage) {
		super(index, entityClass, storage);
		// TODO Auto-generated constructor stub
	}

	public Comparator getComparator() {
		// TODO Auto-generated method stub
		return null;
	}

}
