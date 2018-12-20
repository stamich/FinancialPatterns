package pl.codecity.module.users.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.transaction.annotation.Transactional;
import pl.codecity.module.users.model.Privilege;
import pl.codecity.module.users.model.Role;
import pl.codecity.module.users.model.User;
import pl.codecity.module.users.repository.PrivilegeRepository;
import pl.codecity.module.users.repository.RoleRepository;
import pl.codecity.module.users.repository.UserRepository;
import pl.codecity.module.users.util.Constants;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordHelper passwordHelper;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup) return;

        Privilege DATASET_NEW = createPrivilegeIfNotFound("DATASET_NEW");
        Privilege DATASET_EDIT = createPrivilegeIfNotFound("DATASET_EDIT");
        Privilege DATASET_DELETE = createPrivilegeIfNotFound("DATASET_DELETE");
        Privilege DATASET_VIEW = createPrivilegeIfNotFound("DATASET_VIEW");

        Privilege USER_MANAGEMENT_ADD_USER = createPrivilegeIfNotFound("USER_MANAGEMENT_ADD_USER");
        Privilege USER_MANAGEMENT_EDIT_USER = createPrivilegeIfNotFound("USER_MANAGEMENT_EDIT_USER");
        Privilege USER_MANAGEMENT_DELETE_USER = createPrivilegeIfNotFound("USER_MANAGEMENT_DELETE_USER");
        Privilege USER_MANAGEMENT_VIEW_USER = createPrivilegeIfNotFound("USER_MANAGEMENT_VIEW_USER");

        List<Privilege> adminPrivileges = Arrays.asList(USER_MANAGEMENT_ADD_USER, USER_MANAGEMENT_EDIT_USER, USER_MANAGEMENT_DELETE_USER, USER_MANAGEMENT_VIEW_USER);
        createRoleIfNotFound("ADMIN", adminPrivileges);
        createRoleIfNotFound("USER_PLACING_DATA_TO_SYSTEM", Arrays.asList(DATASET_NEW, DATASET_EDIT, DATASET_VIEW));

        Role adminRole = roleRepository.findByName("ADMIN");
        if (userRepository.findByEmail(Constants.ADMIN_EMAIL) == null) {
            User user = new User();
            user.setFirstName("Admin");
            user.setLastName("Admin");
            user.setPassword(passwordHelper.hashPassword("admin-test"));
            user.setEmail(Constants.ADMIN_EMAIL);
            user.setRoles(Arrays.asList(adminRole));
            user.setActive(true);
            user.setToken("f8d47660-d896-46ef-a10c-40d777b53788-1");
            userRepository.save(user);
        }
        alreadySetup = true;
    }

    @Transactional
    private Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege();
            privilege.setName(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    private Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}
