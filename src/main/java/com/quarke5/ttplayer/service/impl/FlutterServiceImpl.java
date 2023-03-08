package com.quarke5.ttplayer.service.impl;

import com.quarke5.ttplayer.dto.request.JobOfferEvaluationFlutterDTO;
import com.quarke5.ttplayer.dto.request.JobOfferFlutterDTO;
import com.quarke5.ttplayer.dto.request.PersonDTO;
import com.quarke5.ttplayer.dto.request.ProfileDTO;
import com.quarke5.ttplayer.dto.response.ResponsePersonDto;
import com.quarke5.ttplayer.mapper.FlutterMapper;
import com.quarke5.ttplayer.model.*;
import com.quarke5.ttplayer.model.enums.Roles;
import com.quarke5.ttplayer.security.authentication.AuthenticationRequest;
import com.quarke5.ttplayer.security.utilSecurity.JwtUtilService;
import com.quarke5.ttplayer.service.crud.Readable;
import com.quarke5.ttplayer.service.interfaces.*;
import com.quarke5.ttplayer.util.Errors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Service
public class FlutterServiceImpl implements FlutterService, Urls {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Autowired private JwtUtilService jwtTokenUtil;
    @Autowired private UserDetailsServiceImpl userDetailsService;
    @Autowired private MessageSource messageSource;
    @Autowired private FlutterMapper flutterMapper;
    @Autowired private Readable readableService;
    @Autowired private Errors errors;
    @Autowired private ApplicantService applicantService;
    @Autowired private PublisherService publisherService;
    @Autowired private PersonService personService;
    @Autowired private JobOfferService jobOfferService;
    @Autowired private UserService userService;
    @Autowired private RestTemplate restTemplate;

    @Override
    public ResponseEntity<?> createJwtByFlutter(AuthenticationRequest authenticationRequest) throws ExecutionException, InterruptedException {
        User user;
        try {
            user = userService.findByUsernameByStateActive(authenticationRequest.getUsername());
            LOGGER.info(user.getUsername() + user.getPassword() + user.getState());
        }catch (Exception e) {
            LOGGER.error("Incorrecto usuario y/o contraseña - {0}. Asegurese que su cuente este Activa." + e.getMessage());
            errors.logError("Incorrecto usuario y/o contraseña - {0}. Asegurese que su cuente este Activa." + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("authentication.create.jwt.failed", new Object[] {authenticationRequest.getUsername()}, null));
        }final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        user.setConected(true);
        User userChanged = userService.save(user);
        String jwt = jwtTokenUtil.generateToken(userDetails);
        Person person = null;
        Applicant app = null;
        Publisher pub = null;
        if(user.getRole().getRole().equals(Roles.APPLICANT)){
            app = applicantService.getApplicantByUser(user);
        }else if (user.getRole().getRole().equals(Roles.PUBLISHER)){
            pub = publisherService.getPublisherByUser(user);
        }else{
            person = personService.getPersonByUsername(user.getUsername());
        }
        return ResponseEntity.status(HttpStatus.OK).body(flutterMapper.responseLoginUserJasonByFlutter(userChanged, jwt, person, app, pub));
    }

    @Override
    public ResponseEntity<?> logoutUserFlutter(AuthenticationRequest authenticationRequest) {
        try {
            User user = userService.findByUsernameByStateActive(authenticationRequest.getUsername());
            user.setConected(false);
            return ResponseEntity.status(HttpStatus.OK).body(messageSource.getMessage("user.conected",null, null));
        }catch (Exception e){
            LOGGER.error("No se ha podido desloguear correctamente");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("user.isnotconected",null, null));
        }
    }

    @Override
    public ResponseEntity<?> getJobApplicantAllByApplicantByFlutter(Long id) {
        try {
            Applicant applicant = readableService.getPersonTypeApplicantByIdUser(id);
            return getResponseEntity(applicant.getJobApplications());
        } catch (Exception e) {
            LOGGER.error(messageSource.getMessage("jobapplicant.all.applicant.failed " + e.getMessage(), null, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("jobapplicant.all.applicant.failed", null, null));
        }
    }

    @Override
    public ResponseEntity<?> getJobOfferAllByPublisher(Long id) {
        try {
            List<JobOffer> jobOffers = readableService.getPersonTypePublisherByIdUser(id).getJobOfferList();
            return ResponseEntity.status(HttpStatus.OK).body(flutterMapper.toJobOfferList(jobOffers));
        } catch (Exception e) {
            LOGGER.error(messageSource.getMessage("publisehr.all.joboffer.failed " + e.getMessage(),null, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("publisehr.all.joboffer.failed",null, null));
        }
    }

    @Override
    public ResponseEntity<?> getAllAppliedByJobOffer(Long id) {
        try {
            JobOffer jobOffer = jobOfferService.getJobOffer(id);
            return getResponseEntity(jobOffer.getJobApplications());
        } catch (Exception e) {
            LOGGER.error(messageSource.getMessage("jobapplicant.all.applicant.failed " + e.getMessage(),null, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("jobapplicant.all.applicant.failed",null, null));
        }
    }
    @Override
    public ResponseEntity<?> getJobOfferEvaluation(JobOfferEvaluationFlutterDTO dto){
        return null;
    }

    @Override
    public ResponseEntity<?> create(PersonDTO personDTO) {
        ResponseEntity<?> newEntity = personDTO.getRole().equals(Roles.APPLICANT.name()) ?
                restTemplate.postForEntity(URL_APPLICANT, personDTO, ResponsePersonDto.class)
                : restTemplate.postForEntity(URL_PUBLISHER, personDTO, ResponsePersonDto.class);
        return ResponseEntity.status(newEntity.getStatusCode()).body(flutterMapper.toResponseCreateUserByFlutterDTO((ResponsePersonDto) Objects.requireNonNull(newEntity.getBody())));
    }

    @Override
    public ResponseEntity<?> getProfileById(ProfileDTO profileDTO) {
        if(profileDTO.getRole().equals(Roles.APPLICANT.name())){
            ResponseEntity<?> newEntity = restTemplate.getForEntity(URL_APPLICANT + profileDTO.getId(), ResponsePersonDto.class, profileDTO.getId());
            return ResponseEntity.status(newEntity.getStatusCode()).body(flutterMapper.toResponseCreateUserByFlutterDTO((ResponsePersonDto) Objects.requireNonNull(newEntity.getBody())));
        } else if (profileDTO.getRole().equals(Roles.PUBLISHER.name())) {
            ResponseEntity<?> newEntity = restTemplate.getForEntity(URL_PUBLISHER + profileDTO.getId(), ResponsePersonDto.class, profileDTO.getId());
            return ResponseEntity.status(newEntity.getStatusCode()).body(flutterMapper.toResponseCreateUserByFlutterDTO((ResponsePersonDto) Objects.requireNonNull(newEntity.getBody())));
        } else if (profileDTO.getRole().equals(Roles.UTN.name())) {
            ResponseEntity<?> newEntity = restTemplate.getForEntity(URL_PERSON + profileDTO.getId(), ResponsePersonDto.class, profileDTO.getId());
            return ResponseEntity.status(newEntity.getStatusCode()).body(flutterMapper.toResponseCreateUserByFlutterDTO((ResponsePersonDto) Objects.requireNonNull(newEntity.getBody())));
        }
        return null;
    }

    @Override
    public void updatePerson(ProfileDTO profileDTO) {
        if(profileDTO.getRole().equals(Roles.APPLICANT.name())){
            restTemplate.put(URL_APPLICANT + profileDTO.getId(), profileDTO);
        } else if (profileDTO.getRole().equals(Roles.PUBLISHER.name())) {
            restTemplate.put(URL_PUBLISHER + profileDTO.getId(), profileDTO);
        } else if (profileDTO.getRole().equals(Roles.UTN.name())) {
            restTemplate.put(URL_PERSON + profileDTO.getId(), profileDTO);
        }
    }

    @Override
    public void deletePerson(ProfileDTO profileDTO) {
        if(profileDTO.getRole().equals(Roles.APPLICANT.name())){
            restTemplate.delete(URL_APPLICANT + profileDTO.getId(), profileDTO);
        } else if (profileDTO.getRole().equals(Roles.PUBLISHER.name())) {
            restTemplate.delete(URL_PUBLISHER + profileDTO.getId(), profileDTO);
        } else if (profileDTO.getRole().equals(Roles.UTN.name())) {
            restTemplate.delete(URL_PERSON + profileDTO.getId(), profileDTO);
        }
    }

    @Override
    public ResponseEntity<?> updateJobOffer(JobOfferFlutterDTO jobOfferFlutterDTO) {
        try {
            JobOffer jobOffer =  jobOfferService.getJobOffer(Long.valueOf(jobOfferFlutterDTO.getId()));
            JobOffer newJobOffer = jobOfferService.updateJobOffer(jobOffer, jobOfferFlutterDTO);
            jobOfferService.saveJobOffer(newJobOffer);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(jobOfferService.toResponsePublisherJobOffer(newJobOffer, messageSource.getMessage("joboffer.update.success", null, null)));
        } catch (Exception e) {
            LOGGER.error("No se ha podido modificar el aviso con id " + jobOfferFlutterDTO.getId());
            errors.logError("No se ha podido modificar el aviso con id " + jobOfferFlutterDTO.getId());
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(messageSource.getMessage("joboffer.update.failed",new Object[] {e.getMessage()}, null));
        }
    }



    private ResponseEntity<?> getResponseEntity(List<JobApplication> jobApplications) {
        return ResponseEntity.status(HttpStatus.OK).body(flutterMapper.toResponseJobApplication(jobApplications,
                messageSource.getMessage("jobapplicant.all.applicant.success",null, null)));
    }

}
