package com.quarke5.ttplayer.validator.response;

import lombok.*;

@Builder
@AllArgsConstructor
@Data
@Getter
@Setter
@NoArgsConstructor
public class ResponseValidator {
    private boolean result;
    private String response;
}
