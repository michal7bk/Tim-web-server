package pl.bak.timserver.customer.domain.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerTrainingsDto {
    public String name;
    public String username;
    public String email;
}
