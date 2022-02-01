package com.quark.equipocinco.topictwisterbackend.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.quark.equipocinco.topictwisterbackend.dto.response.PlayerResponseDTO;
import org.apache.commons.beanutils.PropertyUtils;
import com.quark.equipocinco.topictwisterbackend.dto.request.PlayerDTO;
import com.quark.equipocinco.topictwisterbackend.exception.PlayerException;
import com.quark.equipocinco.topictwisterbackend.mapper.PlayerMapper;
import com.quark.equipocinco.topictwisterbackend.model.Player;
import com.quark.equipocinco.topictwisterbackend.service.EmailService;
import com.quark.equipocinco.topictwisterbackend.service.PlayerService;
import com.quark.equipocinco.topictwisterbackend.service.common.GenericServiceImpl;
import com.quark.equipocinco.topictwisterbackend.util.logger.Errors;
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
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Service
public class PlayerServiceImpl extends GenericServiceImpl<PlayerDTO, PlayerResponseDTO, Player> implements PlayerService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);

    @Autowired Firestore firestore;
    @Autowired MessageSource messageSource;
    @Autowired Errors errors;
    @Autowired PlayerMapper playerMapper;
    @Autowired ValidatePlayer validatePlayer;
    @Autowired EmailService emailService;
    private Firestore db;

    public PlayerServiceImpl(Firestore db) {
        super(db);
        this.db = db;
        this.db = FirestoreClient.getFirestore();
    }

    @Override
    public ResponseEntity<?> update(String data, PlayerDTO entity) {
        try{
            PlayerResponseDTO newPlayer = playerMapper.toUpdateResponseDTO(entity);
            validatePlayer.validPlayer(newPlayer);
            ApiFuture<WriteResult> collectionsApiFuture = db.collection(getCollectionDataBaseFirebase().getId())
                    .document(newPlayer.getUsername()).set(newPlayer);
            String result = collectionsApiFuture.get().getUpdateTime().toString();
            return ResponseEntity.status(HttpStatus.CREATED).body(messageSource.getMessage("update.entity.success", null,null));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }
    }

    @Override
    public ResponseEntity<?> create(PlayerDTO entity) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if(!verifyIsExists(entity.getEmail())){
            return getCreatePlayerResponseDTO(entity);
        }else {
            errors.logError(messageSource.getMessage("player.isExists", null,null));
            logger.error(messageSource.getMessage("player.isExists", null,null));
            System.out.println(messageSource.getMessage("player.isExists", null,null));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("player.isExists",null, null));
        }
    }

    @Override
    public boolean verifyIsExists(String element) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        boolean result = false;
        for(Player ele : getAllEntities()){
            if(ele.getUsername().equals(element)) result=true;
        }
        return result;
    }

    @Override
    public CollectionReference getCollectionDataBaseFirebase() {
        return firestore.collection("player");
    }

    private ResponseEntity<?> getCreatePlayerResponseDTO(PlayerDTO entity) {
        try{
            Player player = playerMapper.toModel(entity);
            validatePlayer.validPlayer(player);
            ApiFuture<WriteResult> collectionsApiFuture = db.collection(getCollectionDataBaseFirebase().getId())
                    .document(player.getUsername()).set(player);
            emailService.createEmailPlayer(player);
            PlayerResponseDTO response = getNewEntityPersisted(player.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    playerMapper.toResponsePlayer(Objects.requireNonNull(response),
                            messageSource.getMessage("player.created.success", null,null)));
        }catch (PlayerException | ExecutionException | InterruptedException e) {
            logger.error(e.getMessage());
            System.out.println(e.getMessage());
            errors.logError(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(messageSource.getMessage("player.created.failed", new Object[] {e.getMessage()}, null));
        }
    }

    private List<PlayerResponseDTO> getAllEntities() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ExecutionException, InterruptedException {
        List<PlayerResponseDTO> list = new ArrayList<>();
        ApiFuture<QuerySnapshot> query = getCollectionDataBaseFirebase().get();
        List<QueryDocumentSnapshot> documents = query.get().getDocuments();
        for (QueryDocumentSnapshot doc : documents) {
            PlayerResponseDTO object = doc.toObject(clazz);
            PropertyUtils.setProperty(object, "id", doc.getId());
            list.add(object);
        }
        return list;
    }


}
