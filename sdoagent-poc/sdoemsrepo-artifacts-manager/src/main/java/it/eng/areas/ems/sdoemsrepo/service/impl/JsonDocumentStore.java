/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.service.impl;

import java.util.Collection;
import java.util.List;

import io.jsondb.JsonDBTemplate;
import it.eng.areas.ems.sdoemsrepo.service.DocumentStore;

/**
 * @author Bifulco Luigi
 *
 */
public class JsonDocumentStore<E> implements DocumentStore<E> {

	private JsonDBTemplate db;

	private Class<E> entityType;

	public JsonDocumentStore(String storePath, Class<E> clazz) {
		this.entityType = clazz;
		this.db = new JsonDBTemplate(storePath, "it.eng.areas.ems.sdoemsrepo.delegate.model", null);
		if (!this.db.collectionExists(clazz)) {
			this.db.createCollection(clazz);
		}

	}

	@Override
	public void insert(E e) {
		this.db.insert(e);
	}

	@Override
	public List<E> listAll() {
		return this.db.getCollection(this.entityType);
	}

	@Override
	public List<E> find(String query) {
		return this.db.find(query, this.entityType);
	}

	@Override
	public E remove(E e) {
		return this.db.remove(e, this.entityType);
	}

	@Override
	public void update(E e) {
		this.db.upsert(e);
	}

	@Override
	public void removeAll() {
		this.db.dropCollection(this.entityType);
	}

	@Override
	public void insertAll(Collection<E> coll) {
		this.db.insert(coll, this.entityType);
	}

}
