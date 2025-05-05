package org.example.fitnes_project.statistico;

import org.example.fitnes_project.exeption.ResourceNotFoundException;
import org.example.fitnes_project.mesure.Measurement;
import org.example.fitnes_project.mesure.MeasurementRepository;
import org.example.fitnes_project.user.UserRepository;
import org.example.fitnes_project.work.Workout;
import org.example.fitnes_project.work.WorkoutRepository;
import org.example.fitnes_project.work.WorkoutType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MeasurementRepository measurementRepository;

    public StatisticsDto getUserStatistics(Long userId) {
        // Check if user exists
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        StatisticsDto statistics = new StatisticsDto();
        statistics.setUserId(userId);

        // Get total workout stats
        Integer totalWorkouts = workoutRepository.getTotalWorkouts(userId);
        Double totalCaloriesBurned = workoutRepository.getTotalCaloriesBurned(userId);
        Double totalDistance = workoutRepository.getTotalDistance(userId);
        Integer totalDuration = workoutRepository.getTotalDuration(userId);

        statistics.setTotalWorkouts(totalWorkouts != null ? totalWorkouts : 0);
        statistics.setTotalCaloriesBurned(totalCaloriesBurned != null ? totalCaloriesBurned : 0);
        statistics.setTotalDistance(totalDistance != null ? totalDistance : 0);
        statistics.setTotalDurationMinutes(totalDuration != null ? totalDuration : 0);

        // Get workouts by type
        List<Workout> workouts = workoutRepository.findByUserId(userId);
        Map<String, Integer> workoutsByType = new HashMap<>();

        for (WorkoutType type : WorkoutType.values()) {
            long count = workouts.stream()
                    .filter(w -> w.getType() == type)
                    .count();
            if (count > 0) {
                workoutsByType.put(type.name(), (int) count);
            }
        }
        statistics.setWorkoutsByType(workoutsByType);

        // Get weight progress
        List<Measurement> measurements = measurementRepository.findByUserId(userId);
        Map<LocalDate, Double> weightProgress = measurements.stream()
                .collect(Collectors.toMap(
                        m -> m.getTimestamp().toLocalDate(),
                        Measurement::getWeight,
                        (existing, replacement) -> replacement // Keep the latest measurement for the day
                ));
        statistics.setWeightProgress(weightProgress);

        // Get calories burned by date
        Map<LocalDate, Double> caloriesByDate = workouts.stream()
                .collect(Collectors.groupingBy(
                        w -> w.getStartTime().toLocalDate(),
                        Collectors.summingDouble(Workout::getCaloriesBurned)
                ));
        statistics.setCaloriesBurnedByDate(caloriesByDate);

        // Get workouts by date
        Map<LocalDate, Integer> workoutsByDate = workouts.stream()
                .collect(Collectors.groupingBy(
                        w -> w.getStartTime().toLocalDate(),
                        Collectors.summingInt(w -> 1)
                ));
        statistics.setWorkoutsByDate(workoutsByDate);

        return statistics;
    }

    public StatisticsDto getUserStatisticsForDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        // Check if user exists
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        StatisticsDto statistics = new StatisticsDto();
        statistics.setUserId(userId);

        // Get workouts within date range
        List<Workout> workouts = workoutRepository.findByUserIdAndStartTimeBetween(userId, startDateTime, endDateTime);

        // Calculate total stats for the date range
        int totalWorkouts = workouts.size();
        double totalCaloriesBurned = workouts.stream().mapToDouble(Workout::getCaloriesBurned).sum();
        double totalDistance = workouts.stream().mapToDouble(Workout::getDistance).sum();
        int totalDuration = workouts.stream().mapToInt(Workout::getDurationMinutes).sum();

        statistics.setTotalWorkouts(totalWorkouts);
        statistics.setTotalCaloriesBurned(totalCaloriesBurned);
        statistics.setTotalDistance(totalDistance);
        statistics.setTotalDurationMinutes(totalDuration);

        // Get workouts by type for date range
        Map<String, Integer> workoutsByType = new HashMap<>();
        for (WorkoutType type : WorkoutType.values()) {
            long count = workouts.stream()
                    .filter(w -> w.getType() == type)
                    .count();
            if (count > 0) {
                workoutsByType.put(type.name(), (int) count);
            }
        }
        statistics.setWorkoutsByType(workoutsByType);

        // Get measurements within date range
        List<Measurement> measurements = measurementRepository.findByUserIdAndTimestampBetween(userId, startDateTime, endDateTime);

        // Get weight progress for date range
        Map<LocalDate, Double> weightProgress = measurements.stream()
                .collect(Collectors.toMap(
                        m -> m.getTimestamp().toLocalDate(),
                        Measurement::getWeight,
                        (existing, replacement) -> replacement // Keep the latest measurement for the day
                ));
        statistics.setWeightProgress(weightProgress);

        // Get calories burned by date for date range
        Map<LocalDate, Double> caloriesByDate = workouts.stream()
                .collect(Collectors.groupingBy(
                        w -> w.getStartTime().toLocalDate(),
                        Collectors.summingDouble(Workout::getCaloriesBurned)
                ));
        statistics.setCaloriesBurnedByDate(caloriesByDate);

        // Get workouts by date for date range
        Map<LocalDate, Integer> workoutsByDate = workouts.stream()
                .collect(Collectors.groupingBy(
                        w -> w.getStartTime().toLocalDate(),
                        Collectors.summingInt(w -> 1)
                ));
        statistics.setWorkoutsByDate(workoutsByDate);

        return statistics;
    }
}