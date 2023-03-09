package com.quarke5.ttplayer.model;

import com.quarke5.ttplayer.model.enums.State;
import com.quarke5.ttplayer.model.enums.TypeModality;
import com.quarke5.ttplayer.model.enums.TypePosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "JOBOFFER")
@SQLDelete(sql = "UPDATE JOBOFFER SET deleted=true WHERE id = ?")
@Where(clause = "deleted = false")
public class JobOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private String id;

    @NotNull(message = "El campo Tìtulo es obligatorio")
    @Column
    private String title;

    @NotNull(message = "El campo Descripciòn es obligatorio")
    @Column
    private String description;

    @NotNull(message = "El campo Area es obligatorio")
    @Column
    private String area;

    @NotNull(message = "El campo Cuerpo del Aviso es obligatorio")
    @Column
    private String body;

    @NotNull(message = "El campo Años de Experiencia del Aviso es obligatorio")
    @Column
    private String experience;

    @NotNull(message = "El campo Modalidad de trabajo es obligatorio")
    @Enumerated(value = EnumType.STRING)
    @Column
    private TypeModality modality;

    @NotNull(message = "El campo Tipo de Posiciòn es obligatorio")
    @Enumerated(value = EnumType.STRING)
    @Column
    private TypePosition position;

    @Column
    private String createDay;

    @Column
    private String modifiedDay;

    @Column
    private String deletedDay;

    @Enumerated(value = EnumType.STRING)
    @Column
    private State state;

    @Column
    private boolean deleted;

    @ManyToOne
    @JoinColumn(name="publisherId")
    private Publisher publisher;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "jobofferId")
    private List<JobApplication> jobApplications;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "categoryId")
    private Category category;

}
