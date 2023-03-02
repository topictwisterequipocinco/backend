package com.quarke5.ttplayer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseCategoryDto {
    private Long id;
    private String name;
    private String description;
    private LocalDate createDay;
    private LocalDate modifiedDay;
    private LocalDate deletedDay;
    private boolean deleted;
    private String message;
}
