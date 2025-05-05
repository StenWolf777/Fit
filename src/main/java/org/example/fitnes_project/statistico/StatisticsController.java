package org.example.fitnes_project.statistico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "*")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<StatisticsDto> getUserStatistics(@PathVariable Long userId) {
        return ResponseEntity.ok(statisticsService.getUserStatistics(userId));
    }

    @GetMapping("/user/{userId}/date-range")
    public ResponseEntity<StatisticsDto> getUserStatisticsForDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(statisticsService.getUserStatisticsForDateRange(userId, startDate, endDate));
    }
}