package com.quarke5.ttplayer.controller;

import com.quarke5.ttplayer.controller.interfaces.Controllers;
import com.quarke5.ttplayer.controller.interfaces.Messages;
import com.quarke5.ttplayer.dto.request.JobOfferDTO;
import com.quarke5.ttplayer.dto.request.JobOfferEvaluationDTO;
import com.quarke5.ttplayer.dto.request.PostulateDTO;
import com.quarke5.ttplayer.exception.PersonException;
import com.quarke5.ttplayer.service.interfaces.JobOfferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

@RestController
@Api(value = "JobOffer Controller", description = "Controlador con los endpoints que actúan sobre los JobOffer.")
@RequestMapping("/joboffer")
public class JobOfferController implements Controllers<JobOfferDTO>, Messages {

    @Autowired private JobOfferService jobOfferService;

    @Override
    @ApiOperation(value = "joboffer.getById - Devuelve un JobOffer por su ID", response = ResponseEntity.class)
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
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> get(@PathVariable Long id){
        return jobOfferService.getJobOfferById(id);
    }

    @Override
    @ApiOperation(value = "joboffer.update - Modifica o crea un JobOffer " +
            "ID del User del Publisher", response = ResponseEntity.class)
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
    @PutMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid JobOfferDTO jobOfferDTO) throws PersonException, ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return jobOfferService.update(id, jobOfferDTO);
    }

    @Override
    @ApiOperation(value = "joboffer.delete - Elimina un JobOffer por su ID", response = ResponseEntity.class)
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
    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> delete(@PathVariable Long id){
        return jobOfferService.delete(id);
    }

    @ApiOperation(value = "joboffer.create - Crea un JobOffer nuevo. Id del Publisher que publica.", response = ResponseEntity.class)
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
    @PostMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createJob(@PathVariable Long id, @RequestBody @Valid JobOfferDTO jobOfferDTO) throws PersonException, ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return jobOfferService.create(id, jobOfferDTO);
    }

    @Override
    @ApiOperation(value = "joboffer.getAll - Devuelve la lista de Todos los JobOffer.", response = ResponseEntity.class)
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
    @GetMapping(value = "/", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAll() {
        return jobOfferService.getAll();
    }

    @ApiOperation(value = "joboffer.getAllPublished - Devuelve la lista de los JobOffer Estado Publicados.", response = ResponseEntity.class)
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
    @GetMapping(value = "/published-all", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAllPublished() {
        return jobOfferService.getAllPublished();
    }

    @ApiOperation(value = "joboffer.applicantPostulate - Un Applicant se postula a un aviso a traves del ID.", response = ResponseEntity.class)
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
    @PostMapping(value = "/postulate", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> postulate(@RequestBody @Valid PostulateDTO postulateDTO){
        return jobOfferService.postulate(postulateDTO);
    }

    @ApiOperation(value = "joboffer.getJobOfferPending - Devuelve la lista de Todos los JobOffer con State = Pending.", response = ResponseEntity.class)
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
    @GetMapping(value = "/pending", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getJobOfferPending() {
        return jobOfferService.getJobOfferPending();
    }

    @ApiOperation(value = "report-lists.getEvaluationAllJobOffers -UTN: Es la respuesta de UTN" +
            "Evaluacion de sobre cada aviso antes de ser publicado.", response = ResponseEntity.class)
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
    @PostMapping(value = "/evaluation", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getJobOfferAllEvaluation(@RequestBody @Valid JobOfferEvaluationDTO jobOfferEvaluationDTO){
        return jobOfferService.getJobOfferAllEvaluation(jobOfferEvaluationDTO);
    }


}
