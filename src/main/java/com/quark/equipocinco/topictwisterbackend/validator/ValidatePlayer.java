package com.quark.equipocinco.topictwisterbackend.validator;

import com.quark.equipocinco.topictwisterbackend.dto.request.LoginDTO;
import com.quark.equipocinco.topictwisterbackend.dto.response.ResponseValidatorDto;
import com.quark.equipocinco.topictwisterbackend.exception.PlayerException;
import com.quark.equipocinco.topictwisterbackend.model.Player;
import com.quark.equipocinco.topictwisterbackend.mapper.PlayerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class ValidatePlayer extends AbstractValidator{

    @Autowired PlayerMapper playerMapper;
    @Autowired BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public boolean validPlayer(Player player) throws PlayerException {
        Map<String, String> list = getResponseText(player);
        ResponseValidatorDto dto = getResultValidate(list);
        if(!dto.isResult()){throw new PlayerException(dto.getResponse());}

        return dto.isResult();
    }


    private ResponseValidatorDto getResultValidate(Map<String, String> list) {
        ResponseValidatorDto dto = new ResponseValidatorDto();
        dto.setResult(true);
        for (Map.Entry<String, String> ele : list.entrySet()) {
            if (!ele.getValue().equals("1")) {
                dto.setResponse("El campo ingresado : " + " " + ele.getKey() + " / el resultado es : " + " " + ele.getValue());
                dto.setResult(false);
                break;
            }
        }
        return dto;
    }

    private  Map<String, String>  getResponseText(Player player) {
        Map<String, String> list = new HashMap<>();
        list.put(player.getName(), player.getName() != null || !Pattern.matches(REGEX_NAMES, player.getName()) ? "1" : "El Nombre es incorrecto o invalido");
        list.put(player.getUsername(), player.getUsername() != null || !Pattern.matches(REGEX_EMAIL, player.getUsername()) ? "1" : "El Email es incorrecto o invalido");

        return list;
    }

    public boolean validateLogin(Player response, LoginDTO loginDTO) {
        String encryptedPassword = bCryptPasswordEncoder.encode(loginDTO.getPassword());
        return response.getPassword().equals(encryptedPassword);
    }

}
