package com.quark.equipocinco.topictwisterbackend.dto.response;

import lombok.*;

@AllArgsConstructor
@Data
@Getter
@Setter
@NoArgsConstructor
public class PlayerResponseDTO {
    private String id;
    private String name;
    private int wins;
}
