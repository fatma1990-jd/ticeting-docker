package com.microservices.demo.dto;

import com.fasterxml.jackson.annotation.*;
import com.microservices.demo.enums.Gender;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"},ignoreUnknown = true)
public class UserDTO {

    private Long id;

    private String firstName;
    private String lastName;
    private String userName;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passWord;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String confirmPassword;
    private boolean enabled;
    private String phone;
    private RoleDTO role;
    private Gender gender;
}
