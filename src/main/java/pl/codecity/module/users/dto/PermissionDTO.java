package pl.codecity.module.users.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import pl.codecity.module.users.model.Privilege;

import java.util.List;

@Data
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class PermissionDTO {

    private String role;
    private List<Privilege> rights;
}
