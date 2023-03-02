package com.quarke5.ttplayer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseJobOfferFlutterDto {
    private Long id;
    private String title;
    private String description;
    private String area;
    private String body;
    private String experience;
    private String modality;
    private String position;
    private String category;
    private String datePublished;
    private String modifiedDay;
    private String deletedDay;
    private boolean deleted;
    private String state;
    private String message;
}
