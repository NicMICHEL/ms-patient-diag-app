package com.nm.frontms.controller;

import com.nm.frontms.beans.NoteBean;
import com.nm.frontms.beans.PatientBean;
import com.nm.frontms.web.dto.DataDTO;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Controller
@RequestMapping("/client")
public class FrontMSController {

    private static final Logger logger = LogManager.getLogger(FrontMSController.class);
    private final WebClient webClient;

    @Value("${gatewayMSPort}")
    private String gatewayMSPort;

    public FrontMSController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/patientFile/{firstName}/{lastName}")
    public PatientBean getPatientFileFromPatientMS(@PathVariable String firstName, @PathVariable String lastName,
                                                   @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient)
            throws Exception {
        try {
            PatientBean patient = webClient
                    .get()
                    .uri("http://localhost:"+gatewayMSPort+"/patientFile/" + firstName + "/" + lastName)
                    .headers(h -> h.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
                    .retrieve()
                    .bodyToMono(PatientBean.class)
                    .block();
            return patient;
        } catch (WebClientResponseException wcre) {
            logger.error("Exception in method getPatientFileFromPatientMS()", wcre);
            logger.error("Error Response Code is {} and Response Body is {}"
                    , wcre.getStatusCode(), wcre.getResponseBodyAsString());
            throw wcre;
        } catch (Exception ex) {
            logger.error("Exception in method getPatientFileFromPatientMS()", ex);
            throw ex;
        }
    }

    @PostMapping("/updatePatientWithPatientMS")
    public PatientBean updatePatientWithPatientMS(@RequestBody PatientBean newPatientBean
            , @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) throws Exception {
        try {
            return webClient.post()
                    .uri("http://localhost:"+gatewayMSPort+"/patientFile/")
                    .headers(h -> h.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(newPatientBean)
                    .retrieve()
                    .bodyToMono(PatientBean.class)
                    .block();
        } catch (WebClientResponseException wcre) {
            logger.error("Error Response Code is {} and Response Body is {}"
                    , wcre.getStatusCode(), wcre.getResponseBodyAsString());
            logger.error("Exception in method getPatientFileFromPatientMS()", wcre);
            throw wcre;
        } catch (Exception ex) {
            logger.error("Exception in method getPatientFileFromPatientMS()", ex);
            throw ex;
        }
    }

    @GetMapping("/patientNotes/{idPatient}")
    public List<NoteBean> getAllNotesByIdFromNotesMS(@PathVariable String idPatient,
                                                     @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient)
            throws Exception {
        List<NoteBean> notesDTO = new ArrayList<>();
        try {
            notesDTO = webClient
                    .get()
                    .uri("http://localhost:" + gatewayMSPort + "/patientNote/" + idPatient)
                    .headers(h -> h.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
                    .retrieve()
                    //.bodyToMono(new ParameterizedTypeReference<List<NoteBean>>() {})
                    .bodyToFlux(NoteBean.class)
                    .collectList()
                    .block();
        } catch (WebClientResponseException wcre) {
            logger.error("Error Response Code is {} and Response Body is {}"
                    , wcre.getStatusCode(), wcre.getResponseBodyAsString());
            logger.error("Exception in method getAllNotesByIdFromNotesMS()", wcre);
            int code = wcre.getStatusCode().value();
            if (code == 404) {
                NoteBean advertNoteBean = new NoteBean("", "",
                        "Aucune note n'a encore été enregistrée pour ce patient.", "");
                notesDTO.add(advertNoteBean);
            }
        } catch (Exception ex) {
            logger.error("Exception in method getAllNotesByIdFromNotesMS()", ex);
            throw ex;
        }
        return notesDTO;
    }

    @PostMapping("/addNoteWithNotesMS")
    public NoteBean addNoteWithNotesMS(@RequestBody NoteBean newNoteBean
            , @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) throws Exception {
        try {
            return webClient.post()
                    .uri("http://localhost:" + gatewayMSPort + "/patientNote/")
                    .headers(h -> h.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(newNoteBean)
                    .retrieve()
                    .bodyToMono(NoteBean.class)
                    .block();
        } catch (WebClientResponseException wcre) {
            logger.error("Error Response Code is {} and Response Body is {}"
                    , wcre.getStatusCode(), wcre.getResponseBodyAsString());
            logger.error("Exception in method addNoteWithNotesMS()", wcre);
            throw wcre;
        } catch (Exception ex) {
            logger.error("Exception in method addNoteWithNotesMS()", ex);
            throw ex;
        }
    }

    public void setDataDTOAttributes(DataDTO dataDTO, String firstNameToSearch, String lastNameToSearch,
                                     PatientBean patient, List<NoteBean> notesDTOs, String noteText)
    {
        dataDTO.setFirstNameToSearch(firstNameToSearch);
        dataDTO.setLastNameToSearch(lastNameToSearch);
        dataDTO.setPatient(patient);
        dataDTO.setNotesDTOs(notesDTOs);
        dataDTO.setNoteText(noteText);
    }

    public void setPatientInfosTemplateAttributes(Model model, String firstNameToSearch, String lastNameToSearch,
                                                  PatientBean patient, List<NoteBean> notesDTOs, String noteText) {
        DataDTO dataDTO = new DataDTO();
        setDataDTOAttributes(dataDTO, firstNameToSearch, lastNameToSearch, patient, notesDTOs, noteText);
        model.addAttribute("dataDTO", dataDTO);
    }

    @GetMapping("/")
    public String showPatientInfosForm(Model model) {
        setPatientInfosTemplateAttributes(model, "", "",
                new PatientBean("", "", "", " ", "", ""),
                new ArrayList<>(), "");
        return "patientInfos";
    }

    @GetMapping("/getPatientFile")
    public String getPatientFile(
            @Valid @ModelAttribute("dataDTO") DataDTO dataDTO, BindingResult result, Model model,
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) throws Exception {
        if (!result.hasErrors()) {
            String idPatient;
            PatientBean patient;
            try {
                patient = getPatientFileFromPatientMS(dataDTO.getFirstNameToSearch(),
                        dataDTO.getLastNameToSearch(), authorizedClient);
                idPatient = String.valueOf(patient.getIdPatient());
            } catch (WebClientResponseException wcre) {
                int code = wcre.getStatusCode().value();
                String notFoundMessage = "";
                if (code == 404) {
                    notFoundMessage = "Aucun patient avec le nom \"" + dataDTO.getLastNameToSearch()
                            + "\" et le prénom \"" + dataDTO.getFirstNameToSearch() + "\" n'existe en base de données";
                }
                ObjectError error = new ObjectError("globalError", notFoundMessage);
                result.addError(error);
                setDataDTOAttributes(dataDTO,
                        "", "",
                        new PatientBean("", "", "", " ", "", ""),
                        new ArrayList<>(), "");
                model.addAttribute("dataDTO", dataDTO);
                return "patientInfos";
            }
            List<NoteBean> notesDTOs = getAllNotesByIdFromNotesMS(idPatient, authorizedClient);
            setPatientInfosTemplateAttributes(model, "", "", patient, notesDTOs,
                    "");
            return "patientInfos";
        } else {
            setDataDTOAttributes(dataDTO,
                    dataDTO.getFirstNameToSearch(), dataDTO.getLastNameToSearch(),
                    new PatientBean("", "", "", " ", "", ""),
                    new ArrayList<>(), "");
            model.addAttribute("dataDTO", dataDTO);
            return "patientInfos";
        }
    }

    @PostMapping("/updatePatientFile")
    public String updatePatientFile(DataDTO dataDTO, BindingResult result, Model model,
                                    @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient)
            throws Exception {
        PatientBean updatedPatient = updatePatientWithPatientMS(dataDTO.getPatient(), authorizedClient);
        List<NoteBean> notesDTOs = getAllNotesByIdFromNotesMS(String.valueOf(updatedPatient.getIdPatient()),
                authorizedClient);
        setPatientInfosTemplateAttributes(model, "", "", updatedPatient, notesDTOs,
                "");
        return "patientInfos";
    }

    @PostMapping("/addNoteToPatientNotes")
    public String addNoteToPatientNotes(DataDTO dataDTO, BindingResult result, Model model,
                                        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) throws Exception {
        PatientBean patient = dataDTO.getPatient();
        String patientId = String.valueOf(patient.getIdPatient());
        String noteText = dataDTO.getNoteText();
        if (!Objects.equals(noteText, "")) {
            NoteBean noteBean = new NoteBean("");
            noteBean.setPatientId(patientId);
            noteBean.setLastName(patient.getLastName());
            LocalDate localDateNow = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String stringLocalDateNow = localDateNow.format(formatter);
            noteBean.setDate(stringLocalDateNow);
            noteBean.setNoteText(noteText);
            addNoteWithNotesMS(noteBean, authorizedClient);
            List<NoteBean> updatedNotesDTOs = getAllNotesByIdFromNotesMS(noteBean.getPatientId(), authorizedClient);
            setPatientInfosTemplateAttributes(model, "", "", patient, updatedNotesDTOs,
                    "");
            return "patientInfos";
        } else {
            String emptyNoteMessage = " Veuillez écrire une note.";
            FieldError error = new FieldError("dataDTO", "noteText", emptyNoteMessage);
            result.addError(error);
            dataDTO.setNotesDTOs(getAllNotesByIdFromNotesMS(patientId, authorizedClient));
            model.addAttribute("dataDTO", dataDTO);
            return "patientInfos";
        }
    }

}
