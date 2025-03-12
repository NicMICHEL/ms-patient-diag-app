package com.nm.frontms.beans;

import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public class NoteBean {

    private String patientId;

    private String lastName;
    @NotBlank(message = "Veuillez écrire une note.")
    private String noteText;

    private String date;

    public NoteBean() {
    }

    public NoteBean(String patientId, String lastName, String noteText, String date) {
        this.patientId = patientId;
        this.lastName = lastName;
        this.noteText = noteText;
        this.date = date;
    }

    public NoteBean(String noteText) {
        this.noteText = noteText;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "NoteBean{" +
                "patientId='" + patientId + '\'' +
                ", lastName='" + lastName + '\'' +
                ", noteText='" + noteText + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NoteBean noteBean)) return false;
        return Objects.equals(patientId, noteBean.patientId) && Objects.equals(lastName, noteBean.lastName)
                && Objects.equals(noteText, noteBean.noteText) && Objects.equals(date, noteBean.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patientId, lastName, noteText, date);
    }


}
