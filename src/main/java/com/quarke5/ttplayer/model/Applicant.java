package com.quarke5.ttplayer.model;

import com.quarke5.ttplayer.model.enums.Genre;
import com.quarke5.ttplayer.model.enums.TypeStudent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Entity(name = "APPLICANT")
@PrimaryKeyJoinColumn(referencedColumnName = "id")
public class Applicant extends Person{

    @Column
    @Enumerated(value = EnumType.STRING)
    private Genre genre;

    @NotNull(message = "El campo fecha de nacimiento es obligatorio")
    @Column
    private LocalDate birthDate;

    @Column
    @Enumerated(value = EnumType.STRING)
    private TypeStudent typeStudent;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "applicantId")
    private List<JobApplication> jobApplications;

}

