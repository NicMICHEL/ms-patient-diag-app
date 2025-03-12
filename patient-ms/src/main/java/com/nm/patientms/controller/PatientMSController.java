package com.nm.patientms.controller;

import com.nm.patientms.exception.NotFoundException;
import com.nm.patientms.model.Patient;
import com.nm.patientms.service.PatientMSService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/patientFile")
public class PatientMSController {

    private static final Logger logger = LogManager.getLogger(PatientMSController.class);
    @Autowired
    private PatientMSService patientMSService;

    @GetMapping("/{firstName}/{lastName}")
    public Patient get(@PathVariable String firstName, @PathVariable String lastName)
            throws NotFoundException {

        Optional<Patient>  patient      = patientMSService.get(firstName, lastName);
        if (patient.isEmpty()) {
            logger.error("Unable to find patient corresponding to firstName \"{}\" and lastName \"{}\"",
                    firstName, lastName);
            throw new NotFoundException("Patient with firstName: \"" + firstName + "\"  and lastName: \"" + lastName +
                    "\"  doesn't exist in Data Base.");
        }
        return patient.get();
    }

    @GetMapping("/Id/{idPatient}")
    public Patient getById(@PathVariable int idPatient)
            throws NotFoundException {
        Optional<Patient> patient = patientMSService.getById(idPatient);
        if (patient.isEmpty()) {
            logger.error("Unable to find patient corresponding to idPatient \"{}\" ", idPatient);
            throw new NotFoundException("Patient with idPatient: \"" + idPatient +
                    "\"  doesn't exist in Data Base.");
        }
        return patient.get();
    }

    @PostMapping("/")
    public ResponseEntity<Patient> update(@RequestBody Patient patient) throws NotFoundException {
        Patient updatedPatient = patientMSService.update(patient);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(updatedPatient.getIdPatient())
                .toUri();
        return ResponseEntity.created(location).body(updatedPatient);
    }

}
