package com.quarke5.ttplayer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "PARAMETERS")
public class Parameters {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private String id;

    @NotNull(message = "El campo Descripciòn es obligatorio")
    @Column
    private String description;

    @NotNull(message = "El campo Valor a guardar es obligatorio")
    @Column
    private String value;

    @NotNull(message = "El campo Llave es obligatorio")
    @Column
    private String permit;
}
