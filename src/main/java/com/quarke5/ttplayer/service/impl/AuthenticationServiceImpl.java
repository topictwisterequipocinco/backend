package com.quarke5.ttplayer.service.impl;

import com.quarke5.ttplayer.mapper.UserMapper;
import com.quarke5.ttplayer.model.User;
import com.quarke5.ttplayer.model.enums.State;
import com.quarke5.ttplayer.repository.impl.UserDAO;
import com.quarke5.ttplayer.security.authentication.AuthenticationRequest;
import com.quarke5.ttplayer.security.utilSecurity.JwtUtilService;
import com.quarke5.ttplayer.service.interfaces.AuthenticationService;
import com.quarke5.ttplayer.util.Errors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtilService jwtTokenUtil;
    @Autowired private UserDetailsServiceImpl userDetailsService;
    @Autowired private MessageSource messageSource;
    @Autowired private UserDAO userRepository;
    @Autowired private UserMapper userMapper;
    @Autowired private Errors errors;

    @Override
    public ResponseEntity<?> createJwt(AuthenticationRequest authenticationRequest) throws Exception {
        User user = userRepository.getEntity(authenticationRequest.getUsername());
        try {
            if (user.getState().equals(State.ACTIVE)){
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
            }
        }catch (BadCredentialsException e) {
            LOGGER.error("Incorrecto usuario y/o contraseña - {0}. Asegurese que su cuente este Activa." + e.getMessage());
            errors.logError("Incorrecto usuario y/o contraseña - {0}. Asegurese que su cuente este Activa." + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("authentication.create.jwt.failed", new Object[] {authenticationRequest.getUsername()}, null));
        }final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        String jwt = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.responseLoginUserJason(user, jwt));
    }

}
