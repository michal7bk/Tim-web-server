package pl.bak.timserver.coach.controller;

import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.bak.timserver.coach.domain.Coach;
import pl.bak.timserver.coach.domain.dto.CoachInfoDto;
import pl.bak.timserver.coach.domain.dto.CoachTrainingsDto;
import pl.bak.timserver.coach.service.CoachService;
import pl.bak.timserver.training.domain.dto.TrainingDto;

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

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Coach createCoach(@RequestBody @Valid Coach coach) {
        return coachService.save(coach);
    }

    @RequestMapping(value = "/{coachId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("coachId") Long coachId) {
        coachService.delete(coachId);
    }

    @GetMapping(value = "/{coach_id}/accepted-trainings-list")
    public List<TrainingDto> findAcceptedTrainings(@PathVariable Long coach_id) {
        return coachService.findAcceptedTrainings(coach_id);
    }

    @GetMapping(value = "/{coach_id}/proposed-trainings-list")
    public List<TrainingDto> findProposedTrainings(@PathVariable Long coach_id) {
        return coachService.findProposedTrainings(coach_id);
    }

    @GetMapping(value = "/{coach_id}/accepted-trainings")
    public String countAcceptedTrainings(@PathVariable Long coach_id) {
        JsonObject result = new JsonObject();
        result.addProperty("count", String.valueOf(coachService.countAcceptedTrainings(coach_id)));
        return result.toString();
    }

    @GetMapping(value = "/{coach_id}/proposed-trainings")
    public String countProposedTrainings(@PathVariable Long coach_id) {
        JsonObject result = new JsonObject();
        result.addProperty("count", String.valueOf(coachService.countProposedTrainings(coach_id)));
        return result.toString();
    }

    @GetMapping(value = "{coach_id}/unique-customers")
    public String countUniqueCustomers(@PathVariable Long coach_id) {
        JsonObject result = new JsonObject();
        result.addProperty("count", String.valueOf(coachService.countUniqueCustomers(coach_id)));
        return result.toString();
    }


}
