package com.quarke5.ttplayer.service;

import com.quarke5.ttplayer.dto.request.LoginDTO;
import com.quarke5.ttplayer.dto.request.LoginNicknameDTO;
import com.quarke5.ttplayer.dto.request.PlayerDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

public interface PlayerService {

    ResponseEntity<?> get(@PathVariable("id") String id);

    ResponseEntity<?> update(@PathVariable("id")  String id);

    ResponseEntity<?> create(@RequestBody @Valid PlayerDTO entity) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    ResponseEntity<?> loginPlayer(@RequestBody @Valid LoginDTO loginDTO);

    ResponseEntity<?> loginPlayerNickname(LoginNicknameDTO loginNicknameDTO) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    ResponseEntity<?> getAll() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

}
