package com.quarke5.ttplayer.service.impl;

import com.google.cloud.firestore.WriteResult;
import com.quarke5.ttplayer.dto.request.*;
import com.quarke5.ttplayer.exception.JobOfferException;
import com.quarke5.ttplayer.mapper.JobOfferMapper;
import com.quarke5.ttplayer.model.*;
import com.quarke5.ttplayer.model.enums.State;
import com.quarke5.ttplayer.repository.impl.JobOfferDAO;
import com.quarke5.ttplayer.service.crud.Readable;
import com.quarke5.ttplayer.service.emails.EmailsGoogle;
import com.quarke5.ttplayer.service.interfaces.ApplicantService;
import com.quarke5.ttplayer.service.interfaces.JobApplicationService;
import com.quarke5.ttplayer.service.interfaces.JobOfferService;
import com.quarke5.ttplayer.service.interfaces.PublisherService;
import com.quarke5.ttplayer.service.reports.ReportLists;
import com.quarke5.ttplayer.util.Errors;
import com.quarke5.ttplayer.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class JobOfferServiceImpl implements JobOfferService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobOfferServiceImpl.class);
    private static final int NEXT_ID = 1;

    @Autowired private JobOfferDAO repository;
    @Autowired private JobApplicationService jobApplicationService;
    @Autowired private JobOfferMapper mapper;
    @Autowired private MessageSource messageSource;
    @Autowired private EmailsGoogle emailGoogleService;
    @Autowired private PublisherService publisherService;
    @Autowired private ApplicantService applicantService;
    @Autowired private ReportLists reportLists;
    @Autowired private Validator validJobOffer;
    @Autowired private Readable readableService;
    @Autowired private Errors errors;

    @Override
    public ResponseEntity<?> getJobOfferById(Long id) {
        try {
            JobOffer jobOffer = getJobOffer(String.valueOf(id));
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                    mapper.toResponsePublisherJobOffer(jobOffer, messageSource.getMessage("joboffer.response.object.success", null, null)));
        } catch (Exception e) {
            LOGGER.error(messageSource.getMessage("joboffer.search.failed " + e.getMessage(), new Object[]{id}, null));
            errors.logError(messageSource.getMessage("joboffer.search.failed " + e.getMessage(), new Object[]{id}, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("joboffer.search.failed", new Object[]{id}, null));
        }
    }

    @Override
    public ResponseEntity<?> update(Long userIdByPublisher, JobOfferDTO jobOfferDTO) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if(jobOfferDTO.getId() != null && jobOfferDTO.getId() > ZERO){
            JobOffer jobOffer = getJobOffer(String.valueOf(jobOfferDTO.getId()));
            return updateJobOffer(jobOffer, jobOfferDTO);
        }else {
            return create(userIdByPublisher, jobOfferDTO);
        }
    }

    public ResponseEntity<?> updateJobOffer(JobOffer jobOffer, JobOfferDTO jobOfferDTO) {
        try {
            JobOffer newJobOffer = mapper.updateJobOffer(jobOffer, jobOfferDTO);
            validJobOffer.validJobOffer(newJobOffer);
            repository.update(newJobOffer);
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toResponsePublisherJobOffer(newJobOffer, messageSource.getMessage("joboffer.update.success", null, null)));
        } catch (JobOfferException e) {
            LOGGER.error(messageSource.getMessage("joboffer.update.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            errors.logError(messageSource.getMessage("joboffer.update.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(messageSource.getMessage("joboffer.update.failed",new Object[] {e.getMessage()}, null));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        try {
            JobOffer jobOffer = getJobOffer(String.valueOf(id));
            jobOffer.setDeleted(true);
            jobOffer.setDeletedDay(String.valueOf(LocalDate.now()));
            repository.update(jobOffer);
            return ResponseEntity.status(HttpStatus.OK).body(messageSource.getMessage("joboffer.deleted.success", null, null));
        } catch (Exception e) {
            LOGGER.error(messageSource.getMessage("joboffer.deleted.failed " + e.getMessage(), new Object[]{id}, null));
            errors.logError(messageSource.getMessage("joboffer.deleted.failed " + e.getMessage(), new Object[]{id}, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("joboffer.deleted.failed", new Object[]{id}, null));
        }
    }

    @Override
    public ResponseEntity<?> create(Long userIdByPublisher, JobOfferDTO jobOfferDTO) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if(!verifyIsExists(jobOfferDTO.getTitle())){
            return getCreateEntityResponseDTO(userIdByPublisher, jobOfferDTO);
        }else {
            LOGGER.error("No se ha podido crear el aviso. Publisher id " + userIdByPublisher + " Titulo del aviso " + jobOfferDTO.getTitle());
            errors.logError("No se ha podido crear el aviso. Publisher id " + userIdByPublisher + " Titulo del aviso " + jobOfferDTO.getTitle());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("joboffer.created.failed",null, null));
        }
    }

    @Override
    public ResponseEntity<?> getAll() {
        try {
            List<JobOffer> jobOffers = repository.getAllEntities();
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toJobOfferListSimplePublisher(jobOffers));
        } catch (Exception e) {
            LOGGER.error(messageSource.getMessage("joboffer.all.joboffer.failed " + e.getMessage(),null, null));
            errors.logError(messageSource.getMessage("joboffer.all.joboffer.failed " + e.getMessage(),null, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("joboffer.all.joboffer.failed",null, null));
        }
    }

    @Override
    public ResponseEntity<?> getAllPublished() {
        try {
            List<JobOffer> jobOffers = repository.findAllByState("PUBLISHED");
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toJobOfferListSimplePublisher(jobOffers));
        } catch (Exception e) {
            LOGGER.error(messageSource.getMessage("joboffer.all.joboffer.failed " + e.getMessage(),null, null));
            errors.logError(messageSource.getMessage("joboffer.all.joboffer.failed " + e.getMessage(),null, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("joboffer.all.joboffer.failed",null, null));
        }
    }

    @Override
    public ResponseEntity<?> getJobOfferPending() {
        try {
            List<JobOffer> jobOffers = repository.findAllByState("PENDING");
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toJobOfferListSimplePublisher(jobOffers));
        } catch (Exception e) {
            LOGGER.error(messageSource.getMessage("joboffer.all.joboffer.failed " + e.getMessage(),null, null));
            errors.logError(messageSource.getMessage("joboffer.all.joboffer.failed " + e.getMessage(),null, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("joboffer.all.joboffer.failed",null, null));
        }
    }

    @Override
    public ResponseEntity<?> postulate(PostulateDTO postulateDTO) {
        try{
            Applicant applicant = applicantService.getApplicantById(postulateDTO.getApplicantID());
            JobOffer jobOffer = getJobOffer(postulateDTO.getJobofferID());
            return createJobApplicant(applicant, jobOffer);
        }catch (Exception e){
            LOGGER.error("No existe el estudiante " + postulateDTO.getApplicantID() + " o no existe el aviso " + postulateDTO.getJobofferID());
            System.out.println("No existe el estudiante " + postulateDTO.getApplicantID() + " o no existe el aviso " + postulateDTO.getJobofferID());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("applicant.postulate.failed", new Object[]{postulateDTO.getJobofferID() + " " + e.getMessage()}, null));
        }
    }

    private ResponseEntity<?> createJobApplicant(Applicant applicant, JobOffer jobOffer) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if(!jobApplicationService.verifyJobApplicationExists(applicant, jobOffer)){
            try {
                jobApplicationService.createJobApplication(applicant, jobOffer);
                emailGoogleService.createEmailPostulate(jobOffer, applicant);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(messageSource.getMessage("applicant.postulate.success", null, null));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(messageSource.getMessage("system.failed", null, null));
            }
        }else {
            LOGGER.error("No se ha realizado la postulacion porque ya esta postulado al aviso " + jobOffer.getTitle());
            errors.logError("No se ha realizado la postulacion porque ya esta postulado al aviso " + jobOffer.getTitle());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("applicant.postulate.isPostulated", new Object[]{jobOffer.getId() + " " + jobOffer.getTitle()}, null));
        }
    }

    @Override
    public ResponseEntity<?> getJobOfferAllEvaluation(JobOfferEvaluationDTO jobOfferEvaluationDTO) {
        try {
            JobOffer jobOffer = repository.findById(String.valueOf(jobOfferEvaluationDTO.getId()));
            JobOffer jobOfferModify = mapper.modifyJobOffer(jobOffer, jobOfferEvaluationDTO);
            if(jobOfferModify.getState().equals(State.REVIEW)) emailGoogleService.sendEmailPublisherJobOfferReview(jobOfferModify);

            repository.update(jobOfferModify);
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toResponsePublisherJobOffer(jobOfferModify, messageSource.getMessage("joboffer.evaluation.success", null, null)));
        } catch (Exception e) {
            LOGGER.error(messageSource.getMessage("joboffer.evaluation.failed " + e.getMessage(), new Object[]{jobOfferEvaluationDTO.getId()}, null));
            errors.logError(messageSource.getMessage("joboffer.evaluation.failed " + e.getMessage(), new Object[]{jobOfferEvaluationDTO.getId()}, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("joboffer.evaluation.failed", new Object[]{jobOfferEvaluationDTO.getId()}, null));
        }
    }
    @Override
    public JobOffer getJobOffer(String id) {
        return repository.findById(id);
    }

    @Override
    public JobOffer updateJobOffer(JobOffer jobOffer, JobOfferFlutterDTO jobOfferFlutterDTO) throws ExecutionException, InterruptedException {
        return mapper.updateJobOfferByFlutter(jobOffer,jobOfferFlutterDTO);
    }

    @Override
    public void saveJobOffer(JobOffer newJobOffer) {
        repository.update(newJobOffer);
    }

    @Override
    public ResponseEntity<?> toResponsePublisherJobOffer(JobOffer aux, String message) {
        return ResponseEntity.status(HttpStatus.OK).body(mapper.toResponsePublisherJobOffer(aux, message));
    }

    @Override
    public List<JobOffer> getAllJobOffersByPublisherId(Long publisherID) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        List<JobOffer> jobOffers = repository.getAllEntities();
        List<JobOffer> resultList = new ArrayList<>();
        String searchID = String.valueOf(publisherID);
        for(JobOffer jobOffer : jobOffers){
            if(jobOffer.getPublisher()!=null && jobOffer.getPublisher().getId().equals(searchID)){
                resultList.add(jobOffer);
            }
        }
        return resultList;
    }

    private boolean verifyIsExists(String element) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        boolean result = false;
        List<JobOffer> jobOfferList = repository.getAllEntities();
        for(JobOffer ele : jobOfferList){
            if (ele.getTitle().equals(element)) {
                result = true;
                break;
            }
        }
        return result;
    }

    private ResponseEntity<?> getCreateEntityResponseDTO(Long userIdByPublisher, JobOfferDTO jobOfferDTO) {
        try {
            Publisher publisher = publisherService.getPublisherById(userIdByPublisher);
            JobOffer newJobOffer = mapper.toModel(jobOfferDTO, publisher, getLastId());
            validJobOffer.validJobOffer(newJobOffer);
            WriteResult jobOffer = repository.create(newJobOffer);
            emailGoogleService.createEmailJobOfferPublicated(newJobOffer, publisher);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponsePublisherJobOffer(newJobOffer, messageSource.getMessage("joboffer.created.success", null, null)));
        } catch (JobOfferException | ExecutionException | InterruptedException | InvocationTargetException |
                 IllegalAccessException | NoSuchMethodException e) {
            LOGGER.error("No se ha podido crear el aviso. Publisher id no existe : " + userIdByPublisher);
            errors.logError("No se ha podido crear el aviso. Publisher id no existe : " + userIdByPublisher);
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("joboffer.created.failed",new Object[] {e.getMessage()}, null));
        }
    }
    private int getLastId() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return repository.getAllEntities().size() + NEXT_ID;
    }

}