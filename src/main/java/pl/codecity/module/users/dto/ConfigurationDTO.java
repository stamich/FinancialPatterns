package pl.codecity.module.users.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.List;

@Data
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class ConfigurationDTO {

    private List<String> roles;
}
