package com.nm.notesms.service;

import com.nm.notesms.model.Note;
import com.nm.notesms.repository.NotesMSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NotesMSService {

    @Autowired
    private NotesMSRepository notesRepository;

    public Optional<Note> get(String patientId) {
        return notesRepository.findByPatientId(patientId);
    }

    public List<Note> getAll(String patientId) {
        return notesRepository.findAllByPatientId(patientId);
    }

    @Transactional
    public void create(Note note) {
        notesRepository.insert(note);
    }

}
