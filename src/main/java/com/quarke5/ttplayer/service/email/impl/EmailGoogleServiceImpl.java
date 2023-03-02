package com.quarke5.ttplayer.service.email.impl;

import com.quarke5.ttplayer.dto.response.ResponseEmailDTO;
import com.quarke5.ttplayer.mapper.EmailMapper;
import com.quarke5.ttplayer.model.Player;
import com.quarke5.ttplayer.service.email.EmailGoogleService;
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
public class EmailGoogleServiceImpl implements EmailGoogleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailGoogleServiceImpl.class);
    private static final String EMAIL_SUCCESS = "Mail enviado!";
    private static final String EMAIL_FAILED = "Hubo un error al enviar el email.";

    @Autowired private JavaMailSender sender;
    @Autowired private EmailMapper emailMapper;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Override
    public void sendEmailNewPlayer(Player player) {
        LOGGER.info("Construyendo el cuerpo del mail a enviar al nuevo Player...");
        sendEmail(emailMapper.toModelEmailPlayerCreate(player));
    }

    private void sendEmail(ResponseEmailDTO responseEmailDTO){
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(responseEmailDTO.getEmail());
            helper.setText(responseEmailDTO.getBodyText(), true);
            helper.setSubject(responseEmailDTO.getSubject());
            sender.send(message);
            LOGGER.info(EMAIL_SUCCESS);
        } catch (MessagingException e) {
            LOGGER.error(EMAIL_FAILED);
        }
    }
}
