package pl.codecity.module.users.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.codecity.module.users.model.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByToken(String token);
    User findByEmail(String email);
    boolean existsByEmail(String email);

    @Query(value = "SELECT R.name FROM user U INNER JOIN users_roles UR ON UR.user_id = U.id INNER JOIN role R ON R.id = UR.role_id WHERE token = ?1", nativeQuery = true)
    List<String> getUserRoleBasedOnToken(@Param("token") String token);

    @Query(value = "SELECT id FROM user WHERE email = ?1", nativeQuery = true)
    String getId(@Param("token") String token);

    @Modifying
    @Query(value = "UPDATE user u SET u.token = :token WHERE u.id = :id", nativeQuery = true)
    int updateToken(@Param("token") String token, @Param("id") Long id);

    @Query(value = "SELECT email FROM user WHERE email = ?0", nativeQuery = true)
    List<User> findAllByEmail(String email);

    List<User> findAll();
}
