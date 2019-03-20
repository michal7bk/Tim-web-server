package pl.bak.timserver.training.service;

import org.springframework.stereotype.Service;
import pl.bak.timserver.coach.domain.Coach;
import pl.bak.timserver.coach.repository.CoachRepository;
import pl.bak.timserver.customer.domain.Customer;
import pl.bak.timserver.customer.repository.CustomerRepository;
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
    final private CustomerRepository customerRepository;

    public TrainingService(TrainingRepository trainingRepository, CoachRepository coachRepository, CustomerRepository customerRepository) {
        this.trainingRepository = trainingRepository;
        this.coachRepository = coachRepository;
        this.customerRepository = customerRepository;
    }

    public Training save(Training training) {
        return trainingRepository.save(training);
    }

    public Training findTraining(Long id) {
        return trainingRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundExcpetion(Training.class, id));
    }

    public Training proposeTraining(Training training) {
        training.setAccepted(false);
        Coach coach = coachRepository.findById(training.getCoach().getId())
                .orElseThrow(() -> new ObjectNotFoundExcpetion(Coach.class, training.getCoach().getId()));
        Customer customer = customerRepository.findById(training.getCustomer().getId())
                .orElseThrow(() -> new ObjectNotFoundExcpetion(Customer.class, training.getCustomer().getId()));
        if (!overlapsWithExisting(training)) {
            coach.getTrainings().add(training);
            customer.getTrainings().add(training);
            return trainingRepository.save(training);
        } else
            throw new ConflictWithExistingException(Training.class, training.id);
    }


    public void delete(Long id) {
        trainingRepository.deleteById(id);
    }

    public Training acceptTraining(Long trainingId) {
        Training trainingToUpdate = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new ObjectNotFoundExcpetion(Training.class, trainingId));
        trainingToUpdate.setAccepted(true);
        return trainingToUpdate;
    }

    private boolean overlapsWithExisting(Training training) {
        final Coach coach = training.getCoach();
        List<Training> existingTrainings = coach.getTrainings();
        final LocalDateTime startTime = training.getStartTime();
        final LocalDateTime endTime = training.getEndTime();
        for (Training existing : existingTrainings) {
            if (!(startTime.isAfter(existing.getStartTime()) || endTime.isBefore(existing.getEndTime())))
                return true;
        }
        return false;
    }

}
