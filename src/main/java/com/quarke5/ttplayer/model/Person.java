package com.quarke5.ttplayer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "PERSON")
@Inheritance(strategy = InheritanceType.JOINED)
@SQLDelete(sql = "UPDATE PERSON SET deleted=true WHERE id = ?")
@Where(clause = "deleted = false")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

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

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "userId")
    private User user;
}
