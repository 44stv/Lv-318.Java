package org.uatransport.config.modelmapperconfig;

import lombok.RequiredArgsConstructor;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import org.uatransport.entity.Transit;
import org.uatransport.entity.dto.TransitDTO;

@Component
@RequiredArgsConstructor
public class TransitMap extends PropertyMap<Transit, TransitDTO> {

    @Override
    protected void configure() {
        map().setCategoryId(source.getCategory().getId());
        map(source.getCategory().setId(map().getCategoryId()));
    }
}
