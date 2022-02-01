package com.quark.equipocinco.topictwisterbackend.controller;

import com.quark.equipocinco.topictwisterbackend.dto.request.LoginDTO;
import com.quark.equipocinco.topictwisterbackend.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired AuthenticationService authenticationService;

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) throws Exception {
        return authenticationService.loginPlayer(loginDTO);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LoginDTO loginDTO) throws Exception {
        return authenticationService.logoutPlayer(loginDTO);
    }

    @GetMapping("/activate/{username}/{hash}")
    public ResponseEntity<?> activateNewPlayer(@PathVariable String username, @PathVariable String hash){
        return authenticationService.activateAccount(username, hash);
    }
}
