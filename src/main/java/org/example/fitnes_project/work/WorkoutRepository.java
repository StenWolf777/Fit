package org.example.fitnes_project.work;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByUserId(Long userId);
    List<Workout> findByUserIdAndStartTimeBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
    List<Workout> findByUserIdAndType(Long userId, WorkoutType type);

    @Query("SELECT SUM(w.caloriesBurned) FROM Workout w WHERE w.user.id = ?1")
    Double getTotalCaloriesBurned(Long userId);

    @Query("SELECT SUM(w.distance) FROM Workout w WHERE w.user.id = ?1")
    Double getTotalDistance(Long userId);

    @Query("SELECT COUNT(w) FROM Workout w WHERE w.user.id = ?1")
    Integer getTotalWorkouts(Long userId);

    @Query("SELECT SUM(w.durationMinutes) FROM Workout w WHERE w.user.id = ?1")
    Integer getTotalDuration(Long userId);
}
