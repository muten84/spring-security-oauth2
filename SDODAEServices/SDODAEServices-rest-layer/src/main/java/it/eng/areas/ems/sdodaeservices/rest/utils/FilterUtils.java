package it.eng.areas.ems.sdodaeservices.rest.utils;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.SecurityContext;

import org.springframework.stereotype.Component;

import it.eng.areas.ems.sdodaeservices.delegate.model.Gruppo;
import it.eng.areas.ems.sdodaeservices.delegate.model.GruppoComune;
import it.eng.areas.ems.sdodaeservices.delegate.model.GruppoProvincia;
import it.eng.areas.ems.sdodaeservices.delegate.model.Ruolo;
import it.eng.areas.ems.sdodaeservices.delegate.model.TerritorialEntity;
import it.eng.areas.ems.sdodaeservices.delegate.model.Utente;
import it.eng.areas.ems.sdodaeservices.delegate.model.filter.TerritorialFilter;

@Component
public class FilterUtils {

	boolean bypass = false;

	public FilterUtils(boolean bypass) {
		super();
		this.bypass = bypass;
	}

	public FilterUtils() {
		super();
	}

	public void setReadOnly(SecurityContext secContext, List<? extends TerritorialEntity> entities) {
		Utente u = (Utente) secContext.getUserPrincipal();

		for (Ruolo r : u.getRuoli()) {
			if (r.isGlobalSave()) {
				// se ha un ruolo con il salvataggio globale non faccio nulla
				return;
			}
		}

		entities.forEach(e -> {
			setReadOnly(u, e);
		});
	}

	public void setReadOnly(Utente u, TerritorialEntity e) {

		String province = e.getProvince();
		String comune = e.getMunicipality();
		boolean isPresent = false;

		for (Gruppo g : u.getGruppi()) {
			for (GruppoProvincia p : g.getProvince()) {
				if (province.equals(p.getNomeProvincia())) {
					isPresent = true;
					break;
				}
			}

			for (GruppoComune c : g.getComuni()) {
				if (comune.equals(c.getNomeComune())) {
					isPresent = true;
					break;
				}
			}
		}
		e.setReadOnly(!isPresent);
	}

	public List<String> getProvinces(SecurityContext secContext) {
		// se bypass è true evito tutti i controlli enon applico nessun filtro
		if (bypass) {
			return null;
		}
		// carico l'utente loggato
		Utente u = (Utente) secContext.getUserPrincipal();

		for (Ruolo r : u.getRuoli()) {
			if (r.isGlobalSearch()) {
				// se ha un ruolo con la ricerca globale non aggiungo nessun
				// filtro
				return null;
			}
		}

		List<String> toRet = new ArrayList<>();

		// se l'utente non ha gruppi non gli faccio vedere nulla
		if (u.getGruppi() == null || u.getGruppi().isEmpty()) {
			toRet.add("XX");
		}
		// aggiungo le province al filtro
		u.getGruppi().forEach(g -> {
			g.getProvince().forEach(p -> {
				toRet.add(p.getNomeProvincia());
			});

		});
		return toRet;
	}

	public List<String> getMunicipalities(SecurityContext secContext) {
		// se bypass è true evito tutti i controlli enon applico nessun filtro
		if (bypass) {
			return null;
		}
		// carico l'utente loggato
		Utente u = (Utente) secContext.getUserPrincipal();

		for (Ruolo r : u.getRuoli()) {
			if (r.isGlobalSearch()) {
				// se ha un ruolo con la ricerca globale non aggiungo nessun
				// filtro
				return null;
			}
		}

		List<String> toRet = new ArrayList<>();

		// se l'utente non ha gruppi non gli faccio vedere nulla
		if (u.getGruppi() == null || u.getGruppi().isEmpty()) {
			toRet.add("XX");
		}
		// aggiungo i comuni al filtro
		u.getGruppi().forEach(g -> {
			g.getComuni().forEach(c -> {
				toRet.add(c.getNomeComune());
			});

		});
		return toRet;
	}

	/**
	 * Aggiunge le province abilitate dell'utente loggato al filtro
	 * 
	 * @param secContext
	 * @param filter
	 */
	public void addProvinceToFilter(SecurityContext secContext, TerritorialFilter filter) {
		// se bypass è true evito tutti i controlli enon applico nessun filtro
		List<String> provinces = getProvinces(secContext);

		List<String> municipalities = getMunicipalities(secContext);
		if (provinces == null && municipalities == null) {
			return;
		}
		if (filter.getProvinces() == null) {
			filter.setProvinces(new ArrayList<>());
		}

		if (filter.getMunicipalities() == null) {
			filter.setMunicipalities(new ArrayList<>());
		}

		filter.getProvinces().addAll(provinces);
		filter.getMunicipalities().addAll(municipalities);
	}

	public boolean isEnabledToSave(Utente u, TerritorialEntity e) {
		// se bypass è true evito tutti i controlli enon applico nessun filtro
		if (bypass) {
			return true;
		}

		for (Ruolo r : u.getRuoli()) {
			if (r.isGlobalSave()) {
				// se ha un ruolo con il salvataggio globale non faccio nulla
				return true;
			}
		}
		String province = e.getProvince();
		String comune = e.getMunicipality();

		for (Gruppo g : u.getGruppi()) {
			for (GruppoProvincia p : g.getProvince()) {
				if (province.equals(p.getNomeProvincia())) {
					return true;
				}
			}

			for (GruppoComune c : g.getComuni()) {
				if (comune.equals(c.getNomeComune())) {
					return true;
				}
			}
		}
		// se non ha passato nessun controllo inibisco il salvataggio
		return false;

	}

}
