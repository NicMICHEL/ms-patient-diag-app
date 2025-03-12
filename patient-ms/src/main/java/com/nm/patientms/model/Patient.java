package com.nm.patientms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.DynamicUpdate;


@Entity
@Table(name = "p_patient")
@DynamicUpdate
public class Patient {

    @Id
    @Column(name = "p_id")
    private int idPatient;
    @NotBlank
    @Column(name = "p_last_name")
    private String lastName;
    @NotBlank
    @Column(name = "p_first_name")
    private String firstName;
    @NotBlank
    @Column(name = "p_birth_date")
    private String birthDate;
    @NotBlank
    @Column(name = "p_gender")
    private String gender;
    @Column(name = "p_address")
    private String address;
    @Column(name = "p_phone_number")
    private String phoneNumber;

    public Patient() {
    }

    public Patient(int idPatient, String lastName, String firstName, String birthDate, String gender,
                   String address, String phoneNumber) {
        this.idPatient = idPatient;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public int getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "idPatient=" + idPatient +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

}
