package com.quarke5.ttplayer.service.impl;

import com.google.cloud.firestore.WriteResult;
import com.quarke5.ttplayer.dto.response.ResponseJobApplicationDto;
import com.quarke5.ttplayer.mapper.JobApplicationMapper;
import com.quarke5.ttplayer.model.Applicant;
import com.quarke5.ttplayer.model.JobApplication;
import com.quarke5.ttplayer.model.JobOffer;
import com.quarke5.ttplayer.repository.impl.JobApplicantDAO;
import com.quarke5.ttplayer.service.interfaces.JobApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class JobApplicationServiceImpl implements JobApplicationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobApplicationServiceImpl.class);
    private static final int NEXT_ID = 1;

    @Autowired private JobApplicantDAO repository;
    @Autowired private JobApplicationMapper jobApplicationMapper;


    @Override
    public List<JobApplication> findJobApplicantByApplicant(Applicant applicant) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        List<JobApplication> jobApplicationList = repository.getAllEntities();
        List<JobApplication> resultList = new ArrayList<>();
        for (JobApplication job : jobApplicationList){
            if(job.getApplicant().getIdentification().equals(applicant.getIdentification())){
                resultList.add(job);
            }
        }
        return resultList;
    }

    @Override
    public List<JobApplication> findJobApplicantByJobOffer(Long jobofferId) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        List<JobApplication> jobApplicationList = repository.getAllEntities();
        List<JobApplication> resultList = new ArrayList<>();
        String jobOfferID = String.valueOf(jobofferId);
        for (JobApplication job : jobApplicationList){
            if(job.getJobOffer().getId().equals(jobOfferID)){
                resultList.add(job);
            }
        }
        return resultList;
    }

    @Override
    public void createJobApplication(Applicant applicant, JobOffer jobOffer) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        JobApplication job = jobApplicationMapper.toModelJobApplication(applicant, jobOffer, getLastId());

        for (JobApplication ele : repository.getAllEntities()) {
            if (ele.getJobOffer().getTitle().equals(jobOffer.getTitle())) {
                LOGGER.error("La Aplicacion ya existe de " + applicant.getIdentification() +
                        " con el aviso " + jobOffer.getTitle());
                System.out.println("La Aplicacion ya existe de " + applicant.getIdentification() +
                        " con el aviso " + jobOffer.getTitle());
                break;
            }
        }
        WriteResult jobResult = repository.create(job);
        LOGGER.info("Se a creado exitosamente el jobapplication con " + applicant.getIdentification() +
                " con el aviso " + jobOffer.getTitle());
        System.out.println("Se a creado exitosamente el jobapplication con " + applicant.getIdentification() +
                " con el aviso " + jobOffer.getTitle());
    }

    @Override
    public List<ResponseJobApplicationDto> getListToResponseJobApplication(List<JobApplication> jobApplicationList) {
        return jobApplicationMapper.toResponseJobApplication(jobApplicationList);
    }

    private int getLastId() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return repository.getAllEntities().size() + NEXT_ID;
    }
}
