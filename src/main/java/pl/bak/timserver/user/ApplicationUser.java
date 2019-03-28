package pl.bak.timserver.user;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String name;

    private String surname;

    private String password;

    @Email
    private String email;

    @Builder.Default()
    private boolean active = false;

    @Builder.Default()
    private Roles roles = Roles.customer;

    public enum Roles {
        customer, coach
    }

}
