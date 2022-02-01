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
    @Size(min = 4, max = 64, message = "El Nombre debe tener un tamaño entre 4 a 64 caracteres.")
    private String name;

    @NotNull(message = "Apellido no puede estar vacìo")
    @Size(min = 4, max = 64, message = "El Apellido debe tener un tamaño entre 4 a 64 caracteres.")
    private String surname;

    @NotNull(message = "Email no puede estar vacìo")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"
            , message = "Email invalido. Debe contar con al menos el @ y luego a que empresa o servicio pertenece," +
            " por ejemplo google.com o yahoo.com.ar")
    @Email
    private String email;

    @NotNull(message = "Contraseña no puede estar vacìo")
    @Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$",
            message = "Password invalido. Debe tener al menos una letra mayuscula, una letra minuscula," +
                    "un numero, un caracter especial y tener una longitud minima de 8 caracteres")
    private String password;
}
