package com.quark.equipocinco.topictwisterbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Match {
    private int matchID;
    private int initialPlayer;
    private int currentPlayer;
    private int currentRound;
    private String language;
    private boolean isRunning;
    private Round[] rounds;
    private Player playerA;
    private Player playerB;
}
