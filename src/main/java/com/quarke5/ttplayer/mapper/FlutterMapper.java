package com.quarke5.ttplayer.mapper;

import com.quarke5.ttplayer.dto.response.ResponseJobApplicationFlutterDto;
import com.quarke5.ttplayer.dto.response.ResponseJobOfferFlutterDto;
import com.quarke5.ttplayer.dto.response.ResponsePersonDto;
import com.quarke5.ttplayer.dto.response.UserByFlutterDTO;
import com.quarke5.ttplayer.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FlutterMapper {

    public UserByFlutterDTO responseLoginUserJasonByFlutter(User user, String jwt, Person person, Applicant app, Publisher pub) {
        UserByFlutterDTO dto = new UserByFlutterDTO(jwt);
        if(person != null){
            dto.setId(Long.valueOf(person.getUser().getId()));
            dto.setPersonId(person.getId());
            dto.setName(person.getOficialName());
            dto.setLastName(person.getLastName());
            dto.setIdentification(person.getIdentification());
            dto.setPhone(person.getPhoneNumber());
            dto.setUsername(person.getUser().getUsername());
            dto.setPassword(person.getUser().getPassword());
            dto.setRole(String.valueOf(person.getUser().getRole().getRole()));
            dto.setConected(person.getUser().isConected());
            dto.setGenre("");
            dto.setBirthDate("");
            dto.setTypeStudent("");
            dto.setWebPage("");
        }else if(app != null){
            dto.setId(Long.valueOf(app.getUser().getId()));
            dto.setPersonId(app.getId());
            dto.setName(app.getOficialName());
            dto.setLastName(app.getLastName());
            dto.setIdentification(app.getIdentification());
            dto.setPhone(app.getPhoneNumber());
            dto.setUsername(app.getUser().getUsername());
            dto.setPassword(app.getUser().getPassword());
            dto.setRole(String.valueOf(app.getUser().getRole().getRole()));
            dto.setConected(app.getUser().isConected());
            dto.setGenre(app.getGenre().name());
            dto.setBirthDate(app.getBirthDate().toString());
            dto.setTypeStudent(app.getTypeStudent().name());
            dto.setWebPage("");
        }else if(pub != null){
            dto.setId(Long.valueOf(pub.getUser().getId()));
            dto.setPersonId(pub.getId());
            dto.setName(pub.getOficialName());
            dto.setLastName(pub.getLastName());
            dto.setIdentification(pub.getIdentification());
            dto.setPhone(pub.getPhoneNumber());
            dto.setUsername(pub.getUser().getUsername());
            dto.setPassword(pub.getUser().getPassword());
            dto.setRole(String.valueOf(pub.getUser().getRole().getRole()));
            dto.setConected(pub.getUser().isConected());
            dto.setWebPage(pub.getWebPage());
            dto.setGenre("");
            dto.setBirthDate("");
            dto.setTypeStudent("");
        }
        return dto;
    }

    public List<ResponseJobApplicationFlutterDto> toResponseJobApplication(List<JobApplication> jobApplications, String message) {
        List<ResponseJobApplicationFlutterDto> list = new ArrayList<>();
        for(JobApplication job : jobApplications){
            ResponseJobApplicationFlutterDto res = new ResponseJobApplicationFlutterDto();
            res.setJobOfferApplicantID(Long.valueOf(job.getId()));
            res.setApplied(String.valueOf(job.getApplied()));
            res.setDeletedDay(String.valueOf(job.getDeletedDay()));
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
            res.setModality(String.valueOf((job.getJobOffer().getModality())));
            res.setPosition(String.valueOf(job.getJobOffer().getPosition()));
            res.setCategory(job.getJobOffer().getCategory().getName());
            res.setCategoryDescription(job.getJobOffer().getCategory().getDescription());
            res.setDatePublished(String.valueOf(job.getJobOffer().getCreateDay()));
            res.setModifiedDay(String.valueOf(job.getJobOffer().getModifiedDay()));
            res.setJobOfferDeletedDay(String.valueOf(job.getJobOffer().getDeletedDay()));
            res.setJobOfferDeleted(job.getJobOffer().isDeleted());
            res.setState(String.valueOf(job.getJobOffer().getState()));
            res.setMessage(message);
            list.add(res);
        }
        return list;
    }

    public List<ResponseJobOfferFlutterDto> toJobOfferList(List<JobOffer> jobOffers) {
        List<ResponseJobOfferFlutterDto> list = new ArrayList<>();
        for(JobOffer job : jobOffers){
            ResponseJobOfferFlutterDto res = toResponsePublisherJobOffer(job, " ");
            list.add(res);
        }
        return list;
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

    public UserByFlutterDTO toResponseCreateUserByFlutterDTO(ResponsePersonDto body) {
        UserByFlutterDTO dto = new UserByFlutterDTO("");
        dto.setId(body.getId());
        dto.setName(body.getName());
        dto.setLastName(body.getSurname());
        dto.setIdentification(body.getIdentification());
        dto.setPhone(body.getPhoneNumber());
        dto.setUsername(body.getEmail());
        dto.setPassword("");
        dto.setRole(body.getRole());
        dto.setGenre(body.getGenre() != null ? body.getGenre().name() : "");
        dto.setBirthDate(body.getBirthDate() != null ? String.valueOf(body.getBirthDate()) : "");
        dto.setTypeStudent(body.getTypeStudent() != null ? body.getTypeStudent().name() : "");
        dto.setWebPage(body.getWebPage() != null ? body.getWebPage() : "");
        dto.setConected(true);
        return dto;
    }



}
