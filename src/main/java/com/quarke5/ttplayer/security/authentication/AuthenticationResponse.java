package com.quarke5.ttplayer.security.authentication;

import com.quarke5.ttplayer.model.Role;

import java.io.Serializable;

public class AuthenticationResponse implements Serializable {

    private final String jwt;
    private Long id;
    private String username;
    private Role role;

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}