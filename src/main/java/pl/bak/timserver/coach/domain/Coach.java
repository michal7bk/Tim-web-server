package pl.bak.timserver.coach.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import pl.bak.timserver.training.domain.Training;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Table(name = "coach")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coach {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @NotNull
    public String name;
    @NotNull
    public String surname;
    @Email
    public String email;

    public String password;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "coach")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    public List<Training> trainings;


}
