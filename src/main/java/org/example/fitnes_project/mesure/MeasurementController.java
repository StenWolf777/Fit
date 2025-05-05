package org.example.fitnes_project.mesure;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/measurements")
@CrossOrigin(origins = "*")
public class MeasurementController {

    @Autowired
    private MeasurementService measurementService;

    @GetMapping
    public ResponseEntity<List<MeasurementDto>> getAllMeasurements() {
        return ResponseEntity.ok(measurementService.getAllMeasurements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeasurementDto> getMeasurementById(@PathVariable Long id) {
        return ResponseEntity.ok(measurementService.getMeasurementById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MeasurementDto>> getMeasurementsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(measurementService.getMeasurementsByUserId(userId));
    }

    @GetMapping("/user/{userId}/latest")
    public ResponseEntity<MeasurementDto> getLatestMeasurementByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(measurementService.getLatestMeasurementByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<MeasurementDto> createMeasurement(@Valid @RequestBody MeasurementDto measurementDto) {
        return new ResponseEntity<>(measurementService.createMeasurement(measurementDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MeasurementDto> updateMeasurement(@PathVariable Long id, @Valid @RequestBody MeasurementDto measurementDto) {
        return ResponseEntity.ok(measurementService.updateMeasurement(id, measurementDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeasurement(@PathVariable Long id) {
        measurementService.deleteMeasurement(id);
        return ResponseEntity.noContent().build();
    }
}
