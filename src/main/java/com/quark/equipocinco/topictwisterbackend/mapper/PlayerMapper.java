package com.quark.equipocinco.topictwisterbackend.mapper;

import com.quark.equipocinco.topictwisterbackend.dto.request.PlayerDTO;
import com.quark.equipocinco.topictwisterbackend.dto.response.PlayerResponseDTO;
import com.quark.equipocinco.topictwisterbackend.model.Player;
import com.quark.equipocinco.topictwisterbackend.util.logger.GenerateHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {

    private static int NUMBERS_VICTORY_INIT = 0;

    @Autowired BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired GenerateHash generateHash;

    public Player toModel(PlayerDTO dto) {
        Long pass = generateHash.generateAleatorio();
        Player player = Player.builder()
                .name(dto.getName())
                .surname(dto.getSurname())
                .username(dto.getEmail())
                .password(createEncryptedPassword(dto.getPassword()))
                .verificationCode(String.valueOf(pass))
                .connected(false)
                .numberOfVictories(NUMBERS_VICTORY_INIT)
                .build();
        return player;
    }

    public PlayerResponseDTO toResponsePlayer(PlayerResponseDTO player, String message) {
        player.setMessage(message);
        return player;
    }

    public PlayerResponseDTO toUpdatePlayerResponseDTO(PlayerResponseDTO oldPlayer, PlayerDTO entity) {
        oldPlayer.setName(entity.getName());
        oldPlayer.setSurname(entity.getSurname());
        oldPlayer.setUsername(entity.getEmail());
        oldPlayer.setPassword(createEncryptedPassword(entity.getPassword()));
        return oldPlayer;
    }

    public String createEncryptedPassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    public PlayerResponseDTO toUpdateResponseDTO(PlayerDTO entity) {
        PlayerResponseDTO dto = new PlayerResponseDTO();
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setUsername(entity.getEmail());
        dto.setPassword(createEncryptedPassword(entity.getPassword()));
        return dto;
    }

}
