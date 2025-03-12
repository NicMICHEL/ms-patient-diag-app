package com.nm.frontms.controller;

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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;


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

    public void setDataDTOAttributes(DataDTO dataDTO, String firstNameToSearch, String lastNameToSearch,
                                     PatientBean patient)
    {
        dataDTO.setFirstNameToSearch(firstNameToSearch);
        dataDTO.setLastNameToSearch(lastNameToSearch);
        dataDTO.setPatient(patient);
    }

    public void setPatientInfosTemplateAttributes(Model model, String firstNameToSearch, String lastNameToSearch,
                                                  PatientBean patient) {
        DataDTO dataDTO = new DataDTO();
        setDataDTOAttributes(dataDTO, firstNameToSearch, lastNameToSearch, patient);
        model.addAttribute("dataDTO", dataDTO);
    }

    @GetMapping("/")
    public String showPatientInfosForm(Model model) {
        setPatientInfosTemplateAttributes(model, "", "",
                new PatientBean("", "", "", " ", "", ""));
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
                        new PatientBean("", "", "", " ", "", ""));
                model.addAttribute("dataDTO", dataDTO);
                return "patientInfos";
            }
            setPatientInfosTemplateAttributes(model, "", "", patient);
            return "patientInfos";
        } else {
            setDataDTOAttributes(dataDTO,
                    dataDTO.getFirstNameToSearch(), dataDTO.getLastNameToSearch(),
                    new PatientBean("", "", "", " ", "", ""));
            model.addAttribute("dataDTO", dataDTO);
            return "patientInfos";
        }
    }

    @PostMapping("/updatePatientFile")
    public String updatePatientFile(DataDTO dataDTO, BindingResult result, Model model,
                                    @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient)
            throws Exception {
        PatientBean updatedPatient = updatePatientWithPatientMS(dataDTO.getPatient(), authorizedClient);
        setPatientInfosTemplateAttributes(model, "", "", updatedPatient);
        return "patientInfos";
    }

}
