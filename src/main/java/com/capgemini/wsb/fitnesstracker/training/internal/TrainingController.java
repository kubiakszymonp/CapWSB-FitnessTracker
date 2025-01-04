package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingDto;
import com.capgemini.wsb.fitnesstracker.training.api.UpdateTrainingDto;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/trainings")
@RequiredArgsConstructor
class TrainingController {

    private final TrainingServiceImpl trainingService;
    private final TrainingMapper trainingMapper;
    private final UserProvider userProvider;

    @GetMapping
    public List<TrainingDto> getAllTrainings() {
        return trainingService.getAllTrainings()
                .stream()
                .map(trainingMapper::toDto)
                .toList();
    }

    @GetMapping("/{userId}")
    public List<TrainingDto> getTrainingsByUserId(@PathVariable Long userId) {
        return trainingService.getTrainingsByUserId(userId)
                .stream()
                .map(trainingMapper::toDto)
                .toList();
    }

    @GetMapping("/finished/{afterTime}")
    public List<TrainingDto> getFinishedTrainingsAfter(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date afterTime) {
        return trainingService.getFinishedTrainingsAfter(afterTime)
                .stream()
                .map(trainingMapper::toDto)
                .toList();
    }

    @GetMapping("/activityType")
    public List<TrainingDto> getTrainingsByActivityType(@RequestParam ActivityType activityType) {
        return trainingService.getTrainingsByActivityType(activityType)
                .stream()
                .map(trainingMapper::toDto)
                .toList();
    }

    @PostMapping
    public ResponseEntity<TrainingDto> createTraining(@RequestBody UpdateTrainingDto trainingDto) {
        Long userId = trainingDto.userId();
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        Optional<User> userOpt = userProvider.getUser(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Training training = trainingMapper.toEntity(trainingDto, userOpt.get());
        Training createdTraining = trainingService.createTraining(training);
        return ResponseEntity.status(201).body(trainingMapper.toDto(createdTraining));
    }

    @PutMapping("/{trainingId}")
    public ResponseEntity<TrainingDto> updateTraining(
            @PathVariable Long trainingId, @RequestBody UpdateTrainingDto trainingDto) {
        Training updatedTraining = trainingService.updateTraining(trainingId, trainingDto);
        if (updatedTraining == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(trainingMapper.toDto(updatedTraining));
    }
}
