package com.quark.equipocinco.topictwisterbackend.validator;

import com.quark.equipocinco.topictwisterbackend.dto.request.LoginDTO;
import com.quark.equipocinco.topictwisterbackend.dto.response.PlayerResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthValidate {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    public boolean validateLogin(PlayerResponseDTO response, LoginDTO loginDTO) {
        String encryptedPassword = bCryptPasswordEncoder.encode(loginDTO.getPassword());
        return response.getPassword().equals(encryptedPassword);
    }
}
