package pl.codecity.module.users.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import pl.codecity.module.users.enumerate.eRole;
import pl.codecity.module.users.model.Privilege;
import pl.codecity.module.users.model.Role;
import pl.codecity.module.users.repository.RoleRepository;
import pl.codecity.module.users.repository.UserRepository;

import javax.annotation.security.RolesAllowed;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
@Transactional
public class PermissionHelper {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    public Collection<Privilege> getPrivilgeListForRole(eRole p_role) {
        Role role = roleRepository.findByName(p_role.name());
        Collection<Privilege> list = role.getPrivileges();
        return list;
    }

    public boolean checkIfUserExistsAndHasRightsToPerformAction(String token, RolesAllowed securityRolesAllowed) {

        List<String> roleNamesList = userRepository.getUserRoleBasedOnToken(token);
        String[] roles = Arrays.asList(securityRolesAllowed.value()).stream().map(role -> role.name()).toArray(String[]::new);

        boolean hasPermission = CollectionUtils.containsAny(roleNamesList, Arrays.asList(roles));
        return (securityRolesAllowed.value() != null && securityRolesAllowed.value().length > 0 && hasPermission);
    }
}
