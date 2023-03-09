package com.quarke5.ttplayer.mapper;

import com.quarke5.ttplayer.dto.response.ResponseUserDto;
import com.quarke5.ttplayer.exception.PersonException;
import com.quarke5.ttplayer.model.Role;
import com.quarke5.ttplayer.model.User;
import com.quarke5.ttplayer.model.enums.State;
import com.quarke5.ttplayer.security.authentication.AuthenticationResponse;
import com.quarke5.ttplayer.util.GenerateHash;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final GenerateHash generateHash;

    public UserMapper(GenerateHash generateHash) {
        this.generateHash = generateHash;
    }

    public User toModel(String email, Role role, String encode, int id) throws PersonException {
        Long pass = generateHash.generateAleatorio();
        User user = User.builder()
                .id(String.valueOf(id))
                .username(email)
                .password(encode)
                .role(role)
                .state(State.ACTIVE)
                .verificationCode(String.valueOf(pass))
                .conected(true)
                .build();
        return user;
    }

    public ResponseUserDto toUserResponseDto(User user, String message) {
        ResponseUserDto us = ResponseUserDto.builder()
                .id(Long.valueOf(user.getId()))
                .username(user.getUsername())
                .role(user.getRole().getRole().toString())
                .message(message)
                .conected(user.isConected())
                .build();
        return us;
    }

    public AuthenticationResponse responseLoginUserJason(User user, String jwt) {
        AuthenticationResponse auth = new AuthenticationResponse(jwt);
        auth.setUsername(user.getUsername());
        auth.setId(Long.valueOf(user.getId()));
        auth.setRole(user.getRole());
        return auth;
    }

    public User update(User user, String email, String encodePassword) {
        user.setUsername(email);
        user.setPassword(encodePassword);
        return user;
    }
}
