package com.quarke5.ttplayer.service.impl;

import com.google.cloud.firestore.WriteResult;
import com.quarke5.ttplayer.dto.request.PersonDTO;
import com.quarke5.ttplayer.dto.response.UserByFlutterDTO;
import com.quarke5.ttplayer.exception.PersonException;
import com.quarke5.ttplayer.mapper.PersonMapper;
import com.quarke5.ttplayer.mapper.PublisherMapper;
import com.quarke5.ttplayer.model.Applicant;
import com.quarke5.ttplayer.model.Person;
import com.quarke5.ttplayer.model.Publisher;
import com.quarke5.ttplayer.model.User;
import com.quarke5.ttplayer.model.enums.State;
import com.quarke5.ttplayer.repository.impl.PublisherDAO;
import com.quarke5.ttplayer.service.emails.EmailsGoogle;
import com.quarke5.ttplayer.service.interfaces.PublisherService;
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
public class PublisherServiceImpl implements PublisherService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublisherServiceImpl.class);
    private static final int NEXT_ID = 1;

    @Autowired private PublisherDAO repository;
    @Autowired private EmailsGoogle emailGoogleService;
    @Autowired private PublisherMapper publisherMapper;
    @Autowired private MessageSource messageSource;
    @Autowired private Validator validator;
    @Autowired private Errors errors;

    @Override
    public ResponseEntity<?> update(Long id, PersonDTO personDTO) throws PersonException, ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return id > 0L ? updatePublisher(id, personDTO) : create(personDTO);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        try{
            LOGGER.info("el id que recibo es " + id);
            Publisher publisher = getPublisher(String.valueOf(id));
            publisher.setDeleted(true);
            publisher.getUser().setState(State.DELETED);
            repository.update(publisher);
            return ResponseEntity.status(HttpStatus.OK).body(messageSource.getMessage("publisher.deleted.success", null,null));
        }catch (Exception e){
            LOGGER.error("No existe la cuenta de Publisher con id: " + id);
            errors.logError("No existe la cuenta de Publisher con id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("publisher.deleted.failed", new Object[] {id}, null));
        }
    }

    @Override
    public ResponseEntity<?> sendGetPersonByRequest(Person person, Long id) {
        try{
            Publisher publisher = getPublisher(person.getId());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(publisherMapper.toResponsePublisher(publisher, messageSource.getMessage("publisher.response.object.success", null,null)));
        }catch (Exception e){
            LOGGER.error("No existe la cuenta de Publisher con id: " + id);
            errors.logError("No existe la cuenta de Publisher con id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("publisher.search.failed", new Object[] {id}, null));
        }
    }

    @Override
    public ResponseEntity<?> getByIdUserPub(User user) {
        try{
            Publisher publisher = repository.findByUser(user);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(publisherMapper.toResponsePublisher(publisher, messageSource.getMessage("publisher.response.object.success", null,null)));
        }catch (Exception e){
            LOGGER.error("No existe la cuenta de Publisher con id: " + user.getId());
            errors.logError("No existe la cuenta de Publisher con id: " + user.getId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("publisher.search.failed", new Object[] {user.getId()}, null));
        }
    }

    @Override
    public void addJobOffer(Publisher publisher) {
        repository.update(publisher);
    }

    @Override
    public List<Publisher> getAll() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return repository.getAllEntities();
    }

    @Override
    public Publisher getPublisherByUser(User user) throws ExecutionException, InterruptedException {
        return repository.findByUser(user);
    }

    @Override
    public ResponseEntity<?> sendGetPersonByIdRequest(Long id) {
        try{
            Publisher publisher = getPublisherById(id);
            return ResponseEntity.status(HttpStatus.OK).body(publisher);
        }catch (Exception e) {
            LOGGER.error("No existe la cuenta de Publisher con id: " + id + " " + e.getMessage());
            errors.logError("No existe la cuenta de Publisher con id: " + id + " " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("publisher.search.failed",new Object[] {id + e.getMessage()}, null));
        }
    }

    @Override
    public Publisher getPublisherById(Long id) {
        return repository.findById(String.valueOf(id));
    }

    private ResponseEntity<?> updatePublisher(Long id, PersonDTO publisherDTO) {
        try {
            Publisher newPublisher = publisherMapper.toUpdate(getPublisher(String.valueOf(id)), publisherDTO);
            validator.validPublisher(newPublisher);
            repository.update(newPublisher);
            return ResponseEntity.status(HttpStatus.CREATED).body(publisherMapper.toResponsePublisher(newPublisher, messageSource.getMessage("publisher.update.success", null,null)));
        }catch (PersonException e){
            LOGGER.error(messageSource.getMessage("publisher.update.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            errors.logError(messageSource.getMessage("publisher.update.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("publisher.update.failed",new Object[] {e.getMessage()}, null));
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

    private Publisher getPublisher(String id) {
        return repository.findById(String.valueOf(id));
    }

    private boolean verifyIsExists(String element) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        boolean result = false;
        List<Publisher> publisherList = repository.getAllEntities();
        for(Publisher ele : publisherList){
            if (ele.getIdentification().equals(element)) {
                result = true;
                break;
            }
        }
        return result;
    }

    private ResponseEntity<?> getCreateEntityResponseDTO(PersonDTO personDTO) {
        try{
            Publisher newPublisher = publisherMapper.createPublisher(personDTO, getLastId());
            validator.validPublisher(newPublisher);
            WriteResult publisher = repository.create(newPublisher);
            emailGoogleService.createEmailPublisher(newPublisher);
            return ResponseEntity.status(HttpStatus.CREATED).body(publisherMapper.toResponsePublisher(newPublisher, messageSource.getMessage("publisher.update.success", null,null)));
        }catch (PersonException | ExecutionException | InterruptedException e){
            LOGGER.error(messageSource.getMessage("publisher.created.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            errors.logError(messageSource.getMessage("publisher.created.failed " + e.getMessage(),new Object[] {e.getMessage()}, null));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("publisher.created.failed",new Object[] {e.getMessage()}, null));
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
