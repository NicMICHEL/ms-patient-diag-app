package com.nm.patientms.controller;

import com.nm.patientms.exception.NotFoundException;
import com.nm.patientms.model.Patient;
import com.nm.patientms.repository.PatientMSRepository;
import com.nm.patientms.service.PatientMSService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(PatientMSController.class)
public class PatientMSControllerTest {

    @MockitoBean
    private PatientMSRepository patientMSRepository;
    @MockitoBean
    private PatientMSService patientMSService;
    @InjectMocks
    private PatientMSController patientMSController;
    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser(username = "user")
    public void should_get_patient_by_firstname_and_lastname_successfully() throws Exception {
        //given
        Patient patient = new Patient(100, "lastNameTest", "firstnameTest",
                "0000-00-00", "F", "5 rue cinq", "555-555-5555");
        Optional<Patient> testPatient = Optional.of(patient);
        when(patientMSService.get("firstnameTest", "lastNameTest")).thenReturn(testPatient);
        //when
        mockMvc.perform(get("/patientFile/{firstName}/{lastName}", "firstnameTest",
                        "lastNameTest"))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idPatient", is(100)))
                .andExpect(jsonPath("$.lastName", is("lastNameTest")))
                .andExpect(jsonPath("$.firstName", is("firstnameTest")))
                .andExpect(jsonPath("$.birthDate", is("0000-00-00")))
                .andExpect(jsonPath("$.gender", is("F")))
                .andExpect(jsonPath("$.address", is("5 rue cinq")))
                .andExpect(jsonPath("$.phoneNumber", is("555-555-5555")));
    }

    @Test
    @WithMockUser(username = "user")
    public void should_throw_NotFoundException_when_patient_firstname_and_lastname_do_not_exist_in_Data_Base()
            throws Exception {
        //given
        Optional<Patient> emptyPatient = Optional.empty();
        when(patientMSService.get("notFoundFirstName", "notFoundLastName")).thenReturn(emptyPatient);
        //when
        mockMvc.perform(get("/patientFile/{firstName}/{lastName}", "notFoundFirstName",
                        "notFoundLastName")
                        // then
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(NotFoundException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals(
                        "Patient with firstName: \"notFoundFirstName\"  and lastName: \"notFoundLastName\" " +
                                " doesn't exist in Data Base.", Objects.requireNonNull(result.getResolvedException())
                                .getMessage()));
    }

    @Test
    @WithMockUser(username = "user")
    public void should_get_patient_by_idPatient_successfully() throws Exception {
        //given
        Patient patient = new Patient(100, "lastNameTest", "firstnameTest",
                "0000-00-00", "F", "5 rue cinq", "555-555-5555");
        Optional<Patient> testPatient = Optional.of(patient);
        when(patientMSService.getById(100)).thenReturn(testPatient);
        //when
        mockMvc.perform(get("/patientFile/Id/{idPatient}", 100))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idPatient", is(100)))
                .andExpect(jsonPath("$.lastName", is("lastNameTest")))
                .andExpect(jsonPath("$.firstName", is("firstnameTest")))
                .andExpect(jsonPath("$.birthDate", is("0000-00-00")))
                .andExpect(jsonPath("$.gender", is("F")))
                .andExpect(jsonPath("$.address", is("5 rue cinq")))
                .andExpect(jsonPath("$.phoneNumber", is("555-555-5555")));
    }

    @Test
    @WithMockUser(username = "user")
    public void should_throw_NotFoundException_when_patient_idPatient_does_not_exist_in_Data_Base()
            throws Exception {
        //given
        Optional<Patient> emptyPatient = Optional.empty();
        when(patientMSService.getById(0)).thenReturn(emptyPatient);
        //when
        mockMvc.perform(get("/patientFile/Id/{idPatient}", 0)
                        // then
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(NotFoundException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals(
                        "Patient with idPatient: \"0\"  doesn't exist in Data Base.",
                        Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @WithMockUser(username = "user")
    public void should_update_patient_successfully() throws Exception {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/patientFile");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        Patient patientTest = new Patient(100, "lastNameTest", "firstnameTest",
                "0000-00-00", "F", "100 rue cent", "111-111-1111");
        when(patientMSService.update(patientTest)).thenReturn(patientTest);
        //when
        ResponseEntity<Patient> response = patientMSController.update(patientTest);
        //then
        assertEquals(201, response.getStatusCode().value());
        URI location = response.getHeaders().getLocation();
        assertNotNull(location);
        assertEquals("/patientFile/100", location.getPath());
        verify(patientMSService).update(patientTest);
    }

}
