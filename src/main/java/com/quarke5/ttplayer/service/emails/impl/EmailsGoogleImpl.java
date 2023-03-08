package com.quarke5.ttplayer.service.emails.impl;

import com.quarke5.ttplayer.dto.response.ResponseEmailUtnDTO;
import com.quarke5.ttplayer.exception.PersonException;
import com.quarke5.ttplayer.mapper.EmailMapper;
import com.quarke5.ttplayer.model.Applicant;
import com.quarke5.ttplayer.model.JobOffer;
import com.quarke5.ttplayer.model.Person;
import com.quarke5.ttplayer.model.Publisher;
import com.quarke5.ttplayer.service.emails.EmailsGoogle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailsGoogleImpl implements EmailsGoogle {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailsGoogleImpl.class);
    private static final String EMAIL_WELCOME = "Bienvenido a la Bolsa de Trabajo del CUVL-UTN";
    private static final String PATH_BASE = "http://localhost:8082/auth/activate";
    private static final String PUBLICATED = "Ha publicado el siguiente aviso : ";
    private static final String DAY = " el dìa ";
    private static final String POSTULATE = "Ud. se ha postulado al siguiente aviso : ";
    private static final String APPLICANT_POSTULATE = "Se ha postulado al siguiente aviso : ";
    private static final String STATE_REVIEW = "Tiene un aviso en estado de revisiòn";

    private final JavaMailSender sender;
    private final EmailMapper emailMapper;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Autowired
    public EmailsGoogleImpl(JavaMailSender sender, EmailMapper emailMapper) {
        this.sender = sender;
        this.emailMapper = emailMapper;
    }

    @Override
    public void createEmailPerson(Person app){
        ResponseEmailUtnDTO email = emailMapper.toModelEmailCreatePerson(app, PATH_BASE, EMAIL_WELCOME);
        LOGGER.info("Construyendo el cuerpo del mail a enviar.");
        String bodyText  = getBodyText(email);
        String subject = getSubjectText();
        sendEmail(email, bodyText, subject);
    }

    @Override
    public void createEmailApplicant(Applicant applicant) throws PersonException {
        ResponseEmailUtnDTO email = emailMapper.toModelEmailCreateApplicant(applicant, PATH_BASE, EMAIL_WELCOME);
        String bodyText = getBodyText(email);
        String subject = getSubjectText();
        sendEmail(email, bodyText, subject);
    }

    @Override
    public void createEmailPublisher(Publisher publisher) throws PersonException {
        ResponseEmailUtnDTO email = emailMapper.toModelEmailCreatePublisher(publisher, PATH_BASE, EMAIL_WELCOME);
        String bodyText = getBodyText(email);
        String subject = getSubjectText();
        sendEmail(email, bodyText, subject);
    }

    @Override
    public void createEmailJobOfferPublicated(JobOffer jobOffer, Publisher publisher) {
        ResponseEmailUtnDTO email = emailMapper.toModelEmailJobOffer(jobOffer, publisher, PUBLICATED, DAY);
        LOGGER.info("Construyendo el cuerpo para enviar el Mail al Publicador del aviso...");
        String bodyText = "Denominaciòn y Nombres :" + " " + email.getNames()
                + " ." + " " + "Se ha publicado su aviso con el Tìtulo : " + " " + jobOffer.getTitle() + " " +
                " y la Descripciòn : " + " " + jobOffer.getDescription() + " .";
        String subject = "Publicaciòn de su Aviso-Bolsa de Trabajo UTN";
        sendEmail(email, bodyText, subject);
    }

    @Override
    public void createEmailPostulate(JobOffer jobOffer, Applicant applicant) {
        ResponseEmailUtnDTO emailApplicant = emailMapper.toModelEmailPostulateApplicantJobOffer(jobOffer, applicant, POSTULATE, DAY);
        LOGGER.info("Construyendo el cuerpo para enviar el Mail al Postulante sobre el aviso que se aplico...");
        String bodyText = "Denominaciòn y Nombres :" + " " + emailApplicant.getNames()
                + " ." + " " + "Se ha postulado al aviso con el Tìtulo : " + " " + jobOffer.getTitle() + " " +
                " y la Descripciòn : " + " " + jobOffer.getDescription() + " .";
        String subject = "Te postulaste al Aviso-UTN";
        sendEmail(emailApplicant, bodyText, subject);

        ResponseEmailUtnDTO emailPublisher = emailMapper.toModelEmailPostulatedJobOfferByPublisher(jobOffer, applicant, APPLICANT_POSTULATE, DAY);
        LOGGER.info("Construyendo el cuerpo para enviar el Mail al Publicador del aviso que se han postulado...");
        String bodyTextJob = "Denominaciòn y Nombres :" + " " + emailPublisher.getNames()
                + " ." + " " + "Se han postulado a su Aviso con el Tìtulo : " + " " + jobOffer.getTitle() + " " +
                " y la Descripciòn : " + " " + jobOffer.getDescription() + " .";
        String subjectJob = "Se han Postulado a su Aviso";
        sendEmail(emailPublisher, bodyTextJob, subjectJob);
    }

    @Override
    public void sendEmailPublisherJobOfferReview(JobOffer jobOffer) {
        ResponseEmailUtnDTO jobOfferReview = emailMapper.toSendEmailJobOfferReview(jobOffer, STATE_REVIEW);
        String bodyReview = "Tiene un aviso para reveer";
        String subjectReview = "Un aviso para revisar antes de ser publicado";
        sendEmail(jobOfferReview, bodyReview, subjectReview);
    }

    private String getSubjectText() {
        return "Bienvenido a Bolsa de Trabajo CUVL-UTN 2021";
    }

    private String getBodyText(ResponseEmailUtnDTO email) {
        return "Denominaciòn y Nombres :" + " " + email.getNames()
                + " ." + " " + "Presione este link para Activar su cuenta : " + " " + email.getUrl();
    }

    private void sendEmail(ResponseEmailUtnDTO email, String body, String subject){
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(email.getEmail());
            helper.setText(body, true);
            helper.setSubject(subject);
            sender.send(message);
            LOGGER.info("Mail enviado!");
        } catch (MessagingException e) {
            LOGGER.error("Hubo un error al enviar el mail.");
        }
    }


}
