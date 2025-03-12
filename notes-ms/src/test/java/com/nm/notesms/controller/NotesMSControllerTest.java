package com.nm.notesms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nm.notesms.exception.NotFoundException;
import com.nm.notesms.exception.NotesBadRequestException;
import com.nm.notesms.model.Note;
import com.nm.notesms.repository.NotesMSRepository;
import com.nm.notesms.service.NotesMSService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(NotesMSController.class)
public class NotesMSControllerTest {

    @MockBean
    private NotesMSService notesMSService;
    @MockBean
    private NotesMSRepository notesMSRepository;
    @InjectMocks
    private NotesMSController notesMSController;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user")
    public void should_return_all_notes_successfully() throws Exception {
        //given
        Note note1 = new Note("0", "lastNameTest", "noteText1", "0000-01-01");
        Note note2 = new Note("0", "lastNameTest", "noteText2", "0000-02-02");
        List<Note> listNotesTest = Arrays.asList(note1, note2);
        when(notesMSService.getAll("0")).thenReturn(listNotesTest);
        //when
        mockMvc.perform(get("/patientNote/{patientId}", "0"))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].noteText", is("noteText1")))
                .andExpect(jsonPath("$[1].noteText", is("noteText2")))
                .andExpect(jsonPath("$[0].patientId", is("0")))
                .andExpect(jsonPath("$[1].patientId", is("0")));
    }

    @Test
    @WithMockUser(username = "user")
    public void should_throw_NotFoundException_when_none_note_exists_in_Data_Base_with_patientId() throws Exception {
        //given
        List<Note> listNotesTest = List.of();
        when(notesMSService.getAll("0")).thenReturn(listNotesTest);
        //when
        mockMvc.perform(get("/patientNote/{patientId}", "0")
                        // then
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertInstanceOf(NotFoundException.class, result.getResolvedException()))
                .andExpect(
                        result -> assertEquals("None note with patientId: \"0\"  exist in Data Base."
                                , Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void should_create_note_successfully() throws Exception {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/patientNote");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        Note noteTest = new Note("0", "lastNameTest", "noteTest", "0000-00-00");
        doNothing().when(notesMSService).create(noteTest);
        //when
        ResponseEntity<Note> response = notesMSController.create(noteTest);
        //then
        assertEquals(201, response.getStatusCode().value());
        URI location = response.getHeaders().getLocation();
        assertNotNull(location);
        assertEquals("/patientNote/0", location.getPath());
        verify(notesMSService).create(noteTest);
    }

    @Test
    @WithMockUser(username = "user")
    public void should_throw_NoteBadRequestException_when_creating_note_with_empty_text() throws Exception {
        //given
        Note noteTest = new Note("0", "lastNameTest", "", "0000-00-00");
        ObjectMapper mapper = new ObjectMapper();
        String temp = mapper.writeValueAsString(noteTest);
        //when
        mockMvc.perform(post("/patientNote/").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(temp))
                // then
                .andExpect(result -> assertInstanceOf(NotesBadRequestException.class,
                        result.getResolvedException()))
                .andExpect(result -> assertEquals("Empty note"
                        , Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

}
