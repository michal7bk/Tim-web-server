package pl.bak.timserver.coach.service;

import org.springframework.stereotype.Service;
import pl.bak.timserver.coach.domain.Coach;
import pl.bak.timserver.coach.repository.CoachRepository;
import pl.bak.timserver.exception.ConflictWithExistingException;
import pl.bak.timserver.exception.ObjectNotFoundExcpetion;
import pl.bak.timserver.training.domain.Training;

import java.util.List;

@Service
public class CoachService {
    final private CoachRepository coachRepository;

    public CoachService(CoachRepository coachRepository) {
        this.coachRepository = coachRepository;
    }

    public List<Coach> findCoaches() {
        return coachRepository.findAll();
    }

    public Coach findCoach(Long id) {
        return coachRepository.findById(id).orElseThrow(() -> new ObjectNotFoundExcpetion(Coach.class, id));
    }

    public Coach save(Coach coach) {
        if (coachRepository.findByEmail(coach.getEmail()) == null)
            return coachRepository.save(coach);
        else throw new ConflictWithExistingException(Coach.class, coach.getId());
    }

    public void delete(Long id) {
        Coach Coach = coachRepository.findById(id).orElseThrow(() -> new ObjectNotFoundExcpetion(Coach.class, id));
        coachRepository.delete(Coach);
    }

    public List<Training> findAcceptedTrainings(Long id) {
        Coach coach = coachRepository.findById(id).orElseThrow(() -> new ObjectNotFoundExcpetion(Coach.class, id));
        return coach.getAcceptedTrainings();
    }

    public List<Training> findProposedTrainings(Long id) {
        Coach coach = coachRepository.findById(id).orElseThrow(() -> new ObjectNotFoundExcpetion(Coach.class, id));
        return coach.getProposedTrainings();
    }


}
