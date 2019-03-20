package pl.bak.timserver.training.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.bak.timserver.coach.domain.Coach;
import pl.bak.timserver.customer.domain.Customer;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "TRAINING")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    public Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coach_id")
    public Coach coach;

    public LocalDateTime startTime;
    public LocalDateTime endTime;

    public String info;
    public boolean accepted;


}
