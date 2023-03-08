package com.quarke5.ttplayer.controller;

import com.quarke5.ttplayer.dto.request.JobOfferEvaluationFlutterDTO;
import com.quarke5.ttplayer.dto.request.JobOfferFlutterDTO;
import com.quarke5.ttplayer.dto.request.PersonDTO;
import com.quarke5.ttplayer.dto.request.ProfileDTO;
import com.quarke5.ttplayer.exception.PersonException;
import com.quarke5.ttplayer.security.authentication.AuthenticationRequest;
import com.quarke5.ttplayer.service.interfaces.FlutterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(value = "JobOffer Controller", description = "Controlador con los endpoints que actúan sobre los JobOffer.")
@RequestMapping("flutter")
public class FlutterController {

    @Autowired
    FlutterService flutterService;

    @ApiOperation(value = "${flutter.createAuthenticationTokenByFlutter} - Devuelve JWT y Datos del User", response = ResponseEntity.class)
    @PostMapping(value = "/login-flutter", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createAuthenticationTokenByFlutter(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        return flutterService.createJwtByFlutter(authenticationRequest);
    }

    @ApiOperation(value = "${flutter.logoutUserFlutter} - Desloguea al usuario.", response = ResponseEntity.class)
    @PostMapping(value = "/logout-flutter", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> logoutUserFlutter(@RequestBody AuthenticationRequest authenticationRequest) {
        return flutterService.logoutUserFlutter(authenticationRequest);
    }

    @ApiOperation(value = "flutter.getJobApplicantAllByApplicantByFlutter - Applicant: Ver su lista de postulaciones. El ID que se recibe es el del Applicant quien consulta.", response = ResponseEntity.class)
    @GetMapping(value = "/applied/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getJobApplicantAllByApplicantByFlutter(@PathVariable Long id){
        return flutterService.getJobApplicantAllByApplicantByFlutter(id);
    }

    @ApiOperation(value = "flutter.getAllJobOfferByPublisher - Publisher: quiere ver todos sus avisos. El ID del publicador", response = ResponseEntity.class)
    @GetMapping(path = "/publisher/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getJobOfferAllByPublisher(@PathVariable Long id) {
        return flutterService.getJobOfferAllByPublisher(id);
    }

    @ApiOperation(value = "flutter.getAllAppliedByJobOffer - Publisher: Ver quien se aplico en cada aviso. El Id es el del aviso a consultar", response = ResponseEntity.class)
    @GetMapping(value = "/jobapplicants-by-my-offers/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAllAppliedByJobOffer(@PathVariable Long id){
        return flutterService.getAllAppliedByJobOffer(id);
    }

    @ApiOperation(value = "flutter.getJobOfferEvaluation -UTN: Es la respuesta de UTN" +
            "Evaluacion de sobre cada aviso antes de ser publicado.", response = ResponseEntity.class)
    @PostMapping("/evaluation")
    public ResponseEntity<?> getJobOfferEvaluation(@RequestBody @Valid JobOfferEvaluationFlutterDTO dto){
        return flutterService.getJobOfferEvaluation(dto);
    }

    @ApiOperation(value = "${flutter.create} - Crea una Persona nueva", response = ResponseEntity.class)
    @PostMapping(value = "/", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> create(@RequestBody @Valid PersonDTO personDTO) throws PersonException {
        return flutterService.create(personDTO);
    }

    @ApiOperation(value = "${flutter.getProfileById} - Devuelve los datos de una persona por su ID", response = ResponseEntity.class)
    @PostMapping("/get-profile")
    public ResponseEntity<?> getProfileById(@RequestBody @Valid ProfileDTO profileDTO){
        return flutterService.getProfileById(profileDTO);   }



    @ApiOperation(value = "flutter.updatePerson - Modifica una persona y su perfil desde App Flutter", response = ResponseEntity.class)
    @PutMapping(value = "/change-user", produces = {MediaType.APPLICATION_JSON_VALUE})
    public void updatePerson(@RequestBody @Valid ProfileDTO profileDTO){
        flutterService.updatePerson(profileDTO);
    }

    @ApiOperation(value = "flutter.deletePerson - Elimina una persona y su perfil desde App Flutter", response = ResponseEntity.class)
    @DeleteMapping(value = "/delete-user", produces = {MediaType.APPLICATION_JSON_VALUE})
    public void deletePerson(@RequestBody @Valid ProfileDTO profileDTO) {
        flutterService.deletePerson(profileDTO);
    }

    @ApiOperation(value = "flutter.updateJobOffer - Modifica un JobOffer desde App Flutter", response = ResponseEntity.class)
    @PutMapping(value = "/joboffer", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateJobOffer(@RequestBody @Valid JobOfferFlutterDTO jobOfferFlutterDTO){
        return flutterService.updateJobOffer(jobOfferFlutterDTO);
    }


}
