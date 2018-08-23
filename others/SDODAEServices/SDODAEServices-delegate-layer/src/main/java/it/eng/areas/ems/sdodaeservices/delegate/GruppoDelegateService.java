package it.eng.areas.ems.sdodaeservices.delegate;

import java.util.List;

import it.eng.areas.ems.sdodaeservices.delegate.model.Gruppo;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.GruppoFilter;

public interface GruppoDelegateService {

	public Gruppo getGruppoById(String id);

	public boolean deleteGruppoById(String id);

	public long countAll();

	// public List<VCTGruppoDO> getAllVctGruppo();

	public List<Gruppo> getGruppoByFilter(GruppoFilter gruppoFilter);

	public Gruppo insertGruppo(Gruppo gruppo);

	public List<Gruppo> getAllGruppo();

}
