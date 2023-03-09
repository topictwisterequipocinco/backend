package com.quarke5.ttplayer.dto.response;

import com.quarke5.ttplayer.model.enums.State;
import com.quarke5.ttplayer.model.enums.TypeModality;
import com.quarke5.ttplayer.model.enums.TypePosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseJobOfferDto {
    private Long id;
    private String title;
    private String description;
    private String area;
    private String body;
    private String experience;
    private TypeModality modality;
    private TypePosition position;
    private String category;
    private String datePublished;
    private String modifiedDay;
    private String deletedDay;
    private boolean deleted;
    private State state;
    private String message;
}
