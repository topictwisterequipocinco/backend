package com.quarke5.ttplayer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Player {
    private String id;
    private String name;
    private String username;
    private String password;
    private int wins;
}
