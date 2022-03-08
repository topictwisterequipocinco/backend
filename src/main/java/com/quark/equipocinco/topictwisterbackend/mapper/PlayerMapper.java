package com.quark.equipocinco.topictwisterbackend.mapper;

import com.quark.equipocinco.topictwisterbackend.dto.request.PlayerDTO;
import com.quark.equipocinco.topictwisterbackend.dto.response.PlayerResponseDTO;
import com.quark.equipocinco.topictwisterbackend.model.Player;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PlayerMapper {

    private static int NUMBERS_VICTORY_INIT = 0;
    private static int SUM_ONE = 1;

    public Player toModel(PlayerDTO dto, int id) {
        return Player.builder()
                .id(String.valueOf(id))
                .name(dto.getName())
                .username(dto.getEmail())
                .password(dto.getPassword())
                .wins(NUMBERS_VICTORY_INIT)
                .build();
    }

    public Player toUpdateResponseDTO(Player entity) {
        entity.setWins(entity.getWins() + SUM_ONE);
        return entity;
    }

    public PlayerResponseDTO responsePlayerDtoToPlayer(Player response) {
        return PlayerResponseDTO.builder()
                .name(response.getName())
                .id(response.getId())
                .wins(response.getWins())
                .build();
    }

    public PlayerResponseDTO toResponsePlayerResponseDTO(Player player) {
        return PlayerResponseDTO.builder()
                .name(player.getName())
                .id(player.getId())
                .wins(player.getWins())
                .build();
    }

    public List<PlayerResponseDTO> toResponsePlayerList(List<Player> list) {
        List<PlayerResponseDTO> dtoList = new ArrayList<>();
        for (Player player : list) {
            PlayerResponseDTO dto = responsePlayerDtoToPlayer(player);
            dtoList.add(dto);
        }
        return dtoList;
    }
}
