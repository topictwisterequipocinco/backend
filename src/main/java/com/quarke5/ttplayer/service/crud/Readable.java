package com.quarke5.ttplayer.service.crud;

import com.quarke5.ttplayer.model.Applicant;
import com.quarke5.ttplayer.model.Publisher;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.ExecutionException;

public interface Readable {
    ResponseEntity<?> getById(Long id);

    ResponseEntity<?> getByIdentification(String identification) throws ExecutionException, InterruptedException;

    ResponseEntity<?> getByIdUser(Long id);

    Applicant getPersonTypeApplicantByIdUser(Long id) throws ExecutionException, InterruptedException;

    Publisher getPersonTypePublisherByIdUser(Long id);
}
