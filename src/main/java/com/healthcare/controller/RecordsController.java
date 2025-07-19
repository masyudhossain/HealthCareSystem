package com.healthcare.controller;

import com.healthcare.dto.PrescriptionRequest;
import com.healthcare.dto.StatusUpdateRequest;
import com.healthcare.entity.MedicalRecord;
import com.healthcare.service.MedicalRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/records")
public class RecordsController {

    private final MedicalRecordService service;

    public RecordsController(MedicalRecordService service) {
        this.service = service;
    }

    @GetMapping("/{patientId}")
    @PreAuthorize("hasAuthority('view_records')")
    public List<MedicalRecord> viewRecords(@PathVariable Long patientId, Authentication auth) {
        Long requesterId = Long.parseLong(auth.getName());
        boolean isPatient = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("view_records")) &&
                auth.getAuthorities().size() == 1;
        return service.getRecords(requesterId, patientId, isPatient);
    }

    @PatchMapping("/{patientId}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('update_status')")
    public void updateStatus(@PathVariable Long patientId,
                             @RequestBody StatusUpdateRequest request) {
        service.updateStatus(patientId, request.getStatus());
    }

    @PostMapping("/{patientId}/prescriptions")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('prescribe_medication')")
    public void prescribe(@PathVariable Long patientId,
                          @RequestBody PrescriptionRequest request) {
        service.prescribe(patientId, request.getMedication(), request.getDosage());
    }
}
