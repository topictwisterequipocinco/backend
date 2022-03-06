package com.quark.equipocinco.topictwisterbackend.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.quark.equipocinco.topictwisterbackend.dto.request.LoginDTO;
import org.apache.commons.beanutils.PropertyUtils;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class PlayerServiceImpl implements PlayerService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);
    private static final int NEXT_ID = 1;

    @Autowired Firestore firestore;
    @Autowired MessageSource messageSource;
    @Autowired PlayerMapper playerMapper;
    @Autowired ValidatePlayer validatePlayer;
    private Firestore db;

    public PlayerServiceImpl(Firestore db) {
        this.db = db;
        this.db = FirestoreClient.getFirestore();
    }

    @Override
    public ResponseEntity<?> get(String id){
        try {
            Player player = getPlayerByID(id);
            assert player != null;
            return ResponseEntity.status(HttpStatus.OK).body(playerMapper.responsePlayerDtoToPlayer(player));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageSource.getMessage("get.entity.failed", null,null));
        }
    }

    @Override
    public void update(String id) throws ExecutionException, InterruptedException {
        Player oldPlayer = getPlayerByID(id);
        assert oldPlayer != null;
        Player newPlayer = playerMapper.toUpdateResponseDTO(oldPlayer);
        ApiFuture<WriteResult> collectionsApiFuture = db.collection(getCollectionDataBaseFirebase().getId())
                .document(newPlayer.getId()).set(newPlayer);
        String result = collectionsApiFuture.get().getUpdateTime().toString();
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
    public ResponseEntity<?> loginPlayer(LoginDTO loginDTO) {
        try {
            Player responseDTO = getLoginPlayer(loginDTO);
            validatePlayer.validateLogin(responseDTO, loginDTO);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(playerMapper.responsePlayerDtoToPlayer(responseDTO));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        }
    }

    @Override
    public CollectionReference getCollectionDataBaseFirebase() {
        return firestore.collection("player");
    }

    @Override
    public ResponseEntity<?> getAll(){
        try {
            List<Player> list = getAllEntities();
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(playerMapper.toResponsePlayerList(list));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        }
    }

    public boolean verifyIsExists(String element) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        boolean result = false;
        for(Player ele : getAllEntities()){
            if(ele.getUsername().equals(element)) result=true;
        }
        return result;
    }

    private Player getPlayerByID(String data) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = db.collection(getCollectionDataBaseFirebase().getId()).document(data);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        try {
            if(document.exists()) {
                return document.toObject(Player.class);
            }
        }catch (Exception e){
            e.getMessage();
        }
        return null;
    }

    private ResponseEntity<?> getCreatePlayerResponseDTO(PlayerDTO entity) {
        try{
            Player player = playerMapper.toModel(entity, getLastId());
            validatePlayer.validPlayer(player);
            ApiFuture<WriteResult> collectionsApiFuture = db.collection(getCollectionDataBaseFirebase().getId())
                    .document(player.getId()).set(player);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                    playerMapper.toResponsePlayerResponseDTO(player));
        }catch (PlayerException | ExecutionException | InterruptedException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            logger.error(e.getMessage());
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("player.created.failed", new Object[] {e.getMessage()}, null));
        }
    }

    private List<Player> getAllEntities() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ExecutionException, InterruptedException {
        List<Player> list = new ArrayList<>();
        ApiFuture<QuerySnapshot> query = getCollectionDataBaseFirebase().get();
        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
        for (QueryDocumentSnapshot doc : documents) {
            Player object = doc.toObject(Player.class);
            PropertyUtils.setProperty(object, "id", doc.getId());
            list.add(object);
        }
        return list;
    }

    private Player getLoginPlayer(LoginDTO loginDTO) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        for (Player element: getAllEntities()) {
            if(element.getUsername().equals(loginDTO.getEmail())) {return element;}
        }
        return null;
    }


    private int getLastId() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        return getAllEntities().size() + NEXT_ID;
    }

}
