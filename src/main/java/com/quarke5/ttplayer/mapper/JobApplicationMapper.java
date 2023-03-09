package com.quarke5.ttplayer.mapper;

import com.quarke5.ttplayer.dto.response.ResponseJobApplicationDto;
import com.quarke5.ttplayer.model.JobApplication;
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
            res.setApplied(LocalDate.parse(job.getApplied()));
            res.setDeletedDay(LocalDate.parse(job.getDeletedDay()));
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
            res.setDatePublished(LocalDate.parse(job.getJobOffer().getCreateDay()));
            res.setModifiedDay(LocalDate.parse(job.getJobOffer().getModifiedDay()));
            res.setJobOfferDeletedDay(LocalDate.parse(job.getJobOffer().getDeletedDay()));
            res.setJobOfferDeleted(job.getJobOffer().isDeleted());
            res.setState(job.getJobOffer().getState());
            list.add(res);
        }
        return list;
    }

}
