package com.quarke5.ttplayer.service.interfaces;

import com.quarke5.ttplayer.security.authentication.AuthenticationRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<?> createJwt(AuthenticationRequest authenticationRequest) throws Exception;
}
