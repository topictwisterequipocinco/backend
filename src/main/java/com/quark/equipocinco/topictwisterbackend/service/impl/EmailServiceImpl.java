package com.quark.equipocinco.topictwisterbackend.service.impl;

import com.quark.equipocinco.topictwisterbackend.dto.response.ResponseEmailDto;
import com.quark.equipocinco.topictwisterbackend.exception.PlayerException;
import com.quark.equipocinco.topictwisterbackend.mapper.EmailMapper;
import com.quark.equipocinco.topictwisterbackend.model.Player;
import com.quark.equipocinco.topictwisterbackend.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    private static final String EMAIL_WELCOME = "Bienvenido a Topic Twister en Quarks Academy";
    private static final String PATH_BASE = "http://localhost:8080/auth/activate";

    private MessageSource messageSource;
    private JavaMailSender sender;
    private EmailMapper emailMapper;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Autowired
    public EmailServiceImpl(MessageSource messageSource, JavaMailSender sender, EmailMapper emailMapper) {
        this.messageSource = messageSource;
        this.sender = sender;
        this.emailMapper = emailMapper;
    }

    @Override
    public void createEmailPlayer(Player player) throws PlayerException {
        ResponseEmailDto email = emailMapper.toModelEmailCreate(player, PATH_BASE, EMAIL_WELCOME);
        logger.info("Construyendo el cuerpo del email... se enviara al Player Nuevo...");
        String bodyText = "Nombre: " + player.getName() + " , Apellido: " + player.getSurname()
                + " " + " se ha registrado a Topic Twister el dia " + new Date()
                + " ." + " Presione este link para Activar su cuenta: " + email.getUrl();
        sendEmail(email, bodyText, EMAIL_WELCOME);
    }

    private void sendEmail(ResponseEmailDto email, String body, String subject){
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(email.getEmail());
            helper.setText(body, true);
            helper.setSubject(subject);
            sender.send(message);
            logger.info("Mail enviado!");
        } catch (MessagingException e) {
            logger.error("Hubo un error al enviar el mail.");
        }
    }

}
