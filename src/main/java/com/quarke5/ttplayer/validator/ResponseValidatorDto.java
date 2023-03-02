package com.quarke5.ttplayer.validator;

import lombok.*;

@Builder
@AllArgsConstructor
@Data
@Getter
@Setter
@NoArgsConstructor
public class ResponseValidatorDto {
    private boolean result;
    private String response;
}
