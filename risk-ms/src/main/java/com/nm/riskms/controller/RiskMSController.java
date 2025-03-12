package com.nm.riskms.controller;

import com.nm.riskms.beans.NoteBean;
import com.nm.riskms.service.RiskMSService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@RestController
@RequestMapping("/patientRisk")
public class RiskMSController {

    private static final Logger logger = LogManager.getLogger(RiskMSController.class);
    private final WebClient webClient;
    @Autowired
    private RiskMSService riskMSService;
    @Value("${gatewayMSPort}")
    private String gatewayMSPort;

    public RiskMSController(WebClient webClient, RiskMSService riskMSService) {
        this.webClient = webClient;
        this.riskMSService = riskMSService;
    }

    public List<NoteBean> getByIdAllNotesFromNotesMS(String patientId)
            throws Exception {
        try {
            List<NoteBean> notes = webClient
                    .get()
                    .uri("http://localhost:" + gatewayMSPort + "/patientNote/" + patientId)
                    .retrieve()
                    .bodyToFlux(NoteBean.class)
                    .collectList()
                    .block();
            return notes;
        } catch (WebClientResponseException wcre) {
            logger.error("Error Response Code is {} and Response Body is {}"
                    , wcre.getStatusCode(), wcre.getResponseBodyAsString());
            logger.error("Exception in method getByIdAllNotesFromNotesMS()", wcre);
            throw wcre;
        } catch (Exception ex) {
            logger.error("Exception in method getByIdAllNotesFromNotesMS()", ex);
            throw ex;
        }
    }

    @GetMapping("/riskLevel/{patientId}/{birthDate}/{gender}")
    public String getRiskLevel(@PathVariable String patientId, @PathVariable String birthDate,
                               @PathVariable String gender) {
        String riskLevel = "";
        try {
            List<NoteBean> notes = getByIdAllNotesFromNotesMS(patientId);
            riskLevel = riskMSService.getRiskLevelFromPatientAllNotesAndBirthDateAndGender(notes, birthDate, gender);
        } catch (WebClientResponseException wcre) {
            int code = wcre.getStatusCode().value();
            if (code == 404) {
                riskLevel = "NoNotesToAnalyze";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return riskLevel;
    }

}
