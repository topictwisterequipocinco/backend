package com.quarke5.ttplayer.model;

import com.quarke5.ttplayer.model.enums.Genre;
import com.quarke5.ttplayer.model.enums.TypeStudent;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;


@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "APPLICANT")
@SQLDelete(sql = "UPDATE PERSON SET deleted=true WHERE id = ?")
@Where(clause = "deleted = false")
public class Applicant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private String id;

    @NotNull(message = "El campo nombre es obligatorio")
    @Column
    private String oficialName;

    @NotNull(message = "El campo apellido es obligatorio")
    @Column
    private String lastName;

    @NotNull(message = "El campo DNI es obligatorio")
    @Column
    private String identification;

    @NotNull(message = "El campo numero de telefono es obligatorio")
    @Column
    private String phoneNumber;

    @Column
    private boolean deleted;

    @Column
    @Enumerated(value = EnumType.STRING)
    private Genre genre;

    @NotNull(message = "El campo fecha de nacimiento es obligatorio")
    @Column
    private String birthDate;

    @Column
    @Enumerated(value = EnumType.STRING)
    private TypeStudent typeStudent;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "userId")
    private User user;

}

