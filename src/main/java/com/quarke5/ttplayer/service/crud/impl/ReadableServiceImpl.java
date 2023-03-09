package com.quarke5.ttplayer.service.crud.impl;

import com.quarke5.ttplayer.model.Applicant;
import com.quarke5.ttplayer.model.Person;
import com.quarke5.ttplayer.model.Publisher;
import com.quarke5.ttplayer.model.User;
import com.quarke5.ttplayer.model.enums.Roles;
import com.quarke5.ttplayer.repository.impl.PersonDAO;
import com.quarke5.ttplayer.service.crud.Readable;
import com.quarke5.ttplayer.service.interfaces.ApplicantService;
import com.quarke5.ttplayer.service.interfaces.PersonService;
import com.quarke5.ttplayer.service.interfaces.PublisherService;
import com.quarke5.ttplayer.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class ReadableServiceImpl implements Readable {

    @Autowired
    PersonDAO repository;
    @Autowired
    PersonService personService;
    @Autowired
    ApplicantService applicantService;
    @Autowired
    PublisherService publisherService;
    @Autowired
    UserService userService;

    @Override
    public ResponseEntity<?> getById(Long id) {
        Person person = repository.findById(String.valueOf(id));
        if (person.getUser().getRole().getRole().equals(Roles.APPLICANT)) {
            //return applicantService.sendGetPersonByRequest(person, id);
            return null;
        } else if (person.getUser().getRole().getRole().equals(Roles.PUBLISHER)) {
            return publisherService.sendGetPersonByRequest(person, id);
        } else {
            return personService.sendGetPersonByRequest(person, id);
        }
    }

    @Override
    public ResponseEntity<?> getByIdentification(String identification) throws ExecutionException, InterruptedException {
        Person person = repository.getEntity(identification);
        if (person.getUser().getRole().getRole().equals(Roles.APPLICANT)) {
            //return applicantService.sendGetPersonByRequest(person, Long.valueOf(identification));
            return null;
        } else if (person.getUser().getRole().getRole().equals(Roles.PUBLISHER)) {
            return publisherService.sendGetPersonByRequest(person, Long.valueOf(identification));
        } else {
            return personService.sendGetPersonByRequest(person, Long.valueOf(identification));
        }
    }

    @Override
    public ResponseEntity<?> getByIdUser(Long id) {
        User user = getUserById(id);
        Person person = repository.findByUser(user);
        if (person.getUser().getRole().getRole().equals(Roles.APPLICANT)) {
            //return applicantService.getByIdUserApp(user);
            return null;
        } else if (person.getUser().getRole().getRole().equals(Roles.PUBLISHER)) {
            return publisherService.getByIdUserPub(user);
        } else {
            return personService.sendGetPersonByRequest(person, id);
        }
    }

    @Override
    public Applicant getPersonTypeApplicantByIdUser(Long id) throws ExecutionException, InterruptedException {
        User user = getUserById(id);
        return applicantService.getApplicantByUser(user);
    }

    @Override
    public Publisher getPersonTypePublisherByIdUser(Long id) throws ExecutionException, InterruptedException {
        User user = getUserById(id);
        return publisherService.getPublisherByUser(user);
    }

    private User getUserById(Long id) {
        return userService.findByIdUser(id);
    }

}
