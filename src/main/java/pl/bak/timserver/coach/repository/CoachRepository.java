package pl.bak.timserver.coach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bak.timserver.coach.domain.Coach;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Long> {
    Coach findByEmail(String email);
}
