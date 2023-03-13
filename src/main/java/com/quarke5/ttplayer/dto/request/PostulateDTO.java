package com.quarke5.ttplayer.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor
@Data
@Getter
@Setter
@NoArgsConstructor
public class PostulateDTO {
    private String applicantID;
    private String jobofferID;
}
