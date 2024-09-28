package com.example.meettify.config.modelMapper;

import com.example.meettify.dto.member.MemberServiceDTO;
import com.example.meettify.dto.member.role.UserRole;
import com.example.meettify.entity.member.MemberEntity;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
*   worker  : 유요한
*   work    : 동일한 엔티티와 DTO를 일일이 변경 메서드를 만드는 것은 번거로우니
*             ModelMapper을 사용하여 변경하는 작업을 해준다.
*   date    : 2024/09/19
* */

@Configuration
public class RootConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT); // STRICT으로 변경

        // Enum에 대한 커스텀 컨버터 추가
        Converter<String, UserRole> toUserRole = new Converter<>() {
            public UserRole convert(MappingContext<String, UserRole> context) {
                return UserRole.valueOf(context.getSource());
            }
        };

        modelMapper.addConverter(toUserRole);

        return modelMapper;
    }
}
