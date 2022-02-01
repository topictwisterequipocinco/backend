package com.quark.equipocinco.topictwisterbackend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseEmailDto {
    private String names;
    private String surname;
    private String email;
    private String message;
    private String url;
}
