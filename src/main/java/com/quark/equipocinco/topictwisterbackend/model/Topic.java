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
public class Topic {
    private String name;
    private int[] letterCount;
    private List<String> wordList;
}
