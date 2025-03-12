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


    public DataDTO() {
    }

    public DataDTO(String firstNameToSearch, String lastNameToSearch, PatientBean patient,
                   List<NoteBean> notesDTOs, String noteText) {
        this.firstNameToSearch = firstNameToSearch;
        this.lastNameToSearch = lastNameToSearch;
        this.patient = patient;
        this.notesDTOs = notesDTOs;
        this.noteText = noteText;
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DataDTO dataDTO)) return false;
        return Objects.equals(firstNameToSearch, dataDTO.firstNameToSearch) && Objects.equals(lastNameToSearch, dataDTO.lastNameToSearch) && Objects.equals(patient, dataDTO.patient) && Objects.equals(notesDTOs, dataDTO.notesDTOs) && Objects.equals(noteText, dataDTO.noteText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstNameToSearch, lastNameToSearch, patient, notesDTOs, noteText);
    }
}
