package com.quarke5.ttplayer.model;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "PUBLISHER")
@SQLDelete(sql = "UPDATE PERSON SET deleted=true WHERE id = ?")
@Where(clause = "deleted = false")
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private String id;

    @NotNull(message = "El campo nombre es obligatorio")
    @Column
    private String oficialName;

    @NotNull(message = "El campo apellido es obligatorio")
    @Column
    private String lastName;

    @NotNull(message = "El campo DNI es obligatorio")
    @Column
    private String identification;

    @NotNull(message = "El campo numero de telefono es obligatorio")
    @Column
    private String phoneNumber;

    @Column
    private boolean deleted;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "userId")
    private User user;

    @Column
    private String webPage;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "publisherId")
    private List<JobOffer> jobOfferList;

}

