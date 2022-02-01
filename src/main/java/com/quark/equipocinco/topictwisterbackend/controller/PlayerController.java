package com.quark.equipocinco.topictwisterbackend.controller;

import com.quark.equipocinco.topictwisterbackend.controller.interfaces.Controllers;
import com.quark.equipocinco.topictwisterbackend.dto.request.PlayerDTO;
import com.quark.equipocinco.topictwisterbackend.dto.response.PlayerResponseDTO;
import com.quark.equipocinco.topictwisterbackend.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/player")
public class PlayerController implements Controllers<PlayerDTO, PlayerResponseDTO> {

    @Autowired PlayerService playerService;

    @GetMapping(value = "/{data}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> get(@PathVariable("data") String data) throws Exception {
        return playerService.get(data);
    }

    @PutMapping(value = "/{data}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> update(@PathVariable("data") String data, @RequestBody @Valid PlayerDTO playerDTO){
        return playerService.update(data, playerDTO);
    }

    @DeleteMapping(value = "/{data}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> delete(@PathVariable("data") String data) throws Exception {
        return playerService.delete(data);
    }

    @PostMapping(value = "/", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> create(@RequestBody @Valid PlayerDTO playerDTO) throws Exception {
        return playerService.create(playerDTO);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAll() throws Exception {
        return playerService.getAll();
    }

}
