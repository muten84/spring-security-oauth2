package it.esel.terminal.db.perst;

import org.garret.perst.Index;
import org.garret.perst.Persistent;
import org.garret.perst.Storage;

/**
 * Classe radice contenuta nel database Perst
 * 
 * @author Mauro Miranda
 * 
 */
public class PerstRootBean extends Persistent {

	protected Index indexmap;

	/**
	 * Costuttore vuoto usato solo per la serializzazione
	 */
	public PerstRootBean() {
		super();
	}

	public PerstRootBean(Storage db) {
		super();
		indexmap = db.createIndex(String.class, true);
	}

	public Index getIndex(Class clazz, Storage db, boolean unique) {
		Index index = (Index) indexmap.get(clazz.getName());
		if (index == null) {
			index = db.createIndex(String.class, unique);
			indexmap.put(clazz.getName(), index);
		}
		return index;
	}

	public void addIndex(Class clazz, Index index) {
		indexmap.put(clazz.getName(), index);
	}

	public Index getIndexmap() {
		return indexmap;
	}

	public void setIndexmap(Index indexmap) {
		this.indexmap = indexmap;
	}

}
