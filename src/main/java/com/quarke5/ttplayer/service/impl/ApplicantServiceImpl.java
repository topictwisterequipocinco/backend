package com.quarke5.ttplayer.service.impl;

import com.google.cloud.firestore.WriteResult;
import com.quarke5.ttplayer.dto.request.PersonDTO;
import com.quarke5.ttplayer.exception.PersonException;
import com.quarke5.ttplayer.mapper.ApplicantMapper;
import com.quarke5.ttplayer.model.Applicant;
import com.quarke5.ttplayer.model.Person;
import com.quarke5.ttplayer.model.User;
import com.quarke5.ttplayer.model.enums.State;
import com.quarke5.ttplayer.repository.impl.ApplicantDAO;
import com.quarke5.ttplayer.service.emails.EmailsGoogle;
import com.quarke5.ttplayer.service.interfaces.ApplicantService;
import com.quarke5.ttplayer.service.interfaces.UserService;
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
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class ApplicantServiceImpl implements ApplicantService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicantServiceImpl.class);
    private static final int NEXT_ID = 1;

    @Autowired private ApplicantDAO repository;
    @Autowired private EmailsGoogle emailGoogleService;

    @Autowired private UserService userService;
    @Autowired private ApplicantMapper applicantMapper;
    @Autowired private MessageSource messageSource;
    @Autowired private Validator validator;
    @Autowired private Errors errors;

    @Override
    public ResponseEntity<?> sendGetPersonByRequest(Long id){
        try{
            Applicant applicant = getApplicant(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(applicantMapper.toResponseApplicant(applicant, messageSource.getMessage("applicant.response.object.success", null,null)));
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("applicant.search.failed " + e.getMessage(), new Object[] {id}, null));
            errors.logError(messageSource.getMessage("applicant.search.failed " + e.getMessage(), new Object[] {id}, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("applicant.search.failed", new Object[] {id}, null));
        }
    }

    @Override
    public ResponseEntity<?> update(Long id, PersonDTO personDTO) throws PersonException, ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return id > 0L ? updateApplicant(id, personDTO) : create(personDTO);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        try{
            Applicant applicant = getApplicant(id);
            applicant.setDeleted(true);
            applicant.getUser().setState(State.DELETED);
            repository.update(applicant);
            return ResponseEntity.status(HttpStatus.OK).body(messageSource.getMessage("applicant.deleted.success", null,null));
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("applicant.deleted.failed " + e.getMessage(), new Object[] {id}, null));
            errors.logError(messageSource.getMessage("applicant.deleted.failed " + e.getMessage(), new Object[] {id}, null));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("applicant.deleted.failed", new Object[] {id}, null));
        }
    }

    @Override
    public ResponseEntity<?> getByIdUserApp(Long id) {
        try{
            User user = userService.findByIdUser(id);
            Applicant applicant = repository.findByUser(user);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(applicantMapper.toResponseApplicant(applicant, messageSource.getMessage("applicant.response.object.success", null,null)));
        }catch (Exception e){
            LOGGER.error("No existe la cuenta de Applicant con id: " + id);
            errors.logError("No existe la cuenta de Applicant con id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("applicant.search.failed", new Object[] {id}, null));
        }
    }

    @Override
    public void postulateJobOffer(Applicant applicant) throws ExecutionException, InterruptedException {
        repository.create(applicant);
    }

    @Override
    public List<Applicant> getAll() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return repository.getAllEntities();
    }

    @Override
    public Applicant getApplicantByUser(User user) throws ExecutionException, InterruptedException {
        return repository.findByUser(user);
    }

    @Override
    public ResponseEntity<?> sendGetPersonByIdentification(String identification) {
        return null;
    }

    @Override
    public ResponseEntity<?> sendGetPersonByIdRequest(Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(getApplicant(id));
    }

    @Override
    public ResponseEntity<?> getAllApplicant() {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(repository.getAllEntities());
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("person.all.failed " + e.getMessage(),null, null));
            errors.logError(messageSource.getMessage("person.all.failed " + e.getMessage(),null, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("person.all.failed",null, null));
        }
    }

    public ResponseEntity<?> updateApplicant(Long id, PersonDTO applicantDTO) {
        try{
            Applicant newApplicant = applicantMapper.toUpdate(getApplicant(id), applicantDTO);
            validator.validApplicant(newApplicant);
            repository.update(newApplicant);
            return ResponseEntity.status(HttpStatus.OK).body(applicantMapper.toResponseApplicant(newApplicant, messageSource.getMessage("applicant.update.success", null,null)));
        }catch (PersonException e){
            LOGGER.error(messageSource.getMessage("applicant.update.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            errors.logError(messageSource.getMessage("applicant.update.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(messageSource.getMessage("applicant.update.failed",new Object[] {e.getMessage()}, null));
        }
    }

    private ResponseEntity<?> create(PersonDTO applicantDTO) throws PersonException, ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if(!verifyIsExists(applicantDTO.getIdentification())){
            return getCreateEntityResponseDTO(applicantDTO);
        }else {
            LOGGER.error(messageSource.getMessage("person.isExists", null,null));
            errors.logError(messageSource.getMessage("person.isExists", null,null));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("person.isExists",null, null));
        }
    }

    private Applicant getApplicant(Long id) {
        return repository.findById(String.valueOf(id));
    }

    private boolean verifyIsExists(String element) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        boolean result = false;
        List<Applicant> applicantList = repository.getAllEntities();
        for(Applicant ele : applicantList){
            if (ele.getIdentification().equals(element)) {
                result = true;
                break;
            }
        }
        return result;
    }

    private ResponseEntity<?> getCreateEntityResponseDTO(PersonDTO entity) {
        try{
            Applicant applicant = applicantMapper.createApplicant(entity, getLastId());
            validator.validApplicant(applicant);
            WriteResult result = repository.create(applicant);
            emailGoogleService.createEmailPerson(applicant);
            return ResponseEntity.status(HttpStatus.CREATED).body(applicantMapper.toResponseApplicant(applicant, messageSource.getMessage("applicant.created.success", null,null)));
        }catch (ExecutionException | InterruptedException | InvocationTargetException |
                IllegalAccessException | NoSuchMethodException | PersonException e) {
            LOGGER.error(e.getMessage());
            errors.logError(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("applicant.created.failed",new Object[] {e.getMessage()}, null));
        }
    }
    private int getLastId() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return repository.getAllEntities().size() + NEXT_ID;
    }

}
