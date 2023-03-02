package com.quarke5.ttplayer.service.interfaces;

import com.quarke5.ttplayer.dto.request.PersonDTO;
import com.quarke5.ttplayer.model.Person;
import com.quarke5.ttplayer.service.crud.Removable;
import com.quarke5.ttplayer.service.crud.Writeable;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

public interface PersonService extends Removable, Writeable<PersonDTO> {

    ResponseEntity<?> sendGetPersonByRequest(Person person, Long id);

    ResponseEntity<?> getAll() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    Person getPersonByUsername(String username) throws ExecutionException, InterruptedException;

    ResponseEntity<?> getAllApplicant(int page) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    ResponseEntity<?> getAllPublisher(int page) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

}
