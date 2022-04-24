package com.quarke5.ttplayer.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseEmailDTO {
    private Long id;
    private String name;
    private String email;
    private String message;
    private String url;
    private String bodyText;
    private String subject;
}
