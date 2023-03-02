package com.quarke5.ttplayer.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseEmailUtnDTO {
    private String names;
    private String identification;
    private String email;
    private String phone;
    private String message;
    private String url;
}