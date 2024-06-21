package io.kukua.springbootapi.user.dto;

import io.kukua.springbootapi.auth.dto.request.RegisterRequest;
import io.kukua.springbootapi.user.User;
import io.kukua.springbootapi.user.dto.response.UserData;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User fromDto(RegisterRequest registerRequest);
    UserData toDto(User user);

}
