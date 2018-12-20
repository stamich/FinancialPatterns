package pl.codecity.module.users.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.codecity.module.users.model.Privilege;
import pl.codecity.module.users.model.Role;

import java.util.List;

public interface PrivilegeRepository extends CrudRepository<Privilege, Long> {

    Privilege findByName(String name);

    @Query(value = "SELECT R.name FROM user U INNER JOIN users_roles UR ON UR.user_id = U.id INNER JOIN role R ON R.id = UR.role_id WHERE token = ?1", nativeQuery = true)
    List<String> getPrivilegesForRole(@Param("role") Role role);
}
