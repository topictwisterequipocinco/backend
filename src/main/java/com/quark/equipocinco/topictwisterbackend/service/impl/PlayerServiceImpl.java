package com.quark.equipocinco.topictwisterbackend.service.impl;

import com.quark.equipocinco.topictwisterbackend.dto.request.LoginDTO;
import com.quark.equipocinco.topictwisterbackend.repository.PlayerDAO;
import com.quark.equipocinco.topictwisterbackend.dto.request.PlayerDTO;
import com.quark.equipocinco.topictwisterbackend.exception.PlayerException;
import com.quark.equipocinco.topictwisterbackend.mapper.PlayerMapper;
import com.quark.equipocinco.topictwisterbackend.model.Player;
import com.quark.equipocinco.topictwisterbackend.service.PlayerService;
import com.quark.equipocinco.topictwisterbackend.util.error.Errors;
import com.quark.equipocinco.topictwisterbackend.validator.ValidatePlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

@Service
public class PlayerServiceImpl implements PlayerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerServiceImpl.class);
    private static final int NEXT_ID = 1;

    @Autowired MessageSource messageSource;
    @Autowired PlayerMapper playerMapper;
    @Autowired ValidatePlayer validatePlayer;
    @Autowired PlayerDAO playerDAO;
    @Autowired Errors errors;

    @Override
    public ResponseEntity<?> get(String id){
        try {
            Player player = playerDAO.getPlayerByID(id);
            return ResponseEntity.status(HttpStatus.OK).body(playerMapper.responsePlayerDtoToPlayer(player));
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            errors.logError(messageSource.getMessage("player.isNotExists", null,null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("get.entity.failed", null,null));
        }
    }

    @Override
    public ResponseEntity<?> update(String id){
        try {
            Player oldPlayer = playerDAO.getPlayerByID(id);
            Player newPlayer = playerMapper.toUpdateResponseDTO(oldPlayer);
            playerDAO.update(newPlayer);
            return ResponseEntity.status(HttpStatus.OK).body(messageSource.getMessage("player.update.success", new Object[] {id},null));
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            errors.logError(messageSource.getMessage("player.update.failed", new Object[] {id},null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("player.update.failed", new Object[] {id},null));
        }
    }

    @Override
    public ResponseEntity<?> create(PlayerDTO entity) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if(!verifyIsExists(entity.getEmail())){
            return getCreatePlayerResponseDTO(entity);
        }else {
            LOGGER.error(messageSource.getMessage("player.isExists", null,null));
            errors.logError(messageSource.getMessage("player.isExists", null,null));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("player.isExists",null, null));
        }
    }

    @Override
    public ResponseEntity<?> loginPlayer(LoginDTO loginDTO) throws ExecutionException, InterruptedException {
        Player responseDTO = playerDAO.getPlayer(loginDTO);
        if(validatePlayer.validateLogin(responseDTO, loginDTO)){
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(playerMapper.responsePlayerDtoToPlayer(responseDTO));
        }else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(messageSource.getMessage("player.login.failed",null, null));
        }
    }

    @Override
    public ResponseEntity<?> getAll(){
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(playerMapper.toResponsePlayerList(playerDAO.getAllEntities()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(messageSource.getMessage("getAll.entities.failed",null, null));
        }
    }

    private boolean verifyIsExists(String element) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        boolean result = false;
        for(Player ele : playerDAO.getAllEntities()){
            if (ele.getUsername().equals(element)) {
                result = true;
                break;
            }
        }
        return result;
    }

    private ResponseEntity<?> getCreatePlayerResponseDTO(PlayerDTO entity) {
        try{
            Player player = playerMapper.toModel(entity, getLastId());
            validatePlayer.validPlayer(player);
            playerDAO.create(player);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(playerMapper.toResponsePlayerResponseDTO(player));
        }catch (PlayerException | ExecutionException | InterruptedException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            LOGGER.error(e.getMessage());
            errors.logError(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("player.created.failed", new Object[] {e.getMessage()}, null));
        }
    }

    private int getLastId() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return playerDAO.getAllEntities().size() + NEXT_ID;
    }

}
