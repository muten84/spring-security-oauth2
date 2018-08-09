/**
 * 
 */
package it.eng.areas.ems.mobileagent.message.db;

import io.jsondb.JsonDBTemplate;
import io.jsondb.crypto.ICipher;
import io.jsondb.query.CollectionSchemaUpdate;

/**
 * @author Bifulco Luigi
 *
 */
public class ExtendedJsonDBTemplate extends JsonDBTemplate {

	/**
	 * @param dbFilesLocationString
	 * @param baseScanPackage
	 * @param cipher
	 */
	public ExtendedJsonDBTemplate(String dbFilesLocationString, String baseScanPackage, ICipher cipher) {
		super(dbFilesLocationString, baseScanPackage, cipher);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.jsondb.JsonDBTemplate#updateCollectionSchema(io.jsondb.query.
	 * CollectionSchemaUpdate, java.lang.Class)
	 */
	@Override
	public <T> void updateCollectionSchema(CollectionSchemaUpdate update, Class<T> entityClass) {
		// TODO Auto-generated method stub
		super.updateCollectionSchema(update, entityClass);
	}
	
	/* (non-Javadoc)
	 * @see io.jsondb.JsonDBTemplate#backup(java.lang.String)
	 */
	@Override
	public void backup(String backupPath) {
		// TODO Auto-generated method stub
		super.backup(backupPath);
	}
	
	/* (non-Javadoc)
	 * @see io.jsondb.JsonDBTemplate#restore(java.lang.String, boolean)
	 */
	@Override
	public void restore(String restorePath, boolean merge) {
		// TODO Auto-generated method stub
		super.restore(restorePath, merge);
	}

}
