package com.quarke5.ttplayer.service.reports;

import com.quarke5.ttplayer.model.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ReportLists {

    Pageable generatePageable(int number);

    ResponseEntity<?> getAllWithPage(int numberPage);

    ResponseEntity<?> getJobOfferAllWithFilter(String state);

    ResponseEntity<?> getJobApplicantAllByApplicant(Long id);

    ResponseEntity<?> getJobApplicantAllByJobOfferSimplePublisher(Long id);

    ResponseEntity<?> getJobOfferAllByPublisher(Long id);

    ResponseEntity<?> getJobOfferAllSimplePublisher(Category filter, Long id);

}
