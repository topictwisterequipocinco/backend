package com.quark.equipocinco.topictwisterbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Player {
    private String name;
    private String username;
    private String password;
    private String verificationCode;
    private boolean activated;
    private int wins;
}
