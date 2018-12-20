package pl.codecity.module.users.dto;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import static pl.codecity.module.users.util.Constants.STATUS_FAILED;

@Data
@JsonSerialize(include= JsonSerialize.Inclusion.NON_NULL)
public class ErrorDTO {

    private final String message;
    private final String status;

    public ErrorDTO(String message) {
        this.message = message;
        this.status = STATUS_FAILED;
    }
}
