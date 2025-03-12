package com.nm.notesms.controller;

import com.nm.notesms.exception.NotFoundException;
import com.nm.notesms.exception.NotesBadRequestException;
import com.nm.notesms.model.Note;
import com.nm.notesms.service.NotesMSService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/patientNote")
public class NotesMSController {

    private static final Logger logger = LogManager.getLogger(NotesMSController.class);
    @Autowired
    private NotesMSService notesService;

    @GetMapping("/{patientId}")
    public List<Note> getAll(@PathVariable String patientId)
            throws NotFoundException {
        List<Note> notes = notesService.getAll(patientId);
        if (notes.isEmpty()) {
            logger.error("None note with patientId: \"" + patientId + "\"  exists in Data Base.");
            throw new NotFoundException("None note with patientId: \"" + patientId + "\"  exist in Data Base.");
        }
        return notes;
    }

    @PostMapping("/")
    public ResponseEntity<Note> create(@RequestBody Note note) throws NotesBadRequestException {
        if (Objects.equals(note.getNoteText(), "")) {
            logger.error("NotesBadRequestException : Empty note");
            throw new NotesBadRequestException("Empty note");
        } else {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{patientId}")
                    .buildAndExpand(note.getPatientId())
                    .toUri();
            notesService.create(note);
            return ResponseEntity.created(location).build();
        }
    }

}
