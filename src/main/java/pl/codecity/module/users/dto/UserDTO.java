package pl.codecity.module.users.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.ToString;
import pl.codecity.module.users.model.Role;

import java.util.Collection;

@Data
@ToString(callSuper = true, exclude = {"roles", "users"})
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class UserDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String token;
    private String password;
    private String message;
    private String code;
    private Collection<Role> roles;
}
