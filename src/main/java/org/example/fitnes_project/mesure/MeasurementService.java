package org.example.fitnes_project.mesure;

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
public class MeasurementService {

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<MeasurementDto> getAllMeasurements() {
        return measurementRepository.findAll().stream()
                .map(measurement -> {
                    MeasurementDto dto = modelMapper.map(measurement, MeasurementDto.class);
                    dto.setUserId(measurement.getUser().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<MeasurementDto> getMeasurementsByUserId(Long userId) {
        return measurementRepository.findByUserId(userId).stream()
                .map(measurement -> {
                    MeasurementDto dto = modelMapper.map(measurement, MeasurementDto.class);
                    dto.setUserId(userId);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public MeasurementDto getMeasurementById(Long id) {
        Measurement measurement = measurementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Measurement not found with id: " + id));

        MeasurementDto dto = modelMapper.map(measurement, MeasurementDto.class);
        dto.setUserId(measurement.getUser().getId());
        return dto;
    }

    public MeasurementDto createMeasurement(MeasurementDto measurementDto) {
        User user = userRepository.findById(measurementDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + measurementDto.getUserId()));

        Measurement measurement = modelMapper.map(measurementDto, Measurement.class);
        measurement.setUser(user);

        // Set timestamp to now if not provided
        if (measurement.getTimestamp() == null) {
            measurement.setTimestamp(LocalDateTime.now());
        }

        Measurement savedMeasurement = measurementRepository.save(measurement);

        MeasurementDto savedDto = modelMapper.map(savedMeasurement, MeasurementDto.class);
        savedDto.setUserId(user.getId());
        return savedDto;
    }

    public MeasurementDto updateMeasurement(Long id, MeasurementDto measurementDto) {
        Measurement existingMeasurement = measurementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Measurement not found with id: " + id));

        // Update measurement fields but keep the existing user
        User user = existingMeasurement.getUser();
        modelMapper.map(measurementDto, existingMeasurement);
        existingMeasurement.setId(id); // Ensure ID doesn't change
        existingMeasurement.setUser(user); // Ensure user association doesn't change

        Measurement updatedMeasurement = measurementRepository.save(existingMeasurement);

        MeasurementDto updatedDto = modelMapper.map(updatedMeasurement, MeasurementDto.class);
        updatedDto.setUserId(user.getId());
        return updatedDto;
    }

    public void deleteMeasurement(Long id) {
        Measurement measurement = measurementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Measurement not found with id: " + id));
        measurementRepository.delete(measurement);
    }

    public MeasurementDto getLatestMeasurementByUserId(Long userId) {
        Measurement measurement = measurementRepository.findLatestByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No measurements found for user with id: " + userId));

        MeasurementDto dto = modelMapper.map(measurement, MeasurementDto.class);
        dto.setUserId(userId);
        return dto;
    }
}