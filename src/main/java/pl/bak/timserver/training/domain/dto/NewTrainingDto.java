package pl.bak.timserver.training.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewTrainingDto {

    public Long customerId;
    public Long coachId;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public String info;
}
