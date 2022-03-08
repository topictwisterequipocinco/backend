package com.quark.equipocinco.topictwisterbackend.service.impl;

import com.quark.equipocinco.topictwisterbackend.dto.request.LoginDTO;
import com.quark.equipocinco.topictwisterbackend.repository.PlayerDAO;
import com.quark.equipocinco.topictwisterbackend.dto.request.PlayerDTO;
import com.quark.equipocinco.topictwisterbackend.exception.PlayerException;
import com.quark.equipocinco.topictwisterbackend.mapper.PlayerMapper;
import com.quark.equipocinco.topictwisterbackend.model.Player;
import com.quark.equipocinco.topictwisterbackend.service.PlayerService;
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

    private static final Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);
    private static final int NEXT_ID = 1;

    @Autowired MessageSource messageSource;
    @Autowired PlayerMapper playerMapper;
    @Autowired ValidatePlayer validatePlayer;
    @Autowired PlayerDAO playerDAO;

    @Override
    public ResponseEntity<?> get(String id){
        try {
            Player player = playerDAO.getPlayerByID(id);
            return ResponseEntity.status(HttpStatus.OK).body(playerMapper.responsePlayerDtoToPlayer(player));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("get.entity.failed", null,null));
        }
    }

    @Override
    public void update(String id) throws ExecutionException, InterruptedException {
        Player oldPlayer = playerDAO.getPlayerByID(id);
        assert oldPlayer != null;
        Player newPlayer = playerMapper.toUpdateResponseDTO(oldPlayer);
        playerDAO.update(newPlayer);
    }

    @Override
    public ResponseEntity<?> create(PlayerDTO entity) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if(!verifyIsExists(entity.getEmail())){
            return getCreatePlayerResponseDTO(entity);
        }else {
            logger.error(messageSource.getMessage("player.isExists", null,null));
            System.out.println(messageSource.getMessage("player.isExists", null,null));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("player.isExists",null, null));
        }
    }

    @Override
    public ResponseEntity<?> loginPlayer(LoginDTO loginDTO) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Player responseDTO = getLoginPlayer(loginDTO);
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
            logger.error(e.getMessage());
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("player.created.failed", new Object[] {e.getMessage()}, null));
        }
    }

    private Player getLoginPlayer(LoginDTO loginDTO) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        for (Player element: playerDAO.getAllEntities()) {
            if(element.getUsername().equals(loginDTO.getEmail())) {return element;}
        }
        return null;
    }

    private int getLastId() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return playerDAO.getAllEntities().size() + NEXT_ID;
    }

}
