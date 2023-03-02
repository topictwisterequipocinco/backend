package com.quarke5.ttplayer.service.impl;

import com.google.cloud.firestore.WriteResult;
import com.quarke5.ttplayer.dto.request.LoginDTO;
import com.quarke5.ttplayer.dto.request.LoginNicknameDTO;
import com.quarke5.ttplayer.repository.PlayerDAO;
import com.quarke5.ttplayer.dto.request.PlayerDTO;
import com.quarke5.ttplayer.exception.PlayerException;
import com.quarke5.ttplayer.mapper.PlayerMapper;
import com.quarke5.ttplayer.model.Player;
import com.quarke5.ttplayer.service.PlayerService;
import com.quarke5.ttplayer.service.email.EmailGoogleService;
import com.quarke5.ttplayer.util.Errors;
import com.quarke5.ttplayer.validator.ValidatePlayer;
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
    @Autowired EmailGoogleService emailGoogleService;

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
    public ResponseEntity<?> loginPlayer(LoginDTO loginDTO) {
        try{
            Player responseDTO = playerDAO.getPlayer(loginDTO.getEmail());
            if(validatePlayer.validateLogin(responseDTO, loginDTO)){
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(playerMapper.responsePlayerDtoToPlayer(responseDTO));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(messageSource.getMessage("player.login.notExists", new Object[] {loginDTO.getEmail()}, null));
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(messageSource.getMessage("player.login.failed",null, null));
    }

    @Override
    public ResponseEntity<?> loginPlayerNickname(LoginNicknameDTO loginNicknameDTO) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        try{
            Player responseDTO = playerDAO.getPlayerNickname(loginNicknameDTO.getNickname());
            if(validatePlayer.validateLoginNickname(responseDTO, loginNicknameDTO)){
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(playerMapper.responsePlayerDtoToPlayer(responseDTO));
            }
        }catch (Exception e){
            return getCreatePlayerResponseDTONickname(loginNicknameDTO);
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(messageSource.getMessage("player.login.failed",null, null));
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
            WriteResult result = playerDAO.create(player);
            emailGoogleService.sendEmailNewPlayer(player);
            return ResponseEntity.status(HttpStatus.CREATED).body(playerMapper.responsePlayerDtoToPlayer(player));
        }catch (PlayerException | ExecutionException | InterruptedException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            LOGGER.error(e.getMessage());
            errors.logError(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("player.created.failed", new Object[] {e.getMessage()}, null));
        }
    }

    private ResponseEntity<?> getCreatePlayerResponseDTONickname(LoginNicknameDTO loginNicknameDTO) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        try{
            loginNicknameDTO = playerMapper.updateNicknameDto(loginNicknameDTO, getLastId());
            WriteResult result = playerDAO.createNickNameDto(loginNicknameDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(playerMapper.responsePlayerDtoToLoginNicknameDTO(loginNicknameDTO));
        }catch (ExecutionException | InterruptedException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            LOGGER.error(e.getMessage());
            errors.logError(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("player.created.failed", new Object[] {e.getMessage()}, null));
        }
    }

    private int getLastId() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return playerDAO.getAllEntities().size() + NEXT_ID;
    }

}
