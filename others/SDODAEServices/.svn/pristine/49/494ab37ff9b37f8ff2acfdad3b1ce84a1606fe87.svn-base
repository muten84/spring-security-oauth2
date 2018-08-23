package it.eng.areas.ems.sdodaeservices.gis.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.impl.EntityDAOImpl;
import it.eng.area118.sdocommon.dao.impl.FetchRule;
import it.eng.area118.sdocommon.datasource.ContextHolder;
import it.eng.areas.ems.sdodaeservices.entity.filter.DaeFilterDO;
import it.eng.areas.ems.sdodaeservices.entity.gis.VCTDaeDO;
import it.eng.areas.ems.sdodaeservices.entity.gis.VctDaeDODistanceBean;
import it.eng.areas.ems.sdodaeservices.gis.dao.VCTDaeDAO;
import it.esel.parsley.lang.StringUtils;

@Repository
public class VctDaeDAOImpl extends EntityDAOImpl<VCTDaeDO, String> implements VCTDaeDAO {

	private Logger logger = LoggerFactory.getLogger(VctDaeDAOImpl.class);

	@Override
	public Class<VCTDaeDO> getEntityClass() {
		// TODO Auto-generated method stub
		return VCTDaeDO.class;
	}

	@Override
	protected FetchRule getFetchRule(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VCTDaeDO> getAllVCTDAE() {
		if (ContextHolder.getDataSourceType() != null) {
			System.out.println("query on datasource: " + ContextHolder.getDataSourceType());
		}
		return getAll();
	}

	@Override
	public List<VctDaeDODistanceBean> getPagedVctDAEOrderByDistance(Integer pageSize, Integer start, Double latitudine,
			Double longitudine, Integer SRID) {
		String lat = latitudine.toString().replace(",", ".");
		String lon = longitudine.toString().replace(",", ".");
		String sql = "SELECT  id," + "ST_Distance_Sphere(ST_Transform (shape," + SRID + " ) ,ST_Geomfromtext('SRID="
				+ SRID + ";POINT(" + lon + " " + lat + ")') ) as distanceMt ";
		sql += " FROM vct_dae ";
		sql += " WHERE privato = false ";
		sql += " ORDER BY distanceMt";
		sql += " Limit " + pageSize + " OFFSET " + start;
		logger.info(">>> " + sql);
		NativeQuery query = getSession().createSQLQuery(sql);
		query.unwrap(SQLQuery.class).addScalar("id", StringType.INSTANCE);
		query.unwrap(SQLQuery.class).addScalar("distanceMt", DoubleType.INSTANCE);
		List<Object[]> distanceBean = query.list();
		List<VctDaeDODistanceBean> ret = new ArrayList<>();
		for (Object[] objects : distanceBean) {
			VctDaeDODistanceBean r1 = new VctDaeDODistanceBean();
			r1.setId(objects[0].toString().trim());
			r1.setDistanceMt(((Double) objects[1]));
			ret.add(r1);
		}
		return ret;
	}

	@Override
	public List<VctDaeDODistanceBean> getPagedVctDAEOrderByDistance(DaeFilterDO daeFilter) {

		Criteria c = createCriteria();

		ProjectionList projection = Projections.projectionList();

		projection.add(Projections.sqlProjection("ltrim(id) as id ", new String[] { "id" },
				new Type[] { StringType.INSTANCE }));

		if (daeFilter.getLocation().getLatitudine() != null && daeFilter.getLocation().getLongitudine() != null) {
			String lat = daeFilter.getLocation().getLatitudine().toString().replace(",", ".");
			String lon = daeFilter.getLocation().getLongitudine().toString().replace(",", ".");
			projection.add(Projections.sqlProjection(
					"ST_Distance_Sphere(ST_Transform (shape,4326) , ST_Geomfromtext('SRID="
							+ daeFilter.getLocation().getSrid() + ";POINT(" + lon + " " + lat + ")')) as distanceMt ",
					new String[] { "distanceMt" }, new Type[] { DoubleType.INSTANCE }));
			// c.addOrder(Order.asc("distanceMt"));
		}

		if (!StringUtils.isEmpty(daeFilter.getLocation().getGeoJSON())) {
			c.add(Restrictions.sqlRestriction(
					" ST_Contains (ST_SetSRID(ST_GeomFromGeoJSON('" + daeFilter.getLocation().getGeoJSON() + "'), "
							+ daeFilter.getLocation().getSrid() + ") ,ST_Transform(ST_Transform (shape,4326), "
							+ daeFilter.getLocation().getSrid() + ") ) = true"));

			projection.add(Projections.sqlProjection(
					"ST_Distance_Sphere(ST_Transform (shape,4326), ST_Centroid (ST_SetSRID(ST_GeomFromGeoJSON('"
							+ daeFilter.getLocation().getGeoJSON() + "'), " + daeFilter.getLocation().getSrid()
							+ ") ) ) as distanceMt ",
					new String[] { "distanceMt" }, new Type[] { DoubleType.INSTANCE }));
		}

		if (daeFilter.getStatoVisible() != null) {
			c.add(Restrictions.eq("privato", !daeFilter.getStatoVisible()));
		}

		c.setProjection(projection);
		c.setResultTransformer(Transformers.aliasToBean(VctDaeDODistanceBean.class));

		return c.list();

	}

	@Override
	public VCTDaeDO saveOrUpdate(VCTDaeDO daeDO) {

		List<VCTDaeDO> old = executeSQL("select id, matricola " //
				+ " from vct_dae where id ='" + daeDO.getId() + "'");
		String sql;
		if (old != null && old.size() > 0) {
			// update
			sql = "update vct_dae set ";
			sql += " matricola = :matricola ";
			sql += ", shape = ST_Transform (ST_GeomFromText('POINT("
					+ daeDO.getLocation().getCoordinate().getOrdinate(0) + " "
					+ daeDO.getLocation().getCoordinate().getOrdinate(1) + ")', 4326), 3003) ";
			sql += " ,operativo = :operativo ";
			sql += " ,privato = :privato";
			sql += " ,stato = :stato ";
			sql += " ,nome_sede = :nome_sede ";
			sql += " ,indirizzo = :indirizzo ";
			sql += " ,ubicazione =:ubicazione ";
			sql += " ,orari = :orari ";
			sql += " where id = :id ";
		} else {
			// insert
			sql = "INSERT INTO vct_dae(ID,matricola, shape, operativo, privato, stato, nome_sede, indirizzo, ubicazione, orari) ";
			sql += " VALUES(:id";
			sql += ",:matricola ";
			sql += ",ST_Transform (ST_GeomFromText('POINT(" + daeDO.getLocation().getCoordinate().getOrdinate(0) + " "
					+ daeDO.getLocation().getCoordinate().getOrdinate(1) + ")', 4326), 3003)  ";
			sql += " ,:operativo";
			sql += " ,:privato";
			sql += " ,:stato ";
			sql += " ,:nome_sede ";
			sql += " ,:indirizzo ";
			sql += " ,:ubicazione ";
			sql += " ,:orari  )";
		}

		NativeQuery<VCTDaeDO> query = getSession().createNativeQuery(sql);

		query.setParameter("id", daeDO.getId(), StringType.INSTANCE);
		query.setParameter("matricola", daeDO.getMatricola(), StringType.INSTANCE);
		query.setParameter("operativo", daeDO.isOperativo(), BooleanType.INSTANCE);
		query.setParameter("privato", daeDO.isPrivato(), BooleanType.INSTANCE);
		query.setParameter("stato", daeDO.getStato(), StringType.INSTANCE);
		query.setParameter("nome_sede", daeDO.getNomeSede(), StringType.INSTANCE);
		query.setParameter("indirizzo", daeDO.getIndirizzo(), StringType.INSTANCE);
		query.setParameter("ubicazione", daeDO.getUbicazione(), StringType.INSTANCE);
		query.setParameter("orari", daeDO.getOrari(), StringType.INSTANCE);

		query.executeUpdate();

		return daeDO;
	}

	@Override
	public List<VCTDaeDO> executeSQL(String sql) {
		NativeQuery<VCTDaeDO> query = getSession().createNativeQuery(sql);

		List<VCTDaeDO> ret = query.getResultList();

		return ret;
	}

	@Override
	public Integer executeSQLInsert(String sql) {
		Query query = getSession().createSQLQuery(sql);
		return query.executeUpdate();
	}

}
