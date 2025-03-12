package com.nm.riskms.service;

import com.nm.riskms.beans.NoteBean;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RiskMSService {


    public int getAge(String birthDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateOfBirth = LocalDate.parse(birthDate, formatter);
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }

    public int isOneWordPresentInString(String noteText, String targetWord) {
        Pattern pattern = Pattern.compile("\\b" + targetWord + "(?!\\w)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(noteText);
        int wordCount = 0;
        while (matcher.find() && wordCount == 0)
            wordCount = 1;
        return wordCount;
    }

    public int isOneOfTheseSlightlyDifferentlySpelledWordsPresentInString(String noteText, List<String> targetWords) {
        int wordCount = 0;
        int indexInList = 0;
        while (wordCount == 0 && indexInList < targetWords.size()) {
            String targetWord = targetWords.get(indexInList);
            Pattern pattern = Pattern.compile("\\b" + targetWord + "(?!\\w)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(noteText);
            while (matcher.find() && wordCount == 0)
                wordCount = 1;
            indexInList += 1;
        }
        return wordCount;
    }

    public Integer getTriggersNumberInConcatenedNoteText(String concatenedNoteText) {
        int triggerOccurrenceNumber;
        triggerOccurrenceNumber = isOneWordPresentInString(concatenedNoteText, "Hémoglobine A1C")
                + isOneWordPresentInString(concatenedNoteText, "Microalbumine")
                + isOneWordPresentInString(concatenedNoteText, "Taille")
                + isOneWordPresentInString(concatenedNoteText, "Poids")
                + isOneOfTheseSlightlyDifferentlySpelledWordsPresentInString(concatenedNoteText, List.of("Fume",
                "Fumeur", "Fumeuse", "Fumer"))
                + isOneOfTheseSlightlyDifferentlySpelledWordsPresentInString(concatenedNoteText, List.of("Anormal",
                "Anormale", "Anormales"))
                + isOneWordPresentInString(concatenedNoteText, "Cholestérol")
                + isOneOfTheseSlightlyDifferentlySpelledWordsPresentInString(concatenedNoteText, List.of("Vertige",
                "Vertiges"))
                + isOneWordPresentInString(concatenedNoteText, "Rechute")
                + isOneWordPresentInString(concatenedNoteText, "Réaction")
                + isOneWordPresentInString(concatenedNoteText, "Anticorps");
        return triggerOccurrenceNumber;
    }

    public Integer getTriggersNumberInAllNotesFromPatientId(List<NoteBean> notes) {
        StringBuilder concatenedNoteText = new StringBuilder();
        for (NoteBean n : notes) {
            concatenedNoteText.append(" ").append(n.getNoteText());
        }
        return getTriggersNumberInConcatenedNoteText(String.valueOf(concatenedNoteText));
    }

    public String getRiskLevelFromPatientAllNotesAndBirthDateAndGender(List<NoteBean> notes, String birthDate,
                                                                       String gender) {
        int age = getAge(birthDate);
        Integer triggersNumber = getTriggersNumberInAllNotesFromPatientId(notes);
        String riskLevel = "None";
        if (age < 30) {
            if (Objects.equals(gender, "M")) {
                if (triggersNumber == 3 || triggersNumber == 4) riskLevel = "In Danger";
                else if (triggersNumber >= 5) riskLevel = "EarlyOnset";
            } else if (Objects.equals(gender, "F")) {
                if (triggersNumber >= 4 && triggersNumber <= 6) riskLevel = "In Danger";
                else if (triggersNumber >= 7) riskLevel = "EarlyOnset";
            }
        }
        if (age >= 30) {
            if (triggersNumber >= 2 && triggersNumber <= 5) riskLevel = "Borderline";
            else if (triggersNumber == 6 || triggersNumber == 7) riskLevel = "In Danger";
            else if (triggersNumber >= 8) riskLevel = "EarlyOnset";
        }
        return riskLevel;
    }

}
