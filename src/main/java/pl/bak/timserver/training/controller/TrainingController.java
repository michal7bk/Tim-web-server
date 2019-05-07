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


    @GetMapping(value = "/{trainingId}")
    public Training findTraining(@PathVariable Long trainingId) {
        return trainingService.findTraining(trainingId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Training create(@RequestBody @Valid Training Training) {
        return trainingService.save(Training);
    }

    @PostMapping(value = "/propose")
    @ResponseStatus(HttpStatus.CREATED)
    public TrainingDto proposeTraining(@RequestBody @Valid NewTrainingDto newTrainingDto) {
        return trainingService.proposeTraining(newTrainingDto);
    }

    @PutMapping(value = "/{trainingId}/propose")
    public TrainingDto proposeNewDate(@RequestBody @Valid NewDateTrainingDto newDateTrainingDto, @PathVariable Long trainingId) {
        newDateTrainingDto.setId(trainingId);
        return trainingService.proposeNewDate(newDateTrainingDto);
    }

    @DeleteMapping(value = "/{trainingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("trainingId") Long trainingId) {
        trainingService.delete(trainingId);
    }

    @PutMapping(value = "/{trainingId}/accept")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void acceptTraining(@PathVariable Long trainingId) {
        trainingService.acceptTraining(trainingId);
    }

    @PutMapping(value = "/{trainingId}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelTraining(@PathVariable Long trainingId) {
        trainingService.cancelTraining(trainingId);
    }

}
