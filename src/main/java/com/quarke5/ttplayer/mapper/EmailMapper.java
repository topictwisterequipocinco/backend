package com.quarke5.ttplayer.mapper;

import com.quarke5.ttplayer.dto.response.ResponseEmailDTO;
import com.quarke5.ttplayer.dto.response.ResponseEmailUtnDTO;
import com.quarke5.ttplayer.model.*;
import org.springframework.stereotype.Component;

@Component
public class EmailMapper {
    private static final String EMAIL_WELCOME = "Bienvenido a Topic Twister Game";
    private static final String BODY_TEXT = "ya es miembro de la comunidad gamers de Quarks. Felicitaciones!!!!! " + " Puede loguerse y comenzar a jugar.";
    private static final String SUBJECT = "Bienvenido a Topic Twister Game - @QUARKS 2022";

    public ResponseEmailDTO toModelEmailPlayerCreate(Player player) {
        String bodyText = "NickName :" + " " + player.getName() + " " + BODY_TEXT;
        ResponseEmailDTO dto = ResponseEmailDTO.builder()
                .name(player.getName())
                .email(player.getUsername())
                .message(EMAIL_WELCOME)
                .url("")
                .bodyText(bodyText)
                .subject(SUBJECT)
                .build();
        return dto;
    }


    public ResponseEmailUtnDTO toModelEmailCreate(Person person, String path, String welcome) {
        String url = ""+path+"/"+person.getUser().getUsername()+"/"+person.getUser().getVerificationCode()+"";

        ResponseEmailUtnDTO res = ResponseEmailUtnDTO.builder()
                .names(person.getOficialName() + " " + person.getLastName())
                .identification(person.getIdentification())
                .email(person.getUser().getUsername())
                .phone(person.getPhoneNumber())
                .message(welcome)
                .url(url)
                .build();
        return res;
    }

    public ResponseEmailUtnDTO toModelEmailJobOffer(JobOffer jobOffer, Publisher person, String publicated, String day) {
        ResponseEmailUtnDTO res = ResponseEmailUtnDTO.builder()
                .names(person.getOficialName() + " " + person.getLastName())
                .identification(person.getIdentification())
                .email(person.getUser().getUsername())
                .message(publicated + " " + jobOffer.getTitle() + " " +
                        jobOffer.getDescription() + " " + day + " " + jobOffer.getCreateDay())
                .build();
        return res;
    }

    public ResponseEmailUtnDTO toModelEmailPostulateApplicantJobOffer(JobOffer jobOffer, Applicant applicant, String postulate, String day) {
        ResponseEmailUtnDTO res = ResponseEmailUtnDTO.builder()
                .names(applicant.getOficialName() + " " + applicant.getLastName())
                .email(applicant.getUser().getUsername())
                .message(postulate + " " + jobOffer.getTitle() + " " +
                        jobOffer.getDescription() + " " + day + " " + jobOffer.getCreateDay())
                .build();
        return res;
    }

    public ResponseEmailUtnDTO toModelEmailPostulatedJobOfferByPublisher(JobOffer jobOffer, Applicant applicant, String applicantPostulate, String day) {
        ResponseEmailUtnDTO res = ResponseEmailUtnDTO.builder()
                .names(applicant.getOficialName() + " " + applicant.getLastName())
                .email(jobOffer.getPublisher().getUser().getUsername())
                .message(applicantPostulate + " " + jobOffer.getTitle() + " " +
                        jobOffer.getDescription() + " " + day + " " + jobOffer.getCreateDay())
                .build();
        return res;
    }

    public ResponseEmailUtnDTO toSendEmailJobOfferReview(JobOffer jobOffer, String stateReview) {
        ResponseEmailUtnDTO res = ResponseEmailUtnDTO.builder()
                .names(jobOffer.getPublisher().getOficialName() + " " + jobOffer.getPublisher().getLastName())
                .email(jobOffer.getPublisher().getUser().getUsername())
                .message(stateReview + " " + " con el tìtulo : " + " " + jobOffer.getTitle())
                .build();
        return res;
    }

}
