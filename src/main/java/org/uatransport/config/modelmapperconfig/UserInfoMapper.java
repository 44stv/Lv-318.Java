package org.uatransport.config.modelmapperconfig;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.uatransport.entity.User;
import org.uatransport.entity.dto.UserInfo;

public class UserInfoMapper implements Converter<User, UserInfo> {

    @Override
    public UserInfo convert(MappingContext<User, UserInfo> context) {
        User source = context.getSource();
        UserInfo destination = context.getDestination();

        destination.setFirstName(source.getFirstName());
        destination.setLastName(source.getLastName());
        destination.setEmail(source.getEmail());

        return destination;
    }
}
