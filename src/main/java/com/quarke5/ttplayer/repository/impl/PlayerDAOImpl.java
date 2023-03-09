package com.quarke5.ttplayer.repository.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.quarke5.ttplayer.dto.request.LoginNicknameDTO;
import com.quarke5.ttplayer.model.Player;
import com.quarke5.ttplayer.repository.PlayerDAO;
import com.quarke5.ttplayer.util.Errors;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class PlayerDAOImpl implements PlayerDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerDAOImpl.class);
    private static final int FIRST_PLAYER_ARRAY = 0;

    @Autowired Firestore firestore;
    @Autowired Errors errors;
    private Firestore db;

    public PlayerDAOImpl(Firestore db) {
        this.db = db;
        this.db = FirestoreClient.getFirestore();
    }

    public void update(Player newPlayer) {
        try {
            ApiFuture<WriteResult> collectionsApiFuture = db.collection(getCollectionDataBaseFirebase().getId())
                    .document(newPlayer.getId()).set(newPlayer);
            String result = collectionsApiFuture.get().getUpdateTime().toString();
        }catch (ExecutionException | InterruptedException e){
            LOGGER.info(e.getMessage());
            errors.logError(e.getMessage());
        }
    }

    public Player getPlayerByID(String id) {
        try {
            DocumentReference documentReference = db.collection(getCollectionDataBaseFirebase().getId()).document(id);
            ApiFuture<DocumentSnapshot> future = documentReference.get();
            DocumentSnapshot document = future.get();
            if(document.exists()) return document.toObject(Player.class);
        }catch (ExecutionException | InterruptedException e){
            LOGGER.info(e.getMessage());
        }
        return null;
    }

    public WriteResult create(Player player) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> collectionsApiFuture = db.collection(getCollectionDataBaseFirebase().getId())
                .document(player.getId()).set(player);
        if(collectionsApiFuture.isDone()) return collectionsApiFuture.get();
        return null;
    }

    @Override
    public WriteResult createNickNameDto(LoginNicknameDTO loginNicknameDTO) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> collectionsApiFuture = db.collection(getCollectionDataBaseFirebase().getId())
                .document(loginNicknameDTO.getId()).set(loginNicknameDTO);
        if(collectionsApiFuture.isDone()) return collectionsApiFuture.get();
        return null;
    }

    public List<Player> getAllEntities() throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
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

    @Override
    public Player getPlayer(String username) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = getCollectionDataBaseFirebase().whereEqualTo("username", username).get();
        QueryDocumentSnapshot document = future.get().getDocuments().get(FIRST_PLAYER_ARRAY);
        return document.toObject(Player.class);
    }

    @Override
    public Player getPlayerNickname(String nickname) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = getCollectionDataBaseFirebase().whereEqualTo("name", nickname).get();
        QueryDocumentSnapshot document = future.get().getDocuments().get(FIRST_PLAYER_ARRAY);
        return document.toObject(Player.class);
    }

    private CollectionReference getCollectionDataBaseFirebase() {
        return firestore.collection("player");
    }

}
