package it.eng.areas.ems.sdodaeservices.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.UtenteDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.UtenteDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.UtenteImageDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.UtenteLoginDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.UtentePasswordDepthRule;
import it.eng.areas.ems.sdodaeservices.entity.UtenteDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.UtenteFilterDO;

@Repository
public class UtenteDAOImpl extends EntityDAOImpl<UtenteDO, String> implements UtenteDAO {

	@Override
	public Class<UtenteDO> getEntityClass() {
		return UtenteDO.class;
	}

	@Override
	protected FetchRule getFetchRule(String name) {
		if (name.equals(UtenteDeepDepthRule.NAME)) {
			return new UtenteDeepDepthRule();
		}
		if (name.equals(UtenteLoginDepthRule.NAME)) {
			return new UtenteLoginDepthRule();
		}
		if (name.equals(UtentePasswordDepthRule.NAME)) {
			return new UtentePasswordDepthRule();
		}
		if (name.equals(UtenteImageDepthRule.NAME)) {
			return new UtenteImageDepthRule();
		}

		return null;
	}

	@Override
	public List<UtenteDO> searchUtenteByFilter(UtenteFilterDO filter) {

		if (StringUtils.isEmpty(filter.getFetchRule())) {
			filter.setFetchRule(UtenteDeepDepthRule.NAME);
		}
		Criteria criteria = createCriteria(filter);

		criteria.add(Restrictions.eq("deleted", false));

		if (!StringUtils.isEmpty(filter.getId())) {
			criteria.add(Restrictions.eq("id", filter.getId()));
		}

		if (!StringUtils.isEmpty(filter.getNotId())) {
			criteria.add(Restrictions.ne("id", filter.getNotId()));
		}

		if (!StringUtils.isEmpty(filter.getLogon())) {
			if (filter.isLognEqual()) {
				criteria.add(Restrictions.ilike("logon", filter.getLogon().toLowerCase()));
			} else {
				criteria.add(Restrictions.ilike("logon", "%" + filter.getLogon().toLowerCase() + "%"));
			}
		}

		if (!StringUtils.isEmpty(filter.getNome())) {
			criteria.add(Restrictions.ilike("nome", "%" + filter.getNome().toLowerCase() + "%"));
		}

		if (!StringUtils.isEmpty(filter.getCognome())) {
			criteria.add(Restrictions.ilike("cognome", "%" + filter.getCognome().toLowerCase() + "%"));
		}

		if (!StringUtils.isEmpty(filter.getEmail())) {
			if (filter.isEmailLike()) {
				criteria.add(Restrictions.ilike("email", "%" + filter.getEmail().toLowerCase() + "%"));
			} else {
				criteria.add(Restrictions.eq("email", filter.getEmail()));
			}
		}

		if (!StringUtils.isEmpty(filter.getRuoloNome())) {
			criteria.createAlias("ruoli", "ruoli", JoinType.LEFT_OUTER_JOIN);
			criteria.add(Restrictions.eq("ruoli.nome", filter.getRuoloNome()));
		}

		if (!StringUtils.isEmpty(filter.getGruppoNome())) {
			criteria.createAlias("gruppi", "gruppi", JoinType.LEFT_OUTER_JOIN);
			criteria.add(Restrictions.eq("gruppi.nome", filter.getGruppoNome()));
		}

		if (filter.getProvinces() != null || filter.getMunicipalities() != null) {
			criteria.createAlias("comuneResidenza", "comuneResidenza", JoinType.LEFT_OUTER_JOIN);

			Disjunction or = Restrictions.disjunction();
			if (filter.getMunicipalities() != null && !filter.getMunicipalities().isEmpty()) {
				or.add(Restrictions.in("comuneResidenza.nomeComune", filter.getMunicipalities()));
			}
			// se nel filtro è presente la lista di province
			if ((filter.getProvinces() != null && !filter.getProvinces().isEmpty())) {
				criteria.createAlias("comuneResidenza.provincia", "provincia", JoinType.LEFT_OUTER_JOIN);

				or.add(Restrictions.in("provincia.nomeProvincia", filter.getProvinces()));
			}
			criteria.add(or);
		}

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();

	}

	@Override
	public UtenteDO getUtenteByLogonAndPasswordHash(String logon, String pwdHash) {
		Criteria criteria = createCriteria();
		// Mauro- messo ilike in modo che sia case insensitive
		criteria.add(Restrictions.ilike("logon", logon));
		criteria.add(Restrictions.eq("passwordHash", pwdHash));
		// evito che effettuino la login utenti cancellati
		criteria.add(Restrictions.eq("deleted", false));

		return (UtenteDO) criteria.uniqueResult();
	}

}
