package com.quarke5.ttplayer.service.crud;

import com.quarke5.ttplayer.model.Applicant;
import com.quarke5.ttplayer.model.Publisher;
import org.springframework.http.ResponseEntity;

public interface Readable {
    ResponseEntity<?> getById(Long id);

    ResponseEntity<?> getByIdentification(String identification);

    ResponseEntity<?> getByIdUser(Long id);

    Applicant getPersonTypeApplicantByIdUser(Long id);

    Publisher getPersonTypePublisherByIdUser(Long id);
}
