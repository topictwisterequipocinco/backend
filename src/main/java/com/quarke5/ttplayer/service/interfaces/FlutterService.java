package com.quarke5.ttplayer.service.interfaces;

import com.quarke5.ttplayer.dto.request.JobOfferEvaluationFlutterDTO;
import com.quarke5.ttplayer.dto.request.JobOfferFlutterDTO;
import com.quarke5.ttplayer.dto.request.PersonDTO;
import com.quarke5.ttplayer.dto.request.ProfileDTO;
import com.quarke5.ttplayer.security.authentication.AuthenticationRequest;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.ExecutionException;

public interface FlutterService {

    ResponseEntity<?> createJwtByFlutter(AuthenticationRequest authenticationRequest) throws ExecutionException, InterruptedException;

    ResponseEntity<?> logoutUserFlutter(AuthenticationRequest authenticationRequest);

    ResponseEntity<?> getJobApplicantAllByApplicantByFlutter(Long id);

    ResponseEntity<?> getJobOfferAllByPublisher(Long id);

    ResponseEntity<?> getAllAppliedByJobOffer(Long id);

    ResponseEntity<?> getJobOfferEvaluation(JobOfferEvaluationFlutterDTO dto);

    ResponseEntity<?> create(PersonDTO personDTO);

    ResponseEntity<?> getProfileById(ProfileDTO profileDTO);

    void updatePerson(Long id, PersonDTO personDTO);

    void deletePerson(ProfileDTO profileDTO);

    ResponseEntity<?> updateJobOffer(Long id, JobOfferFlutterDTO jobOfferFlutterDTO);

}
