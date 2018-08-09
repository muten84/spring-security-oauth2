package it.eng.areas.ems.sdodaeservices.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.areas.ems.sdodaeservices.dao.GruppoDAO;
import it.eng.areas.ems.sdodaeservices.dao.impl.rule.GruppoDeepDepthRule;
import it.eng.areas.ems.sdodaeservices.entity.ComuneDO;
import it.eng.areas.ems.sdodaeservices.entity.GruppoComuneDO;
import it.eng.areas.ems.sdodaeservices.entity.GruppoDO;
import it.eng.areas.ems.sdodaeservices.entity.GruppoProvinciaDO;
import it.eng.areas.ems.sdodaeservices.entity.filter.GruppoFilterDO;

@Repository
public class GruppoDAOImpl extends EntityDAOImpl<GruppoDO, String> implements GruppoDAO {

	public GruppoDAOImpl() {
	}

	@Override
	public Class<GruppoDO> getEntityClass() {
		return GruppoDO.class;
	}

	protected FetchRule getFetchRule(String name) {
		if (name.equals(GruppoDeepDepthRule.NAME)) {

			return new GruppoDeepDepthRule();
		}

		return null;
	}

	public List<GruppoDO> searchGruppoByFilter(GruppoFilterDO filter) {

		// riempio la lista dei comuni se ho le province piene
		if (filter.getProvinces() != null && filter.getProvinces().size() > 0) {
			Criteria provCri = createCriteria(ComuneDO.class);
			provCri.createAlias("provincia", "provincia");
			provCri.add(Restrictions.in("provincia.nomeProvincia", filter.getProvinces()));
			provCri.setProjection(Projections.property("nomeComune"));

			if (filter.getMunicipalities() == null) {
				filter.setMunicipalities(new ArrayList<>());
			}

			filter.getMunicipalities().addAll(provCri.list());

		}

		Criteria criteria = createCriteria(filter, "super");
		if (filter.getProvinces() != null || filter.getMunicipalities() != null) {
			Disjunction or = Restrictions.disjunction();

			Conjunction and1 = Restrictions.conjunction();

			if (filter.getProvinces() != null) {
				if (filter.getProvinces().size() > 0) {
					DetachedCriteria subQuery = DetachedCriteria.forClass(GruppoProvinciaDO.class, "sub1");
					subQuery.add(Property.forName("super.id").eqProperty("sub1.gruppo.id"));
					subQuery.add(Restrictions.in("nomeProvincia", filter.getProvinces()));
					subQuery.setProjection(Projections.property("id"));

					or.add(Subqueries.exists(subQuery));
				}
				/////////
				DetachedCriteria subQuery2 = DetachedCriteria.forClass(GruppoProvinciaDO.class, "sub12");
				subQuery2.add(Property.forName("super.id").eqProperty("sub12.gruppo.id"));
				if (filter.getProvinces().size() > 0) {
					subQuery2.add(Restrictions.not(Restrictions.in("nomeProvincia", filter.getProvinces())));
				}
				subQuery2.setProjection(Projections.property("id"));

				and1.add(Subqueries.notExists(subQuery2));
			}

			if (filter.getMunicipalities() != null) {
				if (filter.getMunicipalities().size() > 0) {
					DetachedCriteria subQuery = DetachedCriteria.forClass(GruppoComuneDO.class, "sub2");
					subQuery.add(Property.forName("super.id").eqProperty("sub2.gruppo.id"));
					subQuery.add(Restrictions.in("nomeComune", filter.getMunicipalities()));
					subQuery.setProjection(Projections.property("id"));

					or.add(Subqueries.exists(subQuery));
				}
				/////////
				DetachedCriteria subQuery2 = DetachedCriteria.forClass(GruppoComuneDO.class, "sub22");
				subQuery2.add(Property.forName("super.id").eqProperty("sub22.gruppo.id"));
				if (filter.getMunicipalities().size() > 0) {
					subQuery2.add(Restrictions.not(Restrictions.in("nomeComune", filter.getMunicipalities())));
				}
				subQuery2.setProjection(Projections.property("id"));

				and1.add(Subqueries.notExists(subQuery2));
			}
			criteria.add(or);

			criteria.add(and1);
		}
		criteria.addOrder(Order.asc("nome"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public GruppoDO insertGruppo(GruppoDO gruppoDO) {

		return this.save(gruppoDO);
	}

}
