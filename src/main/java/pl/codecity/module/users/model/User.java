package pl.codecity.module.users.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"roles", "users"})
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @ApiModelProperty(notes = "The user id automatically generated", required = true)
    private Long id;

    @ApiModelProperty(notes = "The first name of the user", required = true)
    private String firstName;

    @ApiModelProperty(notes = "The last name of the user", required = true)
    private String lastName;

    @NotNull
    @Column(unique=true)
    @ApiModelProperty(notes = "The user email", required = true)
    private String email;

    @NotNull
    @ApiModelProperty(notes = "The user password", required = true)
    private String password;

    @ApiModelProperty(notes = "Checking if the user is active", required = true)
    private boolean isActive;

    @Column(unique=true)
    @ApiModelProperty(notes = "The user token", required = true)
    private String token;

    @ApiModelProperty(notes = "The user token date and time", required = true)
    private LocalDateTime tokenUpdateTime;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;
}
