package io.kukua.springbootapi.auth.dto;

import io.kukua.springbootapi.auth.dto.response.LoginResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    LoginResponse toDto(String token);

}
