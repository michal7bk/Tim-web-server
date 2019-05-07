package pl.bak.timserver.coach.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.bak.timserver.coach.domain.Coach;
import pl.bak.timserver.coach.domain.dto.CoachInfoDto;
import pl.bak.timserver.coach.domain.dto.CoachTrainingsDto;
import pl.bak.timserver.coach.service.CoachService;
import pl.bak.timserver.common.Counter;
import pl.bak.timserver.training.domain.dto.TrainingsListDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/coaches")
public class CoachController {

    final private CoachService coachService;

    public CoachController(CoachService coachService) {
        this.coachService = coachService;
    }

    @GetMapping()
    public List<CoachTrainingsDto> findCoaches() {
        return coachService.findCoaches();
    }

    @GetMapping(value = "/{coachId}")
    public CoachInfoDto findCoach(@PathVariable Long coachId) {
        return coachService.findCoach(coachId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Coach createCoach(@RequestBody @Valid Coach coach) {
        return coachService.save(coach);
    }

    @DeleteMapping(value = "/{coachId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("coachId") Long coachId) {
        coachService.delete(coachId);
    }

    @GetMapping(value = "/{coachId}/accepted-trainings-list")
    public List<TrainingsListDto> findAcceptedTrainings(@PathVariable Long coachId) {
        return coachService.findAcceptedTrainings(coachId);
    }

    @GetMapping(value = "/{coachId}/proposed-trainings-list")
    public List<TrainingsListDto> findProposedTrainings(@PathVariable Long coachId) {
        return coachService.findProposedTrainings(coachId);
    }

    @GetMapping(value = "/{coachId}/accepted-trainings")
    public Counter countAcceptedTrainings(@PathVariable Long coachId) {
        return new Counter(coachService.countAcceptedTrainings(coachId));
    }

    @GetMapping(value = "/{coachId}/proposed-trainings")
    public Counter countProposedTrainings(@PathVariable Long coachId) {
        return new Counter(coachService.countProposedTrainings(coachId));
    }

    @GetMapping(value = "{coachId}/unique-customers")
    public Counter countUniqueCustomers(@PathVariable Long coachId) {
        return new Counter(coachService.countUniqueCustomers(coachId));
    }


}
