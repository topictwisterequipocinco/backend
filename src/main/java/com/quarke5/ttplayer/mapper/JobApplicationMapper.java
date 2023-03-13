package com.quarke5.ttplayer.mapper;

import com.quarke5.ttplayer.dto.response.ResponseJobApplicationDto;
import com.quarke5.ttplayer.dto.response.ResponseJobApplicationFlutterDto;
import com.quarke5.ttplayer.model.Applicant;
import com.quarke5.ttplayer.model.JobApplication;
import com.quarke5.ttplayer.model.JobOffer;
import com.quarke5.ttplayer.model.enums.State;
import com.quarke5.ttplayer.model.enums.TypeModality;
import com.quarke5.ttplayer.model.enums.TypePosition;
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
            res.setApplied(job.getApplied() != null ? job.getApplied() : "");
            res.setDeletedDay(job.getDeletedDay() != null ? job.getDeletedDay() : "");
            res.setJobAppdeleted(job.isDeleted());
            res.setApplicantID(job.getApplicant().getId() != null ? Long.valueOf(job.getApplicant().getId()) : 0L);
            res.setName(job.getApplicant().getOficialName() != null ? job.getApplicant().getOficialName() : "");
            res.setSurname(job.getApplicant().getLastName() != null ? job.getApplicant().getLastName() : "");
            res.setDni(job.getApplicant().getIdentification() != null ? job.getApplicant().getIdentification() : "");
            res.setEmail(job.getApplicant().getUser().getUsername() != null ? job.getApplicant().getUser().getUsername() : "");
            res.setPhoneNumber(job.getApplicant().getPhoneNumber() != null ? job.getApplicant().getPhoneNumber() : "");
            res.setTypeStudent(job.getApplicant().getTypeStudent() != null ? String.valueOf(job.getApplicant().getTypeStudent()) : "");
            res.setJobOfferID(job.getJobOffer().getId() != null ? Long.valueOf(job.getJobOffer().getId()) : 0L);
            res.setTitle(job.getJobOffer().getTitle() != null ? job.getJobOffer().getTitle() : "");
            res.setDescription(job.getJobOffer().getDescription() != null ? job.getJobOffer().getDescription() : "");
            res.setArea(job.getJobOffer().getArea() != null ? job.getJobOffer().getArea() : "");
            res.setBody(job.getJobOffer().getBody() != null ? job.getJobOffer().getBody() : "");
            res.setExperience(job.getJobOffer().getExperience() != null ? job.getJobOffer().getExperience() : "");
            res.setModality(job.getJobOffer().getModality() != null ? job.getJobOffer().getModality() : TypeModality.valueOf(""));
            res.setPosition(job.getJobOffer().getPosition() != null ? job.getJobOffer().getPosition() : TypePosition.valueOf(""));
            res.setCategory(job.getJobOffer().getCategory().getName() != null ? job.getJobOffer().getCategory().getName() : "");
            res.setCategoryDescription(job.getJobOffer().getCategory().getDescription() != null ? job.getJobOffer().getCategory().getDescription() : "");
            res.setDatePublished(job.getJobOffer().getCreateDay() != null ? job.getJobOffer().getCreateDay() : "");
            res.setModifiedDay(job.getJobOffer().getModifiedDay() != null ? job.getJobOffer().getModifiedDay() : "");
            res.setJobOfferDeletedDay(job.getJobOffer().getDeletedDay() != null ? job.getJobOffer().getDeletedDay() : "");
            res.setJobOfferDeleted(job.getJobOffer().isDeleted());
            res.setState(job.getJobOffer().getState() != null ? job.getJobOffer().getState() : State.valueOf(""));

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
                .deletedDay("")
                .deleted(false)
                .build();
        return jobApplication;
    }

    public List<ResponseJobApplicationFlutterDto> toResponseJobApplicationFlutter(List<JobApplication> jobApplicationList, String message) {
        List<ResponseJobApplicationFlutterDto> list = new ArrayList<>();
        for(JobApplication job : jobApplicationList){
            ResponseJobApplicationFlutterDto res = new ResponseJobApplicationFlutterDto();
            res.setJobOfferApplicantID(Long.valueOf(job.getId()));
            res.setApplied(job.getApplied() != null ? job.getApplied() : "");
            res.setDeletedDay(job.getDeletedDay() != null ? job.getDeletedDay() : "");
            res.setJobAppdeleted(job.isDeleted());
            res.setApplicantID(job.getApplicant().getId() != null ? Long.valueOf(job.getApplicant().getId()) : 0L);
            res.setName(job.getApplicant().getOficialName() != null ? job.getApplicant().getOficialName() : "");
            res.setSurname(job.getApplicant().getLastName() != null ? job.getApplicant().getLastName() : "");
            res.setDni(job.getApplicant().getIdentification() != null ? job.getApplicant().getIdentification() : "");
            res.setEmail(job.getApplicant().getUser().getUsername() != null ? job.getApplicant().getUser().getUsername() : "");
            res.setPhoneNumber(job.getApplicant().getPhoneNumber() != null ? job.getApplicant().getPhoneNumber() : "");
            res.setTypeStudent(job.getApplicant().getTypeStudent() != null ? String.valueOf(job.getApplicant().getTypeStudent()) : "");
            res.setJobOfferID(job.getJobOffer().getId() != null ? Long.valueOf(job.getJobOffer().getId()) : 0L);
            res.setTitle(job.getJobOffer().getTitle() != null ? job.getJobOffer().getTitle() : "");
            res.setDescription(job.getJobOffer().getDescription() != null ? job.getJobOffer().getDescription() : "");
            res.setArea(job.getJobOffer().getArea() != null ? job.getJobOffer().getArea() : "");
            res.setBody(job.getJobOffer().getBody() != null ? job.getJobOffer().getBody() : "");
            res.setExperience(job.getJobOffer().getExperience() != null ? job.getJobOffer().getExperience() : "");
            res.setModality(job.getJobOffer().getModality() != null ? job.getJobOffer().getModality().name() : "");
            res.setPosition(job.getJobOffer().getPosition() != null ? job.getJobOffer().getPosition().name() : "");
            res.setCategory(job.getJobOffer().getCategory().getName() != null ? job.getJobOffer().getCategory().getName() : "");
            res.setCategoryDescription(job.getJobOffer().getCategory().getDescription() != null ? job.getJobOffer().getCategory().getDescription() : "");
            res.setDatePublished(job.getJobOffer().getCreateDay() != null ? job.getJobOffer().getCreateDay() : "");
            res.setModifiedDay(job.getJobOffer().getModifiedDay() != null ? job.getJobOffer().getModifiedDay() : "");
            res.setJobOfferDeletedDay(job.getJobOffer().getDeletedDay() != null ? job.getJobOffer().getDeletedDay() : "");
            res.setJobOfferDeleted(job.getJobOffer().isDeleted());
            res.setState(job.getJobOffer().getState() != null ? job.getJobOffer().getState().name() : "");
            res.setMessage(message != null ? message : "");
            list.add(res);
        }
        return list;
    }
}
