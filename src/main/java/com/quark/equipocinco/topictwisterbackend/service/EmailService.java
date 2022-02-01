package com.quark.equipocinco.topictwisterbackend.service;

import com.quark.equipocinco.topictwisterbackend.exception.PlayerException;
import com.quark.equipocinco.topictwisterbackend.model.Player;

public interface EmailService {
    void createEmailPlayer(Player player) throws PlayerException;
}
