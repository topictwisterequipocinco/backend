package com.quarke5.ttplayer.service.emails;

import com.quarke5.ttplayer.exception.PersonException;
import com.quarke5.ttplayer.model.Applicant;
import com.quarke5.ttplayer.model.JobOffer;
import com.quarke5.ttplayer.model.Person;
import com.quarke5.ttplayer.model.Publisher;

public interface IEmailService {

    void createEmailPerson(Person aux) throws PersonException;

    void createEmailApplicant(Applicant applicant)throws PersonException;

    void createEmailPublisher(Publisher publisher)throws PersonException;

    void createEmailJobOfferPublicated(JobOffer jobOffer, Publisher publisher);

    void createEmailPostulate(JobOffer jobOffer, Applicant applicant);

    void sendEmailPublisherJobOfferReview(JobOffer jobOffer);
}
