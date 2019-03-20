package pl.bak.timserver.training.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.bak.timserver.training.domain.Training;
import pl.bak.timserver.training.service.TrainingService;

import javax.validation.Valid;

@RestController
@RequestMapping("/trainings")
public class TrainingController {

    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }


    @GetMapping(value = "/{training_id}")
    public Training findTraining(@PathVariable Long training_id) {
        return trainingService.findTraining(training_id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Training create(@RequestBody @Valid Training Training) {
        return trainingService.save(Training);
    }

    @PostMapping(value = "/training")
    @ResponseStatus(HttpStatus.CREATED)
    public Training proposeTraining(@RequestBody @Valid Training training) {
        return trainingService.proposeTraining(training);
    }

    @RequestMapping(value = "/{training_id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("training_id") Long training_id) {
        trainingService.delete(training_id);
    }

    @PutMapping(value = "/{training_id}/accept")
    public Training acceptTraining(@PathVariable Long training_id) {
        return trainingService.acceptTraining(training_id);
    }


}
