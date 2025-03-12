package com.nm.riskms.service;

import com.nm.riskms.beans.NoteBean;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class RiskMSServiceTest {

    @InjectMocks
    private RiskMSService riskMSService;


    @Test
    public void should_get_age_successfully() {
        //given
        String birthDateTest = "1975-12-03";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate formattedBirthDateTest = LocalDate.parse(birthDateTest, formatter);
        int ageTest = LocalDate.now().getYear() - formattedBirthDateTest.getYear();
        //when then
        assertEquals(riskMSService
                .getAge("1975-12-03"), ageTest);
    }

    @Test
    public void should_get_word_presence_in_string_successfully() {
        //given
        String stringTest1 = "Le patient fume chaque jour.";
        String stringTest2 = "Le patient se parfume le matin.";
        //when then
        assertEquals(1, riskMSService
                .isOneWordPresentInString(stringTest1, "fume"));
        assertEquals(0, riskMSService
                .isOneWordPresentInString(stringTest2, "fume"));
    }

    @Test
    public void should_get_one_of_these_slightly_differently_spelled_words_presence_in_string_successfully() {
        //given
        String stringTest1 = "Le patient a des réactions anormales et une taille anormale.";
        String stringTest2 = "Le patient ne présente aucune anormalité.";
        //when then
        assertEquals(1, riskMSService
                .isOneOfTheseSlightlyDifferentlySpelledWordsPresentInString(stringTest1,
                        List.of("Anormal", "Anormale", "Anormales")));
        assertEquals(0, riskMSService
                .isOneOfTheseSlightlyDifferentlySpelledWordsPresentInString(stringTest2,
                        List.of("Anormal", "Anormale", "Anormales")));
    }

    @Test
    public void should_get_triggers_number_in_concatened_noteText_successfully() {
        //given
        String concatenedNoteTextTest1 = "Le patient a des réactions anormales et une taille anormale. Il a eu" +
                " des vertiges a perdu du poids. Il va arrêter de fumer. Son taux de cholestérol n'est plus " +
                "vertigineux.";
        String concatenedNoteTextTest2 = "Le patient est en chute libre. Sa maison est enfumée. Il adore" +
                " les petits pois.";
        //when then
        assertEquals(6, riskMSService
                .getTriggersNumberInConcatenedNoteText(concatenedNoteTextTest1));
        assertEquals(0, riskMSService
                .getTriggersNumberInConcatenedNoteText(concatenedNoteTextTest2));
    }

    @Test
    public void should_get_triggers_number_in_all_notes_from_patient_id_successfully() {
        //given
        NoteBean note1 = new NoteBean("0", "lastNameTest", "Des réactions anormales" +
                " et une taille anormale.", "2021-01-01");
        NoteBean note2 = new NoteBean("0", "lastNameTest", "Il a eu des vertiges et a perdu"
                + " du poids.", "2022-02-02");
        NoteBean note3 = new NoteBean("0", "lastNameTest", "Il va arrêter de fumer." +
                " Son taux de cholestérol n'est plus vertigineux. Ses anticorps se portent bien.", "2023-03-03");
        List<NoteBean> notesTest = Arrays.asList(note1, note2, note3);
        //when then
        assertEquals(7, riskMSService
                .getTriggersNumberInAllNotesFromPatientId(notesTest));
    }

    @Test
    public void should_get_risk_level_from_patient_all_notes_and_birth_date_and_gender() {
        //given
        NoteBean noteWithNoneTrigger = new NoteBean("0", "TestNone", "Le patient est en " +
                "chute libre. Sa maison est enfumée. Il adore les petits pois.", "2021-01-01");
        List<NoteBean> notesWithNoneTrigger = List.of(noteWithNoneTrigger);
        NoteBean noteWith2triggers = new NoteBean("0", "lastNameTest", "Hémoglobine A1C. " +
                "Microalbumine", "2021-01-01");
        List<NoteBean> notesWith2Triggers = List.of(noteWith2triggers);
        NoteBean noteWith3triggers = new NoteBean("0", "lastNameTest", "Hémoglobine A1C. " +
                "Microalbumine, TAILLE", "2021-01-01");
        List<NoteBean> notesWith3Triggers = List.of(noteWith3triggers);
        NoteBean noteWith4triggers = new NoteBean("0", "lastNameTest", "Hémoglobine A1C. " +
                "Microalbumine, Taille. Poids", "2021-01-01");
        List<NoteBean> notesWith4Triggers = List.of(noteWith4triggers);
        NoteBean noteWith5triggers = new NoteBean("0", "lastNameTest", "Hémoglobine A1C. " +
                "Microalbumine, Taille. Poids. Fumeur.", "2021-01-01");
        List<NoteBean> notesWith5Triggers = List.of(noteWith5triggers);
        NoteBean noteWith6triggers = new NoteBean("0", "lastNameTest", "Hémoglobine A1C. " +
                "Microalbumine, Taille. Poids. Fumeur. Anormales", "2021-01-01");
        List<NoteBean> notesWith6Triggers = List.of(noteWith6triggers);
        NoteBean noteWith7triggers = new NoteBean("0", "lastNameTest", "Hémoglobine A1C. " +
                "Microalbumine, Taille. Poids. Fumeur. Anormales. rechute.", "2021-01-01");
        List<NoteBean> notesWith7Triggers = List.of(noteWith7triggers);
        NoteBean noteWith8triggers = new NoteBean("0", "lastNameTest", "Hémoglobine A1C. " +
                "Microalbumine, Taille. Poids. Fumeur. Anormales. rechute. ANTICORPS", "2021-01-01");
        List<NoteBean> notesWith8Triggers = List.of(noteWith8triggers);
        //when then
        assertEquals("None", riskMSService
                .getRiskLevelFromPatientAllNotesAndBirthDateAndGender(
                        notesWithNoneTrigger, "1945-12-01", "F"));
        assertEquals("In Danger", riskMSService
                .getRiskLevelFromPatientAllNotesAndBirthDateAndGender(
                        notesWith3Triggers, "2000-12-01", "M"));
        assertEquals("EarlyOnset", riskMSService
                .getRiskLevelFromPatientAllNotesAndBirthDateAndGender(
                        notesWith5Triggers, "2000-12-01", "M"));
        assertEquals("In Danger", riskMSService
                .getRiskLevelFromPatientAllNotesAndBirthDateAndGender(
                        notesWith5Triggers, "2000-12-01", "F"));
        assertEquals("EarlyOnset", riskMSService
                .getRiskLevelFromPatientAllNotesAndBirthDateAndGender(
                        notesWith7Triggers, "2000-12-01", "F"));
        assertEquals("Borderline", riskMSService
                .getRiskLevelFromPatientAllNotesAndBirthDateAndGender(
                        notesWith2Triggers, "1995-01-01", "M"));
        assertEquals("In Danger", riskMSService
                .getRiskLevelFromPatientAllNotesAndBirthDateAndGender(
                        notesWith6Triggers, "1995-01-01", "F"));
        assertEquals("EarlyOnset", riskMSService
                .getRiskLevelFromPatientAllNotesAndBirthDateAndGender(
                        notesWith8Triggers, "1995-01-01", "F"));
    }

}
