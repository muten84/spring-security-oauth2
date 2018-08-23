/**
 * 
 */
package it.eng.areas.ems.sdoemsrepo.delegate.mapper;

import java.util.List;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import it.eng.areas.ems.sdoemsrepo.delegate.model.ArtifactInfo;
import it.eng.areas.ems.sdoemsrepo.delegate.model.ArtifactInfoRequest;
import it.eng.areas.ems.sdoemsrepo.entity.ArtifactDO;

/**
 * @author Bifulco Luigi
 *
 */

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED)
public interface ArtifactsMapper {

	ArtifactInfo fromDO2Info(ArtifactDO artifact);

	@Mappings(value = { @Mapping(source = "artifactId", target = "artifacId"), //
			@Mapping(source = "groupId", target = "groupId"), //
			@Mapping(source = "artifactType", target = "aritfactType")//
	})
	ArtifactInfoRequest fromDO2Request(ArtifactDO artifact);
	
	List<ArtifactInfoRequest> fromDOList2RequestList(List<ArtifactDO> artifact);

}
