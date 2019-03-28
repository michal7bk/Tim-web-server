package pl.bak.timserver.training.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.bak.timserver.training.domain.Training;
import pl.bak.timserver.training.domain.dto.NewDateTrainingDto;
import pl.bak.timserver.training.domain.dto.NewTrainingDto;
import pl.bak.timserver.training.domain.dto.TrainingDto;
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

    @PostMapping(value = "/propose")
    @ResponseStatus(HttpStatus.CREATED)
    public TrainingDto proposeTraining(@RequestBody @Valid NewTrainingDto newTrainingDto) {
        return trainingService.proposeTraining(newTrainingDto);
    }

    @PutMapping(value = "/{training_id}/propose")
    public TrainingDto proposeNewDate(@RequestBody @Valid NewDateTrainingDto newDateTrainingDto, @PathVariable Long training_id) {
        newDateTrainingDto.setId(training_id);
        return trainingService.proposeNewDate(newDateTrainingDto);
    }

    @RequestMapping(value = "/{training_id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("training_id") Long training_id) {
        trainingService.delete(training_id);
    }

    @PutMapping(value = "/{training_id}/accept")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void acceptTraining(@PathVariable Long training_id) {
        trainingService.acceptTraining(training_id);
    }

    @PutMapping(value = "/{training_id}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelTraining(@PathVariable Long training_id) {
        trainingService.cancelTraining(training_id);
    }

}
