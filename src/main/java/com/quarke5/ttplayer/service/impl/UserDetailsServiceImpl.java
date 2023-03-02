package com.quarke5.ttplayer.service.impl;

import com.quarke5.ttplayer.mapper.UserDetailsMapper;
import com.quarke5.ttplayer.model.User;
import com.quarke5.ttplayer.repository.impl.UserDAO;
import com.quarke5.ttplayer.util.Errors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired private UserDAO userRepo;
    @Autowired private Errors errors;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = null;
        try {
            user = userRepo.getEntity(username);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(user == null){
            LOGGER.error("Invalid username or password or user not found in UserDetailsServiceImpl");
            errors.logError("Invalid username or password or user not found in UserDetailsServiceImpl");
            throw new UsernameNotFoundException("Invalid username or password or user not found in UserDetailsServiceImpl");
        }

        return UserDetailsMapper.build(user);
    }

}
