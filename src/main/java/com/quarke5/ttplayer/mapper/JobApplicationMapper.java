package com.quarke5.ttplayer.mapper;

import com.quarke5.ttplayer.dto.response.ResponseJobApplicationDto;
import com.quarke5.ttplayer.model.Applicant;
import com.quarke5.ttplayer.model.JobApplication;
import com.quarke5.ttplayer.model.JobOffer;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class JobApplicationMapper {

    public List<ResponseJobApplicationDto> toResponseJobApplication(List<JobApplication> jobApplications) {
        List<ResponseJobApplicationDto> list = new ArrayList<>();
        for(JobApplication job : jobApplications){
            ResponseJobApplicationDto res = new ResponseJobApplicationDto();
            res.setJobOfferApplicantID(Long.valueOf(job.getId()));
            res.setApplied(job.getApplied());
            res.setDeletedDay(job.getDeletedDay());
            res.setJobAppdeleted(job.isDeleted());
            res.setApplicantID(Long.valueOf(job.getApplicant().getId()));
            res.setName(job.getApplicant().getOficialName());
            res.setSurname(job.getApplicant().getLastName());
            res.setDni(job.getApplicant().getIdentification());
            res.setEmail(job.getApplicant().getUser().getUsername());
            res.setPhoneNumber(job.getApplicant().getPhoneNumber());
            res.setTypeStudent(String.valueOf(job.getApplicant().getTypeStudent()));
            res.setJobOfferID(Long.valueOf(job.getJobOffer().getId()));
            res.setTitle(job.getJobOffer().getTitle());
            res.setDescription(job.getJobOffer().getDescription());
            res.setArea(job.getJobOffer().getArea());
            res.setBody(job.getJobOffer().getBody());
            res.setExperience(job.getJobOffer().getExperience());
            res.setModality((job.getJobOffer().getModality()));
            res.setPosition(job.getJobOffer().getPosition());
            res.setCategory(job.getJobOffer().getCategory().getName());
            res.setCategoryDescription(job.getJobOffer().getCategory().getDescription());
            res.setDatePublished(job.getJobOffer().getCreateDay());
            res.setModifiedDay(job.getJobOffer().getModifiedDay());
            res.setJobOfferDeletedDay(job.getJobOffer().getDeletedDay());
            res.setJobOfferDeleted(job.getJobOffer().isDeleted());
            res.setState(job.getJobOffer().getState());
            list.add(res);
        }
        return list;
    }

    public JobApplication toModelJobApplication(Applicant applicant, JobOffer jobOffer, int id) {
        JobApplication jobApplication = JobApplication.builder()
                .id(String.valueOf(id))
                .applicant(applicant)
                .jobOffer(jobOffer)
                .applied(String.valueOf(LocalDate.now()))
                .deleted(false)
                .build();
        return jobApplication;
    }

}
