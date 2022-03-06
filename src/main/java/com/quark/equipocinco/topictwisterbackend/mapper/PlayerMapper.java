package com.quark.equipocinco.topictwisterbackend.mapper;

import com.quark.equipocinco.topictwisterbackend.dto.request.PlayerDTO;
import com.quark.equipocinco.topictwisterbackend.dto.response.PlayerResponseDTO;
import com.quark.equipocinco.topictwisterbackend.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PlayerMapper {

    private static int NUMBERS_VICTORY_INIT = 0;

    @Autowired BCryptPasswordEncoder bCryptPasswordEncoder;

    public Player toModel(PlayerDTO dto, int id) {
        Player player = Player.builder()
                .id(String.valueOf(id))
                .name(dto.getName())
                .username(dto.getEmail())
                .password(createEncryptedPassword(dto.getPassword()))
                .wins(NUMBERS_VICTORY_INIT)
                .build();
        return player;
    }

    public String createEncryptedPassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    public Player toUpdateResponseDTO(Player entity) {
        entity.setWins(entity.getWins()+1);
        return entity;
    }

    public PlayerResponseDTO responsePlayerDtoToPlayer(Player response) {
        PlayerResponseDTO dto = PlayerResponseDTO.builder()
                .name(response.getName())
                .id(response.getId())
                .wins(response.getWins())
                .build();
        return dto;
    }

    public PlayerResponseDTO toResponsePlayerResponseDTO(Player player) {
        PlayerResponseDTO dto = PlayerResponseDTO.builder()
                .name(player.getName())
                .id(player.getId())
                .wins(player.getWins())
                .build();
        return dto;
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
