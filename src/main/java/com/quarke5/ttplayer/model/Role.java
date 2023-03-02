package com.quarke5.ttplayer.model;

import com.quarke5.ttplayer.model.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "ROLE")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long roleId;

    @Enumerated(value = EnumType.STRING)
    @Column(unique = true)
    private Roles role;
}

