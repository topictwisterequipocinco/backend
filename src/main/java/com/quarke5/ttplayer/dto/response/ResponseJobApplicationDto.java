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
public class ResponseJobApplicationDto {
    private Long jobOfferApplicantID;
    private String applied;
    private String deletedDay;
    private boolean jobAppdeleted;
    private Long applicantID;
    private String name;
    private String surname;
    private String dni;
    private String email;
    private String phoneNumber;
    private String typeStudent;
    private Long jobOfferID;
    private String title;
    private String description;
    private String area;
    private String body;
    private String experience;
    private TypeModality modality;
    private TypePosition position;
    private String category;
    private String categoryDescription;
    private String datePublished;
    private String modifiedDay;
    private String jobOfferDeletedDay;
    private boolean jobOfferDeleted;
    private State state;
}
