package com.nm.patientms.service;

import com.nm.patientms.exception.NotFoundException;
import com.nm.patientms.model.Patient;
import com.nm.patientms.repository.PatientMSRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PatientMSService {

    private static final Logger logger = LogManager.getLogger(PatientMSService.class);
    @Autowired
    private PatientMSRepository patientMSRepository;

    public PatientMSService(PatientMSRepository patientRepository) {
        this.patientMSRepository = patientRepository;
    }

    public Optional<Patient> get(String firstName, String lastName) {
        return patientMSRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    public Optional<Patient> getById(int idPatient) {
        return patientMSRepository.findById(idPatient);
    }

    @Transactional
    public Patient update(Patient patient) throws NotFoundException {
        Optional<Patient> testPatient = patientMSRepository.findById(patient.getIdPatient());
        if (testPatient.isPresent()) {
            patientMSRepository.save(patient);
            return patientMSRepository.findById(patient.getIdPatient()).get();
        } else {
            logger.error("Unable to find and update patient corresponding to id \"{}\"",
                    patient.getIdPatient());
            throw new NotFoundException("Invalid patient id");
        }
    }

}
