package com.nm.patientms.service;

import com.nm.patientms.exception.NotFoundException;
import com.nm.patientms.model.Patient;
import com.nm.patientms.repository.PatientMSRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientMSServiceTest {

    @Mock
    private PatientMSRepository patientMSRepository;
    @InjectMocks
    private PatientMSService patientMSService;

    @Test
    public void should_update_patient_successfully() throws Exception {
        //given
        Patient patient = new Patient(0, "lastNameTest", "firstnameTest",
                "0000-00-00", "F", "5 rue cinq ", "555-555-5555");
        Patient initialPatient = new Patient(0, "lastNameTest", "firstnameTest",
                "0000-00-00", "F", "0 rue zéro ", "000-000-0000");
        Optional<Patient> testPatient = Optional.of(initialPatient);
        when(patientMSRepository.findById(0)).thenReturn(testPatient);
        when(patientMSRepository.save(patient)).thenReturn(patient);
        //when
        Patient updatedPatient = patientMSService.update(patient);
        //then
        verify(patientMSRepository).save(patient);
        assertEquals(initialPatient, updatedPatient);
    }

    @Test
    public void should_throw_NotFoundException_when_updating_patient_not_found_in_data_base() throws Exception {
        //given
        Patient patient = new Patient(0, "lastNameTest", "firstnameTest",
                "0000-00-00", "F", "5 rue cinq ", "555-555-5555");
        Optional<Patient> emptyPatient = Optional.empty();
        when(patientMSRepository.findById(0)).thenReturn(emptyPatient);
        //when then
        NotFoundException thrown = assertThrows(NotFoundException.class,
                () -> {
                    patientMSService.update(patient);
                }, "NotFoundException was expected");
        assertEquals("Invalid patient id", thrown.getMessage());
        verify(patientMSRepository, times(0)).save(any(Patient.class));
    }
}
