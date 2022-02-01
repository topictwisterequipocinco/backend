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
    private String surname;
    private String username;
    private String password;
    private String verificationCode;
    private boolean connected;
    private boolean activated;
    private boolean deleted;
    private int numberOfVictories;
    private int[] score;
    private List<String> words;
}
