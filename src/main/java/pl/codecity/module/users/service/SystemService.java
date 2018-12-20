package pl.codecity.module.users.service;

import org.springframework.stereotype.Service;
import pl.codecity.module.users.dto.ConfigurationDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SystemService {

    public ConfigurationDTO getConfiguration() {
        ConfigurationDTO configurationDTO = new ConfigurationDTO();
        List<String> roles = new ArrayList<String>(Arrays.asList("ADMIN, MANAGEMENT, DIRECTOR, USER_PLACING_DATA_TO_SYSTEM"));
        configurationDTO.setRoles(roles);
        return configurationDTO;
    }
}
