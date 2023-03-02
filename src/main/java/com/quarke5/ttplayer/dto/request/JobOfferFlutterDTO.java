package com.quarke5.ttplayer.dto.request;

import com.quarke5.ttplayer.model.enums.TypeModality;
import com.quarke5.ttplayer.model.enums.TypePosition;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@AllArgsConstructor
@Data
@Getter
@Setter
@NoArgsConstructor
public class JobOfferFlutterDTO {

    private String id;

    @NotNull(message = "Tìtulo no puede estar vacìo.")
    @Size(min = 4, max = 64, message = "El Tìtulo debe tener un tamaño entre 4 a 64 caracteres.")
    private String title;

    @NotNull(message = "El campo Descripciòn no puede estar vacìo.")
    @Size(min = 4, max = 100, message = "Descripciòn debe tener un tamaño entre 4 a 100 caracteres.")
    private String description;

    @NotNull(message = "El campo Area no puede estar vacìo.")
    @Size(min = 4, max = 50, message = "Àrea debe tener un tamaño entre 4 a 50 caracteres.")
    private String area;

    @NotNull(message = "El campo Cuerpo del Aviso no puede estar vacìo.")
    @Size(min = 4, max = 300, message = "El Cuerpo del aviso debe tener un tamaño entre 4 a 300 caracteres.")
    private String body;

    @NotNull(message = "El campo Años de Experiencia del Aviso no puede estar vacìo.")
    @Size(min = 1, max = 2, message = "Experiencia debe tener un tamaño entre 1 a 2 caracteres.")
    private String experience;

    @NotNull(message = "El campo Modalidad de trabajo no puede estar vacìo.")
    private TypeModality modality;

    @NotNull(message = "El campo Tipo de Posiciòn no puede estar vacìo.")
    private TypePosition position;

    @NotNull(message = "El campo Categoria es obligatorio.")
    @Size(min = 2, max = 30, message = "La Categoria debe tener un tamaño entre 4 a 30 caracteres.")
    private String category;
}
