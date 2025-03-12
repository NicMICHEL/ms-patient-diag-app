package com.nm.notesms.repository;

import com.nm.notesms.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotesMSRepository extends MongoRepository<Note, String> {

    Optional<Note> findByPatientId(String patientId);

    List<Note> findAllByPatientId(String patientId);

}
