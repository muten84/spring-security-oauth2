/**
 * 
 */
package it.eng.areas.to.sdoto.docservice.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import it.eng.area118.sdocommon.dao.EntityDAO;
import it.eng.areas.to.sdoto.docservice.entity.DepartmentAdresseeDO;
import it.eng.areas.to.sdoto.docservice.entity.DepartmentAdresseePk;
import it.eng.areas.to.sdoto.docservice.entity.filter.DepartmentAdresseeFilter;

/**
 * @author Bifulco Luigi
 *
 */
@Repository
public interface DepartmentAdresseeDAO extends EntityDAO<DepartmentAdresseeDO, DepartmentAdresseePk> {

	List<DepartmentAdresseeDO> searchDepartmentAddresses(DepartmentAdresseeFilter filter);

	DepartmentAdresseeDO getUserDepartment(String userId);
}
