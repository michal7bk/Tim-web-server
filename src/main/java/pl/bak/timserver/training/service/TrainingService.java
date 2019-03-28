package pl.bak.timserver.training.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.bak.timserver.coach.domain.Coach;
import pl.bak.timserver.coach.repository.CoachRepository;
import pl.bak.timserver.customer.domain.Customer;
import pl.bak.timserver.customer.repository.CustomerRepository;
import pl.bak.timserver.exception.ConflictWithExistingException;
import pl.bak.timserver.exception.ObjectNotFoundException;
import pl.bak.timserver.mail.MailSender;
import pl.bak.timserver.training.domain.Training;
import pl.bak.timserver.training.domain.dto.NewDateTrainingDto;
import pl.bak.timserver.training.domain.dto.NewTrainingDto;
import pl.bak.timserver.training.domain.dto.TrainingDto;
import pl.bak.timserver.training.repository.TrainingRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class TrainingService {

    final private TrainingRepository trainingRepository;
    final private CoachRepository coachRepository;
    final private CustomerRepository customerRepository;
    final private ModelMapper modelMapper;

    public TrainingService(TrainingRepository trainingRepository, CoachRepository coachRepository, CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.trainingRepository = trainingRepository;
        this.coachRepository = coachRepository;
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    public Training save(Training training) {
        return trainingRepository.save(training);
    }

    public Training findTraining(Long id) {
        return trainingRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(Training.class, id));
    }

    public TrainingDto proposeTraining(NewTrainingDto newTrainingDto) {
        Coach coach = coachRepository.findById(newTrainingDto.getCoachId())
                .orElseThrow(() -> new ObjectNotFoundException(Coach.class, newTrainingDto.getCoachId()));
        Customer customer = customerRepository.findById(newTrainingDto.getCustomerId())
                .orElseThrow(() -> new ObjectNotFoundException(Customer.class, newTrainingDto.getCustomerId()));
        Training training = Training.builder()
                .accepted(false)
                .customer(customer)
                .coach(coach)
                .startTime(newTrainingDto.getStartTime())
                .endTime(newTrainingDto.getEndTime())
                .info(newTrainingDto.getInfo())
                .build();
        if (!overlapsWithExisting(training)) {
            coach.getTrainings().add(training);
            MailSender.proposeTraining(training);
            customer.getTrainings().add(training);
            return convertToDto(trainingRepository.save(training));
        } else
            throw new ConflictWithExistingException(Training.class, training.id);
    }

    public TrainingDto proposeNewDate(NewDateTrainingDto trainingDto) {
        try {
            Training training = trainingRepository.getOne(trainingDto.getId());
            training.setStartTime(trainingDto.getStartTime());
            training.setEndTime(trainingDto.getEndTime());
            trainingRepository.save(training);
            return convertToDto(training);
        } catch (EntityNotFoundException e) {
            throw new ObjectNotFoundException(Training.class, trainingDto.getId());
        }
    }

    public void delete(Long id) {
        trainingRepository.deleteById(id);
    }

    public void acceptTraining(Long trainingId) {
        try {
            Training trainingToUpdate = trainingRepository.getOne(trainingId);
            trainingToUpdate.setAccepted(true);
            trainingRepository.save(trainingToUpdate);
            MailSender.acceptTraining(trainingToUpdate);
        } catch (EntityNotFoundException e) {
            throw new ObjectNotFoundException(Training.class, trainingId);
        }

    }

    public void cancelTraining(Long trainingId) {
        try {
            Training trainingToUpdate = trainingRepository.getOne(trainingId);
            trainingToUpdate.setAccepted(false);
            trainingRepository.save(trainingToUpdate);
            MailSender.cancelTraining(trainingToUpdate);
        } catch (EntityNotFoundException e) {
            throw new ObjectNotFoundException(Training.class, trainingId);
        }
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

    private TrainingDto convertToDto(Training training) {
        return modelMapper.map(training, TrainingDto.class);
    }

}
