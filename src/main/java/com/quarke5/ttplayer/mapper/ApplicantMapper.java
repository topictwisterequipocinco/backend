package com.quarke5.ttplayer.mapper;

import com.quarke5.ttplayer.dto.request.PersonDTO;
import com.quarke5.ttplayer.dto.response.ResponsePersonDto;
import com.quarke5.ttplayer.exception.PersonException;
import com.quarke5.ttplayer.model.Applicant;
import com.quarke5.ttplayer.model.Role;
import com.quarke5.ttplayer.model.User;
import com.quarke5.ttplayer.model.enums.Genre;
import com.quarke5.ttplayer.model.enums.Roles;
import com.quarke5.ttplayer.model.enums.TypeStudent;
import com.quarke5.ttplayer.repository.impl.RoleDAO;
import com.quarke5.ttplayer.service.interfaces.UserService;
import com.quarke5.ttplayer.validator.AgeValidate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

@Component
public class ApplicantMapper {

    private final RoleDAO roleRepository;
    private final UserService userService;
    private final AgeValidate ageValidate;

    public ApplicantMapper(RoleDAO roleRepository, UserService userService, AgeValidate ageValidate) {
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.ageValidate = ageValidate;
    }

    public Applicant createApplicant(PersonDTO dto, int id) throws PersonException, ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        int age = 18;
        if(!ageValidate.ageValidateApplicant(dto, age)) return null;

        Role role = roleRepository.getEntity(String.valueOf(Roles.APPLICANT));
        User user = userService.saveUser(dto.getEmail(), dto.getPassword(), role);
        Applicant app = new Applicant();

        buildPerson(app, dto);
        app.setUser(user);
        app.setId(String.valueOf(id));
        return app;
    }

    public Applicant toUpdate(Applicant app, PersonDTO dto) {
        buildPerson(app, dto);
        app.setUser(userService.updateApplicant(app, dto.getEmail(), dto.getPassword()));
        return app;
    }

    private void buildPerson(Applicant app, PersonDTO dto) {
        app.setOficialName(dto.getName());
        app.setLastName(dto.getSurname());
        app.setIdentification(dto.getIdentification());
        app.setPhoneNumber(dto.getPhoneNumber());
        app.setBirthDate(String.valueOf(dto.getBirthDate()));
        if(dto.getGenre().equals("FEMENINO")){
            app.setGenre(Genre.FEMALE);
        }else if (dto.getGenre().equals("MASCULINO")){
            app.setGenre(Genre.MALE);
        }else if (dto.getGenre().equals("OTRO")){
            app.setGenre(Genre.OTHER);
        }
        if(dto.getTypeStudent().equals("ACTIVO")){
            app.setTypeStudent(TypeStudent.ACTIVE);
        }else if (dto.getTypeStudent().equals("REGULAR")){
            app.setTypeStudent(TypeStudent.REGULAR);
        }else if (dto.getTypeStudent().equals("RECIBIDO")){
            app.setTypeStudent(TypeStudent.RECEIVED);
        }
        app.setDeleted(false);
    }

    public ResponsePersonDto toResponseApplicant(Applicant app, String message) {
        ResponsePersonDto dto = ResponsePersonDto.builder()
                .id(Long.valueOf(app.getId()))
                .name(app.getOficialName())
                .surname(app.getLastName())
                .identification(app.getIdentification())
                .phoneNumber(app.getPhoneNumber())
                .email(app.getUser().getUsername())
                .role(app.getUser().getRole().getRole().toString())
                .genre(app.getGenre())
                .birthDate(LocalDate.parse(app.getBirthDate()))
                .typeStudent(app.getTypeStudent())
                .message(message)
                .uri(ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/person/{id}")
                        .buildAndExpand(app.getUser().getId()).toUri())
                .build();
        return dto;
    }

}
