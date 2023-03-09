package com.quarke5.ttplayer.service.impl;

import com.quarke5.ttplayer.dto.request.ForgotDTO;
import com.quarke5.ttplayer.exception.PersonException;
import com.quarke5.ttplayer.mapper.UserMapper;
import com.quarke5.ttplayer.model.*;
import com.quarke5.ttplayer.model.enums.State;
import com.quarke5.ttplayer.repository.impl.UserDAO;
import com.quarke5.ttplayer.service.interfaces.UserService;
import com.quarke5.ttplayer.util.Errors;
import com.quarke5.ttplayer.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final int NEXT_ID = 1;

    @Autowired private UserDAO repository;
    @Autowired private UserMapper userMapper;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired private MessageSource messageSource;
    @Autowired private Validator validator;
    @Autowired private Errors errors;

    @Override
    public User saveUser(String email, String password, Role role) throws PersonException, ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        User user = userMapper.toModel(email, role, bCryptPasswordEncoder.encode(password), getLastId());
        repository.create(user);
        return user;
    }

    @Override
    public ResponseEntity<?> findById(Long id) {
        User user = repository.findById(String.valueOf(id));
        try{
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(userMapper.toUserResponseDto(user, messageSource.getMessage("user.get.success", new Object[] {id}, null)));
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("user.get.failure " + e.getMessage(),new Object[] {id}, null));
            errors.logError(messageSource.getMessage("user.get.failure " + e.getMessage(),new Object[] {id}, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("user.get.failure",new Object[] {id}, null));
        }
    }

    @Override
    public User findByIdUser(Long id) {
        return repository.findById(String.valueOf(id));
    }

    @Override
    public ResponseEntity<?> getAllUsers(int numberPage) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(repository.getAllEntities());
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("user.all.users.failed " + e.getMessage(),null, null));
            errors.logError(messageSource.getMessage("user.all.users.failed " + e.getMessage(),null, null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("user.all.users.failed",null, null));
        }
    }

    @Override
    public ResponseEntity<?> activateAccount(String username, String hash) {
        try {
            User user = repository.findByUsernameByState(username);
            if(user.getVerificationCode().equals(hash)){
                user.setState(State.ACTIVE);
                repository.update(user);
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(messageSource.getMessage("user.activate.success", null,null));
        }catch (Exception e){
            LOGGER.error(messageSource.getMessage("user.activate.failed " + e.getMessage(), new Object[] {username}, null));
            errors.logError(messageSource.getMessage("user.activate.failed " + e.getMessage(), new Object[] {username}, null));
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(messageSource.getMessage("user.activate.failed", new Object[] {username}, null));
        }
    }

    @Override
    public User findByUsername(String username) throws ExecutionException, InterruptedException {
        return repository.getEntity(username);
    }

    @Override
    public User update(Person pub, String email, String password) {
        User newUser = updateUser(pub.getUser(), email, bCryptPasswordEncoder.encode(password));
        repository.update(newUser);
        return newUser;
    }

    @Override
    public ResponseEntity<?> forgot(ForgotDTO forgotDTO) {
        try {
            User user = findByUsername(forgotDTO.getUsername());
            validator.isValidForgot(user, forgotDTO);
            User userModify = updateUser(user, user.getUsername(), bCryptPasswordEncoder.encode(forgotDTO.getFirstPassword()));
            repository.update(userModify);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(messageSource.getMessage("user.forgot.success", null,null));
        }catch (Exception e){
            errors.logError(messageSource.getMessage("user.forgot.failed " + e.getMessage(), new Object[] {forgotDTO.getUsername()}, null));
            LOGGER.error(messageSource.getMessage("user.forgot.failed " + e.getMessage(), new Object[] {forgotDTO.getUsername()}, null));
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(messageSource.getMessage("user.forgot.failed", new Object[] {forgotDTO.getUsername()}, null));
        }
    }

    @Override
    public User findByUsernameByStateActive(String username) {
        return repository.findByUsernameByStateActive(username);
    }

    @Override
    public User save(User user) throws ExecutionException, InterruptedException {
        repository.update(user);
        return repository.getEntity(user.getUsername());
    }

    @Override
    public User updatePublisher(Publisher pub, String email, String password) {
        User user = updateUser(pub.getUser(), email, bCryptPasswordEncoder.encode(password));
        repository.update(user);
        return user;
    }

    @Override
    public User updateApplicant(Applicant app, String email, String password) {
        User user = updateUser(app.getUser(), email, bCryptPasswordEncoder.encode(password));
        repository.update(user);
        return user;
    }

    private User updateUser(User user, String email, String password){
        return userMapper.update(user, email, password);
    }

    private int getLastId() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return repository.getAllEntities().size() + NEXT_ID;
    }
}
