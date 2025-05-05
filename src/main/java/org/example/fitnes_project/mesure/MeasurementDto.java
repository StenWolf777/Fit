package org.example.fitnes_project.mesure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeasurementDto {
    private Long id;
    private LocalDateTime timestamp;
    private double weight;
    private double bodyFatPercentage;
    private int heartRate;
    private double bloodPressureSystolic;
    private double bloodPressureDiastolic;
    private double waistCircumference;
    private double chestCircumference;
    private double hipCircumference;
    private Long userId;
}
