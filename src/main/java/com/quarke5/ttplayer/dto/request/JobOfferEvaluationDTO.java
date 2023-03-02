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
public class JobOfferEvaluationDTO {
    @NotNull(message = "El ID no puede estar vacìo.")
    private Long id;

    @NotNull(message = "Decision no puede estar vacìo.")
    @Size(min = 4, max = 64, message = "El Decision debe tener un tamaño entre 4 a 64 caracteres.")
    private String decision;
}
