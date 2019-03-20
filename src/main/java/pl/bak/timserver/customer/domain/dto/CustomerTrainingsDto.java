package pl.bak.timserver.customer.domain.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerTrainingsDto {
    public Long id;
    public String name;
    public String username;
    public String email;
}
