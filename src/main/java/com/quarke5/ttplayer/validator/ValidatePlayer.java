package com.quarke5.ttplayer.validator;

import com.quarke5.ttplayer.dto.request.LoginDTO;
import com.quarke5.ttplayer.dto.request.LoginNicknameDTO;
import com.quarke5.ttplayer.validator.response.ResponseValidator;
import com.quarke5.ttplayer.exception.PlayerException;
import com.quarke5.ttplayer.model.Player;
import com.quarke5.ttplayer.mapper.PlayerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class ValidatePlayer extends AbstractValidator{

    @Autowired
    PlayerMapper playerMapper;

    @Override
    public boolean validPlayer(Player player) throws PlayerException {
        Map<String, String> list = getResponseText(player);
        ResponseValidator dto = getResultValidate(list);
        if(!dto.isResult()){throw new PlayerException(dto.getResponse());}

        return dto.isResult();
    }

    @Override
    public boolean validateLoginNickname(Player responseDTO, LoginNicknameDTO loginNicknameDTO) {
        return responseDTO.getName().equals(loginNicknameDTO.getNickname());
    }


    private ResponseValidator getResultValidate(Map<String, String> list) {
        ResponseValidator dto = new ResponseValidator();
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
        return response.getPassword().equals(loginDTO.getPassword());
    }

}
