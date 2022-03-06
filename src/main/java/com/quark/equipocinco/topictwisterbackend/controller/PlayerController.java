package com.quark.equipocinco.topictwisterbackend.controller;

import com.quark.equipocinco.topictwisterbackend.dto.request.LoginDTO;
import com.quark.equipocinco.topictwisterbackend.dto.request.PlayerDTO;
import com.quark.equipocinco.topictwisterbackend.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/player")
public class PlayerController{

    @Autowired PlayerService playerService;

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> get(@PathVariable("id") String id) throws Exception {
        return playerService.get(id);
    }

    @PutMapping(value = "/{data}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public void update(@PathVariable("data") String data) throws ExecutionException, InterruptedException {
        playerService.update(data);
    }

    @PostMapping(value = "/", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> create(@RequestBody @Valid PlayerDTO playerDTO) throws Exception {
        return playerService.create(playerDTO);
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO loginDTO) throws Exception {
        return playerService.loginPlayer(loginDTO);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAll() throws Exception {
        return playerService.getAll();
    }

}
