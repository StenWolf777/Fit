package org.example.fitnes_project.statistico;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsDto {
    private Long userId;
    private int totalWorkouts;
    private double totalCaloriesBurned;
    private double totalDistance;
    private int totalDurationMinutes;
    private Map<String, Integer> workoutsByType;
    private Map<LocalDate, Double> weightProgress;
    private Map<LocalDate, Double> caloriesBurnedByDate;
    private Map<LocalDate, Integer> workoutsByDate;
}