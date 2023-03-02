package com.quarke5.ttplayer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class UserByFlutterDTO {
    private final String jwt;
    private Long id;
    private String name;
    private String lastName;
    private String identification;
    private String phone;
    private String username;
    private String password;
    private String role;
    private String genre;
    private String birthDate;
    private String typeStudent;
    private String webPage;
    private boolean conected;
}