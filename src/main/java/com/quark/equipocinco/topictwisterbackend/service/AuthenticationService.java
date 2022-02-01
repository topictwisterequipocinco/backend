package com.quark.equipocinco.topictwisterbackend.service;

import com.quark.equipocinco.topictwisterbackend.dto.request.LoginDTO;
import com.quark.equipocinco.topictwisterbackend.dto.response.PlayerResponseDTO;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<PlayerResponseDTO> loginPlayer(LoginDTO loginDTO);

    ResponseEntity<?> logoutPlayer(LoginDTO loginDTO);

    ResponseEntity<?> activateAccount(String username, String hash);
}
