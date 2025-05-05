package org.example.fitnes_project.work;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutDto {
    private Long id;
    private WorkoutType type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int durationMinutes;
    private double caloriesBurned;
    private double distance;
    private String notes;
    private Long userId;
}
