package pl.bak.timserver.coach.domain.dto;


import lombok.*;
import pl.bak.timserver.training.domain.Training;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CoachInfoDto {
    private Long id;
    public String name;
    public String surname;
    public String email;
    public List<Training> acceptedTrainings;
    public List<Training> proposedTrainings;

}
