package com.quarke5.ttplayer.validator;

import com.quarke5.ttplayer.dto.request.LoginNicknameDTO;
import com.quarke5.ttplayer.model.JobOffer;
import com.quarke5.ttplayer.model.Player;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ValidJobOffer extends AbstractValidator {

    private String getResponseText(JobOffer jobOffer) {
        String response = "";
        response = jobOffer.getTitle() != null || !Pattern.matches(REGEX_NAMES, jobOffer.getTitle()) ? "Esta todo OK!" : "El Nombre o Nombre Oficial es incorrecto o invalido";
        response = jobOffer.getDescription() != null || !Pattern.matches(REGEX_NAMES, jobOffer.getDescription()) ? "Esta todo OK!" : "El Apellido o Nombre Representativo es incorrecto o invalido";
        response = jobOffer.getArea() != null || !Pattern.matches(REGEX_NAMES, jobOffer.getArea()) ? "Esta todo OK!" : "El Area es incorrecto o invalido";
        response = jobOffer.getBody() != null || !Pattern.matches(REGEX_NAMES, jobOffer.getBody()) ? "Esta todo OK!" : "El Cuerpo es incorrecto o invalido";
        response = jobOffer.getExperience() != null || !Pattern.matches(REGEX_NAMES, jobOffer.getExperience()) ? "Esta todo OK!" : "La Experiencia es incorrecto o invalido";
        response = jobOffer.getCategory().getName() != null || !Pattern.matches(REGEX_NAMES, jobOffer.getCategory().getName()) ? "Esta todo OK!" : "La Categoria es incorrecto o invalido";
        return response;
    }

    @Override
    public boolean validateLoginNickname(Player responseDTO, LoginNicknameDTO loginNicknameDTO) {
        return false;
    }
}
