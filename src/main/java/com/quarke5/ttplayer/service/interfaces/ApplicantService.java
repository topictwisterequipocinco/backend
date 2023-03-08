package com.quarke5.ttplayer.service.interfaces;

import com.quarke5.ttplayer.dto.request.PersonDTO;
import com.quarke5.ttplayer.model.Applicant;
import com.quarke5.ttplayer.model.Person;
import com.quarke5.ttplayer.model.User;
import com.quarke5.ttplayer.service.crud.Removable;
import com.quarke5.ttplayer.service.crud.Writeable;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ApplicantService extends Removable, Writeable<PersonDTO> {

    ResponseEntity<?> sendGetPersonByRequest(Long id);

    ResponseEntity<?> getByIdUserApp(Long id);

    void postulateJobOffer(Applicant applicant) throws ExecutionException, InterruptedException;

    List<Applicant> getAll() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    Applicant getApplicantByUser(User user) throws ExecutionException, InterruptedException;

    ResponseEntity<?> sendGetPersonByIdentification(String identification);

    ResponseEntity<?> sendGetPersonByIdRequest(Long id);

    ResponseEntity<?> getAllApplicant();
}
