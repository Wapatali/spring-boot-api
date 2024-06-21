package io.kukua.springbootapi.user.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class UserData {

    private UUID id;
    private String username;

}
