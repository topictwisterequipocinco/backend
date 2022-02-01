package com.quark.equipocinco.topictwisterbackend.service;

import com.quark.equipocinco.topictwisterbackend.dto.request.PlayerDTO;
import com.quark.equipocinco.topictwisterbackend.dto.response.PlayerResponseDTO;
import com.quark.equipocinco.topictwisterbackend.model.Player;
import com.quark.equipocinco.topictwisterbackend.service.common.GenericService;

public interface PlayerService extends GenericService<PlayerDTO, PlayerResponseDTO, Player> {
}
