package com.quarke5.ttplayer.mapper;

import com.quarke5.ttplayer.dto.response.ResponseEmailDTO;
import com.quarke5.ttplayer.model.Player;
import org.springframework.stereotype.Component;

@Component
public class EmailMapper {
    private static final String EMAIL_WELCOME = "Bienvenido a Topic Twister Game";
    private static final String BODY_TEXT = "ya es miembro de la comunidad gamers de Quarks. Felicitaciones!!!!! " + " Puede loguerse y comenzar a jugar.";
    private static final String SUBJECT = "Bienvenido a Topic Twister Game - @QUARKS 2022";

    public ResponseEmailDTO toModelEmailCreate(Player player) {
        String bodyText = "NickName :" + " " + player.getName() + " " + BODY_TEXT;
        ResponseEmailDTO dto = ResponseEmailDTO.builder()
                .name(player.getName())
                .email(player.getUsername())
                .message(EMAIL_WELCOME)
                .url("")
                .bodyText(bodyText)
                .subject(SUBJECT)
                .build();
        return dto;
    }
}
