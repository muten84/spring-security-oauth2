/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.eng.areas.ems.sdoemsrepo.entity.DeviceInfoDO;

/**
 * @author Bifulco Luigi
 *
 */
public interface DeviceInfoRepository extends JpaRepository<DeviceInfoDO, String> {

	List<DeviceInfoDO> findByDeviceId(String deviceId);
}
