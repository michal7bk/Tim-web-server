package pl.bak.timserver.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bak.timserver.coach.domain.Coach;
import pl.bak.timserver.training.domain.Training;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
    List<Training> findByCoach(Coach coach);
}
