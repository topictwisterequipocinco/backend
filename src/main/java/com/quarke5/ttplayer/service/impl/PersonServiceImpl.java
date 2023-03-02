package com.quarke5.ttplayer.service.impl;

import com.google.cloud.firestore.WriteResult;
import com.quarke5.ttplayer.dto.request.JobOfferDTO;
import com.quarke5.ttplayer.dto.request.PersonDTO;
import com.quarke5.ttplayer.dto.response.ResponsePersonDto;
import com.quarke5.ttplayer.exception.JobOfferException;
import com.quarke5.ttplayer.exception.PersonException;
import com.quarke5.ttplayer.mapper.PersonMapper;
import com.quarke5.ttplayer.model.Applicant;
import com.quarke5.ttplayer.model.JobOffer;
import com.quarke5.ttplayer.model.Person;
import com.quarke5.ttplayer.model.Publisher;
import com.quarke5.ttplayer.repository.impl.PersonDAO;
import com.quarke5.ttplayer.service.emails.EmailGoogleService;
import com.quarke5.ttplayer.service.interfaces.ApplicantService;
import com.quarke5.ttplayer.service.interfaces.PersonService;
import com.quarke5.ttplayer.service.interfaces.PublisherService;
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
public class PersonServiceImpl implements PersonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonServiceImpl.class);
    private static final int NEXT_ID = 1;

    @Autowired private PersonDAO repository;
    @Autowired private EmailGoogleService emailGoogleService;
    @Autowired private PersonMapper mapper;
    @Autowired private MessageSource messageSource;
    @Autowired private UserService userService;
    @Autowired private Validator validator;
    @Autowired private PublisherService publisherService;
    @Autowired private ApplicantService applicantService;
    @Autowired private Errors errors;

    @Override
    public ResponseEntity<?> sendGetPersonByRequest(Person person, Long id) {
        try{
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                    mapper.toResponsePerson(person, messageSource.getMessage("person.response.object.success", null,null)));
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("person.search.failed " + e.getMessage(), new Object[] {id}, null));
            errors.logError(messageSource.getMessage("person.search.failed " + e.getMessage(), new Object[] {id}, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("person.search.failed", new Object[] {id}, null));
        }
    }

    @Override
    public ResponseEntity<?> update(Long id, PersonDTO personDTO) throws PersonException, ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return id > 0L ? updatePerson(id, personDTO) : create(personDTO);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        try{
            Person person = getPerson(id);
            LOGGER.info("la persona id es " + person.getId());
            person = mapper.deletePerson(person);
            repository.update(person);
            return ResponseEntity.status(HttpStatus.OK).body(messageSource.getMessage("person.deleted.success", null,null));
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("person.deleted.failed " + e.getMessage(), new Object[] {id}, null));
            errors.logError(messageSource.getMessage("person.deleted.failed " + e.getMessage(), new Object[] {id}, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("person.deleted.failed", new Object[] {id}, null));
        }
    }

    @Override
    public ResponseEntity<?> getAll() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return allPersonList(mapper.toPersonList(repository.getAllEntities()));
    }

    @Override
    public ResponseEntity<?> getAllApplicant(int numberPage) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return allPersonList(mapper.toApplicantList(applicantService.getAll()));
    }

    @Override
    public ResponseEntity<?> getAllPublisher(int numberPage) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return allPersonList(mapper.toPublisherList(publisherService.getAll()));
    }

    @Override
    public Person getPersonByUsername(String username) throws ExecutionException, InterruptedException {
        return repository.findByUser(userService.findByUsername(username));
    }

    private ResponseEntity<?> updatePerson(Long id, PersonDTO personDTO) {
        try{
            Person newPer = mapper.toUpdate(getPerson(id), personDTO);
            validator.validPerson(newPer);
            repository.update(newPer);
            return ResponseEntity.status(HttpStatus.OK).body(mapper.toResponsePerson(newPer, messageSource.getMessage("person.update.success", null,null)));
        }catch (PersonException e){
            LOGGER.error(messageSource.getMessage("person.update.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            errors.logError(messageSource.getMessage("person.update.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(messageSource.getMessage("person.update.failed",new Object[] {e.getMessage()}, null));
        }
    }

    private ResponseEntity<?> create(PersonDTO personDTO) throws PersonException, ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if(!verifyIsExists(personDTO.getIdentification())){
            return getCreateEntityResponseDTO(personDTO);
        }else {
            LOGGER.error(messageSource.getMessage("person.isExists", null,null));
            errors.logError(messageSource.getMessage("person.isExists", null,null));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("person.isExists",null, null));
        }
    }

    private ResponseEntity<?> allPersonList(List<ResponsePersonDto> lists) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(lists);
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("person.all.failed " + e.getMessage(),null, null));
            errors.logError(messageSource.getMessage("person.all.failed " + e.getMessage(),null, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("person.all.failed",null, null));
        }
    }

    private Person getPerson(Long id) {
        return repository.findById(String.valueOf(id));
    }

    private boolean verifyIsExists(String element) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        boolean result = false;
        List<Person> personList = repository.getAllEntities();
        for(Person ele : personList){
            if (ele.getIdentification().equals(element)) {
                result = true;
                break;
            }
        }
        return result;
    }

    private ResponseEntity<?> getCreateEntityResponseDTO(PersonDTO personDTO) {
        try{
            Person newPerson = mapper.createPerson(personDTO, getLastId());
            validator.validPerson(newPerson);
            WriteResult person = repository.create(newPerson);
            emailGoogleService.createEmailPerson(newPerson);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponsePerson(newPerson, messageSource.getMessage("person.created.success", null,null)));
        }catch (PersonException | ExecutionException | InterruptedException e){
            LOGGER.error(messageSource.getMessage("person.create.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            errors.logError(messageSource.getMessage("person.create.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("person.create.failed",new Object[] {e.getMessage()}, null));
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    private int getLastId() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return repository.getAllEntities().size() + NEXT_ID;
    }

}
