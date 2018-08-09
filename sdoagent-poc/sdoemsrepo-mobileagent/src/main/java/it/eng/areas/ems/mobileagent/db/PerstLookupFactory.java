package it.eng.areas.ems.mobileagent.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.garret.perst.Index;
import org.garret.perst.Storage;
import org.garret.perst.StorageFactory;
import org.pmw.tinylog.Logger;

import it.eng.area118.mdo.jme.data.Country;
import it.eng.area118.mdo.jme.data.Dynamic;
import it.eng.area118.mdo.jme.data.Municipality;
import it.eng.area118.mdo.jme.data.Pathology;
import it.eng.area118.mdo.jme.data.PathologyClass;
import it.eng.area118.mdo.jme.data.Province;
import it.eng.area118.mdo.jme.data.Region;
import it.eng.area118.mdo.jme.data.ServiceProvided;
import it.eng.area118.mdo.jme.data.ServiceResultType;
import it.esel.terminal.db.perst.PerstRootBean;

public class PerstLookupFactory {

	public final static int WRITE_MODE = 1;

	protected Storage db;
	protected PerstRootBean root;
	protected Map lookupMap;
	protected Map storeServiceMap;

	public PerstLookupFactory(String completePath) throws DBNotFoundException {
		this(completePath, 100 * 1000, -1);
	}

	public PerstLookupFactory(String file, int pageSize, int createMode) throws DBNotFoundException {
		this.db = StorageFactory.getInstance().createStorage();
		// 100k di pagesize
		try {
			Logger.info("OPENING DB: " + file);
			this.db.open(file, pageSize);
		} catch (Exception e) {
			Logger.error(e,"PerstLookupFactory init Exception ");
			throw new DBNotFoundException();
		}
		lookupMap = new HashMap();
		storeServiceMap = new HashMap();
		root = (PerstRootBean) db.getRoot();
		if (root == null) {
			if (createMode == WRITE_MODE) {
				root = new PerstRootBean(db);
				db.setRoot(root);
			} else {
				throw new DBNotFoundException();
			}
		}

	}

	public PerstLookupFactory(String file, int writeMode) throws DBNotFoundException {
		this(file, 100 * 1000, writeMode);

	}

	public LookupService getLookUpService(Class clazz) {
		LookupService lookup = (LookupService) lookupMap.get(clazz.getName());
		if (lookup == null) {
			Index i = root.getIndex(clazz, db, false);
			if (clazz == Pathology.class) {
				lookup = new PathologyLookupService(i, db);
			} else if (clazz == Dynamic.class) {
				lookup = new DynamicLookupService(i, db);
			} else if (clazz == ServiceProvided.class) {
				lookup = new ServiceProvidedTypeLookupService(i, db);
			} else {
				lookup = new DefaultIndexLookupService(i, clazz, db);
			}
			lookupMap.put(clazz.getName(), lookup);
		}
		return lookup;
	}

	public void putProvince(Province province) {
		Index i = root.getIndex(Province.class, db, true);
		i.put(province.getName(), province);
		if (province.getRegion() != null) {
			this.putRegion(province.getRegion());
		}
	}

	public void putRegion(Region region) {
		Index i = root.getIndex(Region.class, db, true);
		i.put(region.getName(), region);
		if (region.getCountry() != null) {
			this.putCountry(region.getCountry());
		}
	}

	public void putCountry(Country country) {
		Index i = root.getIndex(Country.class, db, true);
		i.put(country.getName(), country);
	}

	public void close() {
		db.close();
	}

	public void putPathology(Pathology pathology) {
		Index i = root.getIndex(Pathology.class, db, true);
		i.put(pathology.getName(), pathology);
		if (pathology.getPathologyClass() != null) {
			this.putPathologyClass(pathology.getPathologyClass());
		}
	}

	public void putPathologyClass(PathologyClass pathologyClass) {
		Index i = root.getIndex(PathologyClass.class, db, true);
		i.put(pathologyClass.getName(), pathologyClass);
	}

	public void putDynamic(Dynamic dynamic) {
		Index i = root.getIndex(Dynamic.class, db, true);
		i.put(dynamic.getName(), dynamic);
	}

	/* Metodi di utilit per riempire i database */
	public void putMunicipality(Municipality m) {
		Index index = root.getIndex(Municipality.class, db, false);
		// indicizzo il comune per il suo intero nome
		index.put(m.getName(), m);
		// aggiungo lo stesso comune al database indicizzandolo + volte, una per
		// ogni parola del nome
		StringTokenizer tonizer = new StringTokenizer(m.getName(), " '");
		//
		ArrayList list = new ArrayList();
		while (tonizer.hasMoreTokens()) {
			list.add(tonizer.nextToken());
		}
		if (list.size() > 1) {
			// faccio l'aggiunta ulteriore solo se il comune ha un nome composto
			for (int i = 0; i < list.size(); i++) {
				index.put((String) list.get(i), m);
			}
		}
		if (m.getProvince() != null) {
			this.putProvince(m.getProvince());
		}
	}

	public void putServiceProvided(ServiceProvided serviceProvided) {
		Index index = root.getIndex(ServiceProvided.class, db, false);
		index.put(serviceProvided.getName(), serviceProvided);

		StringTokenizer tonizer = new StringTokenizer(serviceProvided.getName(), " ");
		//
		ArrayList list = new ArrayList();
		while (tonizer.hasMoreTokens()) {
			list.add(tonizer.nextToken());
		}
		if (list.size() > 1) {
			// faccio l'aggiunta ulteriore solo se il comune ha un nome composto
			for (int i = 0; i < list.size(); i++) {
				index.put((String) list.get(i), serviceProvided);
			}
		}

	}

	// public void putCrew(CrewData crew) {
	// Index i = root.getIndex(CrewData.class, db, true);
	// i.put(crew.getCode(), crew);
	// }

	// public void putSynchDataInfo(SynchDataInfo p2) {
	// Index i = root.getIndex(SynchDataInfo.class, db, true);
	// i.put(p2.getType(), p2);
	// }

	public void putServiceResultType(ServiceResultType p2) {
		Index i = root.getIndex(ServiceResultType.class, db, false);
		i.put(p2.getRefCode(), p2);
	}

	public static void main(String[] args) {
		PerstLookupFactory fact = null;
		try {
			fact = new PerstLookupFactory("db/perst.dbs");
		} catch (DBNotFoundException e) {
			Logger.error(e,"PerstLookupFactory init Exception ");
		}
		System.out.println(fact.getLookUpService(Municipality.class).getAll().size());
	}
}
