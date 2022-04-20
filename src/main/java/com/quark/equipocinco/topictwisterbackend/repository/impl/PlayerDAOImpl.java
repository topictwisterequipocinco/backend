package com.quark.equipocinco.topictwisterbackend.repository.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.quark.equipocinco.topictwisterbackend.model.Player;
import com.quark.equipocinco.topictwisterbackend.repository.PlayerDAO;
import com.quark.equipocinco.topictwisterbackend.util.error.Errors;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
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

    public void create(Player player) {
        ApiFuture<WriteResult> collectionsApiFuture = db.collection(getCollectionDataBaseFirebase().getId())
                .document(player.getId()).set(player);
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

    private CollectionReference getCollectionDataBaseFirebase() {
        return firestore.collection("player");
    }

}
