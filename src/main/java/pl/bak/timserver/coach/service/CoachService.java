package pl.bak.timserver.coach.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.bak.timserver.coach.domain.Coach;
import pl.bak.timserver.coach.domain.dto.CoachInfoDto;
import pl.bak.timserver.coach.domain.dto.CoachTrainingsDto;
import pl.bak.timserver.coach.repository.CoachRepository;
import pl.bak.timserver.exception.ConflictWithExistingException;
import pl.bak.timserver.exception.ObjectNotFoundException;
import pl.bak.timserver.mail.MailSender;
import pl.bak.timserver.training.domain.Training;
import pl.bak.timserver.training.domain.dto.TrainingsListDto;
import pl.bak.timserver.user.ApplicationUser;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class CoachService {
    final private CoachRepository coachRepository;
    private final ModelMapper modelMapper;

    public CoachService(CoachRepository coachRepository, ModelMapper modelMapper) {
        this.coachRepository = coachRepository;
        this.modelMapper = modelMapper;
    }

    public List<CoachTrainingsDto> findCoaches() {
        return coachRepository.findAll().stream().map(this::convertToCoachTrainingsDto).collect(Collectors.toList());
    }

    public CoachInfoDto findCoach(Long id) {
        Coach coach = coachRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(Coach.class, id));
        return convertToDto(coach);
    }

    public Coach save(Coach coach) {
        if (!coachRepository.findByEmail(coach.getEmail()).isPresent()) {
            MailSender.sendMail("Your account was created", coach.getEmail());
            return coachRepository.save(coach);
        } else throw new ConflictWithExistingException(Coach.class, coach.getId());
    }

    public void delete(Long id) {
        Coach Coach = coachRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(Coach.class, id));
        coachRepository.delete(Coach);
    }

    public List<TrainingsListDto> findAcceptedTrainings(Long coachId) {
        Coach coach = coachRepository.findById(coachId).orElseThrow(() -> new ObjectNotFoundException(Coach.class, coachId));
        return coach.getTrainings().stream().filter(Training::isAccepted).map(this::convertToListDto).collect(Collectors.toList());
    }

    public List<TrainingsListDto> findProposedTrainings(Long id) {
        Coach coach = coachRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(Coach.class, id));
        return coach.getTrainings().stream().filter(training -> !training.isAccepted()).map(this::convertToListDto).collect(Collectors.toList());

    }

    public long countAcceptedTrainings(Long coach_id) {
        Coach coach = coachRepository.findById(coach_id)
                .orElseThrow(() -> new ObjectNotFoundException(Coach.class, coach_id));
        return coach.getTrainings().stream().filter(Training::isAccepted).count();
    }

    public long countProposedTrainings(Long coach_id) {
        Coach coach = coachRepository.findById(coach_id)
                .orElseThrow(() -> new ObjectNotFoundException(Coach.class, coach_id));
        return coach.getTrainings().stream().filter(x -> !x.isAccepted()).count();
    }

    public long countUniqueCustomers(Long coach_id) {
        Coach coach = coachRepository.findById(coach_id)
                .orElseThrow(() -> new ObjectNotFoundException(Coach.class, coach_id));
        return coach.getTrainings().stream().filter(distinctByKey(Training::getCustomer)).count();
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public Coach matchCoachByUser(ApplicationUser user) {
        return coachRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new ObjectNotFoundException(Coach.class, user.getId()));
    }

    private CoachInfoDto convertToDto(Coach coach) {
        return modelMapper.map(coach, CoachInfoDto.class);
    }

    private CoachTrainingsDto convertToCoachTrainingsDto(Coach coach) {
        return modelMapper.map(coach, CoachTrainingsDto.class);
    }

    private TrainingsListDto convertToListDto(Training training) {
        return modelMapper.map(training, TrainingsListDto.class);
    }


}
