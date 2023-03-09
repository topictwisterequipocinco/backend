package com.quarke5.ttplayer.service.interfaces;

import com.quarke5.ttplayer.dto.response.ResponseJobApplicationDto;
import com.quarke5.ttplayer.model.Applicant;
import com.quarke5.ttplayer.model.JobApplication;
import com.quarke5.ttplayer.model.JobOffer;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface JobApplicationService {
    List<JobApplication> findJobApplicantByApplicant(Applicant applicant) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    List<JobApplication> findJobApplicantByJobOffer(Long jobofferId) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    void createJobApplication(Applicant applicant, JobOffer jobOffer) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;


    List<ResponseJobApplicationDto> getListToResponseJobApplication(List<JobApplication> jobApplicationList);
}
