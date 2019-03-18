package pl.bak.timserver.training.service;

import org.springframework.stereotype.Service;
import pl.bak.timserver.coach.domain.Coach;
import pl.bak.timserver.coach.repository.CoachRepository;
import pl.bak.timserver.exception.ConflictWithExistingException;
import pl.bak.timserver.exception.ObjectNotFoundExcpetion;
import pl.bak.timserver.training.domain.Training;
import pl.bak.timserver.training.repository.TrainingRepository;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class TrainingService {

    final private TrainingRepository trainingRepository;
    final private CoachRepository coachRepository;

    public TrainingService(TrainingRepository trainingRepository, CoachRepository coachRepository) {
        this.trainingRepository = trainingRepository;
        this.coachRepository = coachRepository;
    }

    public List<Training> findCoachTrainings(Long coach_id) {
        Coach coach = coachRepository.findById(coach_id)
                .orElseThrow(() -> new ObjectNotFoundExcpetion(Coach.class, coach_id));
        return trainingRepository.findByCoach(coach);
    }

    public Training findTraining(Long id) {
        return trainingRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundExcpetion(Training.class, id));
    }

    public Training save(Training training) {
        Coach coach = coachRepository.findById(training.getId())
                .orElseThrow(()->new ObjectNotFoundExcpetion(Coach.class,training.getCoach().getId()));
        if (!overlapsWithExisting(training)) {
            coach.getProposedTrainings().add(training);
            return trainingRepository.save(training);
        }
        else
            throw new ConflictWithExistingException(Training.class, training.id);
    }

    public void delete(Long id) {
        Training training = trainingRepository.findById(id)
                .orElseThrow(()->new ObjectNotFoundExcpetion(Training.class,id));
        trainingRepository.delete(training);
    }

    public Training acceptTraining(Training training) {
        Coach coach = training.getCoach();
        Training trainingUpdate = trainingRepository.findById(training.getId())
                .orElseThrow(()-> new ObjectNotFoundExcpetion(Training.class,training.getId()));
        coach.getProposedTrainings().remove(trainingUpdate);
        coach.getAcceptedTrainings().add(trainingUpdate);
        return training;
    }

    private boolean overlapsWithExisting(Training training) {
        final Coach coach = training.getCoach();
        List<Training> existingTrainings = coach.getAcceptedTrainings();
        final LocalDateTime startTime = training.getStartTime();
        final LocalDateTime endTime = training.getEndTime();
        for (Training existing : existingTrainings) {
            if (!(startTime.isAfter(existing.getStartTime()) || endTime.isBefore(existing.getEndTime())))
                return true;
        }
        return false;
    }

}
