/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.delegate.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;

import it.eng.areas.ems.sdoemsrepo.delegate.model.DeviceConfiguration;
import it.eng.areas.ems.sdoemsrepo.delegate.model.SiteInfo;

/**
 * @author Bifulco Luigi
 *
 */
@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED)
public interface SiteInfoMapper {

	SiteInfo cloneSiteInfo(SiteInfo artifact);

	DeviceConfiguration cloneDeviceConfiguration(DeviceConfiguration configuration);
}
