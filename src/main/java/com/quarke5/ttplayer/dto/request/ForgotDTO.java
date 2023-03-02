package com.quarke5.ttplayer.dto.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Builder
@AllArgsConstructor
@Data
@Getter
@Setter
@NoArgsConstructor
public class ForgotDTO {
    @NotNull(message = "Email no puede estar vacìo")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"
            , message = "Invalid email")
    @Email
    private String username;

    @NotNull(message = "Contraseña no puede estar vacìo")
    @Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$",
            message = "Invalid firstPassword. Must have one upper case letter, one lower case letter," +
                    "one number, one special character and at least 8 characters long")
    private String firstPassword;

    @NotNull(message = "Contraseña no puede estar vacìo")
    @Pattern(regexp = "(?=^.{8,}$)((?=.*\\d)(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$",
            message = "Invalid secondPassword. Must have one upper case letter, one lower case letter," +
                    "one number, one special character and at least 8 characters long")
    private String secondPassword;

}
