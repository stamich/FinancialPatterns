package pl.codecity.module.users.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class PasswordDTO {

    private String currentPassword;
    private String newPassword;
    private String email;
}
