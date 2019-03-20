package pl.bak.timserver.training.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.bak.timserver.coach.domain.dto.CoachTrainingsDto;
import pl.bak.timserver.customer.domain.dto.CustomerTrainingsDto;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDto {
    public Long id;
    public CustomerTrainingsDto customer;
    public CoachTrainingsDto coach;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public String info;
}
