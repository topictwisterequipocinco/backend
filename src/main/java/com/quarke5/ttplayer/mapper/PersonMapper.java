package com.quarke5.ttplayer.mapper;

import com.quarke5.ttplayer.dto.request.PersonDTO;
import com.quarke5.ttplayer.dto.response.ResponsePersonDto;
import com.quarke5.ttplayer.exception.PersonException;
import com.quarke5.ttplayer.model.*;
import com.quarke5.ttplayer.model.enums.Roles;
import com.quarke5.ttplayer.model.enums.State;
import com.quarke5.ttplayer.repository.impl.RoleDAO;
import com.quarke5.ttplayer.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class PersonMapper {

    private static final String ELIMINATED = "Dado de baja";

    @Autowired
    RoleDAO roleRepository;
    @Autowired
    UserService userService;
    @Autowired ApplicantMapper applicantMapper;
    @Autowired PublisherMapper publisherMapper;
    private List<ResponsePersonDto> list;

    public Person createPerson(PersonDTO dto, int id) throws PersonException, ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Role role = roleRepository.getEntity(String.valueOf(Roles.UTN));
        User user = userService.saveUser(dto.getEmail(), dto.getPassword(), role);
        Person per = new Person();

        buildPerson(per, dto);
        per.setUser(user);
        per.setId(String.valueOf(id));
        return per;
    }

    public Person toUpdate(Person per, PersonDTO dto) {
        buildPerson(per, dto);
        per.setUser(userService.update(per, dto.getEmail(), dto.getPassword()));
        return per;
    }

    private void buildPerson(Person per, PersonDTO dto){
        per.setOficialName(dto.getName());
        per.setLastName(dto.getSurname());
        per.setIdentification(dto.getIdentification());
        per.setPhoneNumber(dto.getPhoneNumber());
        per.setDeleted(false);
    }

    public ResponsePersonDto toResponsePerson(Person person, String message) {
        ResponsePersonDto per = ResponsePersonDto.builder()
                .id(Long.valueOf(person.getId()))
                .name(person.getOficialName())
                .surname(person.getLastName())
                .identification(person.getIdentification())
                .phoneNumber(person.getPhoneNumber())
                .email(person.getUser().getUsername())
                .role(person.getUser().getRole().getRole().toString())
                .message(message)
                .uri(ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/person/{id}")
                        .buildAndExpand(person.getUser().getId()).toUri())
                .build();
        return per;
    }

    public List<ResponsePersonDto> toPersonList(List<Person> personList) {
        this.list = new ArrayList<>();
        for(Person person : personList){
            list.add(toResponsePerson(person, ""));
        }
        return list;
    }

    public List<ResponsePersonDto> toApplicantList(List<Applicant> applicants) {
        this.list = new ArrayList<>();
        for(Applicant applicant : applicants){
            list.add(applicantMapper.toResponseApplicant(applicant, ""));
        }
        return list;
    }

    public List<ResponsePersonDto> toPublisherList(List<Publisher> publishers) {
        this.list = new ArrayList<>();
        for(Publisher publisher : publishers){
            list.add(publisherMapper.toResponsePublisher(publisher, ""));
        }
        return list;
    }

    public Person deletePerson(Person person) {
        person.setDeleted(true);
        person.getUser().setState(State.DELETED);
        return person;
    }

}
