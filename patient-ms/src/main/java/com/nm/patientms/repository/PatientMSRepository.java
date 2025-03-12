package com.nm.patientms.repository;

import com.nm.patientms.model.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientMSRepository extends CrudRepository<Patient, Integer> {

    Optional<Patient> findByFirstNameAndLastName(String firstName, String lastName);

}
