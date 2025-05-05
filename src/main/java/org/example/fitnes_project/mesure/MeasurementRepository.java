package org.example.fitnes_project.mesure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    List<Measurement> findByUserId(Long userId);
    List<Measurement> findByUserIdAndTimestampBetween(Long userId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT m FROM Measurement m WHERE m.user.id = ?1 ORDER BY m.timestamp DESC LIMIT 1")
    Optional<Measurement> findLatestByUserId(Long userId);
}