package com.quarke5.ttplayer.controller;

import com.quarke5.ttplayer.controller.interfaces.Controller;
import com.quarke5.ttplayer.dto.request.LoginDTO;
import com.quarke5.ttplayer.dto.request.LoginNicknameDTO;
import com.quarke5.ttplayer.dto.request.PlayerDTO;
import com.quarke5.ttplayer.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/player")
public class PlayerController implements Controller<PlayerDTO> {

    @Autowired PlayerService playerService;

    @Override
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> get(@PathVariable("id") String id){
        return playerService.get(id);
    }

    @Override
    @PutMapping(value = "/{data}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> update(@PathVariable("data") String data){
        return playerService.update(data);
    }

    @Override
    @PostMapping(value = "/", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> create(@RequestBody @Valid PlayerDTO playerDTO) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return playerService.create(playerDTO);
    }

    @Override
    @GetMapping("/")
    public ResponseEntity<?> getAll() throws Exception {
        return playerService.getAll();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO loginDTO) {
        return playerService.loginPlayer(loginDTO);
    }

    @PostMapping("/login/nickname")
    public ResponseEntity<?> login(@RequestBody @Valid LoginNicknameDTO loginNicknameDTO) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return playerService.loginPlayerNickname(loginNicknameDTO);
    }

}
