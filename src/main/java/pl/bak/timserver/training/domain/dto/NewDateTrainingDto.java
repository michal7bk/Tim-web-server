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
public class NewDateTrainingDto {

    public Long id;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
}
