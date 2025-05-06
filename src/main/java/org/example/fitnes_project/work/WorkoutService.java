package org.example.fitnes_project.work;

import org.example.fitnes_project.exeption.ResourceNotFoundException;
import org.example.fitnes_project.user.User;
import org.example.fitnes_project.user.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkoutService {

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<WorkoutDto> getAllWorkouts() {
        return workoutRepository.findAll().stream()
                .map(workout -> {
                    WorkoutDto dto = modelMapper.map(workout, WorkoutDto.class);
                    dto.setUserId(workout.getUser().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<WorkoutDto> getWorkoutsByUserId(Long userId) {
        return workoutRepository.findByUserId(userId).stream()
                .map(workout -> {
                    WorkoutDto dto = modelMapper.map(workout, WorkoutDto.class);
                    dto.setUserId(userId);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public WorkoutDto getWorkoutById(Long id) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with id: " + id));

        WorkoutDto dto = modelMapper.map(workout, WorkoutDto.class);
        dto.setUserId(workout.getUser().getId());
        return dto;
    }

    public WorkoutDto createWorkout(WorkoutDto workoutDto) {
       User user = userRepository.findById(workoutDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + workoutDto.getUserId()));

        Workout workout = modelMapper.map(workoutDto, Workout.class);
        workout.setUser(user);


        if (workout.getDurationMinutes() == 0 && workout.getStartTime() != null && workout.getEndTime() != null) {
            long minutes = java.time.Duration.between(workout.getStartTime(), workout.getEndTime()).toMinutes();
            workout.setDurationMinutes((int) minutes);
        }

        Workout savedWorkout = workoutRepository.save(workout);

        WorkoutDto savedDto = modelMapper.map(savedWorkout, WorkoutDto.class);
        savedDto.setUserId(user.getId());
        return savedDto;
    }

    public WorkoutDto updateWorkout(Long id, WorkoutDto workoutDto) {
        Workout existingWorkout = workoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with id: " + id));

        // Update workout fields but keep the existing user
        User user = existingWorkout.getUser();
        modelMapper.map(workoutDto, existingWorkout);
        existingWorkout.setId(id); // Ensure ID doesn't change
        existingWorkout.setUser(user); // Ensure user association doesn't change

        // Calculate duration if not provided but start and end times are
        if (existingWorkout.getDurationMinutes() == 0 && existingWorkout.getStartTime() != null && existingWorkout.getEndTime() != null) {
            long minutes = java.time.Duration.between(existingWorkout.getStartTime(), existingWorkout.getEndTime()).toMinutes();
            existingWorkout.setDurationMinutes((int) minutes);
        }

        Workout updatedWorkout = workoutRepository.save(existingWorkout);

        WorkoutDto updatedDto = modelMapper.map(updatedWorkout, WorkoutDto.class);
        updatedDto.setUserId(user.getId());
        return updatedDto;
    }

    public void deleteWorkout(Long id) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with id: " + id));
        workoutRepository.delete(workout);
    }

    public List<WorkoutDto> getWorkoutsBetweenDates(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return workoutRepository.findByUserIdAndStartTimeBetween(userId, startDate, endDate).stream()
                .map(workout -> {
                    WorkoutDto dto = modelMapper.map(workout, WorkoutDto.class);
                    dto.setUserId(userId);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
