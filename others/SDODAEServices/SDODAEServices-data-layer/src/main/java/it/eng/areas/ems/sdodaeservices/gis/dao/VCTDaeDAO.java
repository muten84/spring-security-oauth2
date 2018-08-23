package it.eng.areas.ems.sdodaeservices.gis.dao;

import java.util.List;

import it.eng.area118.sdocommon.dao.EntityDAO;
import it.eng.areas.ems.sdodaeservices.entity.filter.DaeFilterDO;
import it.eng.areas.ems.sdodaeservices.entity.gis.VCTDaeDO;
import it.eng.areas.ems.sdodaeservices.entity.gis.VctDaeDODistanceBean;

public interface VCTDaeDAO extends EntityDAO<VCTDaeDO, String> {

	List<VCTDaeDO> getAllVCTDAE();

	List<VctDaeDODistanceBean> getPagedVctDAEOrderByDistance(Integer pageSize, Integer start, Double latitudine,
			Double longitudine, Integer SRID);

	List<VctDaeDODistanceBean> getPagedVctDAEOrderByDistance(DaeFilterDO daeFilter);

	List<VCTDaeDO> executeSQL(String sql);

	VCTDaeDO saveOrUpdate(VCTDaeDO daeDO);

	Integer executeSQLInsert(String string);

}
