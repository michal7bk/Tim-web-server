package pl.bak.timserver.customer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import pl.bak.timserver.training.domain.Training;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "customer")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    public String name;

    public String surname;

    @Email
    public String email;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    public List<Training> trainings;


}
