package pl.bak.timserver.customer.domain.dto;

import lombok.*;
import pl.bak.timserver.training.domain.dto.TrainingDto;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerInfoDto {

    private Long id;
    public String name;
    public String surname;
    public String email;
    public List<TrainingDto> trainings;
}
