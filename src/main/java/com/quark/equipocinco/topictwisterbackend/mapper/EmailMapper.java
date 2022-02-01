package com.quark.equipocinco.topictwisterbackend.mapper;

import com.quark.equipocinco.topictwisterbackend.dto.response.ResponseEmailDto;
import com.quark.equipocinco.topictwisterbackend.model.Player;
import org.springframework.stereotype.Component;

@Component
public class EmailMapper {
    public ResponseEmailDto toModelEmailCreate(Player player, String path, String welcome) {
        String url = ""+path+"/"+player.getUsername()+"/"+player.getVerificationCode()+"";

        ResponseEmailDto res = ResponseEmailDto.builder()
                .names(player.getName() + " " + player.getSurname())
                .email(player.getUsername())
                .message(welcome)
                .url(url)
                .build();
        return res;
    }
}
