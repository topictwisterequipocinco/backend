package com.quarke5.ttplayer.service.interfaces;

import com.quarke5.ttplayer.dto.request.PersonDTO;
import com.quarke5.ttplayer.model.Person;
import com.quarke5.ttplayer.model.Publisher;
import com.quarke5.ttplayer.model.User;
import com.quarke5.ttplayer.service.crud.Removable;
import com.quarke5.ttplayer.service.crud.Writeable;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface PublisherService extends Removable, Writeable<PersonDTO> {

    ResponseEntity<?> sendGetPersonByRequest(Person person, Long id);

    ResponseEntity<?> getByIdUserPub(User user);

    void addJobOffer(Publisher publisher);

    List<Publisher> getAll() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    Publisher getPublisherByUser(User user);
}
