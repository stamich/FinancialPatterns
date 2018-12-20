package pl.codecity.module.users.repository;

import org.springframework.data.repository.CrudRepository;
import pl.codecity.module.users.model.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByName(String name);
}
