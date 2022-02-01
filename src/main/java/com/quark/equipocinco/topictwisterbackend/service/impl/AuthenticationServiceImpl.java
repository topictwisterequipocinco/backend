package com.quark.equipocinco.topictwisterbackend.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.quark.equipocinco.topictwisterbackend.dto.request.LoginDTO;
import com.quark.equipocinco.topictwisterbackend.dto.response.PlayerResponseDTO;
import com.quark.equipocinco.topictwisterbackend.service.AuthenticationService;
import com.quark.equipocinco.topictwisterbackend.validator.AuthValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired AuthValidate authValidate;
    @Autowired MessageSource messageSource;
    @Autowired PlayerServiceImpl playerServiceImpl;

    private Firestore db;

    public AuthenticationServiceImpl(Firestore db) {
        this.db = db;
        this.db = FirestoreClient.getFirestore();
    }

    @Override
    public ResponseEntity<PlayerResponseDTO> loginPlayer(LoginDTO loginDTO) {
        try {
            PlayerResponseDTO response = playerServiceImpl.getNewEntityPersisted(loginDTO.getEmail());
            authValidate.validateLogin(response, loginDTO);
            response.setConnected(true);
            ApiFuture<WriteResult> collectionsApiFuture = db.collection(playerServiceImpl.getCollectionDataBaseFirebase().getId())
                    .document(response.getUsername()).set(response);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        }
    }

    @Override
    public ResponseEntity<?> logoutPlayer(LoginDTO loginDTO) {
        try {
            PlayerResponseDTO response = playerServiceImpl.getNewEntityPersisted(loginDTO.getEmail());
            authValidate.validateLogin(response, loginDTO);
            response.setConnected(false);
            ApiFuture<WriteResult> collectionsApiFuture = db.collection(playerServiceImpl.getCollectionDataBaseFirebase().getId())
                    .document(response.getUsername()).set(response);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(messageSource.getMessage("player.logout.success", null,null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(messageSource.getMessage("player.logout.failed", null,null));
        }
    }

    @Override
    public ResponseEntity<?> activateAccount(String username, String hash) {
        try {
            PlayerResponseDTO response = playerServiceImpl.getNewEntityPersisted(username);
            if(response.getVerificationCode().equals(hash)) response.setActivated(true);
            ApiFuture<WriteResult> collectionsApiFuture = db.collection(playerServiceImpl.getCollectionDataBaseFirebase().getId())
                    .document(response.getUsername()).set(response);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(messageSource.getMessage("player.activate.success", null,null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(messageSource.getMessage("player.activate.failed", null, null));
        }
    }

}
