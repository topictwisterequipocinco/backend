package com.quarke5.ttplayer.service.interfaces;

import com.quarke5.ttplayer.dto.request.ForgotDTO;
import com.quarke5.ttplayer.exception.PersonException;
import com.quarke5.ttplayer.model.*;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

public interface UserService {

    User saveUser(String email, String password, Role role) throws PersonException, ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    ResponseEntity<?> findById(Long id);

    User findByIdUser(Long id);

    ResponseEntity<?> getAllUsers(int page);

    ResponseEntity<?> activateAccount(String username, String hash);

    User findByUsername(String username) throws ExecutionException, InterruptedException;

    User update(Person pub, String email, String password);

    ResponseEntity<?> forgot(ForgotDTO forgotDTO);

    User findByUsernameByStateActive(String username);

    User save(User user) throws ExecutionException, InterruptedException;

    User updatePublisher(Publisher pub, String email, String password);

    User updateApplicant(Applicant app, String email, String password);
}
