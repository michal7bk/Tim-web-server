package pl.bak.timserver.coach.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.bak.timserver.coach.domain.Coach;
import pl.bak.timserver.coach.service.CoachService;
import pl.bak.timserver.training.domain.Training;

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
    public List<Coach> findCoaches() {
        return coachService.findCoaches();
    }

    @GetMapping(value = "/{id}")
    public Coach findCoach(@PathVariable Long id) {
        return coachService.findCoach(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Coach createCoach(@RequestBody @Valid Coach coach) {
        return coachService.save(coach);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        coachService.delete(id);
    }

    @GetMapping(value = "/{coach_id}/trainings")
    public List<Training> findAcceptedTrainings(@PathVariable Long coach_id){
        return coachService.findAcceptedTrainings(coach_id);
    }

    @GetMapping(value = "/{coach_id}/proposed-trainings")
    public List<Training> findProposedTrainings(@PathVariable Long coach_id){
        return coachService.findProposedTrainings(coach_id);
    }


}
