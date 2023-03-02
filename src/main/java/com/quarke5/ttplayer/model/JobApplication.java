package com.quarke5.ttplayer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "JOBAPPLICATION")
@SQLDelete(sql = "UPDATE JOBAPPLICATION SET deleted=true WHERE id = ?")
@Where(clause = "deleted = false")
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private LocalDate applied;

    @Column
    private LocalDate deletedDay;

    @Column
    private boolean deleted;

    @ManyToOne
    @JoinColumn(name="jobofferId")
    private JobOffer jobOffer;

    @ManyToOne
    @JoinColumn(name="applicantId")
    private Applicant applicant;

}
