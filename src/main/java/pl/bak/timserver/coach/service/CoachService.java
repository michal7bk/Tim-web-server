package pl.bak.timserver.coach.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.bak.timserver.coach.domain.Coach;
import pl.bak.timserver.coach.domain.dto.CoachInfoDto;
import pl.bak.timserver.coach.repository.CoachRepository;
import pl.bak.timserver.exception.ConflictWithExistingException;
import pl.bak.timserver.exception.ObjectNotFoundExcpetion;
import pl.bak.timserver.mail.MailSender;
import pl.bak.timserver.training.domain.Training;
import pl.bak.timserver.user.ApplicationUser;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoachService {
    final private CoachRepository coachRepository;
    private final ModelMapper modelMapper;

    public CoachService(CoachRepository coachRepository, ModelMapper modelMapper) {
        this.coachRepository = coachRepository;
        this.modelMapper = modelMapper;
    }

    public List<Coach> findCoaches() {
        return coachRepository.findAll();
    }

    public CoachInfoDto findCoach(Long id) {
        Coach coach = coachRepository.findById(id).orElseThrow(() -> new ObjectNotFoundExcpetion(Coach.class, id));
        return convertToDto(coach);
    }

    public Coach save(Coach coach) {
        if (!coachRepository.findByEmail(coach.getEmail()).isPresent()) {
            MailSender.sendMail("Your account was created", coach.getEmail());
            return coachRepository.save(coach);
        }
        else throw new ConflictWithExistingException(Coach.class, coach.getId());
    }

    public void delete(Long id) {
        Coach Coach = coachRepository.findById(id).orElseThrow(() -> new ObjectNotFoundExcpetion(Coach.class, id));
        coachRepository.delete(Coach);
    }

    public List<Training> findAcceptedTrainings(Long coachId) {
        Coach coach = coachRepository.findById(coachId).orElseThrow(() -> new ObjectNotFoundExcpetion(Coach.class, coachId));
        return coach.getTrainings().stream().filter(Training::isAccepted).collect(Collectors.toList());
    }

    public List<Training> findProposedTrainings(Long id) {
        Coach coach = coachRepository.findById(id).orElseThrow(() -> new ObjectNotFoundExcpetion(Coach.class, id));
        return coach.getTrainings().stream().filter(training -> !training.isAccepted()).collect(Collectors.toList());

    }

    public Coach matchCoachByUser(ApplicationUser user) {
        return coachRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new ObjectNotFoundExcpetion(Coach.class, user.getId()));
    }

    private CoachInfoDto convertToDto(Coach coach) {
        return modelMapper.map(coach, CoachInfoDto.class);
    }


}
