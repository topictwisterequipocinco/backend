package com.quark.equipocinco.topictwisterbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class PlayerDTO {
    @NotNull(message = "Nombre no puede estar vacìo.")
    @Size(min = 2, max = 15, message = "El Nombre debe tener un tamaño entre 2 a 15 caracteres.")
    private String name;

    @NotNull(message = "Email no puede estar vacìo")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"
            , message = "Email invalido. Debe contar con al menos el @ y luego a que empresa o servicio pertenece," +
            " por ejemplo google.com o yahoo.com.ar")
    @Email
    private String email;

    @NotNull(message = "Contraseña no puede estar vacìo")
    private String password;
}