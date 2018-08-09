package it.eng.areas.ems.sdodaeservices.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.bean.DaeNumbers;
import it.eng.areas.ems.sdodaeservices.bean.FirstResponderSubscription;
import it.eng.areas.ems.sdodaeservices.dao.FirstResponderDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.FirstResponderActivationRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.FirstResponderBareRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.FirstResponderCertificatoImageRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.FirstResponderDashboardDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.FirstResponderDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.FirstResponderImageRule;
import it.eng.areas.ems.sdodaeservices.entity.FRStatoProfiloEnum;
import it.eng.areas.ems.sdodaeservices.entity.FirstResponderDO;
import it.eng.areas.ems.sdodaeservices.entity.InterventoDO;
import it.eng.areas.ems.sdodaeservices.entity.NotificheTypeEnum;
import it.eng.areas.ems.sdodaeservices.entity.filter.FirstResponderFilterDO;
import it.esel.parsley.lang.StringUtils;

@Repository
public class FirstResponderDAOImpl extends EntityDAOImpl<FirstResponderDO, String> implements FirstResponderDAO {

	public FirstResponderDAOImpl() {
	}

	@Override
	public Class<FirstResponderDO> getEntityClass() {
		return FirstResponderDO.class;
	}

	@Override
	protected FetchRule getFetchRule(String name) {
		if (name.equals(FirstResponderDeepDepthRule.NAME)) {
			return new FirstResponderDeepDepthRule();
		}
		if (name.equals(FirstResponderBareRule.NAME)) {
			return new FirstResponderBareRule();
		}
		if (name.equals(FirstResponderActivationRule.NAME)) {
			return new FirstResponderActivationRule();
		}
		if (name.equals(FirstResponderImageRule.NAME)) {
			return new FirstResponderImageRule();
		}
		if (name.equals(FirstResponderCertificatoImageRule.NAME)) {
			return new FirstResponderCertificatoImageRule();
		}
		if (name.equals(FirstResponderDashboardDepthRule.NAME)) {
			return new FirstResponderDashboardDepthRule();
		}

		return null;
	}

	@Override
	public List<FirstResponderDO> searchFirstResponderByFilter(FirstResponderFilterDO filter) {
		return searchFirstResponderByFilter(filter, filter.getFetchRule());
	}

	@Override
	public List<FirstResponderDO> getFirstResponderToBeNotified(int catPriority) {

		FirstResponderFilterDO filter = new FirstResponderFilterDO();
		filter.setFetchRule(FirstResponderActivationRule.NAME);

		// Criteria criteria = createCriteria(filter, "fr");
		//
		// criteria.createAlias("dispositivo", "dispositivo");
		//
		// criteria.add(Restrictions.isNotNull("dispositivo.pushToken"));
		// criteria.add(Restrictions.eq("disponibile", true));
		// criteria.add(Restrictions.eq("statoProfilo",
		// FRStatoProfiloEnum.ATTIVO));
		// // FirstResponderFilter frf = new FirstResponderFilter();
		//
		// criteria.createAlias("categoriaFr", "cat");
		// criteria.add(Restrictions.ge("cat.priority", catPriority));
		// criteria.add(Restrictions.isEmpty("interventi"));
		//
		//
		// criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		// List<FirstResponderDO> frNeverIntervention = criteria.list();

		// PRENDO TUTTI I FR CHE SODDISFANO I REQUISITI DI FILTRO MA CHE NON
		// // HANNO FATTO ANCORA ALCUN INTERVENTO
		Criteria criteria = createCriteria(filter, "fr");

		criteria.createAlias("dispositivo", "dispositivo");

		criteria.add(Restrictions.isNotNull("dispositivo.pushToken"));
		criteria.add(Restrictions.eq("disponibile", true));
		criteria.add(Restrictions.eq("statoProfilo", FRStatoProfiloEnum.ATTIVO));

		criteria.createAlias("categoriaFr", "cat");
		// criteria.createAlias("interventi", "int", JoinType.INNER_JOIN);
		criteria.add(Restrictions.ge("cat.priority", catPriority));

		DetachedCriteria userSubCriteria2 = DetachedCriteria.forClass(InterventoDO.class, "int2")
				.add(Restrictions.and(Property.forName("int2.eseguitoDa.id").eqProperty("fr.id"),
						Restrictions.and(Restrictions.isNull("int2.dataAnnullamento"),
								Restrictions.isNull("int2.dataChiusura"), Restrictions.isNull("int2.dataRifiuto"))))
				.setProjection(Projections.property("id"));

		criteria.add(Subqueries.notExists(userSubCriteria2));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<FirstResponderDO> frNotInIntervention = criteria.list();

		List<FirstResponderDO> retList = new ArrayList<FirstResponderDO>();
		retList.addAll(frNotInIntervention);

		// retList.addAll(frNeverIntervention);

		return retList;

	}

	@Override
	public List<FirstResponderDO> getFRToBeNotifiedModifyOrClosure(String eventId, int catPriority) {

		FirstResponderFilterDO filter = new FirstResponderFilterDO();
		filter.setFetchRule(FirstResponderActivationRule.NAME);
		Criteria criteria = createCriteria(filter, "fr");

		// carico tutti i FR che hanno ricevuto la notifica di nuova emergenza
		// su quell'evento:
		criteria.createAlias("notifiche", "notifiche");

		criteria.add(Restrictions.eq("notifiche.event.id", eventId));
		criteria.add(Restrictions.eq("notifiche.tipoNotifica", NotificheTypeEnum.NUOVA_EMERGENZA));

		// l'utente non deve essere attivo su un altro intervento
		DetachedCriteria userSubCriteria1 = DetachedCriteria.forClass(InterventoDO.class, "int1");
		userSubCriteria1.add(Property.forName("int1.eseguitoDa.id").eqProperty("fr.id"));
		userSubCriteria1.add(Restrictions.ne("int1.event.id", eventId));
		userSubCriteria1.add(Restrictions.isNull("int1.dataAnnullamento"));
		userSubCriteria1.add(Restrictions.isNull("int1.dataChiusura"));
		userSubCriteria1.add(Restrictions.isNull("int1.dataRifiuto"));

		userSubCriteria1.setProjection(Projections.property("id"));
		criteria.add(Subqueries.notExists(userSubCriteria1));

		DetachedCriteria userSubCriteria2 = DetachedCriteria.forClass(InterventoDO.class, "int2");

		userSubCriteria2.add(Property.forName("int2.eseguitoDa.id").eqProperty("fr.id"));
		userSubCriteria2.add(Restrictions.isNull("int2.dataAnnullamento"));
		userSubCriteria2.add(Restrictions.isNull("int2.dataChiusura"));
		userSubCriteria2.add(Restrictions.isNotNull("int2.dataRifiuto"));
		userSubCriteria2.add(Restrictions.eq("int2.event.id", eventId));

		userSubCriteria2.setProjection(Projections.property("id"));

		// Cerco tutti i fr che non hanno l'intervento o che se lo hanno non è
		// rifiutato
		criteria.add(Subqueries.notExists(userSubCriteria2));

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return criteria.list();
	}

	// mauro - vecchio metodo

	// @Override
	// public List<FirstResponderDO> getFRToBeNotifiedModifyOrClosure(String
	// eventId, int catPriority) {
	//
	// FirstResponderFilterDO filter = new FirstResponderFilterDO();
	// filter.setFetchRule(FirstResponderActivationRule.NAME);
	// Criteria criteria = createCriteria(filter, "fr");
	// criteria.createAlias("lastPosition", "lp");
	// criteria.createAlias("lp.gpsLocation", "gps");
	// criteria.createAlias("dispositivo", "dispositivo");
	//
	// criteria.add(Restrictions.isNotNull("dispositivo.pushToken"));
	// criteria.add(Restrictions.eq("disponibile", true));
	// criteria.add(Restrictions.eq("statoProfilo", FRStatoProfiloEnum.ATTIVO));
	// // FirstResponderFilter frf = new FirstResponderFilter();
	// criteria.createAlias("categoriaFr", "cat");
	// criteria.add(Restrictions.ge("cat.priority", catPriority));
	//
	// // l'utente non deve essere attivo su un altro intervento
	// DetachedCriteria userSubCriteria1 =
	// DetachedCriteria.forClass(InterventoDO.class, "int1");
	// userSubCriteria1.add(Property.forName("int1.eseguitoDa.id").eqProperty("fr.id"));
	// userSubCriteria1.add(Restrictions.ne("int1.event.id", eventId));
	// userSubCriteria1.add(Restrictions.isNull("int1.dataAnnullamento"));
	// userSubCriteria1.add(Restrictions.isNull("int1.dataChiusura"));
	// userSubCriteria1.add(Restrictions.isNull("int1.dataRifiuto"));
	//
	// userSubCriteria1.setProjection(Projections.property("id"));
	// criteria.add(Subqueries.notExists(userSubCriteria1));
	//
	// DetachedCriteria userSubCriteria2 =
	// DetachedCriteria.forClass(InterventoDO.class, "int2");
	//
	// userSubCriteria2.add(Property.forName("int2.eseguitoDa.id").eqProperty("fr.id"));
	// userSubCriteria2.add(Restrictions.isNull("int2.dataAnnullamento"));
	// userSubCriteria2.add(Restrictions.isNull("int2.dataChiusura"));
	// userSubCriteria2.add(Restrictions.isNotNull("int2.dataRifiuto"));
	// userSubCriteria2.add(Restrictions.eq("int2.event.id", eventId));
	//
	// userSubCriteria2.setProjection(Projections.property("id"));
	//
	// // Cerco tutti i fr che non hanno l'intervento o che se lo hanno non è
	// // rifiutato
	// criteria.add(Subqueries.notExists(userSubCriteria2));
	//
	// criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	//
	// return criteria.list();
	// }

	/**
	 * TODO Deve essere implementato
	 * 
	 * @param filter
	 * @param fetchRule
	 * @return
	 */
	@Override
	public List<FirstResponderDO> searchFirstResponderByFilter(FirstResponderFilterDO filter, String fetchRule) {
		Criteria criteria = createCriteria(filter, "fr");

		// if (fetchRule != null) {
		// criteria.setFetchMode(fetchRule, FetchMode.JOIN);
		// }

		criteria.createAlias("utente", "ut");

		criteria.createAlias("categoriaFr", "cat");
		// quelli cancellati non li mostro mai
		criteria.add(Restrictions.eq("ut.deleted", false));

		if (filter != null) {

			if (!StringUtils.isEmpty(filter.getId())) {
				criteria.add(Restrictions.eq("id", filter.getId()));
			}

			if (!StringUtils.isEmpty(filter.getNome())) {
				criteria.add(Restrictions.ilike("ut.nome", "%" + filter.getNome() + "%"));
			}

			if (!StringUtils.isEmpty(filter.getCognome())) {
				criteria.add(Restrictions.ilike("ut.cognome", "%" + filter.getCognome() + "%"));
			}

			if (!StringUtils.isEmpty(filter.getEmail())) {
				criteria.add(Restrictions.ilike("ut.email", "%" + filter.getEmail() + "%"));
			}

			if (!StringUtils.isEmpty(filter.getLogon())) {
				if (filter.isLogonLike()) {
					criteria.add(Restrictions.ilike("ut.logon", "%" + filter.getLogon().toUpperCase() + "%"));
				} else {
					criteria.add(Restrictions.ilike("ut.logon", filter.getLogon().toUpperCase()));
				}
			}

			if (filter.getCategoriaFrIDS() != null && !filter.getCategoriaFrIDS().isEmpty()) {
				criteria.add(Restrictions.in("cat.id", filter.getCategoriaFrIDS()));
			}

			if (filter.getHaveCoords() != null && filter.getHaveCoords()) {
				criteria.createAlias("lastPosition", "lp");
				criteria.createAlias("lp.gpsLocation", "gps");

				criteria.add(Restrictions.isNotNull("gps.latitudine"));
				criteria.add(Restrictions.isNotNull("gps.longitudine"));
			}

			if (filter.getCoordinatesFromMinutes() != null) {
				criteria.createAlias("lastPosition", "lp");
				criteria.createAlias("lp.gpsLocation", "gps");
				Calendar c = Calendar.getInstance();
				c.add(Calendar.MINUTE, -filter.getCoordinatesFromMinutes());
				criteria.add(Restrictions.ge("gps.timeStamp", c));
			}

			if (filter.getIsAvailable() != null) {
				criteria.add(Restrictions.eq("disponibile", filter.getIsAvailable()));
			}

			if (filter.getStatoProfilo() != null) {
				criteria.add(Restrictions.eq("statoProfilo", filter.getStatoProfilo()));
			}

			if (filter.getDaAttivare() != null && filter.getDaAttivare() == true) {
				criteria.add(Restrictions.eqOrIsNull("statoProfilo", FRStatoProfiloEnum.IN_ATTESA_DI_ATTIVAZIONE));
			}

			if (filter.getDaAttivare() != null && filter.getDaAttivare() == false) {
				criteria.add(Restrictions.eq("statoProfilo", FRStatoProfiloEnum.ATTIVO));
			}

			// preparo i join
			if (!StringUtils.isEmpty(filter.getComune()) || !StringUtils.isEmpty(filter.getProvince())
					|| (filter.getProvinces() != null && !filter.getProvinces().isEmpty())
					|| (filter.getMunicipalities() != null && !filter.getMunicipalities().isEmpty())) {
				criteria.createAlias("ut.comuneResidenza", "comuneResidenza", JoinType.LEFT_OUTER_JOIN);

				if (!StringUtils.isEmpty(filter.getProvince())
						|| filter.getProvinces() != null && !filter.getProvinces().isEmpty()) {
					criteria.createAlias("comuneResidenza.provincia", "provincia", JoinType.LEFT_OUTER_JOIN);
				}
			}

			if (!StringUtils.isEmpty(filter.getComune())) {
				criteria.add(Restrictions.eq("comuneResidenza.nomeComune", filter.getComune()));
			}
			// provincia
			if (!StringUtils.isEmpty(filter.getProvince())) {
				criteria.add(Restrictions.eq("provincia.nomeProvincia", filter.getProvince()));
			}

			if (filter.getProvinces() != null || filter.getMunicipalities() != null) {
				Disjunction or = Restrictions.disjunction();
				// se nel filtro è presente la lista di province
				if (filter.getProvinces() != null && !filter.getProvinces().isEmpty()) {
					or.add(Restrictions.in("provincia.nomeProvincia", filter.getProvinces()));
				}
				// se nel filtro è presente la lista di comuni
				if (filter.getMunicipalities() != null && !filter.getMunicipalities().isEmpty()) {
					or.add(Restrictions.in("comuneResidenza.nomeComune", filter.getMunicipalities()));
				}
				criteria.add(or);
			}

			if (filter.getDataIscrizioneMax() != null) {
				criteria.add(Restrictions.le("dataIscrizione", filter.getDataIscrizioneMax()));
			}

			criteria.addOrder(Order.asc("ut.cognome"));
			criteria.addOrder(Order.asc("ut.nome"));

			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		}
		List<FirstResponderDO> ret = criteria.list();

		return ret;
	}

	@Override
	public FirstResponderDO insertFirstResponder(FirstResponderDO firstResponderDO) {

		return this.save(firstResponderDO);
	}

	@Override
	public FirstResponderDO getFRByLogonAndPasswordHash(String logon, String pwdHash) {

		Criteria criteria = createCriteria();
		criteria.createAlias("utente", "ut");
		criteria.add(Restrictions.ilike("ut.logon", logon.toUpperCase()));
		criteria.add(Restrictions.eq("ut.passwordHash", pwdHash));
		criteria.add(Restrictions.eq("ut.deleted", false));

		return (FirstResponderDO) criteria.uniqueResult();
	}

	@Override
	public List<FirstResponderSubscription> countFirstResponderSubscriptionPerMonth(Calendar from, Calendar to) {
		Criteria criteria = createCriteria();
		ProjectionList projL = Projections.projectionList();
		projL.add(Projections.sqlGroupProjection(
				"count(*) as numIscritti, to_char(this_.data_iscrizione, 'yyyy-MM-dd') as aaaammgg",
				"to_char(this_.data_iscrizione, 'yyyy-MM-dd') order by aaaammgg asc",
				new String[] { "numIscritti", "aaaammgg" }, new Type[] { IntegerType.INSTANCE, StringType.INSTANCE }));
		criteria.setProjection(projL);
		criteria.setResultTransformer(Transformers.aliasToBean(FirstResponderSubscription.class));

		criteria.add(Restrictions.ge("dataIscrizione", from.getTime()));
		criteria.add(Restrictions.le("dataIscrizione", to.getTime()));

		return criteria.list();
	}

	@Override
	public FirstResponderDO getFirstResponderByUserId(String fetchRule, String userId) {
		Criteria criteria = createCriteria(fetchRule);
		criteria.add(Restrictions.eq("utente.id", userId));
		return (FirstResponderDO) criteria.uniqueResult();
	}

	@Override
	public List<FirstResponderDO> loadFRDoNotDisturbEnd(String hour) {
		Criteria criteria = createCriteria();
		criteria.createAlias("utente", "ut");
		criteria.add(Restrictions.eq("ut.deleted", false));
		// Carico tutti quelli che hanno abilitato il non disturbare e non sono
		// disponibili
		criteria.add(Restrictions.eqOrIsNull("doNotDisturb", true));
		criteria.add(Restrictions.eqOrIsNull("disponibile", false));

		// Se l'inizio è minore di fine
		Criterion a11 = Restrictions.leProperty("doNotDisturbFrom", "doNotDisturbTo");
		// l'orario deve essere minore di from OR maggiore di to
		Criterion a12 = Restrictions.or(Restrictions.ge("doNotDisturbFrom", hour),
				Restrictions.le("doNotDisturbTo", hour));
		Criterion o1 = Restrictions.and(a11, a12);

		// Se la fine è minore dell'inizio
		Criterion a21 = Restrictions.leProperty("doNotDisturbTo", "doNotDisturbFrom");
		// l'orario deve essere minore di from AND maggiore di to
		Criterion a22 = Restrictions.and(Restrictions.ge("doNotDisturbFrom", hour),
				Restrictions.le("doNotDisturbTo", hour));
		Criterion o2 = Restrictions.and(a21, a22);
		// Metto in or le due condizioni
		criteria.add(Restrictions.or(o1, o2));

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<FirstResponderDO> loadFRDoNotDisturbStart(String hour) {
		//
		Criteria criteria = createCriteria();

		criteria.createAlias("utente", "ut");
		criteria.add(Restrictions.eq("ut.deleted", false));
		// Carico tutti quelli che hanno abilitato il non disturbare e sono
		// disponibili
		criteria.add(Restrictions.eqOrIsNull("doNotDisturb", true));
		criteria.add(Restrictions.eqOrIsNull("disponibile", true));

		// Se l'inizio è minore di fine
		Criterion a11 = Restrictions.leProperty("doNotDisturbFrom", "doNotDisturbTo");
		// l'orario deve essere maggiore di from AND minore di to
		Criterion a12 = Restrictions.and(Restrictions.le("doNotDisturbFrom", hour),
				Restrictions.ge("doNotDisturbTo", hour));
		Criterion o1 = Restrictions.and(a11, a12);

		// Se la fine è minore dell'inizio
		Criterion a21 = Restrictions.leProperty("doNotDisturbTo", "doNotDisturbFrom");
		// l'orario deve essere maggiore di from OR minore di to
		Criterion a22 = Restrictions.or(Restrictions.le("doNotDisturbFrom", hour),
				Restrictions.ge("doNotDisturbTo", hour));
		Criterion o2 = Restrictions.and(a21, a22);
		// Metto in or le due condizioni
		criteria.add(Restrictions.or(o1, o2));

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<FirstResponderDO> loadFRSilentEnd(String hour) {
		Criteria criteria = createCriteria();

		criteria.createAlias("utente", "ut");
		criteria.add(Restrictions.eq("ut.deleted", false));
		// Carico tutti quelli che hanno abilitato il silenzioso e non sono
		// disponibili
		criteria.add(Restrictions.eqOrIsNull("silent", true));
		criteria.add(Restrictions.eqOrIsNull("disponibile", false));

		// Se l'inizio è minore di fine
		Criterion a11 = Restrictions.leProperty("silentFrom", "silentTo");
		// l'orario deve essere minore di from OR maggiore di to
		Criterion a12 = Restrictions.or(Restrictions.ge("silentFrom", hour), Restrictions.le("silentTo", hour));
		Criterion o1 = Restrictions.and(a11, a12);

		// Se la fine è minore dell'inizio
		Criterion a21 = Restrictions.leProperty("silentTo", "silentFrom");
		// l'orario deve essere minore di from AND maggiore di to
		Criterion a22 = Restrictions.and(Restrictions.ge("silentFrom", hour), Restrictions.le("silentTo", hour));
		Criterion o2 = Restrictions.and(a21, a22);
		// Metto in or le due condizioni
		criteria.add(Restrictions.or(o1, o2));

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<DaeNumbers> fetchFrNumbersByCategory(Calendar from, Calendar to) {
		Criteria criteria = createCriteria();
		criteria.createAlias("categoriaFr", "categoriaFr");

		ProjectionList projL = Projections.projectionList();
		projL.add(Projections.alias(Projections.count("id"), "numDAE"));
		projL.add(Projections.alias(Projections.property("categoriaFr.descrizione"), "status"));
		projL.add(Projections.groupProperty("categoriaFr.descrizione"));

		criteria.setProjection(projL);
		criteria.setResultTransformer(Transformers.aliasToBean(DaeNumbers.class));

		criteria.add(Restrictions.ge("dataIscrizione", from.getTime()));
		criteria.add(Restrictions.le("dataIscrizione", to.getTime()));

		criteria.createAlias("utente", "ut");
		criteria.add(Restrictions.eqOrIsNull("ut.deleted", false));

		return criteria.list();
	}
}
