package com.nm.notesms.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "note")
public class Note {
    @NotBlank
    private String patientId;
    @NotBlank
    private String lastName;
    @NotBlank
    private String noteText;
    @NotBlank
    private String date;


    public Note() {
    }

    public Note(String patientId, String lastName, String noteText, String date) {
        this.patientId = patientId;
        this.lastName = lastName;
        this.noteText = noteText;
        this.date = date;
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
        return "note{" +
                "patientId='" + patientId + '\'' +
                ", lastName='" + lastName + '\'' +
                ", noteText='" + noteText + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

}
