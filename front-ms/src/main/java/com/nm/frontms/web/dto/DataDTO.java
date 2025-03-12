package com.nm.frontms.web.dto;

import com.nm.frontms.beans.PatientBean;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public class DataDTO {


    @NotBlank(message = "Veuillez renseigner le prénom.")
    private String firstNameToSearch;
    @NotBlank(message = "Veuillez renseigner le nom.")
    private String lastNameToSearch;
    private PatientBean patient;


    public DataDTO() {
    }

    public DataDTO(String firstNameToSearch, String lastNameToSearch, PatientBean patient) {
        this.firstNameToSearch = firstNameToSearch;
        this.lastNameToSearch = lastNameToSearch;
        this.patient = patient;
    }

    public String getFirstNameToSearch() {
        return firstNameToSearch;
    }

    public void setFirstNameToSearch(String firstNameToSearch) {
        this.firstNameToSearch = firstNameToSearch;
    }

    public String getLastNameToSearch() {
        return lastNameToSearch;
    }

    public void setLastNameToSearch(String lastNameToSearch) {
        this.lastNameToSearch = lastNameToSearch;
    }

    public PatientBean getPatient() {
        return patient;
    }

    public void setPatient(PatientBean patient) {
        this.patient = patient;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DataDTO dataDTO)) return false;
        return Objects.equals(firstNameToSearch, dataDTO.firstNameToSearch) && Objects.equals(lastNameToSearch, dataDTO.lastNameToSearch) && Objects.equals(patient, dataDTO.patient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstNameToSearch, lastNameToSearch, patient);
    }
}
