package pl.bak.timserver.coach.domain.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CoachTrainingsDto {
    public Long id;
    public String name;
    public String surname;
    public String email;

}
