package it.eng.areas.ems.sdodaeservices.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.LogicalExpression;
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
import it.eng.areas.ems.sdodaeservices.bean.DaeEventActivation;
import it.eng.areas.ems.sdodaeservices.dao.EventDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.EventAppDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.EventDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.EventDetailDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.EventSearchDepthRule;
import it.eng.areas.ems.sdodaeservices.entity.EventDO;
import it.eng.areas.ems.sdodaeservices.entity.FirstResponderDO;
import it.eng.areas.ems.sdodaeservices.entity.InterventoDO;
import it.eng.areas.ems.sdodaeservices.entity.NotificheTypeEnum;
import it.eng.areas.ems.sdodaeservices.entity.filter.EventFilterDO;
import it.esel.parsley.lang.StringUtils;

@Repository
public class EventDAOImpl extends EntityDAOImpl<EventDO, String> implements EventDAO {

	public EventDAOImpl() {
	}

	@Override
	public Class<EventDO> getEntityClass() {
		return EventDO.class;
	}

	protected FetchRule getFetchRule(String name) {
		if (name.equals(EventDeepDepthRule.NAME)) {
			return new EventDeepDepthRule();
		}
		if (name.equals(EventSearchDepthRule.NAME)) {
			return new EventSearchDepthRule();
		}
		if (name.equals(EventDetailDepthRule.NAME)) {
			return new EventDetailDepthRule();
		}

		if (name.equals(EventAppDepthRule.NAME)) {
			return new EventAppDepthRule();
		}

		return null;
	}

	@Override
	public List<EventDO> getAvailableEvents(String frID, Integer maxIntervention, String fetchRule) {
		Calendar timeFilter = Calendar.getInstance();
		timeFilter.add(Calendar.MINUTE, -120);
		// PRENDO PRIMA TUTTI GLI EVENTI CHE NON HANNO INTERVENTI
		Criteria criteria = createCriteria(EventDeepDepthRule.NAME, "ev");
		criteria.add(Restrictions.ge("timestamp", timeFilter));
		criteria.add(Restrictions.eq("closed", false));
		criteria.add(Restrictions.isEmpty("interventi"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		// controllo la priorità del FR
		criteria.createAlias("categoriaFr", "categoriaFr");
		criteria.add(Subqueries.propertyLe("categoriaFr.priority", getPrioritySub(frID)));

		List<EventDO> eventNotManaged = criteria.list();

		// Ora prendo tutti gli eventi che hanno interventi<MAX non gestiti dal
		// FR in causa
		criteria = createCriteria(EventDeepDepthRule.NAME, "ev");
		criteria.createAlias("interventi", "int");
		criteria.add(Restrictions.ne("int.eseguitoDa.id", frID));
		criteria.add(Restrictions.eq("closed", false));
		criteria.add(Restrictions.isNotNull("int.dataAccettazione"));
		criteria.add(Restrictions.ge("timestamp", timeFilter));
		// controllo la priorità del FR
		criteria.createAlias("categoriaFr", "categoriaFr");
		criteria.add(Subqueries.propertyLe("categoriaFr.priority", getPrioritySub(frID)));

		// l'evento non deve avere interventi accettati o rifiutati da quel fr,
		// nel caso viene preso dalla seconda query
		criteria.add(Subqueries.notExists(getNotManagedSub(frID)));

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<EventDO> managedRet = new ArrayList<>();

		List<EventDO> evtWithIntervention = criteria.list();
		for (EventDO eventDO : evtWithIntervention) {

			if (eventDO.getInterventi().size() <= maxIntervention - 1) {
				if (!isRejected(frID, eventDO.getId()) && !isAlreadyAccepted(frID, eventDO.getId())) {
					managedRet.add(eventDO);
				}
			}
		}
		List<EventDO> totalRET = new ArrayList<>();

		totalRET.addAll(eventNotManaged);

		totalRET.addAll(managedRet);

		return totalRET;
	}

	public DetachedCriteria getNotManagedSub(String frID) {
		DetachedCriteria notManagedSub = DetachedCriteria.forClass(InterventoDO.class, "int2");
		notManagedSub.add(Property.forName("int2.event.id").eqProperty("ev.id"));

		LogicalExpression rifiutato = Restrictions.and(Restrictions.isNotNull("int2.dataRifiuto"),
				Restrictions.eq("int2.eseguitoDa.id", frID));

		LogicalExpression accettato = Restrictions.and(Restrictions.isNotNull("int2.dataAccettazione"),
				Restrictions.eq("int2.eseguitoDa.id", frID));

		notManagedSub.add(Restrictions.or(accettato, rifiutato));
		notManagedSub.setProjection(Projections.property("id"));

		return notManagedSub;
	}

	public DetachedCriteria getPrioritySub(String frID) {
		DetachedCriteria frPrioritySub = DetachedCriteria.forClass(FirstResponderDO.class, "fr");
		frPrioritySub.createAlias("categoriaFr", "catFR");
		frPrioritySub.add(Restrictions.eq("id", frID));
		frPrioritySub.setProjection(Projections.property("catFR.priority"));

		return frPrioritySub;
	}

	public boolean isRejected(String frId, String eventID) {
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.eq("id", eventID));
		criteria.createAlias("interventi", "int");
		criteria.add(Restrictions.eq("int.eseguitoDa.id", frId));
		criteria.add(Restrictions.isNotNull("int.dataRifiuto"));
		return criteria.list().size() > 0;

	}

	public boolean isAlreadyAccepted(String frId, String eventID) {
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.eq("id", eventID));
		criteria.createAlias("interventi", "int");
		criteria.add(Restrictions.eq("int.eseguitoDa.id", frId));
		criteria.add(Restrictions.isNotNull("int.dataAccettazione"));
		return criteria.list().size() > 0;

	}

	@Override
	public List<EventDO> searchEventByFilter(EventFilterDO filter) {
		Criteria criteria = createCriteria(filter, "super");

		if (!StringUtils.isEmpty(filter.getId())) {
			criteria.add(Restrictions.eq("id", filter.getId()));
		}

		if (!StringUtils.isEmpty(filter.getCoRiferimento())) {
			criteria.add(Restrictions.eq("coRiferimento", filter.getCoRiferimento()));
		}

		if (!StringUtils.isEmpty(filter.getCartellinoEvento())) {
			criteria.add(Restrictions.like("cartellino", "%" + filter.getCartellinoEvento() + "%"));
		}

		if (!StringUtils.isEmpty(filter.getComune())) {
			criteria.add(Restrictions.ilike("comune", "%" + filter.getComune() + "%"));
		}

		if (!StringUtils.isEmpty(filter.getIndirizzo())) {
			criteria.add(Restrictions.ilike("indirizzo", "%" + filter.getIndirizzo() + "%"));
		}

		if (filter.getDataDA() != null) {
			criteria.add(Restrictions.ge("timestamp", filter.getDataDA()));
		}

		if (filter.getDataA() != null) {
			criteria.add(Restrictions.le("timestamp", filter.getDataA()));
		}

		if (!StringUtils.isEmpty(filter.getCategoria())) {
			criteria.createAlias("categoriaFr", "categoriaFr", JoinType.LEFT_OUTER_JOIN);
			criteria.add(Restrictions.eq("categoriaFr.descrizione", filter.getCategoria()));
		}
		if (filter.getFetchRule().equals(EventAppDepthRule.NAME)
				|| filter.getFetchRule().equals(EventDetailDepthRule.NAME) || !StringUtils.isEmpty(filter.getFrID())
				|| !StringUtils.isEmpty(filter.getNomeFR()) || !StringUtils.isEmpty(filter.getCognomeFR())) {

		}
		// questo ramo viene usato dal FR per caricare i propri eventi
		if (!StringUtils.isEmpty(filter.getFrID()) || !StringUtils.isEmpty(filter.getNomeFR())
				|| !StringUtils.isEmpty(filter.getCognomeFR())) {
			criteria.createAlias("interventi", "interventi", JoinType.LEFT_OUTER_JOIN);
			criteria.createAlias("interventi.eseguitoDa", "eseguitoDa", JoinType.LEFT_OUTER_JOIN);

			if (!StringUtils.isEmpty(filter.getFrID())) {
				criteria.add(Restrictions.eq("eseguitoDa.id", filter.getFrID()));
			}

			if (filter.isManaged()) {
				// l'intervento non deve essere rifiutato e deve essere
				// chiuso dall'utente (arrivo sul luogo)
				criteria.add(Restrictions.isNull("interventi.dataRifiuto"));
				criteria.add(Restrictions.isNotNull("interventi.dataChiusura"));
			}

			if (filter.isAccepted()) {
				// l'intervento non deve essere rifiutato e deve essere
				// accettato
				criteria.add(Restrictions.isNull("interventi.dataRifiuto"));
				criteria.add(Restrictions.isNotNull("interventi.dataAccettazione"));
			}

			if (!StringUtils.isEmpty(filter.getNomeFR()) || !StringUtils.isEmpty(filter.getCognomeFR())) {
				criteria.createAlias("eseguitoDa.utente", "utente");

				if (!StringUtils.isEmpty(filter.getNomeFR())) {
					criteria.add(Restrictions.ilike("utente.nome", "%" + filter.getNomeFR().toLowerCase() + "%"));
				}

				if (!StringUtils.isEmpty(filter.getCognomeFR())) {
					criteria.add(Restrictions.ilike("utente.cognome", "%" + filter.getCognomeFR().toLowerCase() + "%"));
				}
			}
		}

		if (filter.isManaged() || filter.isAccepted()) {
			DetachedCriteria subQ = DetachedCriteria.forClass(InterventoDO.class, "sub");

			subQ.setProjection(Projections.property("sub.id"));

			subQ.add(Property.forName("sub.event.id").eqProperty("super.id"));

			if (filter.isManaged()) {
				// l'intervento non deve essere rifiutato e deve essere
				// chiuso dall'utente (arrivo sul luogo)
				subQ.add(Restrictions.isNull("dataRifiuto"));
				subQ.add(Restrictions.isNotNull("dataChiusura"));
			}

			if (filter.isAccepted()) {
				// l'intervento non deve essere rifiutato e deve essere
				// accettato
				subQ.add(Restrictions.isNull("dataRifiuto"));
				subQ.add(Restrictions.isNotNull("dataAccettazione"));
			}

			criteria.add(Subqueries.exists(subQ));
		}

		// se nel filtro è presente la lista di province
		if (filter.getProvinces() != null || filter.getMunicipalities() != null) {
			Disjunction or = Restrictions.disjunction();

			if (filter.getProvinces() != null && !filter.getProvinces().isEmpty()) {
				filter.getProvinces().forEach(p -> {
					or.add(Restrictions.ilike("provincia", p));
				});
			}

			if (filter.getMunicipalities() != null && !filter.getMunicipalities().isEmpty()) {
				filter.getMunicipalities().forEach(p -> {
					// il comune sull'evento è concatenato alla località
					or.add(Restrictions.ilike("comune", p + " - %"));
				});
			}

			criteria.add(or);
		}

		criteria.addOrder(Order.desc("timestamp"));

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public EventDO insertEvent(EventDO eventDO) {

		return this.save(eventDO);
	}

	@Override
	public List<DaeEventActivation> countDaeEvent(String categoryId, Calendar from, Calendar to) {
		Criteria criteria = createCriteria();
		ProjectionList projL = Projections.projectionList();
		projL.add(Projections.sqlGroupProjection("count(*) as numAttivazioni, provincia as provincia", "provincia",
				new String[] { "numAttivazioni", "provincia" },
				new Type[] { IntegerType.INSTANCE, StringType.INSTANCE }));
		criteria.setProjection(projL);
		criteria.setResultTransformer(Transformers.aliasToBean(DaeEventActivation.class));

		criteria.add(Restrictions.ge("timestamp", from));
		criteria.add(Restrictions.le("timestamp", to));

		if (categoryId != null) {
			criteria.add(Restrictions.eq("categoriaFr.id", categoryId));
		}

		return criteria.list();
	}

	@Override
	public List<DaeEventActivation> countDaeEventByDay(Calendar from, Calendar to, String categoryId) {
		Criteria criteria = createCriteria();
		ProjectionList projL = Projections.projectionList();
		projL.add(Projections.sqlGroupProjection(
				"count(*) as numAttivazioni, to_char(this_.TIME_STAMP, 'yyyy-MM-dd')  as aaaammgg",
				"to_char(this_.TIME_STAMP, 'yyyy-MM-dd') order by aaaammgg asc",
				new String[] { "numAttivazioni", "aaaammgg" },
				new Type[] { IntegerType.INSTANCE, StringType.INSTANCE }));
		// criteria.addOrder(Order.asc("data"));
		criteria.setProjection(projL);
		Date startDate = null;

		criteria.add(Restrictions.ge("timestamp", from));
		criteria.add(Restrictions.le("timestamp", to));

		if (!StringUtils.isEmpty(categoryId)) {
			criteria.add(Restrictions.eq("categoriaFr.id", categoryId));
		}

		criteria.setResultTransformer(Transformers.aliasToBean(DaeEventActivation.class));

		return criteria.list();
	}

	@Override
	public List<DaeEventActivation> countDaeAcceptedEventByDay(Calendar from, Calendar to, String categoryId) {
		Criteria criteria = createCriteria();
		ProjectionList projL = Projections.projectionList();
		projL.add(Projections.sqlGroupProjection(
				" count(*) as numAttivazioni, to_char(this_.TIME_STAMP, 'yyyy-MM-dd')  as aaaammgg",
				" to_char(this_.TIME_STAMP, 'yyyy-MM-dd') order by aaaammgg asc",
				new String[] { "numAttivazioni", "aaaammgg" },
				new Type[] { IntegerType.INSTANCE, StringType.INSTANCE }));

		criteria.setProjection(projL);

		criteria.add(Restrictions.ge("timestamp", from));
		criteria.add(Restrictions.le("timestamp", to));

		if (!StringUtils.isEmpty(categoryId)) {
			criteria.add(Restrictions.eq("categoriaFr.id", categoryId));
		}

		criteria.add(Restrictions.gt("acceptedResponders", 0));
		criteria.setResultTransformer(Transformers.aliasToBean(DaeEventActivation.class));

		return criteria.list();
	}

	@Override
	public List<EventDO> getNotifiedEvents(String frID, Long maxInt, String depth) {

		Criteria criteria = createCriteria(EventDeepDepthRule.NAME, "ev");
		// Prendo tutti gli eventi che sono stati notificati al FR e non sono
		// chiusi

		criteria.createAlias("notifiche", "notifiche");
		// prendo le notifiche verso quell'utente
		criteria.add(Restrictions.eq("notifiche.firstResponder.id", frID));
		criteria.add(Restrictions.eq("notifiche.tipoNotifica", NotificheTypeEnum.NUOVA_EMERGENZA));

		// escludo gli eventi troppo vecchi
		Calendar timeFilter = Calendar.getInstance();
		timeFilter.add(Calendar.MINUTE, -120);
		criteria.add(Restrictions.ge("timestamp", timeFilter));
		criteria.add(Restrictions.eq("closed", false));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		// l'evento non deve essere rifiutato dal FR
		DetachedCriteria userSubCriteria1 = DetachedCriteria.forClass(InterventoDO.class, "int2");
		userSubCriteria1.add(Property.forName("int2.event.id").eqProperty("ev.id"));

		LogicalExpression rifiutato = Restrictions.and(Restrictions.isNotNull("int2.dataRifiuto"),
				Restrictions.eq("int2.eseguitoDa.id", frID));
		LogicalExpression chiuso = Restrictions.and(Restrictions.isNotNull("int2.dataChiusura"),
				Restrictions.eq("int2.eseguitoDa.id", frID));
		userSubCriteria1.add(Restrictions.or(rifiutato, chiuso));
		userSubCriteria1.setProjection(Projections.property("id"));
		criteria.add(Subqueries.notExists(userSubCriteria1));

		// l'evento non deve avere interventi < max int meno l'eventuale
		// intervento del FR
		// (Mauro) tolto il controllo sul numero dei responder perchè sull'app
		// deve uscire sempre

		// DetachedCriteria userSubCriteria2 =
		// DetachedCriteria.forClass(InterventoDO.class, "int2");
		// userSubCriteria2.add(Property.forName("int2.event.id").eqProperty("ev.id"));
		// userSubCriteria2.add(Restrictions.and(Restrictions.isNotNull("int2.dataAccettazione"),
		// Restrictions.ne("int2.eseguitoDa.id", frID)));
		// userSubCriteria2.setProjection(Projections.count("id"));
		// criteria.add(Subqueries.gt(maxInt, userSubCriteria2));

		return criteria.list();
	}

	@Override
	public List<DaeEventActivation> listDAEActivationsByType(String categoryId, Calendar fromCal, Calendar toCal) {
		Criteria criteria = createCriteria();

		criteria.createAlias("notifiche", "notifiche");
		criteria.createAlias("notifiche.firstResponder", "firstResponder");

		ProjectionList projL = Projections.projectionList();
		projL.add(Projections.sqlGroupProjection(
				" count(*) as numAttivazioni, to_char(this_.TIME_STAMP, 'yyyy-MM-dd')  as aaaammgg",
				" to_char(this_.TIME_STAMP, 'yyyy-MM-dd') order by aaaammgg asc",
				new String[] { "numAttivazioni", "aaaammgg" },
				new Type[] { IntegerType.INSTANCE, StringType.INSTANCE }));

		criteria.setProjection(projL);

		criteria.add(Restrictions.ge("timestamp", fromCal));
		criteria.add(Restrictions.le("timestamp", toCal));

		criteria.add(Restrictions.eq("firstResponder.categoriaFr.id", categoryId));
		criteria.add(Restrictions.eq("notifiche.tipoNotifica", NotificheTypeEnum.NUOVA_EMERGENZA));

		criteria.setResultTransformer(Transformers.aliasToBean(DaeEventActivation.class));

		return criteria.list();
	}

	@Override
	public List<DaeEventActivation> listDAEAcceptedByType(String categoryId, Calendar fromCal, Calendar toCal) {
		Criteria criteria = createCriteria();

		criteria.createAlias("interventi", "interventi");
		criteria.createAlias("interventi.eseguitoDa", "eseguitoDa");

		ProjectionList projL = Projections.projectionList();
		projL.add(Projections.sqlGroupProjection(
				" count(*) as numAttivazioni, to_char(this_.TIME_STAMP, 'yyyy-MM-dd')  as aaaammgg",
				" to_char(this_.TIME_STAMP, 'yyyy-MM-dd') order by aaaammgg asc",
				new String[] { "numAttivazioni", "aaaammgg" },
				new Type[] { IntegerType.INSTANCE, StringType.INSTANCE }));

		criteria.setProjection(projL);

		criteria.add(Restrictions.ge("timestamp", fromCal));
		criteria.add(Restrictions.le("timestamp", toCal));

		criteria.add(Restrictions.eq("eseguitoDa.categoriaFr.id", categoryId));
		criteria.add(Restrictions.isNotNull("interventi.dataAccettazione"));

		criteria.setResultTransformer(Transformers.aliasToBean(DaeEventActivation.class));

		return criteria.list();
	}

}
