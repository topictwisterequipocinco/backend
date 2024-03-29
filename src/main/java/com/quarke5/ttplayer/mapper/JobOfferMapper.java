package com.quarke5.ttplayer.mapper;

import com.quarke5.ttplayer.dto.request.JobOfferDTO;
import com.quarke5.ttplayer.dto.request.JobOfferEvaluationDTO;
import com.quarke5.ttplayer.dto.request.JobOfferFlutterDTO;
import com.quarke5.ttplayer.dto.response.ResponseJobOfferDto;
import com.quarke5.ttplayer.dto.response.ResponseJobOfferFlutterDto;
import com.quarke5.ttplayer.model.*;
import com.quarke5.ttplayer.model.enums.State;
import com.quarke5.ttplayer.repository.impl.CategoryDAO;
import com.quarke5.ttplayer.service.emails.EmailsGoogle;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class JobOfferMapper {

    private final CategoryDAO categoryRepository;
    private final EmailsGoogle emailGoogleService;

    public JobOfferMapper(CategoryDAO categoryRepository, EmailsGoogle emailGoogleService) {
        this.categoryRepository = categoryRepository;
        this.emailGoogleService = emailGoogleService;
    }

    public JobOffer toModel(JobOfferDTO dto, Publisher publisher, int id) throws ExecutionException, InterruptedException {
        Category category = categoryRepository.getEntity(dto.getCategory());
        JobOffer job = JobOffer.builder()
                .id(String.valueOf(id))
                .title(dto.getTitle())
                .description(dto.getDescription())
                .body(dto.getBody())
                .area(dto.getArea())
                .createDay(String.valueOf(LocalDate.now()))
                .modifiedDay(String.valueOf(LocalDate.now()))
                .deleted(false)
                .experience(dto.getExperience())
                .modality(dto.getModality())
                .position(dto.getPosition())
                .state(State.PENDING)
                .publisher(publisher)
                .category(category)
                .build();
        return job;
    }

    public ResponseJobOfferFlutterDto toResponsePublisherJobOffer(JobOffer jobOffer, String message) {
        ResponseJobOfferFlutterDto dto = ResponseJobOfferFlutterDto.builder()
                .id(Long.valueOf(jobOffer.getId()))
                .title(jobOffer.getTitle())
                .description(jobOffer.getDescription())
                .body(jobOffer.getBody())
                .area(jobOffer.getArea())
                .datePublished(jobOffer.getCreateDay())
                .modifiedDay(jobOffer.getModifiedDay())
                .deletedDay(jobOffer.getDeletedDay())
                .experience(jobOffer.getExperience())
                .modality(jobOffer.getModality().name())
                .position(jobOffer.getPosition().name())
                .state(jobOffer.getState().name())
                .category(jobOffer.getCategory().getName())
                .message(message)
                .build();
        return dto;
    }

    public JobOffer updateJobOffer(JobOffer jobOffer, JobOfferDTO dto) throws ExecutionException, InterruptedException {
        Category category = categoryRepository.getEntity(dto.getCategory());

        jobOffer.setTitle(dto.getTitle());
        jobOffer.setDescription(dto.getDescription());
        jobOffer.setBody(dto.getBody());
        jobOffer.setArea(dto.getArea());
        jobOffer.setModifiedDay(String.valueOf(LocalDate.now()));
        jobOffer.setExperience(dto.getExperience());
        jobOffer.setModality(dto.getModality());
        jobOffer.setPosition(dto.getPosition());
        jobOffer.setCategory(category);
        return jobOffer;
    }



    public List<ResponseJobOfferFlutterDto> toJobOfferList(List<JobOffer> jobOffers) {
        List<ResponseJobOfferFlutterDto> list = new ArrayList<>();
        for(JobOffer job : jobOffers){
            ResponseJobOfferFlutterDto res = toResponsePublisherJobOffer(job, " ");
            list.add(res);
        }
        return list;
    }

    public List<ResponseJobOfferFlutterDto> toPendingJobOfferList(List<JobOffer> jobOffers) {
        List<ResponseJobOfferFlutterDto> list = new ArrayList<>();
        for(JobOffer job : jobOffers){
            if(job.getState().equals(State.PENDING)){
                ResponseJobOfferFlutterDto res = toResponsePublisherJobOffer(job, " ");
                list.add(res);
            }
        }
        return list;
    }

    public JobOffer modifyJobOffer(JobOffer jobOffer, JobOfferEvaluationDTO dto){
        if(dto.getDecision().equals((State.APPROVED).toString())){
            jobOffer.setState(State.PUBLISHED);
        }else if(dto.getDecision().equals((State.REJECTED).toString())){
            jobOffer.setState(State.REJECTED);
        }else {
            jobOffer.setState(State.REVIEW);
        }
        return jobOffer;
    }

    public List<ResponseJobOfferFlutterDto> toJobOfferListSimplePublisher(List<JobOffer> jobOffers) {
        List<ResponseJobOfferFlutterDto> list = new ArrayList<>();
        for(JobOffer job : jobOffers){
            ResponseJobOfferFlutterDto res = toResponsePublisherJobOffer(job, " ");
            list.add(res);
        }
        return list;
    }

    public List<ResponseJobOfferFlutterDto> toJobOfferListSimplePublisherByFilter(List<JobOffer> jobOffers, Category filter) throws ExecutionException, InterruptedException {
        List<ResponseJobOfferFlutterDto> list = new ArrayList<>();
        Category category = categoryRepository.getEntity(filter.getName());
        for(JobOffer job : jobOffers){
            if(job.getCategory().getName().equals(category.getName())){
                ResponseJobOfferFlutterDto res = toResponsePublisherJobOffer(job, " ");
                list.add(res);
            }
        }
        return list;
    }

    public JobOffer updateJobOfferByFlutter(JobOffer jobOffer, JobOfferFlutterDTO dto) throws ExecutionException, InterruptedException {
        Category category = categoryRepository.getEntity(dto.getCategory());

        jobOffer.setTitle(dto.getTitle());
        jobOffer.setDescription(dto.getDescription());
        jobOffer.setBody(dto.getBody());
        jobOffer.setArea(dto.getArea());
        jobOffer.setModifiedDay(String.valueOf(LocalDate.now()));
        jobOffer.setExperience(dto.getExperience());
        jobOffer.setModality(dto.getModality());
        jobOffer.setPosition(dto.getPosition());
        jobOffer.setCategory(category);
        return jobOffer;
    }
}
