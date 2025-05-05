package org.example.fitnes_project.mesure;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.fitnes_project.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "measurements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Measurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;
    private double weight; // in kg
    private double bodyFatPercentage; // in %
    private int heartRate; // BPM
    private double bloodPressureSystolic; // mmHg
    private double bloodPressureDiastolic; // mmHg
    private double waistCircumference; // in cm
    private double chestCircumference; // in cm
    private double hipCircumference; // in cm

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}