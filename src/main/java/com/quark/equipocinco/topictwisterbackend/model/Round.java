package com.quark.equipocinco.topictwisterbackend.model;

import com.quark.equipocinco.topictwisterbackend.model.enums.Turn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Round {
    private Character letter;
    private float endTime;
    private Turn currentTurn;
    private List<Topic> topicList;
    private boolean[] _playerAWordsValidation;
    private boolean[] _playerBWordsValidation;
}
