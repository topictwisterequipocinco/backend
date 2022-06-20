package com.quarke5.ttplayer.mapper;

import com.quarke5.ttplayer.dto.request.LoginNicknameDTO;
import com.quarke5.ttplayer.dto.request.PlayerDTO;
import com.quarke5.ttplayer.dto.response.PlayerResponseDTO;
import com.quarke5.ttplayer.model.Player;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PlayerMapper {

    private static int NUMBERS_VICTORY_INIT = 0;
    private static int SUM_ONE = 1;

    public Player toModel(PlayerDTO dto, int id) {
        Player player = Player.builder()
                .id(String.valueOf(id))
                .name(dto.getName())
                .username(dto.getEmail())
                .password(dto.getPassword())
                .wins(NUMBERS_VICTORY_INIT)
                .build();
        return player;
    }

    public Player toUpdateResponseDTO(Player entity) {
        entity.setWins(entity.getWins() + SUM_ONE);
        return entity;
    }

    public PlayerResponseDTO responsePlayerDtoToPlayer(Player response) {
        return PlayerResponseDTO.builder()
                .name(response.getName())
                .id(Integer.parseInt(response.getId()))
                .wins(response.getWins())
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


    public LoginNicknameDTO updateNicknameDto(LoginNicknameDTO loginNicknameDTO, int lastId) {
        loginNicknameDTO.setId(String.valueOf(lastId));
        loginNicknameDTO.setWins("0");
        return loginNicknameDTO;
    }

    public PlayerResponseDTO responsePlayerDtoToLoginNicknameDTO(LoginNicknameDTO dto) {
        return PlayerResponseDTO.builder()
                .name(dto.getNickname())
                .id(Integer.parseInt(dto.getId()))
                .wins(Integer.parseInt(dto.getWins()))
                .build();
    }
}
