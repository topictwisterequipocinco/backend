package com.quarke5.ttplayer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseJobApplicationFlutterDto {
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
    private String modality;
    private String position;
    private String category;
    private String categoryDescription;
    private String datePublished;
    private String modifiedDay;
    private String jobOfferDeletedDay;
    private boolean jobOfferDeleted;
    private String state;
    private String message;
}
