/**
 * 
 */
package it.eng.areas.ordinari.aaa.model.mapper;

import java.util.List;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import it.eng.areas.ems.ordinari.entity.TsAuthorityDO;
import it.eng.areas.ordinari.aaa.model.AAAModuleDTO;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED)
public interface DTOMapper {

	@Mappings({ @Mapping(source = "type", target = "field") })
	AAAModuleDTO do2dto(TsAuthorityDO authority);

	List<AAAModuleDTO> doCollection2dtoCollection(List<TsAuthorityDO> fromList);
}
