package com.quark.equipocinco.topictwisterbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class LoginDTO {
    @NotNull(message = "Email no puede estar vacìo")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"
            , message = "Email invalido")
    @Email
    private String email;

    @NotNull(message = "Contraseña no puede estar vacìo")
    @Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$",
            message = "Password invalido. Debe tener al menos una letra mayuscula, una letra minuscula," +
                    "un numero, un caracter especial y tener una longitud minima de 8 caracteres")
    private String password;
}
