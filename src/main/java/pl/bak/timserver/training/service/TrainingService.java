package pl.bak.timserver.training.service;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;
import pl.bak.timserver.coach.domain.Coach;
import pl.bak.timserver.coach.repository.CoachRepository;
import pl.bak.timserver.customer.domain.Customer;
import pl.bak.timserver.customer.repository.CustomerRepository;
import pl.bak.timserver.exception.ConflictWithExistingException;
import pl.bak.timserver.exception.ObjectNotFoundExcpetion;
import pl.bak.timserver.mail.MailSender;
import pl.bak.timserver.training.domain.Training;
import pl.bak.timserver.training.domain.dto.NewTrainingDto;
import pl.bak.timserver.training.domain.dto.TrainingDto;
import pl.bak.timserver.training.repository.TrainingRepository;

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
                .orElseThrow(() -> new ObjectNotFoundExcpetion(Training.class, id));
    }

    public TrainingDto proposeTraining(NewTrainingDto newTrainingDto) {
        Coach coach = coachRepository.findById(newTrainingDto.getCoachId())
                .orElseThrow(() -> new ObjectNotFoundExcpetion(Coach.class, newTrainingDto.getCoachId()));
        Customer customer = customerRepository.findById(newTrainingDto.getCustomerId())
                .orElseThrow(() -> new ObjectNotFoundExcpetion(Customer.class, newTrainingDto.getCustomerId()));
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

    public void delete(Long id) {
        trainingRepository.deleteById(id);
    }

    public TrainingDto acceptTraining(Long trainingId) {
        Training trainingToUpdate = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new ObjectNotFoundExcpetion(Training.class, trainingId));
        trainingToUpdate.setAccepted(true);
        MailSender.acceptTraining(trainingToUpdate);
        return convertToDto(trainingToUpdate);
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

    private Training convertToEntity(NewTrainingDto newTrainingDto) throws ParseException {

        return modelMapper.map(newTrainingDto, Training.class);
    }

}
