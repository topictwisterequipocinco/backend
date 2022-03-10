package com.quark.equipocinco.topictwisterbackend.dto.response;

import lombok.*;

@Builder
@AllArgsConstructor
@Data
@Getter
@Setter
@NoArgsConstructor
public class PlayerResponseDTO {
    private int id;
    private String name;
    private int wins;
}
