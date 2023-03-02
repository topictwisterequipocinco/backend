package com.quarke5.ttplayer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Entity(name = "PUBLISHER")
@PrimaryKeyJoinColumn(referencedColumnName = "id")
public class Publisher extends Person{

    @Column
    private String webPage;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "publisherId")
    private List<JobOffer> jobOfferList;

}

