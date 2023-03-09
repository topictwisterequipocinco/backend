package com.quarke5.ttplayer.service.interfaces;

import com.quarke5.ttplayer.dto.request.JobOfferDTO;
import com.quarke5.ttplayer.dto.request.JobOfferEvaluationDTO;
import com.quarke5.ttplayer.dto.request.JobOfferFlutterDTO;
import com.quarke5.ttplayer.dto.request.PostulateDTO;
import com.quarke5.ttplayer.exception.PersonException;
import com.quarke5.ttplayer.model.JobOffer;
import com.quarke5.ttplayer.model.Publisher;
import com.quarke5.ttplayer.service.crud.Removable;
import com.quarke5.ttplayer.service.crud.Writeable;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface JobOfferService extends Removable, Writeable<JobOfferDTO> {
    ResponseEntity<?> create(Long id, JobOfferDTO entity) throws PersonException, ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    ResponseEntity<?> getJobOfferById(Long id);

    ResponseEntity<?> getAll();

    ResponseEntity<?> getAllPublished();

    ResponseEntity<?> getJobOfferPending();

    ResponseEntity<?> postulate(PostulateDTO postulateDTO);

    ResponseEntity<?> getJobOfferAllEvaluation(JobOfferEvaluationDTO jobOfferEvaluationDTO);

    JobOffer getJobOffer(Long id);

    JobOffer updateJobOffer(JobOffer jobOffer, JobOfferFlutterDTO jobOfferFlutterDTO) throws ExecutionException, InterruptedException;

    void saveJobOffer(JobOffer newJobOffer);

    ResponseEntity<?> toResponsePublisherJobOffer(JobOffer aux, String message);

    List<JobOffer> getAllJobOffersByPublisherId(Long publisherID) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;
}
