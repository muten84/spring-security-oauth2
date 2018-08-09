package it.eng.areas.ordinari.aaa.model.mapper;

import it.eng.areas.ems.ordinari.entity.TsAuthorityDO;
import it.eng.areas.ordinari.aaa.model.AAAModuleDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class DTOMapperImpl implements DTOMapper {

    @Override
    public AAAModuleDTO do2dto(TsAuthorityDO authority) {
        if ( authority == null ) {
            return null;
        }

        AAAModuleDTO aAAModuleDTO = new AAAModuleDTO();

        aAAModuleDTO.setField( authority.getType() );

        return aAAModuleDTO;
    }

    @Override
    public List<AAAModuleDTO> doCollection2dtoCollection(List<TsAuthorityDO> fromList) {
        if ( fromList == null ) {
            return null;
        }

        List<AAAModuleDTO> list = new ArrayList<AAAModuleDTO>();
        for ( TsAuthorityDO tsAuthorityDO : fromList ) {
            list.add( do2dto( tsAuthorityDO ) );
        }

        return list;
    }
}
