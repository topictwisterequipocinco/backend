package com.quarke5.ttplayer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseUserDto {
    private Long id;
    private String username;
    private String role;
    private String message;
    private boolean conected;
}
