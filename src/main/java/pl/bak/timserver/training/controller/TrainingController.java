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


    @GetMapping(value = "/{id}")
    public Training findTraining(@PathVariable Long id) {
        return trainingService.findTraining(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Training create(@RequestBody @Valid Training Training) {
        return trainingService.save(Training);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        trainingService.delete(id);
    }

    @PutMapping(value = "/accept")
    public Training acceptTraining(@RequestBody @Valid Training training) {
        return trainingService.acceptTraining(training);
    }


}
