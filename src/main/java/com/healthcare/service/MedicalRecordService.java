package com.healthcare.service;

import com.healthcare.entity.MedicalRecord;
import com.healthcare.repository.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository repository;

    public MedicalRecordService(MedicalRecordRepository repository) {
        this.repository = repository;
    }

    public List<MedicalRecord> getRecords(Long requesterId, Long patientId, boolean isPatient) {
        if (isPatient && !requesterId.equals(patientId)) {
            throw new SecurityException("Patients can only access their own records.");
        }
        return repository.findByPatientId(patientId);
    }

    public void updateStatus(Long patientId, String status) {
        MedicalRecord record = MedicalRecord.builder()
                .patientId(patientId)
                .data("Status Updated: " + status)
                .createdAt(LocalDateTime.now())
                .build();
        repository.save(record);
    }

    public void prescribe(Long patientId, String med, String dosage) {
        MedicalRecord record = MedicalRecord.builder()
                .patientId(patientId)
                .data("Prescribed: " + med + " - " + dosage)
                .createdAt(LocalDateTime.now())
                .build();
        repository.save(record);
    }
}
