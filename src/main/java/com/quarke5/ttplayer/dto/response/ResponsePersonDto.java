package com.quarke5.ttplayer.dto.response;

import com.quarke5.ttplayer.model.enums.Genre;
import com.quarke5.ttplayer.model.enums.TypeStudent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ResponsePersonDto {
    private Long id;
    private URI uri;
    private String name;
    private String surname;
    private String identification;
    private String phoneNumber;
    private String email;
    private String role;
    private Genre genre;
    private LocalDate birthDate;
    private TypeStudent typeStudent;
    private String webPage;
    private String message;
}
