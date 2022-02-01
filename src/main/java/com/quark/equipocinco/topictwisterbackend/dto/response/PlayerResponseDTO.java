package com.quark.equipocinco.topictwisterbackend.dto.response;

import com.quark.equipocinco.topictwisterbackend.model.Player;
import lombok.*;

@AllArgsConstructor
@Data
@Getter
@Setter
@NoArgsConstructor
public class PlayerResponseDTO extends Player {
    private String id;
    private String message;
}
