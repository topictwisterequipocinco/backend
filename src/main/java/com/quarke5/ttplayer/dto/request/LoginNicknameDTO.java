package com.quarke5.ttplayer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class LoginNicknameDTO {
    @NotNull(message = "Nombre no puede estar vacìo.")
    @Size(min = 2, max = 15, message = "El Nombre debe tener un tamaño entre 2 a 15 caracteres.")
    private String nickname;

    private String id;
    private String wins;

}
