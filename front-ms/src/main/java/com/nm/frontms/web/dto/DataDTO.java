package com.nm.frontms.web.dto;

import com.nm.frontms.beans.NoteBean;
import com.nm.frontms.beans.PatientBean;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Objects;

public class DataDTO {

    @NotBlank(message = "Veuillez renseigner le prénom.")
    private String firstNameToSearch;
    @NotBlank(message = "Veuillez renseigner le nom.")
    private String lastNameToSearch;
    private PatientBean patient;
    private List<NoteBean> notesDTOs;
    private String noteText;
    private String riskLevel;

    public DataDTO() {
    }

    public DataDTO(String firstNameToSearch, String lastNameToSearch, PatientBean patient,
                   List<NoteBean> notesDTOs, String noteText, String riskLevel) {
        this.firstNameToSearch = firstNameToSearch;
        this.lastNameToSearch = lastNameToSearch;
        this.patient = patient;
        this.notesDTOs = notesDTOs;
        this.noteText = noteText;
        this.riskLevel = riskLevel;
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

    public List<NoteBean> getNotesDTOs() {
        return notesDTOs;
    }

    public void setNotesDTOs(List<NoteBean> notesDTOs) {
        this.notesDTOs = notesDTOs;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DataDTO formData)) return false;
        return Objects.equals(firstNameToSearch, formData.firstNameToSearch) && Objects.equals(lastNameToSearch,
                formData.lastNameToSearch) && Objects.equals(patient, formData.patient) && Objects.equals(notesDTOs,
                formData.notesDTOs) && Objects.equals(noteText, formData.noteText) && Objects.equals(riskLevel,
                formData.riskLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstNameToSearch, lastNameToSearch, patient, notesDTOs, noteText, riskLevel);
    }

}
