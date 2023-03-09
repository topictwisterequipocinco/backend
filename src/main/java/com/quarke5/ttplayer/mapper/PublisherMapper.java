package com.quarke5.ttplayer.mapper;

import com.quarke5.ttplayer.dto.request.PersonDTO;
import com.quarke5.ttplayer.dto.response.ResponsePersonDto;
import com.quarke5.ttplayer.exception.PersonException;
import com.quarke5.ttplayer.model.Publisher;
import com.quarke5.ttplayer.model.Role;
import com.quarke5.ttplayer.model.User;
import com.quarke5.ttplayer.model.enums.Roles;
import com.quarke5.ttplayer.repository.impl.RoleDAO;
import com.quarke5.ttplayer.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

@Component
public class PublisherMapper {

    private final RoleDAO roleRepository;
    private final UserService userService;

    @Autowired
    public PublisherMapper(RoleDAO roleRepository, UserService userService) {
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    public Publisher createPublisher(PersonDTO dto, int id) throws PersonException, ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Role role = roleRepository.getEntity(String.valueOf(Roles.PUBLISHER));
        User user = userService.saveUser(dto.getEmail(), dto.getPassword(), role);
        Publisher pub = new Publisher();

        buildPerson(pub, dto);
        pub.setUser(user);
        pub.setId(String.valueOf(id));
        return pub;
    }

    public Publisher toUpdate(Publisher pub, PersonDTO dto) {
        buildPerson(pub, dto);
        pub.setUser(userService.updatePublisher(pub, dto.getEmail(), dto.getPassword()));
        return pub;
    }

    private void buildPerson(Publisher pub, PersonDTO dto){
        pub.setOficialName(dto.getName());
        pub.setLastName(dto.getSurname());
        pub.setIdentification(dto.getIdentification());
        pub.setPhoneNumber(dto.getPhoneNumber());
        pub.setWebPage(dto.getWebPage());
        pub.setDeleted(false);
    }

    public ResponsePersonDto toResponsePublisher(Publisher publisher, String message) {
        ResponsePersonDto dto = ResponsePersonDto.builder()
                .id(Long.valueOf(publisher.getId()))
                .name(publisher.getOficialName())
                .surname(publisher.getLastName())
                .identification(publisher.getIdentification())
                .phoneNumber(publisher.getPhoneNumber())
                .email(publisher.getUser().getUsername())
                .role(publisher.getUser().getRole().getRole().toString())
                .webPage(publisher.getWebPage())
                .message(message)
                .uri(ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/person/{id}")
                        .buildAndExpand(publisher.getUser().getId()).toUri())
                .build();
        return dto;
    }

}


