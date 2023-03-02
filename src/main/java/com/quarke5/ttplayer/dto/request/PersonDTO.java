package com.quarke5.ttplayer.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Data
@Getter
@Setter
@NoArgsConstructor
public class PersonDTO {

    private Long id;

    @NotNull(message = "Nombre no puede estar vacìo.")
    @Size(min = 4, max = 64, message = "El Nombre debe tener un tamaño entre 4 a 64 caracteres.")
    private String name;

    @NotNull(message = "Apellido no puede estar vacìo")
    @Size(min = 4, max = 64, message = "El Apellido debe tener un tamaño entre 4 a 64 caracteres.")
    private String surname;

    @NotNull(message = "El campo DNI es obligatorio")
    @Size(min = 7, max = 12, message = "DNI debe tener un tamaño entre 7 a 15 caracteres.")
    private String identification;

    @NotNull(message = "Email no puede estar vacìo")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"
            , message = "Invalid email")
    @Email
    private String email;

    @NotNull(message = "Contraseña no puede estar vacìo")
    @Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$",
            message = "Invalid password. Must have one upper case letter, one lower case letter," +
                    "one number, one special character and at least 8 characters long")
    private String password;

    @NotNull(message = "Telèfono no puede estar vacìo")
    @Size(min = 8, max = 20, message = "Invalid phone number")
    private String phoneNumber;

    private String role;
    private String genre;
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate birthDate;
    private String typeStudent;
    @URL()
    private String webPage;
}
