package com.quarke5.ttplayer.controller;

import com.quarke5.ttplayer.controller.interfaces.Messages;
import com.quarke5.ttplayer.model.Category;
import com.quarke5.ttplayer.service.reports.ReportLists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "JobOffer Controller", description = "Controlador con los endpoints que actúan sobre los JobOffer.")
@RequestMapping("/report-lists")
public class ReportListsController implements Messages {

    @Autowired
    ReportLists reportLists;

    @ApiOperation(value = "report-lists.getAllWithPage - Devuelve la lista de Todos los JobOffer Con Page.", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 201, message = CREATED),
            @ApiResponse(code = 202, message = ACCEPTED),
            @ApiResponse(code = 304, message = NOT_MODIFIED),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE),
            @ApiResponse(code = 406, message = NOT_ACCEPTABLE),
            @ApiResponse(code = 503, message = SERVICE_UNAVAILABLE),
    })
    @GetMapping(value = "/with-page", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAllWithPage(@RequestParam(name = "page",defaultValue = "0") int page) {
        return reportLists.getAllWithPage(page);
    }

    @ApiOperation(value = "report-lists.getJobApplicantAllByApplicant - Applicant: Ver su lista de postulaciones. El ID que se recibe es el del Applicant quien consulta.", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 201, message = CREATED),
            @ApiResponse(code = 202, message = ACCEPTED),
            @ApiResponse(code = 304, message = NOT_MODIFIED),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE),
            @ApiResponse(code = 406, message = NOT_ACCEPTABLE),
            @ApiResponse(code = 503, message = SERVICE_UNAVAILABLE),
    })
    @GetMapping(value = "/jobapplicants/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getJobApplicantAllByApplicant(@PathVariable Long id){
        return reportLists.getJobApplicantAllByApplicant(id);
    }

    @ApiOperation(value = "report-lists.getJobApplicantAllByJobOfferSimplePublisher - Publisher: Ver quien se aplico en cada aviso. El Id es el del aviso a consultar", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 201, message = CREATED),
            @ApiResponse(code = 202, message = ACCEPTED),
            @ApiResponse(code = 304, message = NOT_MODIFIED),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE),
            @ApiResponse(code = 406, message = NOT_ACCEPTABLE),
            @ApiResponse(code = 503, message = SERVICE_UNAVAILABLE),
    })
    @GetMapping(value = "/jobapplicants-by-my-offers/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getJobApplicantAllByJobOfferSimplePublisher(@PathVariable Long id){
        return reportLists.getJobApplicantAllByJobOfferSimplePublisher(id);
    }

    @ApiOperation(value = "report-lists.getAllJobOfferByPublisher - Publisher: quiere ver todos sus avisos. El ID del publicador", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 201, message = CREATED),
            @ApiResponse(code = 202, message = ACCEPTED),
            @ApiResponse(code = 304, message = NOT_MODIFIED),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE),
            @ApiResponse(code = 406, message = NOT_ACCEPTABLE),
            @ApiResponse(code = 503, message = SERVICE_UNAVAILABLE),
    })
    @GetMapping(path = "/publisher/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getJobOfferAllByPublisher(@PathVariable Long id) {
        return reportLists.getJobOfferAllByPublisher(id);
    }

    //Todo a revisar.....ya resuelto en el front
    @ApiOperation(value = "report-lists.getAllJobOfferSimplePublisherByFilter - Publicador quiere ver sus avisos por algun filtro de Categoria. El ID del publicador", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 201, message = CREATED),
            @ApiResponse(code = 202, message = ACCEPTED),
            @ApiResponse(code = 304, message = NOT_MODIFIED),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE),
            @ApiResponse(code = 406, message = NOT_ACCEPTABLE),
            @ApiResponse(code = 503, message = SERVICE_UNAVAILABLE),
    })
    @GetMapping(path = "/publisher/filter/{filter}/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getJobOfferAllSimplePublisher(@RequestParam Category filter, @PathVariable Long id) {
        return reportLists.getJobOfferAllSimplePublisher(filter, id);
    }

    @ApiOperation(value = "report-lists.getJobOfferAllWithFilter - UTN: Devuelve la lista de los JobOffer segun el state solicitado.", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = OK_RESPONSE),
            @ApiResponse(code = 201, message = CREATED),
            @ApiResponse(code = 202, message = ACCEPTED),
            @ApiResponse(code = 304, message = NOT_MODIFIED),
            @ApiResponse(code = 401, message = UNAUTHORIZED_RESPONSE),
            @ApiResponse(code = 403, message = FORBIDDEN_RESPONSE),
            @ApiResponse(code = 404, message = NOT_FOUND_RESPONSE),
            @ApiResponse(code = 406, message = NOT_ACCEPTABLE),
            @ApiResponse(code = 503, message = SERVICE_UNAVAILABLE),
    })
    @GetMapping(path = "/filter/{state}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getJobOfferAllWithFilter(@PathVariable String state){
        return reportLists.getJobOfferAllWithFilter(state);
    }

}
