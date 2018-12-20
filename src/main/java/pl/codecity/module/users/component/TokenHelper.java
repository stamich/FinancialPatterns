package pl.codecity.module.users.component;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TokenHelper {

    public String generateTokenBasedOnUserID(Long userID) {
        return UUID.randomUUID() + "-" + userID;
    }
}
