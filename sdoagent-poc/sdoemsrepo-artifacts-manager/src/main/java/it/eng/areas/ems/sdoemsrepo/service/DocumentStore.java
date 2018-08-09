/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.service;

import java.util.Collection;
import java.util.List;

/**
 * @author Bifulco Luigi
 *
 */
public interface DocumentStore<E> {

	/**
	 * @param e
	 */
	void insert(E e);

	/**
	 * @return
	 */
	List<E> listAll();

	/**
	 * @param query
	 * @return
	 */
	List<E> find(String query);

	/**
	 * @param e
	 */
	E remove(E e);

	/**
	 * @param e
	 */
	void update(E e);

	/**
	 * 
	 */
	void removeAll();

	/**
	 * @param coll
	 */
	void insertAll(Collection<E> coll);

}
