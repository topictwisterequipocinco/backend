package com.quarke5.ttplayer.dto.request;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@Data
@Getter
@Setter
@NoArgsConstructor
public class CategoryDTO {

    private Long id;

    @NotNull(message = "Nombre no puede estar vacìo.")
    @Size(min = 2, max = 64, message = "El Nombre debe tener un tamaño entre 4 a 64 caracteres.")
    private String name;

    @NotNull(message = "Apellido no puede estar vacìo")
    @Size(min = 2, max = 100, message = "El Apellido debe tener un tamaño entre 4 a 64 caracteres.")
    private String description;
}