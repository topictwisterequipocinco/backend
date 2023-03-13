package com.quarke5.ttplayer.service.impl;

import com.quarke5.ttplayer.dto.request.*;
import com.quarke5.ttplayer.dto.response.ResponsePersonDto;
import com.quarke5.ttplayer.dto.response.UserByFlutterDTO;
import com.quarke5.ttplayer.mapper.FlutterMapper;
import com.quarke5.ttplayer.model.*;
import com.quarke5.ttplayer.model.enums.Roles;
import com.quarke5.ttplayer.model.enums.State;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(FlutterServiceImpl.class);

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
    @Autowired private JobApplicationService jobApplicationService;

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

        if(user.getState().equals(State.ACTIVE)){
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
        }else {
            LOGGER.error("El estado del usuario no esta activo, por lo cual no puede operar " + user.getUsername());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("user.isnotactivestate",null, null));
        }
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
            Applicant applicant = applicantService.getApplicantById(id);
            List<JobApplication> jobApplicationList = jobApplicationService.findJobApplicantByApplicant(applicant);
            return ResponseEntity.status(HttpStatus.OK).body(jobApplicationService.getListToResponseJobApplicationFlutter(jobApplicationList,
                    messageSource.getMessage("jobapplicant.all.applicant.success", null, null)));
        } catch (Exception e) {
            LOGGER.error(messageSource.getMessage("jobapplicant.all.applicant.failed " + e.getMessage(), null, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("jobapplicant.all.applicant.failed", null, null));
        }
    }

    @Override
    public ResponseEntity<?> getJobOfferAllByPublisher(Long publisherID) {
        try {
            List<JobOffer> jobOffers = jobOfferService.getAllJobOffersByPublisherId(publisherID);
            return ResponseEntity.status(HttpStatus.OK).body(flutterMapper.toJobOfferList(jobOffers));
        } catch (Exception e) {
            LOGGER.error("Fallo la operacion de pedido de la lista de avisos publicados.");
            System.out.println("Fallo la operacion de pedido de la lista de avisos publicados.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("publisehr.all.joboffer.failed",null, null));
        }
    }

    @Override
    public ResponseEntity<?> getAllAppliedByJobOfferByFlutter(Long jobofferId) {
        try {
            List<JobApplication> jobApplicationList = jobApplicationService.findJobApplicantByJobOffer(jobofferId);
            return ResponseEntity.status(HttpStatus.OK).body(jobApplicationService.getListToResponseJobApplicationFlutter(jobApplicationList,
                    messageSource.getMessage("jobapplicant.all.applicant.success", null, null)));
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

        return newEntity.getBody() != null ?
                ResponseEntity.status(newEntity.getStatusCode()).body(flutterMapper.toResponseCreateUserByFlutterDTO((ResponsePersonDto) Objects.requireNonNull(newEntity.getBody())))
                : ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("applicant.created.failed",null, null));
    }

    @Override
    public ResponseEntity<?> getProfileById(ProfileDTO profileDTO) {
        if(profileDTO.getRole().equals(Roles.APPLICANT.name())){
            ResponseEntity<?> newEntity = restTemplate.getForEntity(URL_APPLICANT + profileDTO.getId(), Applicant.class, profileDTO.getId());
            return newEntity.getBody() != null ?
                    ResponseEntity.status(newEntity.getStatusCode()).body(flutterMapper.responseLoginUserJasonByFlutter(null,"",null,(Applicant) Objects.requireNonNull(newEntity.getBody()),null))
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("applicant.search.failed",new Object[] {profileDTO.getId() + " " + profileDTO.getRole()}, null));
        } else if (profileDTO.getRole().equals(Roles.PUBLISHER.name())) {
            ResponseEntity<?> newEntity = restTemplate.getForEntity(URL_PUBLISHER + profileDTO.getId(), Publisher.class, profileDTO.getId());
            return newEntity.getBody() != null ?
                    ResponseEntity.status(newEntity.getStatusCode()).body(flutterMapper.responseLoginUserJasonByFlutter(null,"",null, null,(Publisher) Objects.requireNonNull(newEntity.getBody())))
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("publisher.search.failed",new Object[] {profileDTO.getId() + " " + profileDTO.getRole()}, null));
        } else if (profileDTO.getRole().equals(Roles.UTN.name())) {
            ResponseEntity<?> newEntity = restTemplate.getForEntity(URL_PERSON + profileDTO.getId(), Person.class, profileDTO.getId());
            return newEntity.getBody() != null ?
                ResponseEntity.status(newEntity.getStatusCode())
                    .body(flutterMapper.responseLoginUserJasonByFlutter(null,"",(Person) Objects.requireNonNull(newEntity.getBody()),null,null))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("person.search.failed",new Object[] {profileDTO.getId() + " " + profileDTO.getRole()}, null));
        }
        return null;
    }

    @Override
    public void updatePerson(Long id, PersonDTO personDTO) {
        if(personDTO.getRole().equals(Roles.APPLICANT.name())){
            restTemplate.put(URL_APPLICANT + id, personDTO);
        } else if (personDTO.getRole().equals(Roles.PUBLISHER.name())) {
            restTemplate.put(URL_PUBLISHER + id, personDTO);
        } else if (personDTO.getRole().equals(Roles.UTN.name())) {
            restTemplate.put(URL_PERSON + id, personDTO);
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
    public ResponseEntity<?> updateJobOffer(Long id, JobOfferFlutterDTO jobOfferFlutterDTO) {
        try {
            JobOffer jobOffer =  jobOfferService.getJobOffer(id);
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


}
