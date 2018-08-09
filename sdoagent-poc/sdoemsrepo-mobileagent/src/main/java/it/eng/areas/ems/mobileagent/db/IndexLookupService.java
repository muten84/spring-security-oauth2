package it.eng.areas.ems.mobileagent.db;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.garret.perst.Index;
import org.garret.perst.Key;
import org.garret.perst.Storage;

import it.eng.area118.mdo.jme.data.Item;

public abstract class IndexLookupService implements LookupService {

	protected Index index;
	protected Class entityClass;
	protected Storage storage;

	public IndexLookupService(Index index, Class entityClass, Storage storage) {
		super();
		this.index = index;
		this.entityClass = entityClass;
		this.storage = storage;
	}

	public List lookup(String pattern) {
		Key from = new Key(pattern, true);
		Key to = new Key(getNextString(pattern), true);
		Iterator iterator = index.iterator(from, to, Index.ASCENT_ORDER);
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
			Item toBeFiltered = (Item) iterator.next();
			list.add(toBeFiltered);
		}
		return new ArrayList(list);
	}

	public abstract Comparator getComparator();

	public List getAll() {
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
			Item toBeFiltered = (Item) iterator.next();
			if (!list.contains(toBeFiltered)) {
				//System.out.println(">>>>" + toBeFiltered.getName());
				list.add(toBeFiltered);
			}

		}
		return new ArrayList(list);

	}

	public LookupViewer getViewer() {
		return new LookupViewer() {
			public String show(Object item) {
				if (item != null) {
					return ((Item) item).getName();
				}
				return "";
			}
		};
	}

	/**
	 * Genera una stringa lessicalmente successiva a quella data
	 * 
	 * @param s
	 * @return
	 */
	private String getNextString(String s) {
		char[] chars = s.toUpperCase().toCharArray();
		int overflow = 1;
		for (int i = chars.length - 1; i > 0; i--) {
			if (overflow == 1) {
				if (chars[i] == 'Z') {
					chars[i] = 'A';
					overflow = 1;
				} else {
					chars[i] = (char) (chars[i] + overflow);
					overflow = 0;
				}
			}
		}
		String newString = new String(chars);
		return newString;
	}
}
