package io.kukua.springbootapi.user.dto;

import io.kukua.springbootapi.auth.dto.request.RegisterRequest;
import io.kukua.springbootapi.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User fromDto(RegisterRequest registerRequest);

}
