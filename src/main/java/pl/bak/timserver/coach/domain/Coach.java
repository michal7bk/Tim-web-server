package pl.bak.timserver.coach.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "coach")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    public List<Training> acceptedTrainings;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "coach")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    public List<Training> proposedTrainings;


}
