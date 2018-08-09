package it.eng.areas.ems.sdodaeservices.dao.impl;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
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
import it.eng.areas.ems.sdodaeservices.bean.DaeSubscription;
import it.eng.areas.ems.sdodaeservices.dao.DaeDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.DaeAppMobileDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.DaeDashboardDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.DaeDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.DaeForFaultDepthRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.DaeImageRule;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.DaeMinimalDepthRule;
import it.eng.areas.ems.sdodaeservices.entity.DaeDO;
import it.eng.areas.ems.sdodaeservices.entity.DaeFaultDO;
import it.eng.areas.ems.sdodaeservices.entity.DaeFaultStatoEnum;
import it.eng.areas.ems.sdodaeservices.entity.ProgrammaManutenzioneDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.DaeFilterDO;
import it.esel.parsley.lang.StringUtils;

@Repository
public class DaeDAOImpl extends EntityDAOImpl<DaeDO, String> implements DaeDAO {

	public DaeDAOImpl() {
	}

	@Override
	public Class<DaeDO> getEntityClass() {
		return DaeDO.class;
	}

	@Override
	protected FetchRule getFetchRule(String name) {
		if (name.equals(DaeDeepDepthRule.NAME)) {
			return new DaeDeepDepthRule();
		}

		if (name.equals(DaeImageRule.NAME)) {
			return new DaeImageRule();
		}
		if (name.equals(DaeMinimalDepthRule.NAME)) {
			return new DaeMinimalDepthRule();
		}

		if (name.equals(DaeAppMobileDepthRule.NAME)) {
			return new DaeAppMobileDepthRule();
		}

		if (name.equals(DaeDashboardDepthRule.NAME)) {
			return new DaeDashboardDepthRule();
		}
		if (name.equals(DaeForFaultDepthRule.NAME)) {
			return new DaeForFaultDepthRule();
		}

		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DaeDO> getAllDaeWithLocation() {
		Criteria criteria = createCriteria(DaeDeepDepthRule.NAME);
		criteria.add(Restrictions.isNotNull("posizione"));
		criteria.add(Restrictions.eqOrIsNull("deleted", false));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<DaeDO> searchDaeByFilter(DaeFilterDO filter) {

		Criteria criteria = createCriteria(filter, "super");

		if (!StringUtils.isEmpty(filter.getFetchRule())) {
			criteria.setFetchMode(filter.getFetchRule(), FetchMode.JOIN);
		}

		criteria.createAlias("posizione", "pos", JoinType.INNER_JOIN);
		criteria.createAlias("posizione.comune", "comun", JoinType.INNER_JOIN);
		criteria.createAlias("posizione.indirizzo", "indirizzo", JoinType.INNER_JOIN);
		criteria.createAlias("tipologiaStruttura", "tipStrutt");

		// criteria.add(Restrictions.isNotNull("posizione"));
		criteria.add(Restrictions.eqOrIsNull("deleted", false));

		if (filter.getIds() != null) {
			criteria.add(Restrictions.in("id", filter.getIds()));
		}

		if (!StringUtils.isEmpty(filter.getId())) {
			criteria.add(Restrictions.eq("id", filter.getId()));
		}
		if (!StringUtils.isEmpty(filter.getPosizione())) {
			criteria.add(Restrictions.eq("pos.id", filter.getPosizione()));
		}
		if (!StringUtils.isEmpty(filter.getComune())) {
			criteria.add(Restrictions.ilike("comun.nomeComune", "%" + filter.getComune().toUpperCase() + "%"));
		}
		if (!StringUtils.isEmpty(filter.getModello())) {
			criteria.add(Restrictions.ilike("modello", "%" + filter.getModello().toUpperCase() + "%"));
		}
		if (!StringUtils.isEmpty(filter.getNomeSede())) {
			criteria.add(Restrictions.ilike("nomeSede", "%" + filter.getNomeSede().toUpperCase() + "%"));
		}

		if (!StringUtils.isEmpty(filter.getEmail())) {
			criteria.createAlias("responsabile", "responsabile");
			criteria.add(Restrictions.ilike("responsabile.email", "%" + filter.getEmail() + "%"));
		}

		if (!StringUtils.isEmpty(filter.getTipo())) {
			criteria.add(Restrictions.eq("tipo", filter.getTipo()));
		}
		if (filter.getScadenzaDae() != null) {
			criteria.add(Restrictions.le("scadenzaDae", filter.getScadenzaDae()));
		}

		if (filter.getStatoVisible() != null || filter.getStatoVisible118() != null
				|| !StringUtils.isEmpty(filter.getNotInStatoDAE()) || !StringUtils.isEmpty(filter.getCurrentStato())) {
			criteria.createAlias("currentStato", "cS", JoinType.LEFT_OUTER_JOIN);
		}

		if (filter.getStatoVisible() != null) {
			criteria.add(Restrictions.or(Restrictions.eq("cS.visible", filter.getStatoVisible()),
					Restrictions.isNull("currentStato")));
		}

		if (filter.getStatoVisible118() != null) {
			criteria.add(Restrictions.or(Restrictions.eq("cS.visible118", filter.getStatoVisible118()),
					Restrictions.isNull("currentStato")));
		}

		if (!StringUtils.isEmpty(filter.getNotInStatoDAE())) {
			criteria.add(Restrictions.or(Restrictions.ne("cS.nome", filter.getNotInStatoDAE()),
					Restrictions.isNull("currentStato")));
		}

		if (!StringUtils.isEmpty(filter.getCurrentStato())) {
			criteria.add(Restrictions.eq("cS.nome", filter.getCurrentStato()));
		}

		if (filter.getTipologiaStruttura() != null) {
			criteria.add(Restrictions.ilike("tipStrutt.descrizione",
					"%" + filter.getTipologiaStruttura().toUpperCase() + "%"));
		}
		if (filter.getIndirizzo() != null) {
			criteria.add(Restrictions.ilike("indirizzo.name", "%" + filter.getIndirizzo().toUpperCase() + "%"));
		}
		if (filter.getStatoValidazione() != null) {
			criteria.add(Restrictions.eq("statoValidazione", filter.getStatoValidazione()));
		}

		if (filter.getTipoManutenzione() != null || filter.getScadenzaManutenzioneA() != null
				|| filter.getScadenzaManutenzioneDa() != null || filter.getTipoManutenzioneList() != null) {
			// aggiungo il join se uno dei parametri è stato valorizzato
			criteria.createAlias("programmiManutenzione", "programmiManutenzione", JoinType.LEFT_OUTER_JOIN);
		}

		if (filter.getTipoManutenzione() != null) {
			criteria.add(Restrictions.eq("programmiManutenzione.tipoManutenzione",
					filter.getTipoManutenzione().getDescription()));
		}

		if (filter.getTipoManutenzioneList() != null && filter.getTipoManutenzioneList().length > 0) {
			List<String> types = Arrays.asList(filter.getTipoManutenzioneList()).stream().map(e -> e.getDescription())
					.collect(Collectors.toList());

			criteria.add(Restrictions.in("programmiManutenzione.tipoManutenzione", types));
		}

		if (filter.getScadenzaManutenzioneA() != null && filter.getScadenzaManutenzioneDa() != null) {
			// il programma manutenzione deve essere compreso tra le due
			// date
			criteria.add(Restrictions.ge("programmiManutenzione.scadenzaDopo", filter.getScadenzaManutenzioneDa()));
			criteria.add(Restrictions.le("programmiManutenzione.scadenzaDopo", filter.getScadenzaManutenzioneA()));
			// seleziono il programma con la scadenza maggiore
			DetachedCriteria subselect = DetachedCriteria.forClass(ProgrammaManutenzioneDO.class, "sub");
			subselect.setProjection(Projections.max("sub.scadenzaDopo"));
			subselect.add(Restrictions.eqProperty("sub.dae.id", "super.id"));
			if (filter.getTipoManutenzione() != null) {
				// filtro tutti quelli dello stesso tipo
				subselect.add(Restrictions.eq("sub.tipoManutenzione", filter.getTipoManutenzione().getDescription()));
			}
			//
			criteria.add(Property.forName("programmiManutenzione.scadenzaDopo").eq(subselect));
		}

		if (filter.getOperativo() != null) {
			criteria.add(Restrictions.eq("operativo", filter.getOperativo()));
		}

		if (!StringUtils.isEmpty(filter.getProvince())
				|| (filter.getProvinces() != null && !filter.getProvinces().isEmpty())) {
			criteria.createAlias("comun.provincia", "provincia", JoinType.LEFT_OUTER_JOIN);
		}

		if (!StringUtils.isEmpty(filter.getProvince())) {
			criteria.add(Restrictions.eq("provincia.nomeProvincia", filter.getProvince()));
		}

		if (filter.getProvinces() != null || filter.getMunicipalities() != null) {

			Disjunction or = Restrictions.disjunction();
			// se nel filtro è presente la lista di province
			if ((filter.getProvinces() != null && !filter.getProvinces().isEmpty())) {
				or.add(Restrictions.in("provincia.nomeProvincia", filter.getProvinces()));
			}

			// se nel filtro è presente la lista di comuni
			if ((filter.getMunicipalities() != null && !filter.getMunicipalities().isEmpty())) {
				or.add(Restrictions.in("comun.nomeComune", filter.getMunicipalities()));
			}
			criteria.add(or);
		}

		if (filter.getIsInFault() != null) {
			if (filter.getIsInFault()) {
				criteria.createAlias("guasti", "guasti", JoinType.LEFT_OUTER_JOIN);

				if (filter.getFaultState() != null) {
					criteria.add(Restrictions.eq("guasti.statoAttuale", filter.getFaultState()));
				} else {
					criteria.add(Restrictions.in("guasti.statoAttuale",
							Arrays.asList(DaeFaultStatoEnum.APERTA, DaeFaultStatoEnum.DA_VERIFICARE,
									DaeFaultStatoEnum.VERIFICATA, DaeFaultStatoEnum.ERRATA,
									DaeFaultStatoEnum.AZIONI_INTRAPRESE)));
				}
			} else {
				// non deve esistere nessuna segnalazione aperta
				DetachedCriteria subQuery = DetachedCriteria.forClass(DaeFaultDO.class, "subFault");
				subQuery.add(Property.forName("super.id").eqProperty("subFault.dae.id"));
				subQuery.add(Restrictions.in("statoAttuale",
						Arrays.asList(DaeFaultStatoEnum.APERTA, DaeFaultStatoEnum.DA_VERIFICARE,
								DaeFaultStatoEnum.VERIFICATA, DaeFaultStatoEnum.ERRATA,
								DaeFaultStatoEnum.AZIONI_INTRAPRESE)));
				subQuery.setProjection(Projections.property("id"));

				criteria.add(Subqueries.notExists(subQuery));
			}
		}
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (List<DaeDO>) criteria.list();
	}

	@Override
	public DaeDO insertDae(DaeDO daeDO) {

		if (daeDO.getResponsabile().getDae() == null) {
			daeDO.getResponsabile().setDae(daeDO);
		}

		// if (daeDO.getCertificatoDae() != null) {
		// if (daeDO.getCertificatoDae().getDae() == null) {
		// daeDO.getCertificatoDae().setDae(daeDO);
		// }
		// }
		return this.save(daeDO);
	}

	@Override
	public List<DaeSubscription> countDaeSubscription(Calendar from, Calendar to) {
		Criteria criteria = createCriteria();
		ProjectionList projL = Projections.projectionList();
		projL.add(Projections.sqlGroupProjection(
				"count(*) as numDAE, to_char(this_.data_inserimento, 'yyyy-MM-dd') as aaaammgg",
				"to_char(this_.data_inserimento, 'yyyy-MM-dd') order by aaaammgg asc",
				new String[] { "numDAE", "aaaammgg" }, new Type[] { IntegerType.INSTANCE, StringType.INSTANCE }));
		criteria.setProjection(projL);
		criteria.setResultTransformer(Transformers.aliasToBean(DaeSubscription.class));

		criteria.add(Restrictions.ge("dataInserimento", from.getTime()));
		criteria.add(Restrictions.le("dataInserimento", to.getTime()));

		return criteria.list();
	}

	@Override
	public List<DaeDO> findDuplicate(DaeDO daeDO, List<String> duplicateDaeFieldList) {
		Criteria c = createCriteria(DaeMinimalDepthRule.NAME);

		// escludo quelli cancellati
		c.add(Restrictions.eqOrIsNull("deleted", false));

		PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();

		Set<String> join = new HashSet<>();

		duplicateDaeFieldList.forEach(s -> {
			try {
				Object value = null;
				if (s.contains(".")) {
					value = propertyUtilsBean.getNestedProperty(daeDO, s);
					//
					if (value != null) {
						String[] joins = s.split("\\.");
						for (int i = 0; i < joins.length - 1; i++) {
							if (!join.contains(joins[i])) {
								if (i > 0) {
									c.createAlias(joins[i - 1] + "." + joins[i], joins[i]);
								} else {
									c.createAlias(joins[i], joins[i]);
								}
								join.add(joins[i]);
							}
						}

						c.add(Restrictions.eq(joins[joins.length - 2] + "." + joins[joins.length - 1], value));
					}
				} else {
					value = propertyUtilsBean.getProperty(daeDO, s);
					if (value != null) {
						c.add(Restrictions.eq(s, value));
					}
				}

			} catch (Exception e) {
			}
		});

		return c.list();
	}

	@Override
	public List<DaeNumbers> fetchDaeNumbersByStatus(Calendar from, Calendar to) {
		Criteria criteria = createCriteria();
		criteria.createAlias("currentStato", "currentStato");

		ProjectionList projL = Projections.projectionList();
		projL.add(Projections.alias(Projections.count("id"), "numDAE"));
		projL.add(Projections.alias(Projections.property("currentStato.nome"), "status"));
		projL.add(Projections.groupProperty("currentStato.nome"));

		criteria.setProjection(projL);
		criteria.setResultTransformer(Transformers.aliasToBean(DaeNumbers.class));

		criteria.add(Restrictions.ge("dataInserimento", from.getTime()));
		criteria.add(Restrictions.le("dataInserimento", to.getTime()));

		criteria.add(Restrictions.eqOrIsNull("deleted", false));

		return criteria.list();
	}

}
